package com.mechzombie.continuum.server

import com.google.inject.Inject
import com.mechzombie.continuum.ContinuumType
import com.mechzombie.continuum.persistence.ContinuumPersistence
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






    private ContinuumPersistence persistence

    @Inject
    ContinuumServer(ContinuumPersistence persistence) {
        this.persistence = persistence
    }

    def login(String user, String pass) {
        def id = persistence.findUser(user, pass)
    }


    ContinuumType createContinuumType(String userName, String continuumTypeName) {
        def ct = new ContinuumType(continuumTypeName)
        println 'created a new continuum type ' + continuumTypeName
        return ct
    }

    List<ContinuumType> getAllContinuumTypes(String userName) {
        //stuff to do!
    }



}
