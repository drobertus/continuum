package com.mechzombie.continuum

import com.mechzombie.continuum.client.ContinuumClient
import com.mechzombie.continuum.persistence.MongoPersistence
import com.mechzombie.continuum.protocol.ContinuumTypeMsg
import com.mechzombie.continuum.protocol.LoginResponse
import com.mechzombie.continuum.protocol.StandardMsg
import com.mechzombie.continuum.runner.EmbeddedRunner
import com.mongodb.BasicDBObject
import com.mongodb.BasicDBObjectBuilder
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.MongoClient
import com.mongodb.WriteConcern
import groovy.json.JsonParser
import groovy.json.internal.JsonParserCharArray
import spock.lang.Specification

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class ClientIntegTest extends Specification {

    EmbeddedRunner runningServer
    String rootPath
    MongoClient mongoClient
    DB mongoDB
    DBCollection users

    def userToFind = 'bob'
    def userPassword = 'pass'

    def "test making a continuum type"() {
        expect:

        def client = new ContinuumClient(rootPath)
        def result = client.login(userToFind, 'pass')
        //JsonParser parser = new JsonParserCharArray()

        //StandardMsg msg = parser.parse(result.toCharArray())
        println 'msgType = ' + result.getMsgBody().getMessageType()

        def sessid = result.msgBody.sessionId
        assertNotNull sessid
        println " login = ${result}"
        println "sessionId= ${sessid}"

        def contTypes = client.getContinuumTypes(sessid)
        contTypes.each {
            println 'contType ' + it
        }
        //TODO: escape spaces and non-REST compliant characters

        ContinuumTypeMsg newType = client.createContinuumType('bob', 'meeting') // type')
        assertNotNull newType
        assertEquals newType.name, 'meeting'
        //TODO: parse into protocol object
    }

    void setup() {
        runningServer = new EmbeddedRunner()
        Thread.start runningServer.run()
        rootPath = runningServer.pathToRESTInterface

        mongoClient = new MongoClient('localhost', 27017)
        //setup user 'bob' in MongoDB
        mongoDB = mongoClient.getDB(MongoPersistence.DB_NAME)
        //clear the

        if(!mongoDB.collectionExists(MongoPersistence.USER_COLLECTION)) {
            DBObject options = new BasicDBObject("capped", true)
                .append("size", 500)
            users = mongoDB.createCollection(MongoPersistence.USER_COLLECTION, options)
           users.createIndex(new BasicDBObject('username', 1))
        }
        else {
            users = mongoDB.getCollection(MongoPersistence.USER_COLLECTION)
        }

        DBObject userToAdd = new BasicDBObjectBuilder().add('username',userToFind)
            .add('password', userPassword).get()
        println userToAdd.toString()
        users.insert(userToAdd, WriteConcern.NORMAL)
        println "user count at start = ${users.count()}"
        //users.
    }
    void cleanup() {
        users.drop()
        mongoDB.dropDatabase()
        runningServer.shutdown()
    }

}