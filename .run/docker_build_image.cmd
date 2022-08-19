FOR /F "tokens=*" %%g in (settings/app.env) do SET %%g
docker build -t "%PROJECT_TAG%" -t "%PROJECT_TAG%:%VERSION%" -f docker/app.dockerfile ^
    --build-arg ENV=local --build-arg JAR_PREFIX=civalue_test ^
    --build-arg DUMMY_USER="%DUMMY_USER%" --build-arg DUMMY_PASS="%DUMMY_PASS%" ^
    --progress=plain --no-cache .
