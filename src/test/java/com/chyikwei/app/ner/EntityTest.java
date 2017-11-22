package com.chyikwei.app.ner;

import org.junit.Test;

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
}