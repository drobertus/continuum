package com.mechzombie.continuum.web

import com.mechzombie.continuum.protocol.ContinuumType
import com.mechzombie.continuum.protocol.StandardMsg
import com.mechzombie.continuum.server.ContinuumServer
import com.sun.jersey.core.header.MediaTypes
import groovy.json.JsonBuilder
import groovy.util.logging.Log

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context

//TODO: make this a Jersey REST servlet with Guice injection
@Log
@Path('/rest')
class ContinuumRESTServlet {

    ContinuumServer server// = ContinuumServer.instance

    @Inject
    ContinuumRESTServlet(ContinuumServer server) {
        this.server = server
    }

    @GET
    @Produces('text/json')
    @Path('/login/{userName}/{password}')
    String login(@PathParam("userName") String userName, @PathParam("password")String pass ) {
        println "login from ${userName} with pass ${pass}"
        def userSessionId = server.login(userName, pass)
        def loginResp = [:]
        if(userSessionId){
            loginResp.put('sessionId', userSessionId)
        } else {
            loginResp.put('error', 'either username not found or password incorrect')
        }

        def responseMsg = new StandardMsg(msgType: 'loginReply', msgBody: loginResp)
        return new JsonBuilder(responseMsg).toString()

    }

    @GET
    @Produces('text/json')
    @Path('/createContinuumType/{id}/{continuumTypeName}')
    String createContinuumType(@PathParam("id") String id, @PathParam("continuumTypeName") String name) {
        //return 'thing'
        //TODO: some authentication, check permissions
        def ct = server.createContinuumType(id, name)

        //convert from server to protocol version

        ContinuumType ctProtocol = new ContinuumType(name: ct.getNameAsString())
        StandardMsg msg = new StandardMsg(msgType: 'createContinuumResponse',
        msgBody: ctProtocol)
        return new JsonBuilder(msg).toString()
    }

    @GET
    @Produces('text/json')
    @Path('/getContinuumTypes/{userId}')
    String getContinuumTypes(@PathParam('userId')String userId) {
        //return all the names

        return 'a,b,c'
    }

}
