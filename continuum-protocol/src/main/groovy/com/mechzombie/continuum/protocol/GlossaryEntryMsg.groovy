package com.mechzombie.continuum.protocol


class GlossaryEntryMsg implements JsonSerializable {

    String name

    //a gloss entry can exist for a type or an instance
    boolean definedAtTypeLevel

    Object associatedObject

    def description

    def cognates = []

    @Override
    String getMessageType() {
        'GlossaryEntry'
    }
}
