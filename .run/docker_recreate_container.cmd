FOR /F "tokens=*" %%g in (settings/app.env) do SET %%g
docker container stop "%PROJECT_TAG%_container"
docker container rm "%PROJECT_TAG%_container"
docker run ^
    -p 3001:%API_PORT% ^
    -p 9000:9000/udp ^
    --privileged ^
    -v "%cd:\=/%/settings:/opt/app/settings" ^
    --name %PROJECT_TAG%_container ^
    -d --net civalue-dockers-network --rm -i -t %PROJECT_TAG%:latest