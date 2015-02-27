package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.GlossaryEntry

/**
 * Created by David on 2/1/2015.
 */
class BoundaryEventType {

    private final GlossaryEntry boundaryName

    // TODO: a boundary can often be associated with a
    // document "meeting agenda", etc- how to represent this?

    private final PhaseType precedingPhase
    private final PhaseType succeedingPhase

    protected BoundaryEventType(GlossaryEntry name, PhaseType precedingPhase, PhaseType succeedingPhase) {
        this.boundaryName = name
        this.precedingPhase = precedingPhase
        this.succeedingPhase = succeedingPhase
    }

    def getName() {
        return boundaryName
    }

    BoundaryEvent getClone() {
        return new BoundaryEvent(this.boundaryName, this.precedingPhase, this.succeedingPhase)
    }
}
