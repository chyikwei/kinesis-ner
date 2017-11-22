package com.chyikwei.app.model;

import java.util.*;

/**
 * Basic implementation of MultiFieldEntities
 */
public class BaseMultiFieldEntities implements MultiFieldEntities {

  private UUID uuid;
  private Map<String, Set<Entity>> entityMap;

  public BaseMultiFieldEntities(UUID uid) {
    this.uuid = uid;
    entityMap = new HashMap<>();
  }

  /**
   *{@inheritDoc}
   */
  @Override
  public UUID getUUID() {
    return uuid;
  }

  /**
   *{@inheritDoc}
   */
  @Override
  public Map<String, Set<Entity>> getEntities() {
    return entityMap;
  }

  /**
   *{@inheritDoc}
   */
  @Override
  public boolean addEntities(String field, Entity entity) {
    Set<Entity> set;
    if (entityMap.containsKey(field)) {
      set = entityMap.get(field);
      return set.add(entity);
    }
    else {
      set = new HashSet<>();
      set.add(entity);
      entityMap.put(field, set);
      return true;
    }
  }
}
