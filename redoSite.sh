#!/bin/sh
while true; do 
	mvn clean site:site
	sleep 30
done
