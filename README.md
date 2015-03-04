# Trydent
Simple JavaFX GUI Engine.

# Quick Start Guide

Install Gradle, either via your favorite package manager or [here](https://gradle.org/).

To build our project, run `gradle build`. The first run may take a little while as it downloads the corresponding
dependencies.

To run only our tests, run `gradle test`. For other useful commands, see `gradle tasks`.

To see some blinking lights, look in `src/main/java/edu/gatech/cs2340/trydent/sample`. By default, `gradle build` outputs .class and .jar files to the `build/` directory in the root of your repository. For example, try:

`java -cp build/libs/Trydent.jar edu.gatech.cs2340.trydent.sample.AnimationExample`
