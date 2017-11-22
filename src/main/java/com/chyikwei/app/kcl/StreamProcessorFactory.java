package com.chyikwei.app.kcl;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.chyikwei.app.model.BaseMultiFieldEntitiesFactory;
import com.chyikwei.app.model.NewsTextRecordFactory;
import com.chyikwei.app.model.MultiFieldEntitiesFactory;
import com.chyikwei.app.model.TextRecordFactory;
import com.chyikwei.app.ner.*;
import com.chyikwei.app.persistence.EntityPersister;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersister;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersisterConfig;


/**
 * Used to create StreamProcessor
 */
public class StreamProcessorFactory implements IRecordProcessorFactory {

  private EntityPersister entitypersister;
  private EntityExtractor entityExtractor;
  private TextRecordFactory textRecordFactory;
  private MultiFieldEntitiesFactory entitiesFactory;

  public StreamProcessorFactory(EntityExtractor extractor,
                                EntityPersister persister,
                                TextRecordFactory textFactory,
                                MultiFieldEntitiesFactory entFactory) {
    entitypersister = persister;
    entityExtractor = extractor;
    textRecordFactory = textFactory;
    entitiesFactory = entFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRecordProcessor createProcessor() {
    return new StreamProcessor(entityExtractor, entitypersister, textRecordFactory, entitiesFactory);
  }
}
