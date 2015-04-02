package com.mechzombie.continuum.inject

import com.google.inject.Singleton
import com.mechzombie.continuum.web.ContinuumREST
//import com.mechzombie.continuum.web.ContinuumRESTService
import com.sun.jersey.api.core.PackagesResourceConfig
import com.sun.jersey.guice.JerseyServletModule
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer


class ContinuumRESTModule extends JerseyServletModule {

    @Override
    void configureServlets() {
        // excplictly bind GuiceContainer before binding Jersey resources
        // otherwise resource won't be available for GuiceContainer
        // when using two-phased injection
        bind(GuiceContainer.class);

       // bind (ContinuumREST.class).to(ContinuumRESTService.class)in(Singleton.class)

        PackagesResourceConfig resourceConfig = new PackagesResourceConfig('com.mechzombie.continuum.web')
        for (Class<?> resource : resourceConfig.getClasses()) {
            bind(resource);
        }

        serve("/*").with(GuiceContainer.class);
    }
}
