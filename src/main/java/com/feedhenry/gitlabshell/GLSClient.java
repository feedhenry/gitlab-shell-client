package com.feedhenry.gitlabshell;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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
      keys.add(new GLSKey(keyParts[0], keyParts[1]));
    }
    return keys;
  }
  
  public void addKey(String keyId, String key) throws Exception {
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-keys add-key %s %s", keyId, key));
  }
  
  public void rmKey(String keyId) throws Exception {
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-keys rm-key %s", keyId));
  }
  
  public List<GLSProject> listProjects() throws Exception {
    List<GLSProject> keys = new ArrayList<GLSProject>();
    List<String> res = executeCommand("~/gitlab-shell/bin/gitlab-projects list-projects");
    for (String projectName : res) {
      keys.add(new GLSProject(projectName));
    }
    return keys;
  }
  
  public void addProject(String projectName) throws Exception {
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-projects add-project %s", projectName));
  }
  
  public void rmProject(String projectName) throws Exception {
    executeCommand(String.format("~/gitlab-shell/bin/gitlab-projects rm-project %s", projectName));
  }
  
  public List<String> executeCommand(String command) throws Exception {
    JSch jsch = getJSch();

    Session session = jsch.getSession(user, host, port);
    session.setConfig("StrictHostKeyChecking", "no");

    session.connect();
    Channel channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    channel.connect();
    StringBuilder err = new StringBuilder();
    List<String> res = new ArrayList<String>();
    Scanner errScanner = new Scanner(((ChannelExec) channel).getErrStream());
    Scanner resScanner = new Scanner(channel.getInputStream());
    resScanner.useDelimiter(Pattern.compile("\\n"));
    while (true) {
      while(errScanner.hasNext()) {
        err.append(errScanner.next());
      }
      while(resScanner.hasNext()) {
        res.add(resScanner.next());
      }
      if (channel.isClosed()) {
        break;
      }
      Thread.sleep(100);
    }
    if (!err.toString().isEmpty()) {
      throw new Exception("Unable to process command (" + err.toString() + ")");
    }
    channel.disconnect();
    session.disconnect();
    
    return res;
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
