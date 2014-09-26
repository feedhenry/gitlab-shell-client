package com.feedhenry.gitlabshell;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GLSKeyTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    GLSKey key = new GLSKey("mykeyid", "mykeystring", "mykeycomment");
    
    assertEquals("mykeyid", key.getKeyId());
    assertEquals("mykeystring", key.getKey());
    assertEquals("mykeycomment", key.getComment());
  }

}
