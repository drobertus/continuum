package com.mechzombie.continuum.protocol


class ContinuumType {

    String name
    String description

    List<PhaseType> phaseTypes = []
    // a boundary is a transition into, out of, or between phases
    Map<String, BoundaryType> boundaryTypes = [:]

    Map<String, ContinuumType> childTypes = [:]

    Map<String, Continuum> instances = [:]

}
