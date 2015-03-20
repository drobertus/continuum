package com.mechzombie.continuum

import com.mechzombie.continuum.client.ContinuumClient
import com.mechzombie.continuum.runner.EmbeddedRunner
import spock.lang.Specification

import static org.junit.Assert.assertEquals

class ClientIntegTest extends Specification {

    EmbeddedRunner runningServer
    String rootPath

    def "test making a continuum type"() {
        expect:

        def client = new ContinuumClient(rootPath)
        def result = client.login('bob', 'pass')
        assertEquals 'STUFF!', result
        println " login = ${result}"
    }

    void setup() {
        runningServer = new EmbeddedRunner()
        Thread.start runningServer.run()
        rootPath = runningServer.pathToRESTInterface
    }
    void cleanup() {
        runningServer.shutdown()
    }

}