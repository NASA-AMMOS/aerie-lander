# Building Aerie Lander

## Pre-Requisites 

| Name    | Version | Notes        | Download                 |
|---------|---------|--------------|--------------------------|
| OpenJDK | 17.0.X  | HotSpot JVM  | https://adoptium.net/temurin |
| Gradle  | 7.4     | Build system | https://gradle.org       |

## Instructions

### Building

Run `./gradlew classes`.

The dependencies on which the compilation of the aerie-lander mission model rely can be provided by either a 
local [aerie project](https://github.com/NASA-AMMOS/aerie) or as regular gradle libraries sourced from Github's 
maven package repository.

**Building with a local aerie project**

`./gradlew build -PuseLocalAerie=true -PlocalAeriePath=<your-local-path-to-aerie>` 

If preferred, the `useLocalAerie` and `localAeriePath` parameters can be set permanently 
in the `gradle.properties`
 


### Testing

Run `gradle test`.

### Updating Dependencies

Run `gradle dependencyUpdates` to view a report of the project dependencies that are have updates available or are up-to-date.
