package com.chyikwei.app.kcl;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class StreamProcessor implements IRecordProcessor {

  private static final Log LOG = LogFactory.getLog(StreamProcessor.class);

  private String shardId;

  @Override
  public void initialize(InitializationInput initializationInput) {
    shardId = initializationInput.getShardId();
    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("initialize: shard %s", shardId));
    }
  }

  @Override
  public void processRecords(ProcessRecordsInput processRecordsInput) {
    List<Record> records = processRecordsInput.getRecords();
    //TODO: process record
    for (Record record : records) {

    }
  }

  @Override
  public void shutdown(ShutdownInput shutdownInput) {
    //TODO: handle shutdown
  }
}
