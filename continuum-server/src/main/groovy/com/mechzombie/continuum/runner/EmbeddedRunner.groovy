package com.mechzombie.continuum.runner

import com.google.inject.servlet.GuiceFilter
import com.mechzombie.continuum.inject.StartStopListener
import com.mechzombie.continuum.server.ContinuumServer
import groovy.util.logging.Log
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

@Log
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
        log.info "the rest server should be starting!"
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS)
        context.setContextPath('/continuum')
        ServletHolder sh = new ServletHolder(new DefaultServlet())
        context.addEventListener(new StartStopListener())
        context.addFilter(GuiceFilter.class, '/*', null)
        context.addServlet(sh, '/*')
        server.setHandler(context)

        server.start()
        println "the rest server should be started!"
    }

}
