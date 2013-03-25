mvn clean
mvn net.sf.doodleproject:doxygen-maven-plugin:report
mvn javadoc:javadoc
sudo rm /var/www/doxygen/ -rf
sudo rm /var/www/apidocs -rf
sudo cp -r target/doxygen /var/www
sudo cp -r target/site/apidocs /var/www
