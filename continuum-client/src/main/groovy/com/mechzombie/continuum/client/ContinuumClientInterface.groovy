package com.mechzombie.continuum.client

import com.mechzombie.continuum.protocol.Continuum
import com.mechzombie.continuum.protocol.ContinuumType
import com.mechzombie.continuum.protocol.Glossary


interface ContinuumClientInterface {


    def login(String user, String pass)

    ContinuumType createContinuumType(String userId, String typeName)

    def getOngoingData(Date startDate, Date endDate)

    Continuum getContinuum(def id)

    Glossary getGlossary(String continuumType)

    List<String> getContinuumTypes(String userId)





}