package com.mechzombie.continuum.protocol

class ContinuumMsg implements JsonSerializable {

    // the name fo the continuum exists as a glossary entry
    String name
    // the type of this continuum
    String type

    /**
     * a child type is a category of continuum that can exist in
     * the current continuum
     */
    Map<String, ContinuumTypeMsg> childTypes = [:]
    // children are grouped by unique type and then specific name
    Map<ContinuumTypeMsg, Map<String, ContinuumMsg>> children = [:]

    List<PhaseMsg> phases = []
    Map<String, BoundaryMsg> boundaries = [:]

    @Override
    String getMessageType() {
        'Continuum'
    }
}
