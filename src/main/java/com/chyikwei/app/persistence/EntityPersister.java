package com.chyikwei.app.persistence;

import java.util.List;

import com.chyikwei.app.model.MultiFieldEntities;


/**
 * class that persist extracted entities
 */
public interface EntityPersister {

  /**
   * Initialize persiter
   */
  public void initialize();

  /**
   * Persist list of objects
   */
  public void persister(List<MultiFieldEntities> multiFieldEntities);

  /**
   * Persister should flush its buffer and persist all pending records
   */
  public void checkpoint();


}
