package com.mechzombie.continuum.protocol

import groovy.json.internal.LazyMap


class ContinuumTypeMsg implements JsonSerializable{

    String name
    String description

    Collection<String> phaseTypes = []
    // a boundary is a transition into, out of, or between phases
    Collection<String> boundaryTypes = []

    Collection<String> childContinuumTypes = []

    Collection<String> instances = []

}
