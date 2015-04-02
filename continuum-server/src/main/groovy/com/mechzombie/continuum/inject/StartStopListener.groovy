package com.mechzombie.continuum.inject

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.servlet.GuiceServletContextListener
import com.mechzombie.continuum.Continuum
import com.mechzombie.continuum.server.ContinuumServer

import javax.servlet.ServletContextEvent

/**
 * Created by David on 3/23/2015.
 */
class StartStopListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {

        return Guice.createInjector(new ContinuumServerModule(), new ContinuumRESTModule())

    }

    @Override
    void contextInitialized(ServletContextEvent event) {
        this.getInjector() //.getBinding(ContinuumServer)
        //event.getServletContext().set
    }

    @Override
    void contextDestroyed(ServletContextEvent event) {

    }
}
