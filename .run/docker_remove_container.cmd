FOR /F "tokens=*" %%g in (settings/app.env) do SET %%g
docker container stop "%PROJECT_TAG%_container"
docker container rm "%PROJECT_TAG%_container"
