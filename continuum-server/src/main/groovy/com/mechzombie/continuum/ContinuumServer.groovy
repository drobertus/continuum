package com.mechzombie.continuum

import com.mechzombie.continuum.runner.ContinuumRESTServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler

class ContinuumServer {



    static void main(String[] args) {


        Server server = new Server(4455)


        ServletContextHandler context = new ServletContextHandler()
        context.setContextPath('continuum')
        context.addServlet(ContinuumRESTServlet, 'rest')

        // set up the goodies
        server.setHandler(context)

        server.start()
        server.join()
    }
}
