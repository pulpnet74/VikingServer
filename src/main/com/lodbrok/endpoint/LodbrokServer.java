/**
 * com.lodbrok.endpoint package contains the server and its support classes
 */
package com.lodbrok.endpoint;

import static com.lodbrok.endpoint.tool.LagerthaFactory.DEFAULT_BACKLOG;
import static com.lodbrok.endpoint.tool.LagerthaFactory.KATTEGAT_PORT;
import static com.lodbrok.endpoint.tool.LagerthaFactory.MAIN_CONTEXT;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.lodbrok.control.LodbrokController;
import com.lodbrok.endpoint.tool.LodbrokFilter;
import com.lodbrok.endpoint.tool.LodbrokHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

// JMX support: uncomment the below imports to use JMX
//import static com.lodbrok.endpoint.tool.LagerthaFactory.DEFAULT_DATABASE_SCHEMA;
//import static com.lodbrok.endpoint.tool.LagerthaFactory.DEFAULT_NUMBER_OF_THREADS;
//import java.lang.management.ManagementFactory;
//import javax.management.InstanceAlreadyExistsException;
//import javax.management.JMException;
//import javax.management.MBeanRegistrationException;
//import javax.management.MBeanServer;
//import javax.management.MalformedObjectNameException;
//import javax.management.NotCompliantMBeanException;
//import javax.management.ObjectName;
//import com.lodbrok.jmx.Ragnarok;

/**
 * @author Fabio Riberto
 * 
 *         This class implements the HTTP server itself
 *
 */
public class LodbrokServer {

	/**
	 * This method is the entry point of the server. When server.start() is
	 * called the server switches on and it is ready to accept requests
	 * 
	 * @param args
	 * @throws Exception
	 * 
	 */
	public static void main(String[] args) throws Exception {
		try {
			// Switch the server on
			HttpServer server = HttpServer.create(new InetSocketAddress(KATTEGAT_PORT), DEFAULT_BACKLOG);
			HttpContext context = server.createContext(MAIN_CONTEXT,
					new LodbrokHandler(LodbrokController.getInstance()));
			context.getFilters().add(new LodbrokFilter());
			server.setExecutor(Executors.newCachedThreadPool()); // Threads
																	// creation,
																	// reusing
																	// and
																	// optimization
			server.start();
			System.out.println("\\m/ >_< \\m/    LodbrokServer works for the Kattegat inhabitants @ http://localhost:"
					+ server.getAddress().getPort() + "/" + "    \\m/ >_< \\m/");
			// JMX injection: uncomment and specialize below code to use JMX
			// // Configure JMX
			// MBeanServer mBeanServer =
			// ManagementFactory.getPlatformMBeanServer(); // Instance
			// // of
			// // MBean
			// // server
			// Ragnarok mBeanClient = new Ragnarok(DEFAULT_NUMBER_OF_THREADS,
			// DEFAULT_DATABASE_SCHEMA);
			// ObjectName objName = new
			// ObjectName("com.lodbrok.jmx:type=Ragnarok");
			// mBeanServer.registerMBean(mBeanClient, objName); // MBean client
			// // registration
			// do {
			// // JMX tasks
			// // } while (mBeanClient.getThreadCount() != 0);
			// } catch (JMException e) {
			// System.err.println("JMX unexpected exception -
			// ".concat(e.getMessage()));
		} catch (BindException e) {
			System.err.println("Expected BindException - ".concat(e.getMessage()));
		} catch (Exception e) {
			System.err.println("LodbrokServer reached the Valhalla for the will of Odin");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
