package com.mechzombie.continuum.glossary

import spock.lang.Specification

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertEquals

class GlossaryTest extends Specification {

    def gloss = new Glossary()

    def "test glossary getOrCreate"() {
        setup:
            def entryName1 = 'Bob'
        when: "we add a new entry"
            def entry1 = gloss.getOrCreateEntry(entryName1)
        then: 'the returned value should be a Glossary Entry'
            assertNotNull entry1
            assertEquals entryName1, entry1.name

        and: "the glossary will have the entry"
            assertEquals 1, gloss.entries.size()

        when: "we get the entry by name again"
            def entry2 = gloss.getOrCreateEntry(entryName1)
        then: "we should be returned the same instance"
            assertEquals entry1, entry2
    }

    def "test adding an entry directly"() {
        setup:
            def name1 = 'Bill'
            def name2 = 'Susan'
            def newEntry = new GlossaryEntry(name1)
        when: 'we add an entry to the glossary'
            gloss.addEntry(newEntry)
        then: 'the entry should be recognized by name'


    }
}