# METHOD NOTES
> While the [DEVELOPMENT.MD](./DEVELOPMENT.md) file contains all the challenges/issues faced while building this project, this file includes the method involved in creating the project.

## Initial Steps
1. The first step was to obviously setup the environment, with the right JAVA version and Gradle Version. I overrun many issues while getting the dev environment setup because I was doing it on an Ubuntu Cloud VM. (I prefer cloud environments more than a local dev environment so that I do not mess up with other projects and global configurations.) The issues and challenges have been widely discussed [here](./DEVELOPMENT.md).

2. To ease out the process, I will list down each objective and add comments on how I implemented them.

    + 🔄 Integrate the 🐳 Docker Java library (https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md) to enable connect and manage 👷‍♀️ workers.
    > I added the dependency in the `build.gradle` file and also created `DockerConfig.java` to enable configurations to the Docker Service. 

    + Add the required fields and database migration for the 👷‍♂️ Worker entity to keep **all** the information associated on available at the container level (like 🔌 ports, 👨‍💼 name, 🟢 status, etc..)
    > The `respository/WorkerStatRespository.java` queries the DB to get the information of workers running.

    + Add required entities and tables to track the 👷‍♂️ Worker statistics.
    > The `model/WorkerStats.java` creates a class of WorkerStats with appropriate data members to monitor CPU Usage, etc. 

    + Update the 👷‍♂️ WorkerController to add actions for:
        - * 📄 List workers (paginated)  *`service/WorkerService.java` has the method `getWorkerStat()` to list out workers.*
        - * ▶️ Start and ⏹️ Stop worker   *`service/DockerService.java` has the required methods defined to stop and start workers.*
        - * 🔍 Get worker information
        - * 📊 Get worker statistics     *`service/DockerService.java` prints the stats using `workerStats()` method*
3. Steps to run the  Service
    a. export environment variables (Spring was not able to read env variables from `.env` file on my environment).
    ```sh
    export SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/test
    export SPRING_DATASOURCE_USERNAME=postgres
    export SPRING_DATASOURCE_PASSWORD=postgres
    ```
    b. Restart the postgresql server (or check if it is already running).
    ```sh
    sudo service postgresql restart
    ```
    c. (Optional) If you do not have JAVA_HOME as v11 setup, please do so. (This project runs uses Gradle 6.8 which is compatible with JAVA 11. I have tried testing with other versions of JAVA but v16+ do not work. So, v11 is a safe choice.)
    ```sh
    sudo update-alternatives --config java
    export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
    ```
    d. Run gradle run in the root of the project
    ```sh
    ./gradlew run --no-daemon
    # since I'm running this service on a cloud VM, therefore adding the --no-daemon flag to reduce memory usage.
    ```

Thanks!