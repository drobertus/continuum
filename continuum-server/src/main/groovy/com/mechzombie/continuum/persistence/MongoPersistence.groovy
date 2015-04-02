package com.mechzombie.continuum.persistence

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.name.Named
import com.mongodb.BasicDBObject
import com.mongodb.BasicDBObjectBuilder
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBCursor
import com.mongodb.DBObject
import com.mongodb.MongoClient

/**
 * Created by David on 3/24/2015.
 */
class MongoPersistence implements ContinuumPersistence {

    static final String DB_NAME = 'continuum'
    static final String USER_COLLECTION = 'users'
    static final String contColl = 'continuum'
    static final String phaseColl = 'phases'
    static final String glossColl = 'glossaries'
    static final String contTypeColl = 'continuum_type'

    private MongoClient mongoClient
    private DB mongoDB
    private DBCollection users

    @Inject
    MongoPersistence(@Named("mongoClient")MongoClient client){
        this.mongoClient = client
        mongoDB = mongoClient.getDB(DB_NAME)
        users = mongoDB.getCollection(USER_COLLECTION)
    }
//
//    def getMongoClient() {
//
//        if (!mongoClient) {
//            mongoClient = new MongoClient("localhost", 27017);
//            mongoDB = mongoClient.getDB(dbName)
//        }
//        mongoDB.getCollectionNames().each {
//            println "client go ${it}"
//        }
//        return mongoClient
//    }

    @Override
    Object findUser(String userName, String password) {

        println 'user count before quesry= ' +users.count()
        //ReadPreference readPrefs = new ReadPreference
        def userQuery = new BasicDBObjectBuilder().add('username', userName)
            .add('password', password).get()
        def userFinder = users.find(userQuery)
        println "user count = ${userFinder.count()}"

        if(userFinder.hasNext()){
           def sessionID = UUID.randomUUID().toString()

            return sessionID
        }
        return null
//
//        if(userFinder.count() == 1) {
//            DBObject theUser = userFinder.next()
//            println theUser.toString()
//            def id = UUID.randomUUID().toString()
//            userSessions.put( id, user)
//            return id
//        }
//        else {
//            DBCursor cursor = users.find()
//            println 'user count = ' +users.count()
//            while(cursor.hasNext())
//                println( "object= ${cursor.next().toString()}")
//        }
//        return null
    }
}
