#!/bin/bash

cd "$(dirname "$0")"

echo " Courier Tracking System Starting..."

if ! docker info > /dev/null 2>&1; then
  echo "Docker Desktop is not running."
  read -p "Press Enter to exit..."
  exit 1
fi

docker compose up --build

read -p "Press Enter to close..."


