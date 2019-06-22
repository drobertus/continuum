package com.mechzombie.continuum.protocol

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

class StandardMsg {

    static def messageCategory = 'StandardMsg'

    def msgBody

    String toJSON(JsonBuilder builder = new JsonBuilder()) {
        return JsonOutput.toJson(this)
    }

}
