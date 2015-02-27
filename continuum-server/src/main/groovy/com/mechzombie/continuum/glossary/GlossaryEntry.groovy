package com.mechzombie.continuum.glossary

import groovy.transform.CompileStatic

@CompileStatic
class GlossaryEntry {
    String name
    def description
    def cognates = []
}
