package com.mechzombie.continuum

/**
 * Created by David on 2/1/2015.
 */
class Phase {

    final PhaseType type

    Phase nextPhase
    Phase previousPhase

    Phase(PhaseType phaseType, Phase previousPhase = null, Phase nextPhase = null) {
        type = phaseType
        this.nextPhase = nextPhase
        this.previousPhase = previousPhase
    }

}
