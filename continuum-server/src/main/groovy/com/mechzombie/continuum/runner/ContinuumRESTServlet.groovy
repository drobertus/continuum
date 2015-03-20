package com.mechzombie.continuum.runner

import groovy.util.logging.Log

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

//TODO: make this a Jersey REST servlet with Guice injection
@Log
@Path('/rest')
class ContinuumRESTServlet {

    @GET
    @Produces('text/html')
    @Path('/login/{userName}/{password}')
    String login(@PathParam("userName") String userName, @PathParam("password")String pass ) {
        println "login from ${userName} with pass ${pass}"
        return 'STUFF!'
    }

}
