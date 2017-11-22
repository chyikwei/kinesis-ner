package com.chyikwei.app.model;

/**
 * Entity is a string tuple with type of this entity and the value of it.
 * Example: ("PERSON", "John")
 */
public class Entity {

  public final String entityType;
  public final String entityName;

  public Entity(String type, String name) {
    entityType = type;
    entityName = name;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + entityType.hashCode();
    hash = hash * 31 + entityName.hashCode();
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Entity) {
      Entity entity = (Entity) obj;
      return this.entityType.equals(entity.entityType)
          && this.entityName.equals(entity.entityName);
    }
    return false;
  }

  @Override
  public String toString() {
    return entityType + "__" + entityName;
  }
}
