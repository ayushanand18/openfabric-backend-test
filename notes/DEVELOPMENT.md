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
2. Postgresql Setup of Ubuntu (execution permissions error)
    a. To install execute
    ```sh
    sudo apt upgdate
    sudo apt install postgresql
    ```
    b. To start a postgre server execute:
    ```sh
    systemctl restart postgresql
    # or if systemctl is not running on your system due to its overhead then write
    service postgresql restart

    # to check service status
    ss -nlt | grep 5432
    ```
    3. Make sure to change the authentication mode to md5 for password based logins at `/etc/postgresql/12/main/pg_hba.conf`
    [This StackOverflow Thread would help](https://stackoverflow.com/questions/18664074/getting-error-peer-authentication-failed-for-user-postgres-when-trying-to-ge)
    4. Now restart the server.
    5. Create the user `openfabric` with the following command:
    ```postgre
    CREATE USER openfabric WITH PASSWORD '{password}';
    ```
    6. Edit the `pg_hba.conf` file again to change from trust to md5.
    7. Restart the server.
    8. Run the command to create new database named `test`
    ```sh
    psql -U postgres
    CREATE DATABASE test;
    GRANT ALL PRIVILEGES ON DATABASE test TO postgres;
    ```
