package com.mechzombie.continuum.client

import com.mechzombie.continuum.client.dto.Glossary
import sun.net.www.protocol.http.HttpURLConnection


class ContinuumClient implements ContinuumClientInterface {

    def rootPath
    ContinuumClient(String rootPath) {
        this.rootPath = rootPath
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
    def getContinuumDetails(def Object id) {
        return null
    }

    @Override
    Glossary getGlossary(String continuumType) {
        return null
    }

    @Override
    def getContinuumTypes() {
        return null
    }

    @Override
    def createContinuumType(String typeName) {
        return null
    }
}
