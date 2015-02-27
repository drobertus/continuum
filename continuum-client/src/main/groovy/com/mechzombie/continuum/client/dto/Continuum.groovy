package com.mechzombie.continuum.client.dto

class Continuum {

    // the name fo the continuum exists as a glossary entry
    final GlossaryEntry name
    // the type of this continuum
    final ContinuumType type

   // final EventBus eventBus

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
}
