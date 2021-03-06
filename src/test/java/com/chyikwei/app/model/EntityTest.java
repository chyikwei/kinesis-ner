package com.chyikwei.app.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class EntityTest {

  @Test
  public void equals() throws Exception {
    Entity e1 = new Entity("T1", "E1");
    // same type & name
    assertEquals(e1, new Entity("T1", "E1"));

    // same type but diff name
    assertNotEquals(e1, new Entity("T1", "E2"));
  }

  @Test
  public void hashcode() throws Exception {
    Entity e1 = new Entity("T1", "E1");
    // same type & name
    assertEquals(e1.hashCode(), new Entity("T1", "E1").hashCode());

    // same type but diff name
    assertNotEquals(e1.hashCode(), new Entity("T1", "E2").hashCode());
  }

  public static class NewsObjectEntitiesTest {

    static final int numField = 10;
    static final int numEntities = 20;

    UUID testId;
    MultiFieldEntities newsEntities;


    @Before
    public void setUp() throws Exception {
      testId = UUID.randomUUID();
      newsEntities = new BaseMultiFieldEntities(testId);

      for (int i=0; i < numField; i++) {
        for (int j=0; j < numEntities; j++) {
          Entity entity = new Entity("TYPE_" + i, "NAME_" + j);
          newsEntities.addEntities("Field_" + i, entity);
        }
      }
    }

    @Test
    public void getUUID() throws Exception {
      assertEquals(testId, newsEntities.getUUID());
    }

    @Test
    public void getEntities() throws Exception {
      Map<String, Set<Entity>> map = newsEntities.getEntities();
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
      assertTrue(newsEntities.addEntities("FIELD_" + numField, e_old));
      // new entity
      assertTrue(newsEntities.addEntities("FIELD_0", e_new));

      // duplicates
      assertFalse(newsEntities.addEntities("FIELD_0", e_old));
    }

  }
}