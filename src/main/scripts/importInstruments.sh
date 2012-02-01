#!/bin/sh
# java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportInstrumentsCSV <CSVFILENAME> <SPRINGFILE>

CLASSPATH=.:$AQ_HOME/target/site/distribution/aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar
java -classpath $CLASSPATH com.activequant.utils.ImportInstrumentsCSV instruments.csv fwspring.xml