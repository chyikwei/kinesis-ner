package com.chyikwei.app.model;

import com.chyikwei.app.model.Entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Object to store entities from different fields
 */
public interface MultiFieldEntities {

  /**
   * get UUID of the object
   */
  public UUID getUUID();

  /**
   * get entities in the object (field -> set of entities)
   *
   * @return map of field to entities
   */
  public Map<String, Set<Entity>> getEntities();

  /**
   * add entities
   *
   * @param field field in the object
   * @param entity value of the entity
   *
   * @return true if this is a new entity. false if entity already stored
   */
  public boolean addEntities(String field, Entity entity);
}
