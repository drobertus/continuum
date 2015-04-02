package com.mechzombie.continuum.protocol

import groovy.json.JsonBuilder


class StandardMsg {

    static def messageCategory = 'StandardMsg'

    def msgBody

    String toJSON(JsonBuilder builder = new JsonBuilder()) {
        builder.setContent(this)
        return builder.toString()
    }


}
