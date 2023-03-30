jar cvf framework.jar -C framework/build/web/WEB-INF/classes/ etu002038
mv framework.jar Test/build/web/WEB-INF/lib/
jar cvf temporaire.war -C  Test/build/web .  
mv temporaire.war /opt/apache-tomcat-9.0.73/webapps/


