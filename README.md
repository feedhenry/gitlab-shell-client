# Gitlab Shell Client

Java Client for remote administration of Gitlab Shell

## Installation

### Maven (pom.xml)

```xml
<dependency>
    <groupId>com.feedhenry.gitlabshell</groupId>
    <artifactId>gitlab-shell-client</artifactId>
    <version>1.2.1</version>
</dependency>
```

### Gradle (build.gradle)

```groovy
dependencies {
  compile 'com.feedhenry.gitlabshell:gitlab-shell-client:1.2.1'
}
```

## Usage

```java
GLSClient client = new GLSClient.Builder()
  .user("git")
  .host("127.0.0.1")
  .port(22)
  .publicKey("ssh-rsa AAAA....")
  .privateKey("-----BEGIN RSA PRIVATE KEY-----\nMIIEow....\n-----END RSA PRIVATE KEY-----")
  .build();

List<GLSKey> keys = client.listKeys();

List<GLSProject> projects = client.getProjects();

client.addProject("myrepos/repo1.git");

client.rmProject("myrepos/repo1.git");

client.addKey("user1", "ssh-rsa AAAA....");

client.rmKey("user1");
```

To run a custom ssh command

```java
client.executeCommand("echo 'hello'");
```
