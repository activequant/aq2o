echo "Starting up AQ2o installation-less JMS server"
java -classpath .:aq2o-2.2-SNAPSHOT-jar-with-dependencies.jar com.activequant.server.LocalJMSServer localhost 61616
