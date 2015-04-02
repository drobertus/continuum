package com.mechzombie.continuum.client

import com.mechzombie.continuum.protocol.ContinuumMsg
import com.mechzombie.continuum.protocol.ContinuumTypeMsg
import com.mechzombie.continuum.protocol.GlossaryMsg


interface ContinuumClientInterface {


    def login(String user, String pass)

    ContinuumTypeMsg createContinuumType(String userId, String typeName)

    def getOngoingData(Date startDate, Date endDate)

    ContinuumMsg getContinuum(def id)

    GlossaryMsg getGlossary(String continuumType)

    List<String> getContinuumTypes(String userId)





}