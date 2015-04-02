package com.mechzombie.continuum.protocol

//import com.mechzombie.continuum.ContinuumType
import groovy.json.JsonBuilder
import groovy.json.JsonParser
import groovy.json.internal.JsonParserCharArray
import groovy.json.internal.LazyMap
import groovy.transform.CompileStatic

@CompileStatic
class MsgObjectConverter {

    JsonBuilder builder = new JsonBuilder()
    JsonParser parser = new JsonParserCharArray()



    StandardMsg toStandardMsg(JsonSerializable body) {
        StandardMsg msg = new StandardMsg(msgBody: body)
        return msg
    }

    String toStandardMsgString(JsonSerializable body) {
        return toStandardMsg(body).toJSON(builder)
    }

    StandardMsg getStdMsgFromString(String msg) {
        def stdMsg = (StandardMsg)parser.parse(msg)
        LazyMap body =(LazyMap)stdMsg.msgBody
        String type = body.messageType
        def theClass = type as Class
        JsonSerializable instance = (JsonSerializable)theClass.newInstance()
        instance.populateMe(body)
        stdMsg.msgBody = instance
        return stdMsg
    }
}
