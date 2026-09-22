#!/bin/sh
set -eu

export RABBITMQ_DEFAULT_USER="${RABBITMQ_DEFAULT_USER:-app}"
export RABBITMQ_DEFAULT_PASS="${RABBITMQ_PASSWORD:-app}"

exec docker-entrypoint.sh "$@"
