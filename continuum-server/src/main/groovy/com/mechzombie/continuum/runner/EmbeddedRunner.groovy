package com.mechzombie.continuum.runner

import com.google.inject.servlet.GuiceFilter
import com.mechzombie.continuum.inject.StartStopListener
import com.mechzombie.continuum.server.ContinuumServer
import com.mechzombie.continuum.services.ContinuumMonitor
import com.sun.jersey.spi.container.servlet.ServletContainer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

/**
 * Created by David on 3/19/2015.
 */
class EmbeddedRunner implements Runnable {

    Server server
    def serverPort
    ContinuumServer contServ

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
        //TODO get object instantiation under injection

        server = new Server(serverPort)
        println "the rest server should be starting!"
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS)
        context.setContextPath('/continuum')
        ServletHolder sh = new ServletHolder(new DefaultServlet())// new ServletContainer())
        context.addEventListener(new StartStopListener())
        context.addFilter(GuiceFilter.class, '/*', null)
//        sh.setInitParameter('com.sun.jersey.config.property.resourceConfigClass',
//            'com.sun.jersey.api.core.PackagesResourceConfig')
//        sh.setInitParameter("com.sun.jersey.config.property.packages","com.mechzombie.continuum.web")
        context.addServlet(sh, '/*')
        server.setHandler(context)

        server.start()
        println "the rest server should be started!"
    }

}
