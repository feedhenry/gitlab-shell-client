# 3.0.1

- use byte reading instead of scanner to fix issue with blocking indefinitely on err scanner next check

# 3.0.0

- Added unit tests
- omit 'key-' prefix when getting key name or adding/removing a key (underlying gitlab-shell command will add it)
- omit '.git' suffix when getting project name or adding/removing a project (underlying gitlab-shell command will add it)
- removed project name setter