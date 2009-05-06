#!/bin/sh

git pull
mvn clean compile exec:java -Dexec.mainClass="dk.teachus.tools.NightlySnapshot"
