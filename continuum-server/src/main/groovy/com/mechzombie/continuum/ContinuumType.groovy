package com.mechzombie.continuum

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.glossary.Glossary
import com.mechzombie.continuum.glossary.GlossaryEntry
import com.mechzombie.continuum.monitoring.MonitorContinuumEvent
import groovy.transform.CompileStatic

@CompileStatic
class ContinuumType {
    final GlossaryEntry name
    def description

    // there is a glossary for type specific terminology
    final Glossary glossary

    private EventBus eventBus

    // a 'phase' of a continuum is a
    private List<PhaseType> phases = []
    // a boundary is a transition into, out of, or between phases
    Map<String, BoundaryEventType> boundaryTypes = [:]

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
        PhaseType priorPhase
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

    BoundaryEventType createBoundary(String name, PhaseType phase, PhaseBoundary boundary) {

        if(!name) {
            throw new Exception ("A Boundary must have a name")
        }

        int phasePosition = -1
        def coincidentPhase
        // check that there is at least one phasetype
        if(!phase ) {
            throw new Exception ("Boundaries exist at start or end of a phase type - select a valid phase")
        }else {
            def pos = 0
            for(PhaseType pt : phases) {
                if (pt == phase) {
                    phasePosition = pos
                }
                pos ++
            }
            if (phasePosition == -1) {
                throw new Exception ("PhaseType ${phase.name} does not appear to exist in continuum ${this.name.name}" )
            }
        }

        def glossEntry = glossary.getOrCreateEntry(name)
        def bound

        //TODO- ultimately we will need more logic to determine if the
        //proceeding/succeeding phase is Coincident with the selected phase
        if (PhaseBoundary.ENTRY.equals( boundary) ){
            coincidentPhase = phases[phasePosition - 1]
            bound = new BoundaryEventType(glossEntry, coincidentPhase, phase)
            phase.entryBoundary = bound
            if(coincidentPhase) coincidentPhase.exitBoundary = bound
        }
        else {
            coincidentPhase = phases[phasePosition + 1]
            bound = new BoundaryEventType(glossEntry, phase, coincidentPhase)
            phase.exitBoundary = bound
            if(coincidentPhase) coincidentPhase.entryBoundary = bound
        }

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
