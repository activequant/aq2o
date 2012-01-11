#!/bin/sh
# java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportMarketDataInstrumentsCSV <CSVFILENAME> <SPRINGFILE> 
java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportMarketDataInstrumentsCSV instruments.csv fwspring.xml