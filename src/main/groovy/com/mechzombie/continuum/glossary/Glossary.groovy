package com.mechzombie.continuum.glossary

/**
 * Created by David on 1/31/2015.
 */
class Glossary {

    Map<String, GlossaryEntry> entries = [:]

    void addEntry(GlossaryEntry glossaryEntry) {
        entries.put(glossaryEntry.name, glossaryEntry)
    }

    GlossaryEntry getOrCreateEntry(String name){
        def entry = entries.get(name)
        if (!entry) {
            entry = new GlossaryEntry(name: name)
            addEntry(entry)
        }
        return entry
    }
}
