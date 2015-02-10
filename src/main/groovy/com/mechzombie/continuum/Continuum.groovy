package com.mechzombie.continuum

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.glossary.Glossary
import com.mechzombie.continuum.glossary.GlossaryEntry
import com.mechzombie.continuum.monitoring.TaskScheduleEvent

class Continuum {

    // the name fo the continuum exists as a glossary entry
    final GlossaryEntry name
    // the type of this continuum
    final ContinuumType type

    final EventBus eventBus

    /**
     * A set of keywords relevant to a continuum.
     * All names of all child types and other pertinent terms are
     * included in the glossary.
     */
    final Glossary glossary

    /**
     * a child type is a category of continuum that can exist in
     * the current continuum
     */
    Map<String, ContinuumType> childTypes = [:]
    // children are grouped by unique type and then specific name
    Map<ContinuumType, Map<String, Continuum>> children = [:]

    List<Phase> phases = []
    Map<String, Boundary> boundaries = [:]

    /**
     * This allows a continuum to be created in isolation
     * from a type.
     *
     * @param typeName
     * @param specificName
     */
    Continuum(String typeName, String specificName) {
        glossary = new Glossary()
        def entry = glossary.getOrCreateEntry(typeName)
        this.type = new ContinuumType(typeName)
        name = glossary.getOrCreateEntry(specificName)
    }

    /**
     * This constructor allows the meta data from the type
     * to be carried over
     * @param type
     * @param specificName
     */
    Continuum(ContinuumType type, String specificName) {
        glossary = new Glossary()
        this.eventBus = type.getEventBus()

        //populate the glossary with all known types
        type.glossary.entries.each { key, val ->
            glossary.addEntry(val)
        }

        //copy over the child types
        type.childTypes.each {key, val ->
            this.childTypes.put(key, val)
        }

        //create phases from phase types
        Phase prev
        type.phases.each { it ->
            def aPhase = new Phase(it, prev)

            Boundary entryBound
            if (prev) {
                prev.nextPhase = aPhase
                entryBound = prev.exitBoundary

            }
            else if(it.entryBoundary) {
                entryBound = it.entryBoundary.getClone()
            }
            if (entryBound) {
                aPhase.entryBoundary = entryBound
                this.boundaries.put(entryBound.name.name, entryBound)
            }

            Boundary exitBound

            if(it.exitBoundary) {
                exitBound = it.exitBoundary.getClone()
                aPhase.exitBoundary = exitBound
                this.boundaries.put(exitBound.name.name, exitBound)
            }

            this.phases << aPhase
            prev = aPhase
        }



        glossary.addEntry(type.name)
        this.type = type
        //name sure the name of this continuum is it the glossary
        name = glossary.getOrCreateEntry(specificName)
    }

    String getName() {
        return name.name
    }

    ContinuumType getOrCreateSupportedType(String continuumType) {
        def existing = childTypes.get(continuumType)
        if (!existing) {
            def ge = glossary.getOrCreateEntry(continuumType)
            existing = new ContinuumType(continuumType)
            childTypes.put(continuumType, existing)
        }
        return existing
    }

    /**
     * returns a new continuum if one of the
     * requested name and type is not present
     * This will not create a type if it does not exist.
     * Need to call "addSupportedType" first
     * @param type
     * @param name
     * @return
     */
    Continuum getOrCreateChildContinuum (String type, String name) {
        def contType = childTypes.get(type)
        if (!contType) {
            throw new Exception ("Type ${type} is not recognized.")
        }

        Map<String,Continuum> theCont = children.get(type)
        if (!theCont) {
            theCont = new HashMap<String,Continuum>()
                children.put(contType, theCont)
        }
        def cont = theCont.get(name)
        if(!cont){
            cont = new Continuum(contType, name)
        }
        return cont
    }

    Phase getPhase(String phaseName) {
        for(Phase p : phases) {
            if (phaseName.equals(p.type.name)) {
                return p
            }
        }
        return null;
    }

    GlossaryEntry getGlossaryEntry(String name) {
        return glossary.entries[name]
    }

    int getPhaseTypeCount() {
        return phases.size()
    }

    Map<String, Boundary> getBoundaries() {
        boundaries
    }

    Boundary getBoundary(String boundaryName) {
        return boundaries.get(boundaryName)
    }

    boolean setPhaseStartDate(String phaseName, Date date) {
        def phase = this.getPhase(phaseName)
        if (phase) {
            phase.setStartDate(date)
            if (phase.previousPhase) {
                phase.previousPhase.setEndDate(date)
            }
            if (phase.entryBoundary && phase.entryBoundary.boundaryTask) {
                phase.entryBoundary.boundaryTask.scheduledDate = date
                eventBus.post(new TaskScheduleEvent(phase.entryBoundary.boundaryTask));
            }
            return true
        }
        else {
            return false
        }
    }

    boolean setPhaseEndDate(String phaseName, Date date) {
        def phase = this.getPhase(phaseName)
        if (phase) {
            phase.setEndDate(date)
            if (phase.nextPhase) {
                phase.nextPhase.setStartDate(date)
            }
            if (phase.exitBoundary && phase.exitBoundary.boundaryTask) {
                phase.exitBoundary.boundaryTask.scheduledDate = date
                eventBus.post(new TaskScheduleEvent(phase.exitBoundary.boundaryTask));
            }
            return true
        }
        else {
            return false
        }
    }

    /**
     * set the continuum in motion
     * @param calendar
     * @return
     */
    boolean begin(Calendar calendar) {

    }

//    /**
//     * The monitor allows tasks to be scheduled, etc
//     * @param continuumMonitor
//     */
//    def setEventBus(EventBus bus) {
//        this.eventBus = bus
//    }
}
