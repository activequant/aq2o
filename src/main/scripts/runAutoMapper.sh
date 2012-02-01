#!/bin/sh
# java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportMarketDataInstrumentsCSV <CSVFILENAME> <SPRINGFILE> 

CLASSPATH=.:$AQ_HOME/target/site/distribution/aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar
java -classpath $CLASSPATH com.activequant.tools.mapping.Mdi2InstrumentMapper