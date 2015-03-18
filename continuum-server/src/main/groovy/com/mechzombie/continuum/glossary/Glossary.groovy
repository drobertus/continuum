package com.mechzombie.continuum.glossary

import groovy.transform.CompileStatic

@CompileStatic
class Glossary {

    Map<String, GlossaryEntry> entries = [:]

    void addEntry(GlossaryEntry glossaryEntry) {
        entries.put(glossaryEntry.name, glossaryEntry)
    }

    GlossaryEntry getOrCreateEntry(String name, String description = null, List<String> cognates = []){
        def entry = entries.get(name)
        if (!entry) {
            entry = new GlossaryEntry(name, description, cognates)
            addEntry(entry)
        }
        return entry
    }

    Glossary getClone() {
        def clone = new Glossary()
        for(GlossaryEntry ge: entries.values()) {
           clone.addEntry(ge.getClone())
        }
        return clone
    }
}
