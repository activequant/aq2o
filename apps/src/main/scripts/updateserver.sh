echo "Updating server ..."
rm aq2o-apps-2.2-SNAPSHOT-bundle.zip
wget http://user:user@server.activequant.com/downloads/aq2o-apps-2.2-SNAPSHOT-bundle.zip
unzip aq2o-apps-2.2-SNAPSHOT-bundle.zip aq2o/*.jar
mv aq2o/* .
rmdir aq2o
rm aq2o-apps-2.2-SNAPSHOT-bundle.zip

echo "Done. Please restart"