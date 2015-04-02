package com.mechzombie.continuum.protocol

/**
 * Created by David on 2/23/2015.
 */
class GlossaryMsg implements JsonSerializable{

    Map<String, GlossaryEntryMsg> entries = [:]

    @Override
    String getMessageType() {
        'Glossary'
    }
}
