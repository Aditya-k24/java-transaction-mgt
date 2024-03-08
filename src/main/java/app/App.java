package app;

import org.apache.catalina.startup.Tomcat;

import app.dao.Database;

public class App {
    public static void main(String[] args) throws Exception {
    	Database.startDerbyDatabase();
		Tomcat tomcat = EmbeddedTomcat.start();
		EmbeddedTomcat.waitForExit(tomcat);
		Database.stopDerbyDatabase();
    }
}
