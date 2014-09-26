package com.feedhenry.gitlabshell;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GLSProjectTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    GLSProject project = new GLSProject("myproject");
    
    assertEquals("myproject", project.getProjectName());
  }

}
