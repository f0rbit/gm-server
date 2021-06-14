# Basic Platformer

This is an example of how to use the gm-server framework to implement a very basic online platformer.
This example does not cover handling clients disconnecting or connecting, or moving rooms.

# Demonstration

In order to run this on your local machine follow these steps

1. Download `platformer-basic` to your machine
2. Open your terminal and location to where you downloaded the code above
3. Navigate into `server`
4. Run `gradlew install`, this will install the the shaded jar `platformer-basic-1.0-SNAPSHOT-all.jar` into `build/libs`
5. Run `java -jar build/libs/platformer-basic-1.0-SNAPSHOT-all.jar`
6. Unzip `client-application.zip`
7. Navigate inside the unzipped folder and run `client.exe` twice, in order to see it working.
8. Move around in both clients.
