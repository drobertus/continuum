package com.mechzombie.continuum.client

import com.mechzombie.continuum.client.dto.Glossary


class ContinuumClient implements ContinuumClientInterface {



    @Override
    def login(String user, String pass) {
        return null
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
}
