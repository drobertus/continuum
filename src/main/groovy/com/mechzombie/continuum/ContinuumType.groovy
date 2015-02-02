package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.Glossary
import com.mechzombie.continuum.glossary.GlossaryEntry

/**
 * Created by David on 1/31/2015.
 */
class ContinuumType {
    final GlossaryEntry name
    def description

    // there is a glossary for type specific terminology
    final Glossary glossary

    // a 'phase' of a continuum is a
    private List<PhaseType> phases = []
    // a boundary is a transition into, out of, or between phases
    def boundaries = []
    Map<String, ContinuumType> childTypes = [:]

    Map<String, Continuum> instances = [:]

    ContinuumType(String name) {
        glossary = new Glossary()
        this.name = glossary.getOrCreateEntry(name)
    }

    String getNameAsString() {
        return name.name
    }

    PhaseType appendPhaseType(String phaseToAppend) {
        def entry = glossary.getOrCreateEntry(phaseToAppend)
        def priorPhase
        if (!phases.empty) {
            priorPhase = phases.get(phases.size() -1)
        }


        def pt = new PhaseType(this, entry, priorPhase)
        if (priorPhase) priorPhase.nextPhase = pt

        this.phases << pt
        return pt
    }

    List<PhaseType> getPhases() {
        return phases.asImmutable()
    }

    int getPhaseTypeCount() {
        return phases.size()
    }

    ContinuumType getOrCreateSupportedType(String name) {
        def child = new ContinuumType(name)
        this.glossary.addEntry(child.getName())
        childTypes.put(name, child)
        return child
    }

    Continuum createContinuum(String instanceName) {
        def theCont = instances[instanceName]
        if (!theCont) {
            theCont = new Continuum(this, instanceName)
            instances.put(instanceName, theCont)
        }

        return theCont
    }

    GlossaryEntry getGlossaryEntry(String entryName) {
        return glossary.entries[entryName]
    }
}
