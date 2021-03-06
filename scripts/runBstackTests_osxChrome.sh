#!/usr/bin/env bash
#2 arguments passed in when running through DevOps (VStack)
localID=$1
browserstackUrl=$2
currentDateTime="`date +%Y%m%d%H%M%S`";

mvn clean verify -Pbstack \
-Dbrowser=osxChrome \
-Dbrowserstack.url=$browserstackUrl \
-Dlocal.id=$localID \
-Dbuild=osxChrome$currentDateTime