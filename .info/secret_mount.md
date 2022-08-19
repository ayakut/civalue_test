Source: https://codefresh.io/docker-tutorial/announcing-docker-buildkit-support/

**Secret Storage with Buildkit**
Accessing private repositories or resources has always been an issue with the traditional Docker build engine.  Even if you removed secret files after use, the secrets could end up in the metadata of the image.  With Docker 18.09, a new –secrets flag was added that allows users to pass secret information to a Docker build, without storing that information in the final image.

Note that the usage of “secrets” here is in terms of temporary secrets needed as part of the build, i.e. private SSH keys needed for cloning a private repository.  Do not use this functionality for permanent secrets needed to deploy an application, such as database credentials.

For example, we can create a secret file:

echo “my-test-secret” &lt; secret.txt

Our Dockerfile will consist of the following:
# syntax = docker/dockerfile:1.0-experimental
FROM alpine
# shows secret from default secret location:
RUN --mount=type=secret,id=mysecret cat /run/secrets/mysecret`

Then, to build the image, run:
docker build -t buildkit-secret-demo --secret id=mysecret,src=secret.txt .


This command passes the secret.txt file to the RUN command in the Dockerfile, mounting it at /run/secrets (the default secret location).
If you navigate to the /run/secrets directory in the container, you may be shocked to find your secrets file there, but it is only a blank, 0-byte file.