#!/bin/sh

set +x

case "$1" in
  run_app)
    exec java -jar /opt/app/app.jar --spring.config.location=file:/opt/app/settings/application.properties
    ;;
  *)
    # The command is something like bash. Just run it in the right environment.
    exec "$@"
    ;;
esac