#!/usr/bin/env bash

mvn clean verify \
-Pgrid \
-Denvironment=sandpit \
-Dcucumber.options="--tags @Sandpit"