package com.feedhenry.gitlabshell;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GLSClientTest {
  
  private GLSClient client;
  
  @Before
  public void setUp() throws Exception {
    client = Mockito.spy(new GLSClient.Builder()
    .host("testhost")
    .port(22)
    .user("testuser")
    .publicKey("testpubkey")
    .privateKey("testprivkey").build());
  }

  @After
  public void tearDown() throws Exception {
    client = null;
  }

  
  @Test
  public void testAddProjects() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());

    client.addProject("testrepo1");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-projects add-project testrepo1.git"));

    client.addProject("ns1/testrepo2");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-projects add-project ns1/testrepo2.git"));
  }
  
  @Test(expected=NullPointerException.class)
  public void testAddProjectNullParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.addProject(null);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testAddProjectEmptyParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.addProject("");
  }
  
  @Test
  public void testRmProjects() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());

    client.rmProject("testrepo1");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-projects rm-project testrepo1.git"));

    client.rmProject("ns1/testrepo2");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-projects rm-project ns1/testrepo2.git"));
  }
  
  @Test(expected=NullPointerException.class)
  public void testRmProjectNullParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.rmProject(null);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testRmProjectEmptyParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.rmProject("");
  }
  
  @Test
  public void testListProjects() throws Exception {
    // test projects
    ByteArrayOutputStream mockRes = Mockito.mock(ByteArrayOutputStream.class);
    Mockito.when(mockRes.toString()).thenReturn("testrepo1.git\nns1/testrepo2.git\nns1/ns2/testrepo3.git");
    Mockito.doReturn(mockRes).when(client).executeCommand(Mockito.anyString());
    
    String[] projects = client.listProjects();
    assertEquals(3, projects.length);
    assertEquals("testrepo1", projects[0]);
    assertEquals("ns1/testrepo2", projects[1]);
    assertEquals("ns1/ns2/testrepo3", projects[2]);
  }
  
  @Test
  public void testAddKey() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());

    client.addKey("user1", "ssh-rsa AAAA1234");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-keys add-key key-user1 \"ssh-rsa AAAA1234\""));

    client.addKey("user2", "ssh-rsa AAAA5678 user2@example.com");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-keys add-key key-user2 \"ssh-rsa AAAA5678 user2@example.com\""));
  }
  
  @Test(expected=NullPointerException.class)
  public void testAddKeyNullParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.addKey(null, "AAAA1234");
  }
  
  @Test(expected=NullPointerException.class)
  public void testAddKeyNullParams2() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.addKey("user1", null);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testAddKeyEmptyParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.addKey("", "AAAA1234");
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testAddKeyEmptyParams2() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.addKey("user1", "");
  }
  
  @Test
  public void testRmKey() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());

    client.rmKey("user1");
    Mockito.verify(client, Mockito.times(1)).executeCommand(Mockito.eq("~/gitlab-shell/bin/gitlab-keys rm-key key-user1"));
  }
  
  @Test(expected=NullPointerException.class)
  public void testRmKeyNullParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.rmKey(null);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testRmKeyEmptyParams1() throws Exception {
    Mockito.doReturn(null).when(client).executeCommand(Mockito.anyString());
    client.rmKey("");
  }
    
  @Test
  public void testListKeys() throws Exception {
    // test projects
    ByteArrayOutputStream mockRes = Mockito.mock(ByteArrayOutputStream.class);
    Mockito.when(mockRes.toString()).thenReturn("key-user1 AAAA1234 user1@example.com\nkey-user1 AAAA5678 user1@example.com@1234\nkey-user2 AAAA8765 user2\nkey-user3 AAAA9876");
    Mockito.doReturn(mockRes).when(client).executeCommand(Mockito.anyString());
    
    List<GLSKey> keys = client.listKeys();
    assertEquals(4, keys.size());
    GLSKey key = keys.get(0);
    assertEquals("user1", key.getKeyId());
    assertEquals("AAAA1234", key.getKey());
    assertEquals("user1@example.com", key.getComment());
    key = keys.get(1);
    assertEquals("user1", key.getKeyId());
    assertEquals("AAAA5678", key.getKey());
    assertEquals("user1@example.com@1234", key.getComment());
    key = keys.get(2);
    assertEquals("user2", key.getKeyId());
    assertEquals("AAAA8765", key.getKey());
    assertEquals("user2", key.getComment());
    key = keys.get(3);
    assertEquals("user3", key.getKeyId());
    assertEquals("AAAA9876", key.getKey());
    assertEquals(null, key.getComment());
  }

}
