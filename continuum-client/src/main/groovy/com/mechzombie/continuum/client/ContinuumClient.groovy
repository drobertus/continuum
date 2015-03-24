package com.mechzombie.continuum.client

import com.mechzombie.continuum.protocol.Continuum
import com.mechzombie.continuum.protocol.ContinuumType
import com.mechzombie.continuum.protocol.Glossary
import com.mechzombie.continuum.protocol.StandardMsg
import groovy.json.JsonParser
import groovy.json.internal.JsonParserCharArray

class ContinuumClient implements ContinuumClientInterface {


    def sessionId
    def rootPath

    JsonParser parser

    ContinuumClient(String rootPath) {
        this.rootPath = rootPath
        parser = new JsonParserCharArray()
    }

    @Override
    def login(String user, String pass) {
        def result = "${rootPath}/login/${user}/${pass}".toURL().getText()
        println "got result ${result}"
        return result
    }



    @Override
    def getOngoingData(Date startDate, Date endDate) {
        return null
    }

    @Override
    Continuum getContinuum(def Object id) {
        return null
    }

    @Override
    Glossary getGlossary(String continuumType) {
        return null
    }

    @Override
    List<String> getContinuumTypes(String userId) {
        def result = "${rootPath}/getContinuumTypes/${userId}".toURL().getText()

        return result.split(',')
    }

    @Override
    ContinuumType createContinuumType(String userId, String typeName) {
        def response =  "${rootPath}/createContinuumType/${userId}/${URLEncoder.encode(typeName)}".toURL().getText()
        //return null;
        ContinuumType theCT = (ContinuumType)getStdMsg(response).msgBody
        return theCT
    }

    private StandardMsg getStdMsg (String response) {

        StandardMsg  stdMsg = parser.parse(response)
        return stdMsg
    }
}
