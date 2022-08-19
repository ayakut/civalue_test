# syntax=docker/dockerfile:1.0-experimental

#----------------------- THE BUILDING JAR STAGE -----------------------
FROM maven:3.6.3-jdk-11 AS jar-builder

# create work folders
RUN mkdir -p /opt/project

# copy project sources
COPY . /opt/project

# create and deploy JAR packages
WORKDIR /opt/project/parent
ARG DUMMY_USER
ARG DUMMY_PASS
RUN mvn -DskipTests=true -Dfile.encoding=UTF-8 -q -e -U clean deploy
#----------------------- END OF THE BUILDING JAR STAGE -----------------------

#----------------------- THE BUILDING IMAGE STAGE -----------------------
FROM adoptopenjdk/openjdk8

# Never prompts the user for choices on installation/configuration of packages
ENV DEBIAN_FRONTEND noninteractive
ENV TERM linux

# update packages and configure OS
# The ENV's value could be prvided as a parameter in the docker-build command, e.g. --build-arg ENV=local
ARG ENV
RUN if [ "$ENV" = "local" ] ; \
    then \
        echo "Not all packages will updated for local environment" \
        && apt-get update \
        && apt-get install -y --no-install-recommends \
            htop mc locales dumb-init less telnet gzip traceroute netcat; \
    else \
        apt-get update \
        && apt-get install -y --no-install-recommends \
            gcc mc htop libc6-dev netcat curl traceroute git procps vim sudo less openssh-client locales dumb-init \
        && rm -rf /var/lib/apt/lists/*; \
    fi
# https://stackoverflow.com/a/38553499
RUN sed -i -e 's/# en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=en_US.UTF-8
# switch to en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8
ENV LC_CTYPE en_US.UTF-8
ENV LC_MESSAGES en_US.UTF-8

# copy project related files into the image
RUN mkdir -p /opt/app/log
WORKDIR /opt/app
# The JAR_PREFIX's value must be prvided as a parameter in the docker-build command,
# e.g. --build-arg JAR_PREFIX=HotelsCatalog
ARG JAR_PREFIX
COPY --from=jar-builder "/opt/project/target/${JAR_PREFIX}*.jar" ./app.jar
COPY docker/entrypoints ./entrypoints

#ensure the copied scripts could be executed + removing \r-sumbol
RUN for script_file in ./entrypoints/*.sh; \
    do \
        echo "Processing: $script_file"; \
        chmod +x $script_file; \
        sed -i 's/\r//g' $script_file; \
    done

# define work ports
EXPOSE ${API_PORT:-3000}
EXPOSE 55555
EXPOSE 9000/udp

#define default entry-point
ENTRYPOINT ["dumb-init", "./entrypoints/start.sh"]
CMD ["run_app"]
#----------------------- END -----------------------