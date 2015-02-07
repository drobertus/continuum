package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.GlossaryEntry
import com.mechzombie.continuum.services.Task

/**
 * Created by David on 2/1/2015.
 */
class Boundary {

    private final GlossaryEntry boundaryName

    // TODO: a boundary can often be associated with a
    // document "meeting agenda", etc- how to represent this?

    private final PhaseType precedingPhase
    private final PhaseType succeedingPhase

    Task boundaryTask

    protected Boundary(GlossaryEntry name, PhaseType precedingPhase, PhaseType succeedingPhase) {
        this.boundaryName = name
        this.precedingPhase = precedingPhase
        this.succeedingPhase = succeedingPhase
        boundaryTask = new Task()
    }

    def getName() {
        return boundaryName
    }

    Boundary getClone() {
        return new Boundary(this.boundaryName, this.precedingPhase, this.succeedingPhase)
    }
}
