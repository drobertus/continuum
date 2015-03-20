package com.mechzombie.continuum.client

import com.mechzombie.continuum.client.dto.Glossary


interface ContinuumClientInterface {


    def login(String user, String pass)

    def getOngoingData(Date startDate, Date endDate)

    def getContinuumDetails(def id)

    Glossary getGlossary(String continuumType)

    def getContinuumTypes()

    def createContinuumType(String typeName)



}