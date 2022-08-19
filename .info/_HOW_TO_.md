# CONFIGURING PROJECT
## Prepare the app.env file
This file provides meta info as a set of env-variables for configuring image building and running stages.
You can use _**.info/app.env**_ as an example of app.env file. Just edit all parameters and put then 
this file in  the _**./settings/app.env**_

## Prepare the application.properties file
This file is used on the docker-run stage for running your spring-boot-based Java-application. 
You can use _**.info/application.properties**_ as an example of application.properties file. 
Just edit all parameters and put then this file in  the _**./settings/application.properties**_

**Pay attention! You should prepare and provide the application.properties files!
Otherwise, the service will be configured by default dummy values!** 

**For local running, if you use MongoDB and/or Redis services 
that run in docker-containers either then pay attention to host-names for these services, 
probably you need to use their docker-container name as the host-name instead!**

## Build docker
The following example demonstrates how to build your Docker-image.
See: [Docker build reference][l2] or [Docker build extended description][l5]

E.g.:

    docker build -t your_app_tag_container -f docker/app.dockerfile 
        --build-arg ENV=local --build-arg JAR_PREFIX=MyApp
        --build-arg ENV_VAR1="%ENV_VAR1_VALUE%" --build-arg ENV_VAR2="%ENV_VAR2_VALUE%"
        --progress=plain --no-cache .
    
Pay attention to the above example! You should set environment variables and use them for setting build-args.

See: _`.run/docker_build_image.cmd`_ for more certain example

### Dicker image file structure
The project related part have the following structure

- `/opt` - the root dir of the project
    - `/app` - the application-related code (jar-file for the Java-written)
    - `/app/entrypoints` - the application-related scripts and entry-points
    - `/app/log` - the application-related log files
    - `/app/settings` - the application-related setting files

## Run Docker
See: [Docker run reference][l3] and/or [Docker run extended description][l4]
E.g.:
 
    docker run -p 8090:8080 --name your_app_tag_container --privileged ^
        -v <on your host path to>/settings:/opt/app/settings ^
        -d -i -t ^
        --net <use your default net where redis and mongo are deployed or skip this parmeter at all> ^
        your_app_tag:latest

**Pay attention!**

On running the docker you have to mount from your host system a directory containing the following files:
- ./settings/application.properties

You should mount this directory to the /opt/app/settings path in the container
 
See: _`.run/docker_recreate_container.cmd`_ for more certain example

# PROJECT MAINTENANCE
You can find in the **_.run_** sub-directory a lot of _*.cmd_ windows-shell-related scripts that particularly 
could be easily adopted for running in linux-bash interpreter

[l1]: https://docs.docker.com/engine/reference/commandline/run/#set-environment-variables--e---env---env-file

[l2]: https://docs.docker.com/engine/reference/commandline/build/

[l3]: https://docs.docker.com/engine/reference/run/

[l4]: https://docs.docker.com/engine/reference/commandline/run/

[l5]: https://docs.docker.com/engine/reference/commandline/build/
