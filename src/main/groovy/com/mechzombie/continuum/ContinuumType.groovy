package com.mechzombie.continuum

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.glossary.Glossary
import com.mechzombie.continuum.glossary.GlossaryEntry
import com.mechzombie.continuum.monitoring.MonitorContinuumEvent
import com.mechzombie.continuum.services.ContinuumMonitor

/**
 * Created by David on 1/31/2015.
 */
class ContinuumType {
    final GlossaryEntry name
    def description

    // there is a glossary for type specific terminology
    final Glossary glossary

    private EventBus eventBus

    // a 'phase' of a continuum is a
    private List<PhaseType> phases = []
    // a boundary is a transition into, out of, or between phases
    Map<String, BoundaryType> boundaryTypes = [:]

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
            eventBus.post(new MonitorContinuumEvent(theCont))
        }

        return theCont
    }

    GlossaryEntry getGlossaryEntry(String entryName) {
        return glossary.entries[entryName]
    }

    BoundaryType createBoundary(String name, PhaseType preceedingPhase, PhaseType subsequentPhase) {

        if(!name) {
            throw new Exception ("A Boundary must have a name")
        }

        // check that there is at least one phasetype
        if(!preceedingPhase && !subsequentPhase) {
            throw new Exception ("Boundary ${name} requires a phasetype to follow to proceed (or both)")
        }

        def validProceeding = true
        if(preceedingPhase){
            validProceeding = this.phases.contains(preceedingPhase)
        }

        def validSuceeding = true
        if(subsequentPhase){
            validSuceeding = this.phases.contains(subsequentPhase)
        }

        if (!validProceeding || !validSuceeding) {
            throw new Exception("The boundary must be coincident with phases that are part of this continuum type")
        }


        def glossEntry = glossary.getOrCreateEntry(name)
        def bound = new BoundaryType(glossEntry, preceedingPhase, subsequentPhase)
        preceedingPhase.exitBoundary = bound
        subsequentPhase.entryBoundary = bound

        this.boundaryTypes.put(name, bound)
        return bound
    }

    void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus
    }

    EventBus getEventBus() {
        return this.eventBus
    }
}
