package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.GlossaryEntry

/**
 * Created by David on 2/1/2015.
 */
class PhaseType {

    final GlossaryEntry phaseName
    final ContinuumType continuumType

    BoundaryType entryBoundary
    BoundaryType exitBoundary

    PhaseType nextPhase
    PhaseType previousPhase

    PhaseType(ContinuumType continuumType, GlossaryEntry phaseName,
              PhaseType previousPhase = null, PhaseType nextPhase = null) {
        this.phaseName = phaseName
        this.continuumType = continuumType
        this.nextPhase = nextPhase
        this.previousPhase = previousPhase
    }

    String getName() {
        return phaseName.name
    }
}
