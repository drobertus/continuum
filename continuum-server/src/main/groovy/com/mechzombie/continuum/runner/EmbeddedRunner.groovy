package com.mechzombie.continuum.runner

import com.mechzombie.continuum.services.ContinuumMonitor
import com.sun.jersey.spi.container.servlet.ServletContainer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

/**
 * Created by David on 3/19/2015.
 */
class EmbeddedRunner implements Runnable {

    Server server
    def serverPort
    EmbeddedRunner(int serverPort = 4455) {
       this.serverPort = serverPort
    }

    void shutdown() {
        server.stop()
    }

    String getPathToRESTInterface() {
        return "http://localhost:${serverPort}/continuum/rest"
    }

    @Override
    void run() {
        server = new Server(serverPort)
        println "the rest server should be starting!"
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS)
        context.setContextPath('/continuum')
        ServletHolder sh = new ServletHolder(new ServletContainer())
        sh.setInitParameter('com.sun.jersey.config.property.resourceConfigClass',
            'com.sun.jersey.api.core.PackagesResourceConfig')
        sh.setInitParameter("com.sun.jersey.config.property.packages","com.mechzombie.continuum.runner")
        // com.sun.jersey.spi.container.servlet.ServletContainer
        // com.sun.jersey.config.property.packages
        // com.sun.jersey.spi.container.servlet.ServletContainer
        context.addServlet(sh, '/*') //'com.sun.jersey.spi.container.servlet.ServletContainer', '/rest/*')
        //context.setInitParameter("com.sun.jersey.config.property.packages","com.mechzombie.continuum.runner")
        // set up the goodies
        server.setHandler(context)
        //com.sun.jersey.config.property.packages

        server.start()
        println "the rest server should be started!"
        //server.join()

    }
//
//    <servlet>
//    <servlet-name>Jersey REST Service</servlet-name>
//    <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
//    <!-- Register resources and providers under com.vogella.jersey.first package. -->
//    <init-param>
//    <param-name>jersey.config.server.provider.packages</param-name>
//        <param-value>com.vogella.jersey.first</param-value>
//    </init-param>
//    <load-on-startup>1</load-on-startup>
//    </servlet>
//  <servlet-mapping>
//    <servlet-name>Jersey REST Service</servlet-name>
//    <url-pattern>/rest/*</url-pattern>
//  </servlet-mapping>
}
