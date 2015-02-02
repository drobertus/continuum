package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.Glossary
import com.mechzombie.continuum.glossary.GlossaryEntry

class Continuum {

    // the name fo the continuum exists as a glossary entry
    final GlossaryEntry name
    // the type of this continuum
    final ContinuumType type

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
            if (prev) {
                prev.nextPhase = aPhase
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

    GlossaryEntry getGlossaryEntry(String name) {
        return glossary.entries[name]
    }

    int getPhaseTypeCount() {
        return phases.size()
    }
}
