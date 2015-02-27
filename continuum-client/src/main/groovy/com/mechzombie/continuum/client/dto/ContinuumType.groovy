package com.mechzombie.continuum.client.dto

/**
 * Created by David on 2/23/2015.
 */
class ContinuumType {
    final GlossaryEntry name
    def description

    // there is a glossary for type specific terminology
    final Glossary glossary

    //private EventBus eventBus

    // a 'phase' of a continuum is a
    private List<PhaseType> phases = []
    // a boundary is a transition into, out of, or between phases
    Map<String, BoundaryType> boundaryTypes = [:]

    Map<String, ContinuumType> childTypes = [:]

    Map<String, Continuum> instances = [:]
}
