#!/bin/sh
# java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportMarketDataInstrumentsCSV <CSVFILENAME> <SPRINGFILE> 

CLASSPATH=.:$AQ_HOME/target/aq2o-2.2-SNAPSHOT-jar-with-dependencies.jar
java -classpath $CLASSPATH com.activequant.utils.ImportMarketDataInstrumentsCSV marketdatainstruments.csv fwspring.xml