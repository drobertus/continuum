package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.GlossaryEntry
import com.mechzombie.continuum.tasks.Task

/**
 * Created by David on 2/1/2015.
 */
class BoundaryEvent {

    private final GlossaryEntry boundaryName

    // TODO: a boundary can often be associated with a
    // document "meeting agenda", etc- how to represent this?

    private final PhaseType precedingPhase
    private final PhaseType succeedingPhase

    private Task boundaryTask

    protected BoundaryEvent(GlossaryEntry name, PhaseType precedingPhase, PhaseType succeedingPhase) {
        this.boundaryName = name
        this.precedingPhase = precedingPhase
        this.succeedingPhase = succeedingPhase
    }

    GlossaryEntry getName() {
        return boundaryName
    }

    boolean hasValidTask() {
         return boundaryTask && boundaryTask.isValid()
    }

    void setBoundaryTask(Closure toRun) {
        boundaryTask = new Task(toExecute: toRun)
    }

    void setBoundaryTask(Date runTime, Closure toRun) {
       boundaryTask = new Task(scheduledDate: runTime, toExecute: toRun)
    }

    Task getBoundaryTask() {
        boundaryTask
    }
}
