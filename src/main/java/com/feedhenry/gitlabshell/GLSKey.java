package com.feedhenry.gitlabshell;

public class GLSKey {

  private String keyId;
  private String key;
  
  public GLSKey(String keyId, String key) {
    this.keyId = keyId;
    this.key = key;
  }
  
  public String getKeyId() {
    return keyId;
  }
  
  public String getKey() {
    return key;
  }
}
