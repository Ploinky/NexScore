[![checkstyle/build/deploy](https://github.com/Ploinky/NexScore/actions/workflows/checkstyle_build_deploy.yml/badge.svg)](https://github.com/Ploinky/NexScore/actions/workflows/checkstyle_build_deploy.yml)

# NexScore
Nexus last hit counter for League of Legends

Keeps a statistic showing in how many of their games League of Legends players have last hit the opponents Nexus.
Only statistics for registered players will be kept, starting from the time they are registered.

But why would I need such a statistic I hear you say? Well you really don't.

# Install
Runs on Java 11, using Gradle.

# Official instance
Hosted using AWS Elastic Beanstalk on http://nexscore-env.eba-3z3vn5cc.eu-central-1.elasticbeanstalk.com/.

# API Endpoints
| Endpoint | Information | Parameters |
| - |:-:| - |
| / | Shows information on the API | N/A |
| /player | POST a new player to be tracked | <ul><li>*name*: The current summoner name of the player to be added</li></ul> |
| /players | Get a List of all tracked players | N/A |
