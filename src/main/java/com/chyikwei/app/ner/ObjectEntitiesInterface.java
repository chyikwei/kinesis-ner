package com.chyikwei.app.ner;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ObjectEntitiesInterface {

  /**
   * get UUID of the object
   */
  public UUID getUUID();

  /**
   * get entities in the object (field -> set of entities)
   *
   * @return map of entities
   */
  public Map<String, Set<Entity>> getEntities();

  /**
   * add entities
   *
   * @param field type of this new entity
   * @param entity value of the entity
   *
   * @return true if this is a new entity. false if entity already stored
   */
  public boolean addEntities(String field, Entity entity);
}
