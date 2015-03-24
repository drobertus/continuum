package com.mechzombie.continuum.inject

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import com.mechzombie.continuum.server.ContinuumServer

/**
 * Created by David on 3/23/2015.
 */
class ContinuumServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ContinuumServer.class).in(Singleton.class)
    }
}
