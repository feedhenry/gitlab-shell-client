package com.feedhenry.gitlabshell;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.jcraft.jsch.*;

public class GLSClient {
  
  private String user;
  private String host;
  private int port;
  private String publicKey;
  private String privateKey;
  
  private GLSClient(Builder builder) {
    this.user = builder.user;
    this.host = builder.host;
    this.port = builder.port;
    this.publicKey = builder.publicKey;
    this.privateKey = builder.privateKey;
  }

  public List<GLSKey> listKeys() throws Exception {
    List<GLSKey> keys = new ArrayList<GLSKey>();
    List<String> res = executeCommand("~/gitlab-shell/bin/gitlab-keys list-keys");
    for (String keyLine : res) {
      String[] keyParts = keyLine.split(" ");
      String comment = keyParts.length > 2 ? keyParts[2] : null;
      String keyId = keyParts[0].replaceFirst("^key-", "");
      keys.add(new GLSKey(keyId, keyParts[1], comment));
    }
    return keys;
  }

  /**
   * 
   * @param keyId user identifier for this key i.e. the id that permissions are checked against
   * @param fullKey the full ssh key, inlcuding any ssh comment e.g. "ssh-rsa AAAAbcde user@example.com"
   * @throws Exception
   */
  public void addKey(String keyId, String fullKey) throws Exception {
    Validate.notBlank(keyId, "keyId must not be null or an empty string");
    Validate.notBlank(fullKey, "fullKey must not be null or an empty string");
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-keys add-key %s \"%s\"", "key-" + keyId, fullKey));
  }
  
  /**
   * 
   * @param keyId user identifier of the key to remove i.e. the id that permissions are checked against
   * @throws Exception
   */
  public void rmKey(String keyId) throws Exception {
    Validate.notBlank(keyId, "keyId must not be null or an empty string");
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-keys rm-key %s", "key-" + keyId));
  }
  
  public List<GLSProject> listProjects() throws Exception {
    List<GLSProject> keys = new ArrayList<GLSProject>();
    List<String> res = executeCommand("~/gitlab-shell/bin/gitlab-projects list-projects");
    for (String projectName : res) {
      keys.add(new GLSProject(projectName.replaceFirst(".git$", "")));
    }
    return keys;
  }
  
  public void addProject(String projectName) throws Exception {
    Validate.notBlank(projectName, "projectName must not be null or an empty string");
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-projects add-project %s", projectName + ".git"));
  }
  
  public void rmProject(String projectName) throws Exception {
    Validate.notBlank(projectName, "projectName must not be null or an empty string");
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-projects rm-project %s", projectName + ".git"));
  }
  
  public List<String> executeCommand(String command) throws Exception {
    JSch jsch = getJSch();

    Session session = jsch.getSession(user, host, port);
    session.setConfig("StrictHostKeyChecking", "no");
    session.setTimeout(20000); // 20 seconds
    session.connect();
    
    Channel channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    InputStream in=channel.getInputStream();
    InputStream errs = ((ChannelExec) channel).getErrStream();
    channel.connect();
    
    StringBuilder res = new StringBuilder();
    StringBuilder err = new StringBuilder();
    int exitStatus = 0;
    byte[] resTmp = new byte[1024];
    byte[] errTmp = new byte[1024];
    while (true) {
      while(in.available() > 0) {
        int i = in.read(resTmp, 0, 1024);
        if (i < 0) break;
        res.append(resTmp);
      }
      while(errs.available() > 0) {
        int i = errs.read(errTmp, 0, 1024);
        if (i < 0) break;
        err.append(errTmp);
      }
      if (channel.isClosed()) {
        if (in.available() > 0) continue;
        exitStatus = channel.getExitStatus();
        break;
      }
      Thread.sleep(100);
    }
    if (err.length() > 0) {
      throw new Exception("Unable to process command (" + err.toString() + ")");
    }
    channel.disconnect();
    session.disconnect();
    
    return Arrays.asList(res.toString().split("\\n"));
  }
  
  private JSch getJSch() throws Exception {
    JSch jsch = new JSch();
    
    // remove all identities, then add the configured one from properties
    jsch.removeAllIdentity();
    jsch.addIdentity(user, privateKey.getBytes(Charset.defaultCharset()), publicKey.getBytes(Charset.defaultCharset()), null);
    return jsch;
  }
  
  public static class Builder {
    private String user;
    private String host;
    private int port;
    private String privateKey;
    private String publicKey;
    
    public Builder user(String user) {
      this.user = user;
      return this;
    }
    
    public Builder host(String host) {
      this.host = host;
      return this;
    }
    
    public Builder port(int port) {
      this.port = port;
      return this;
    }
    
    public Builder privateKey(String privateKey) {
      this.privateKey = privateKey;
      return this;
    }
    
    public Builder publicKey(String publicKey) {
      this.publicKey = publicKey;
      return this;
    }

    public GLSClient build() {
      return new GLSClient(this);
    }
  }
}
