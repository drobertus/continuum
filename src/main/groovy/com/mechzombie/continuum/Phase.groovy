package com.mechzombie.continuum

/**
 * Created by David on 2/1/2015.
 */
class Phase {

    final PhaseType type

    Boundary entryBoundary
    Boundary exitBoundary

    Phase nextPhase
    Phase previousPhase

    Date startDate
    Date endDate

    Phase(PhaseType phaseType, Phase previousPhase = null, Phase nextPhase = null) {
        type = phaseType
        this.nextPhase = nextPhase
        this.previousPhase = previousPhase
    }

    void setStartDate(Date startDate) {
        this.startDate = startDate
        if(previousPhase) {
            previousPhase.endDate = startDate
        }
        if (this.entryBoundary) {
            entryBoundary.boundaryTask.scheduledDate = startDate
        }
    }
}
