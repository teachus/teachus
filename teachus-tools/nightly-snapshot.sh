#!/bin/sh

svn up > target/nightly-snapshot.log
mvn clean compile exec:java -Dexec.mainClass="dk.teachus.tools.NightlySnapshot" >> target/nightly-snapshot.log
