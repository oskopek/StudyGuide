#!/bin/bash

mvn versions:set -DnewVersion=$1 -Pall
mvn versions:commit -Pall
