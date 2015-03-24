package com.mechzombie.continuum.protocol

class Continuum {

    // the name fo the continuum exists as a glossary entry
    String name
    // the type of this continuum
    String type

    /**
     * a child type is a category of continuum that can exist in
     * the current continuum
     */
    Map<String, ContinuumType> childTypes = [:]
    // children are grouped by unique type and then specific name
    Map<ContinuumType, Map<String, Continuum>> children = [:]

    List<Phase> phases = []
    Map<String, Boundary> boundaries = [:]
}
