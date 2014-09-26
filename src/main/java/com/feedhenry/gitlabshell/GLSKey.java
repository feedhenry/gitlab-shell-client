package com.feedhenry.gitlabshell;

public class GLSKey {

  private String keyId;
  private String key;
  private String comment;
  
  public GLSKey(String keyId, String key, String comment) {
    this.keyId = keyId;
    this.key = key;
    this.comment = comment;
  }
  
  /**
   * 
   * @return the user identifier for this key i.e. the key name with the 'key-' prefix removed
   */
  public String getKeyId() {
    return keyId;
  }
  
  /**
   * 
   * @return the ssh public key string
   */
  public String getKey() {
    return key;
  }
  
  /**
   * 
   * @return ssh key comment, or null if there is none
   */
  public String getComment() {
    return comment;
  }
}
