#!/usr/bin/env bash
set -e
echo "==================================================="
echo "  NIDUS - Neuroimaging Stroke Identification Model"
echo "  Clinical Reading Room Workstation"
echo "==================================================="
echo ""
echo "Starting application with embedded H2 database..."
echo "Access NIDUS at: http://localhost:8080"
echo "H2 Console: http://localhost:8080/h2-console"
echo ""

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
java -jar "$SCRIPT_DIR/target/app-dashboard-0.1.0.jar" --spring.profiles.active=h2
