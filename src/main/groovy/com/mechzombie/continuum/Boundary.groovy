package com.mechzombie.continuum

import com.mechzombie.continuum.glossary.GlossaryEntry

/**
 * Created by David on 2/1/2015.
 */
class Boundary {

    GlossaryEntry boundaryName

    // TODO: a boundary can often be associated with a
    // document "meeting agenda", etc- how to represent this?

    PhaseType precedingPhase
    PhaseType subsequentPhase


}
