package com.mechzombie.continuum.glossary

import groovy.transform.CompileStatic

@CompileStatic
class GlossaryEntry {
    String name
    String description
    List<String> cognates = []

    GlossaryEntry(String name, String description = null, List<String> cognates = []) {
        this.name = name
        this.description = description
        this.cognates = cognates
    }

    GlossaryEntry getClone() {
        return new GlossaryEntry(name, description, cognates)
    }
}
