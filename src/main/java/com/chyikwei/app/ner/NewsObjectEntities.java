package com.chyikwei.app.ner;

import java.util.*;

public class NewsObjectEntities implements ObjectEntities {

  private UUID uuid;
  private Map<String, Set<Entity>> entityMap;

  public NewsObjectEntities(UUID uid) {
    this.uuid = uid;
    entityMap = new HashMap<>();
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public Map<String, Set<Entity>> getEntities() {
    return entityMap;
  }

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
