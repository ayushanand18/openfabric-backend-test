# DEVELOPMENT NOTES
> This document contains all the challenges/issues faced while setting up the environment and creating the project.

1. Gradle Version Compatibility with Installed JAVA Version
    + The configuration of JAVA on my system was `openjdk 17.07` which did not support Gradle 6.8 as set earlier in the `gradle-wrapper.properties` file.
    ```sh
    $ java -version
    openjdk version "17.0.7" 2023-04-18 LTS
    OpenJDK Runtime Environment Microsoft-7626293 (build 17.0.7+7-LTS)
    OpenJDK 64-Bit Server VM Microsoft-7626293 (build 17.0.7+7-LTS, mixed mode, sharing)
    ```
    ```properties
    distributionUrl=https\://services.gradle.org/distributions/gradle-6.8-all.zip
    # changed to
    distributionUrl=https\://services.gradle.org/distributions/gradle-7.4-all.zip
    ```
