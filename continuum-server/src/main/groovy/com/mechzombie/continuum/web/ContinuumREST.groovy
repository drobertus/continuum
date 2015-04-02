package com.mechzombie.continuum.web

import com.mechzombie.continuum.protocol.ContinuumTypeMsg
import com.mechzombie.continuum.protocol.ErrorMsg
import com.mechzombie.continuum.protocol.LoginResponse
import com.mechzombie.continuum.protocol.MsgObjectConverter
import com.mechzombie.continuum.protocol.ServerObjConverter
import com.mechzombie.continuum.server.ContinuumServer
import groovy.util.logging.Log

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Log
@Path('/rest')
class ContinuumREST {

    ContinuumServer server
    ServerObjConverter converter

    @Inject
    ContinuumREST(ContinuumServer server, ServerObjConverter converter) {
        this.server = server
        this.converter = converter
    }

    @GET
    @Produces('text/json')
    @Path('/login/{userName}/{password}')
    String login(@PathParam("userName") String userName, @PathParam("password")String pass ) {
        log.info "login from ${userName} with pass ${pass}"
        def userSessionId = server.login(userName, pass)


        //def responseMsg = new StandardMsg(msgBody: )
        def body
        if(userSessionId){
            body = new LoginResponse(name: userName, sessionId: userSessionId)
        } else {
            body = new ErrorMsg(errMsg: "Either username not found or password incorrect")
        }
        //def responseMsg = new StandardMsg(msgBody: body)
        //(msgType: 'loginReply', msgBody: loginResp)
        return converter.toStandardMsgString(body)
        //return new JsonBuilder(responseMsg).toString()
    }

    @GET
    @Produces('text/json')
    @Path('/createContinuumType/{id}/{continuumTypeName}')
    String createContinuumType(@PathParam("id") String id, @PathParam("continuumTypeName") String name) {
    //return 'thing'
        //TODO: some authentication, check permissions
        def ct = server.createContinuumType(id, name)

        //convert from server to protocol version

        ContinuumTypeMsg ctProtocol = new ContinuumTypeMsg(name: ct.getNameAsString())
        //StandardMsg msg = new StandardMsg(msgType: 'createContinuumResponse',
        //msgBody: ctProtocol)
        return converter.toStandardMsgString(ctProtocol)// new JsonBuilder(msg).toString()
    }


    @GET
    @Produces('text/json')
    @Path('/getContinuumTypes/{userId}')
    String getContinuumTypes(@PathParam('userId')String userId) {
        //return all the names

        return 'a,b,c'
    }


}
