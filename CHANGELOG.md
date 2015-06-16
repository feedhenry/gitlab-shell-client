# 4.0.1

- Using futures to read stdout & stderr to streams to improve performance

# 4.0.0

- removed GLSProject object in favour of just using a String (Thats all a project is anyways, for now) 

# 3.0.2

- use ByteArrayOutputStream for dumping bytes are they are read

# 3.0.1

- use byte reading instead of scanner to fix issue with blocking indefinitely on err scanner next check

# 3.0.0

- Added unit tests
- omit 'key-' prefix when getting key name or adding/removing a key (underlying gitlab-shell command will add it)
- omit '.git' suffix when getting project name or adding/removing a project (underlying gitlab-shell command will add it)
- removed project name setter