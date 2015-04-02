package com.mechzombie.continuum.client

import com.mechzombie.continuum.protocol.ContinuumMsg
import com.mechzombie.continuum.protocol.ContinuumTypeMsg
import com.mechzombie.continuum.protocol.GlossaryMsg
import com.mechzombie.continuum.protocol.MsgObjectConverter
import com.mechzombie.continuum.protocol.StandardMsg
import groovy.json.JsonParser
import groovy.json.internal.JsonParserCharArray

class ContinuumClient implements ContinuumClientInterface {


    def sessionId
    def rootPath
    MsgObjectConverter converter



    //JsonParser parser

    ContinuumClient(String rootPath) {
        this.rootPath = rootPath
        //parser = new JsonParserCharArray()
        converter = new MsgObjectConverter()
    }

    @Override
    def login(String user, String pass) {
        def result = "${rootPath}/login/${user}/${pass}".toURL().getText()
        println "got result ${result}"
        return converter.getStdMsgFromString(result)
        //return result
    }



    @Override
    def getOngoingData(Date startDate, Date endDate) {
        return null
    }

    @Override
    ContinuumMsg getContinuum(def Object id) {
        return null
    }

    @Override
    GlossaryMsg getGlossary(String continuumType) {
        return null
    }

    @Override
    List<String> getContinuumTypes(String userId) {
        def result = "${rootPath}/getContinuumTypes/${userId}".toURL().getText()

        return result.split(',')
    }

    @Override
    ContinuumTypeMsg createContinuumType(String userId, String typeName) {
        def response =  "${rootPath}/createContinuumType/${userId}/${URLEncoder.encode(typeName)}".toURL().getText()
        //return null;
        ContinuumTypeMsg theCT = (ContinuumTypeMsg)getStdMsg(response).msgBody
        return theCT
    }

    private StandardMsg getStdMsg (String response) {

        StandardMsg  stdMsg = converter.getStdMsgFromString(response)
        return stdMsg
    }
}
