package com.chyikwei.app;

import java.util.UUID;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.chyikwei.app.kcl.StreamProcessorFactory;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersisterConfig;
import com.chyikwei.app.util.StreamUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;

public class KinesisNerApplication {
  private static final Log LOG = LogFactory.getLog(KinesisNerApplication.class);

  // TODO: move to property file
  private static final Regions regions = Regions.US_EAST_1;
  private static final String appName = "kinesis-ner-sample";
  private static final String streamName = "ner-sample-stream";
  private static final String dynamoTableName = "ddb-news-entities";
  private static final String dynamoTableHashKey = "uuid";
  private static final int numOfShards = 1;

  /**
   * Start kinesis NER application
   *
   * @param args
   */
  public static void main(String[] args) {

    AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
    ClientConfiguration config = new ClientConfiguration();

    LOG.info("Creating dynamoDB client...");
    AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withRegion(regions).build();
    DynamoDB ddb = new DynamoDB(dynamoDBClient);

    LOG.info("Creating kinesis client...");
    AmazonKinesis kinesis = AmazonKinesisClientBuilder.standard().withRegion(regions).build();
    StreamUtils.createKinesisStream(kinesis, streamName, numOfShards);

    String workerId = String.valueOf(UUID.randomUUID());
    LOG.info(String.format("generate worker id: %s", workerId));

    KinesisClientLibConfiguration kclConfig =
        new KinesisClientLibConfiguration(appName, streamName, credentialsProvider, workerId);
    kclConfig.withCommonClientConfig(config);
    kclConfig.withRegionName(regions.getName());
    kclConfig.withInitialPositionInStream(InitialPositionInStream.LATEST);

    // persister
    DynamoEntityPersisterConfig persisterConfig = new DynamoEntityPersisterConfig(
        dynamoTableName, dynamoTableHashKey);

    // stream recordProcessor
    IRecordProcessorFactory streamProcessorFactory = new StreamProcessorFactory(ddb, persisterConfig);

    LOG.info("start KCL worker...");
    final Worker worker = new Worker.Builder()
        .recordProcessorFactory(streamProcessorFactory)
        .config(kclConfig)
        .build();

    int exitCode = 0;
    try {
      worker.run();
    } catch (Throwable t) {
      LOG.error("Caught throwable while processing data.", t);
      exitCode = 1;
    }
    System.exit(exitCode);
  }
}
