#!/bin/sh

svn up
mvn clean compile exec:java -Dexec.mainClass="dk.teachus.tools.Release"
