#!/usr/bin/env bash

mvn clean verify \
-Pgrid \
-Denvironment=preprod \
-Dcucumber.options="--tags @Preprod"
