package com.mechzombie.continuum.client.dto

/**
 * Created by David on 2/23/2015.
 */
class GlossaryEntry {

    String name

    //a gloss entry can exist for a type or an instance
    boolean definedAtTypeLevel

    Object associatedObject

    def description

    def cognates = []

}
