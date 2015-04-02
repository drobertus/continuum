package com.mechzombie.continuum.protocol

import groovy.json.internal.LazyMap
import groovy.transform.CompileStatic

import java.lang.reflect.Field

@CompileStatic
trait JsonSerializable{

    String getMessageType() {
        this.class.name
    }

    void populateMe(LazyMap map){
        String type = map.messageType
        Class<?> theClass = type as Class
        map.keySet().each {key ->
            if (key != 'messageType') {
                Field fld = theClass.getDeclaredField((String) key)
                fld.setAccessible(true)
                fld.set(this, map.get(key))
            }
        }
    }
}