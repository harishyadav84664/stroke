#!/usr/bin/env bash
set -e
echo "==================================================="
echo "  NIDUS - Neuroimaging Stroke Identification Model"
echo "  Clinical Reading Room Workstation (MySQL Mode)"
echo "==================================================="
echo ""
echo "Starting application with MySQL..."
echo "Access NIDUS at: http://localhost:8080"
echo ""

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
java -jar "$SCRIPT_DIR/target/app-dashboard-0.1.0.jar" --spring.profiles.active=mysql
