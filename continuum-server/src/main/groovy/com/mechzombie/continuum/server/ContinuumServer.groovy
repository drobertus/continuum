package com.mechzombie.continuum.server

import com.google.inject.Inject
import com.mechzombie.continuum.ContinuumType
import com.mongodb.BasicDBObject
import com.mongodb.BasicDBObjectBuilder
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBCursor
import com.mongodb.DBObject
import com.mongodb.MongoClient


/**
 * This is the main "intersection" between the client calls and the
 * back-end services
 */
class ContinuumServer {

    Map<String, String> userSessions = [:]


    static final String dbName = 'continuum'
    static final String userColl = 'users'
    static final String contColl = 'continuum'
    static final String phaseColl = 'phases'
    static final String glossColl = 'glossaries'
    static final String contTypeColl = 'continuum_type'

    private MongoClient mongoClient
    private DB mongoDB
    private DBCollection users

    @Inject
    ContinuumServer() {

    }

    def login(String user, String pass) {
        if(!users) {
            getMongoClient()
            if (!mongoDB.collectionExists(userColl)) {
                DBObject options = new BasicDBObject("capped", true)
                    .append("size", 500)
                users = mongoDB.createCollection(userColl, options)
            }
            else {
                users = mongoDB.getCollection(userColl)
            }
        }
        println 'user count before quesry= ' +users.count()
        //ReadPreference readPrefs = new ReadPreference
        def userQuery = new BasicDBObjectBuilder().add('username', user)
            .add('password', pass).get()
        def userFinder = users.find(userQuery)
        println "user count = ${userFinder.count()}"
        if(userFinder.count() == 1) {
            DBObject theUser = userFinder.next()
            println theUser.toString()
            def id = UUID.randomUUID().toString()
            userSessions.put( id, user)
            return id
        }
        else {
            DBCursor cursor = users.find()
            println 'user count = ' +users.count()
            while(cursor.hasNext())
                println( "object= ${cursor.next().toString()}")
        }
        return null
    }


    ContinuumType createContinuumType(String userName, String continuumTypeName) {
        def ct = new ContinuumType(continuumTypeName)
        println 'created a new continuum type ' + continuumTypeName
        return ct
    }

    List<ContinuumType> getAllContinuumTypes(String userName) {
        //stuff to do!
    }

    def getMongoClient() {

        if (!mongoClient) {
            mongoClient = new MongoClient("localhost", 27017);
            mongoDB = mongoClient.getDB(dbName)
        }
        mongoDB.getCollectionNames().each {
            println "client go ${it}"
        }
        return mongoClient
    }

}
