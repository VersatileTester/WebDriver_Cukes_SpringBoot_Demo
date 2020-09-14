#!/usr/bin/env bash

mvn clean verify \
-Pgrid \
-Denvironment=test \
-Dcucumber.options="--tags @Test"