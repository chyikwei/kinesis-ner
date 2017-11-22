package com.chyikwei.app.util;

import java.util.concurrent.TimeUnit;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Modified from amazon-kinesis-data-visualization-sample
 */
public class StreamUtils {
  private static final Log LOG = LogFactory.getLog(StreamUtils.class);
  private static final long CREATION_WAIT_TIME_IN_SECONDS = TimeUnit.SECONDS.toMillis(30);
  private static final long DELAY_BETWEEN_STATUS_CHECKS_IN_SECONDS = TimeUnit.SECONDS.toMillis(30);

  /**
   *
   * @param streamName stream name
   * @param shards num of shard in the stream
   * @return `true` is table created, 'false' if table already existed
   */
  public static boolean createKinesisStream(AmazonKinesis kinesis, final String streamName, int shards) {
    try {
      if (isActive(kinesis, streamName)) {
        return false;
      }
    } catch (ResourceNotFoundException ex) {
      // create stream
      LOG.info(String.format("Creating stream %s...", streamName));
      kinesis.createStream(streamName, shards);
      try {
        Thread.sleep(CREATION_WAIT_TIME_IN_SECONDS);
      } catch (InterruptedException e) {
        LOG.warn(String.format("Interrupted while waiting for %s stream to become active. Aborting.", streamName));
        Thread.currentThread().interrupt();
        return true;
      }
    }

    // Wait for stream to become active
    int maxRetries = 3;
    int i = 0;
    while (i < maxRetries) {
      i++;
      try {
        if (isActive(kinesis, streamName)) {
          return true;
        }
      } catch (ResourceNotFoundException ignore) {
        // The stream may be reported as not found if it was just created.
      }
      try {
        Thread.sleep(DELAY_BETWEEN_STATUS_CHECKS_IN_SECONDS);
      } catch (InterruptedException e) {
        LOG.warn(String.format("Interrupted while waiting for %s stream to become active. Aborting.", streamName));
        Thread.currentThread().interrupt();
        return true;
      }
    }
    throw new RuntimeException("Stream " + streamName + " did not become active within 2 minutes.");

  }

    public static boolean isActive(AmazonKinesis kinesis, String streamName) {
      String status = kinesis.describeStream(streamName).getStreamDescription().getStreamStatus();
      return "ACTIVE".equals(status);
    }


}
