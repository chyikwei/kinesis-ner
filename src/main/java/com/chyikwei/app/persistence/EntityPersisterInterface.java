package com.chyikwei.app.persistence;

import java.util.List;

import com.chyikwei.app.ner.ObjectEntitiesInterface;


/**
 * class that persist extracted entities
 */
public interface EntityPersisterInterface {

  /**
   * Initialize persiter
   */
  public void initialize();

  /**
   * Persist list of objects
   */
  public void persister(List<ObjectEntitiesInterface> objectEntities);

  /**
   * Persister should flush its buffer and persist all pending records
   */
  public void checkpoint();


}
