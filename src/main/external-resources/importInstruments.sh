#!/bin/sh
# java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportInstrumentsCSV <CSVFILENAME> <SPRINGFILE> 
java -classpath .:aq2o-2.0-SNAPSHOT-jar-with-dependencies.jar com.activequant.utils.ImportInstrumentsCSV instruments.csv fwspring.xml