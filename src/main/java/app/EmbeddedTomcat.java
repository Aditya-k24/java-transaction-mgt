package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.glassfish.jersey.servlet.ServletContainer;

public class EmbeddedTomcat {

	public static Tomcat start() throws Exception {

		// https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat

		String webappDirLocation = "src/main/webapp/";

		Tomcat tomcat = new Tomcat();

		// The port that we should run on can be set into an environment variable
		// Look for that variable and default to 8080 if it isn't there.
		String webPort = System.getProperty("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}

		tomcat.setPort(Integer.valueOf(webPort));

		StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

		// Configure Jersey servlet
		Tomcat.addServlet(ctx, "jersey", new ServletContainer(new JerseyConfig()));
		ctx.addServletMappingDecoded("/api/*", "jersey");

		// Declare an alternative location for your "WEB-INF/classes" dir
		// Servlet 3.0 annotation will work
		File additionWebInfClasses = new File("target/classes");
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
		ctx.setResources(resources);

		tomcat.start();

		return tomcat;
	}

	public static void waitForExit(Tomcat tomcat) throws IOException, LifecycleException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

			do {
				System.out.println("type EXIT to stop application");
			} while (!"exit".equalsIgnoreCase(reader.readLine()));

			tomcat.stop();
		}
	}

}