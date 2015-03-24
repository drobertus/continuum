package com.mechzombie.continuum.inject

import com.mechzombie.continuum.web.ContinuumRESTServlet
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

        PackagesResourceConfig resourceConfig = new PackagesResourceConfig('com.mechzombie.continuum.web') //"jersey.resources.package");
        for (Class<?> resource : resourceConfig.getClasses()) {
            bind(resource);
        }

        serve("/*").with(GuiceContainer.class);
    }
}
