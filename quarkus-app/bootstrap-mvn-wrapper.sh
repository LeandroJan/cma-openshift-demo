#!/bin/sh
set -e

if command -v mvn >/dev/null 2>&1; then
  echo "Generating Maven wrapper..."
  mvn -N io.takari:maven:wrapper
  echo "Done. Use ./mvnw to build or run in dev mode."
else
  echo "ERROR: 'mvn' not found in PATH. Install Maven or run this on a host with Maven available."
  exit 1
fi
