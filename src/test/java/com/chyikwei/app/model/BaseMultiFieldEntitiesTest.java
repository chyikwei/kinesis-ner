package com.chyikwei.app.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class BaseMultiFieldEntitiesTest {
  static final int numField = 10;
  static final int numEntities = 20;

  UUID testId;
  MultiFieldEntities objEntities;
  BaseMultiFieldEntitiesFactory factory;

  @Before
  public void setUp() throws Exception {
    testId = UUID.randomUUID();
    factory = BaseMultiFieldEntitiesFactory.getInstance();
    objEntities = factory.newObject(testId);

    for (int i=0; i < numField; i++) {
      for (int j=0; j < numEntities; j++) {
        Entity entity = new Entity("TYPE_" + i, "NAME_" + j);
        objEntities.addEntities("Field_" + i, entity);
      }
    }
  }

  @Test
  public void getUUID() throws Exception {
    assertEquals(testId, objEntities.getUUID());
  }

  @Test
  public void getEntities() throws Exception {
    Map<String, Set<Entity>> map = objEntities.getEntities();
    for (int i=0; i < numField; i++) {
      String key = "Field_" + i;
      assertTrue(map.containsKey(key));
      Set<Entity> entSet = map.get(key);
      for (int j=0; j < numEntities; j++) {
        Entity entity = new Entity("TYPE_" + i, "NAME_" + j);
        assertTrue(entSet.contains(entity));
      }
    }
  }

  @Test
  public void addEntities() throws Exception {
    // new records
    Entity e_old = new Entity("TYPE_0", "NAME_0");
    Entity e_new = new Entity("TYPE_0", "NAME_0");

    // new field
    assertTrue(objEntities.addEntities("FIELD_" + numField, e_old));
    // new entity
    assertTrue(objEntities.addEntities("FIELD_0", e_new));

    // duplicates
    assertFalse(objEntities.addEntities("FIELD_0", e_old));
  }

}