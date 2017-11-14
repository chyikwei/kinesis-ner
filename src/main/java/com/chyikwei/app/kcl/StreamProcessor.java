package com.chyikwei.app.kcl;

import java.util.HashMap;
import java.util.List;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharacterCodingException;
import java.util.Map;

import com.chyikwei.app.ner.*;
import com.chyikwei.app.persistence.EntityPersisterInterface;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.kinesis.model.Record;

import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;


/**
 * Kinesis Stream Processor.
 * Will process each record and store NER result to DynamoDB
 */
public class StreamProcessor implements IRecordProcessor {

  private static final Log LOG = LogFactory.getLog(StreamProcessor.class);

  // process recrod retries
  private static final long PORCESS_RETRY_TIME_MS = 1000L;
  private static final int PROCESS_RETRIES = 3;

  // checkpoint retries
  private static final long CHECKPOINT_RETRY_TIME_MS = 5000L;
  private static final long CHECKPOINT_INTERVAL_MS = 60000L;
  private static final int CHECKPOINT_RETRIES = 5;

  private final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

  private String shardId;
  private long nextCheckpointTimeInMillis;

  // extractor
  private EntityExtractInterface entityExtractor;

  // persister
  private EntityPersisterInterface persiter;


  /**
   * initialize NER
   *
   * @param initializationInput initial input
   */
  @Override
  public void initialize(InitializationInput initializationInput) {
    shardId = initializationInput.getShardId();
    entityExtractor = StanfordEntityExtractor.getInstance();

    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("initialize: shard %s", shardId));
    }
  }


  @Override
  public void processRecords(ProcessRecordsInput processRecordsInput) {
    List<Record> records = processRecordsInput.getRecords();

    // process with retries
    processRecordsWithRetry(records);

    // save checkpoint
    if (System.currentTimeMillis() > nextCheckpointTimeInMillis) {
      checkpoint(processRecordsInput.getCheckpointer());
      nextCheckpointTimeInMillis = System.currentTimeMillis() + CHECKPOINT_INTERVAL_MS;
    }
  }

  /**
   * process each records with retries
   *
   * @param records Data records to be processed.
   */
  private void processRecordsWithRetry(List<Record> records) {

    // copy from official example
    for (Record record : records) {
      boolean processedSuccessfully = false;
      for (int i = 0; i < PROCESS_RETRIES; i++) {
        try {
          // process single record
          processSingleRecord(record);

          processedSuccessfully = true;
          break;
        } catch (Throwable t) {
          LOG.warn("Caught throwable while processing record " + record, t);
        }

        // backoff if we encounter an exception.
        try {
          Thread.sleep(PORCESS_RETRY_TIME_MS);
        } catch (InterruptedException e) {
          LOG.debug("Interrupted sleep", e);
        }
      }

      if (!processedSuccessfully) {
        //TODO: save failed records to different dynamoDB table
        LOG.error("Couldn't process record " + record + ". Skipping the record.");
      }
    }
  }

  /**
   * process single record
   *
   *  @param record record to be processed
   */
  private void processSingleRecord(Record record) {

    try {
      String data = decoder.decode(record.getData()).toString();
      TextRecordInterface textRecord = NewsTextRecord.fromJson(data);

      // extract etities from each field
      Map<String, List<Entity>> entityMap = new HashMap<>();
      for (Pair<String, String> pair : textRecord.getTextFields()) {
        String field = pair.getLeft();
        String text = pair.getRight();
        List<Entity> entities = entityExtractor.annotate(text);
        entityMap.put(field, entities);
      }

      // save entities
      saveEntities(entityMap);

      if (LOG.isDebugEnabled()) {
        LOG.info("processed: " + record.getSequenceNumber() + ", " + record.getPartitionKey() + ", " +
            textRecord.getUUID());
      }

    } catch (CharacterCodingException e) {
      LOG.error("Malformed data", e);
    }
  }

  private void saveEntities(Map<String, List<Entity>> entityMap) {
    // TODO: save entities
  }


  /**
   * checkpoint with retries
   *
   * @param checkpointer record checkpointer
   */
  private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
    LOG.info("Checkpointing shard " + shardId);
    // copy from official example app
    for (int i=0; i < CHECKPOINT_RETRIES; i++) {
      try {
        checkpointer.checkpoint();
        break;
      } catch (ShutdownException se) {
        LOG.info("Caught shutdown exception, skipping checkpoint.", se);
        break;
      } catch (InvalidStateException e) {
        LOG.error("Cannot save checkpoint to the DynamoDB table.", e);
        break;
      } catch (ThrottlingException e) {

        // retry with ThrottlingException
        LOG.info("Caught ThrottlingException. retry " + (i + 1));
        if (i >= (CHECKPOINT_RETRIES - 1)) {
          LOG.error("Checkpoint failed after " + (i + 1) + "attempts.", e);
          break;
        }
      }
    }
  }

  @Override
  public void shutdown(ShutdownInput shutdownInput) {
    LOG.info("Shutting down record processor for shard: " + shardId);

    if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
      // only checkpoint with reason TERMINATE
      checkpoint(shutdownInput.getCheckpointer());
    }
  }
}
