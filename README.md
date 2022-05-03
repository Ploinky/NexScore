[![CI](https://github.com/Ploinky/NexScore/actions/workflows/gradle.yml/badge.svg)](https://github.com/Ploinky/NexScore/actions/workflows/gradle.yml)


# NexScore
Nexus last hit counter for League of Legends

Keeps a statistic showing in how many games League of Legends players have last hit the opponents Nexus.
Only statistics for registered players will be kept, starting from the time they are registered.

# Install
Runs on Java 11, using Gradle.

# Official instance
Hosted using AWS Elastic Beanstalk on http://nexscore-env.eba-prnmp5zi.eu-central-1.elasticbeanstalk.com/.

# API Endpoints
| Endpoint | Information | Parameters |
| - |:-:| - |
| / | Shows information on the API | N/A |
| /greeting | Shows a generic greeting | <ul><li>*greeting*: Use this greeting instead of 'Hello'</li></ul> |
