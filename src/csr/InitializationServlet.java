package com.cisco.ettx.provisioning;

import org.apache.log4j.PropertyConfigurator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;


/** Initializes Config read and logging at the moment. Could also ultimately 
 * put methods on this to allow refresh of config, etc.
 */
public class InitializationServlet extends HttpServlet {

  public void init() {
    String log4jfile = Config.getInstance().getLog4JConfig();
    if ((log4jfile == null) || (log4jfile.length() == 0)) {
        System.out.println("No Log4J configuration applied.");
        return;
    }
    
    String prefix =  getServletContext().getRealPath("/");    
    System.out.println("Loading Log4J config " + prefix+log4jfile);
    // if the log4j-init-file is not set, then no point in trying
    PropertyConfigurator.configure(prefix+log4jfile);
  }

  public
  void doGet(HttpServletRequest req, HttpServletResponse res) {
  }
}

