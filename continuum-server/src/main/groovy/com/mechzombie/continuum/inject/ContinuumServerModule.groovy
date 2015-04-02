package com.mechzombie.continuum.inject

import com.google.inject.AbstractModule
import com.google.inject.Provider
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.mechzombie.continuum.persistence.ContinuumPersistence
import com.mechzombie.continuum.persistence.MongoPersistence
import com.mechzombie.continuum.protocol.MsgObjectConverter
import com.mechzombie.continuum.server.ContinuumServer
import com.mongodb.MongoClient


class ContinuumServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ContinuumServer.class).in(Singleton.class)
        bind(ContinuumPersistence.class).to(MongoPersistence.class).in(Singleton.class)
        bind(MsgObjectConverter.class).in(Singleton.class)
    }

    @Provides
    @Named(value="mongoClient")
    MongoClient getMongoClient() {
        new MongoClient("localhost", 27017);
    }
}
