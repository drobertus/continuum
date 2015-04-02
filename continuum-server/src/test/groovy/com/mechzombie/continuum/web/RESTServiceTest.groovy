package com.mechzombie.continuum.web

import com.mechzombie.continuum.protocol.MsgObjectConverter
import com.mechzombie.continuum.protocol.ServerObjConverter
import com.mechzombie.continuum.server.ContinuumServer
import spock.lang.Shared
import spock.lang.Specification

import static groovy.util.GroovyTestCase.assertEquals


class RESTServiceTest extends Specification {

    ContinuumServer continuumServer
    ContinuumREST servlet

    @Shared
    ServerObjConverter converter = new ServerObjConverter()

    def "unsuccessful login"() {
        setup:
            def name = 'Bob'
            def password = 'pass'
           // continuumServer.login(_) >> null
        when:
            def response = servlet.login(name, password)

        then:
            assertEquals '{"messageCategory":"StandardMsg","msgBody":{"messageType":"com.mechzombie.continuum.protocol.ErrorMsg","errMsg":"Either username not found or password incorrect"}}',
                response
            1 * continuumServer.login(name, password) >> null

    }


    def "successful login"() {
        setup:
        def name = 'Bob'
        def password = 'pass'
        def userId = 10045

        when:
        def response = servlet.login(name, password)

        then:
        assertEquals '{"messageCategory":"StandardMsg","msgBody":{"sessionId":"10045","messageType":"com.mechzombie.continuum.protocol.LoginResponse","name":"Bob"}}',
            response
        1 * continuumServer.login(name, password) >> userId

    }

    void setup() {
        continuumServer = Mock(ContinuumServer)
        //converter = new MsgObjectConverter()
        servlet = new ContinuumREST(continuumServer, converter)
    }



}