package com.mechzombie.continuum

import groovy.transform.CompileStatic

/**
 * Created by David on 2/1/2015.
 */
@CompileStatic
class Phase {

    public static final String PHASE_START_PRECEEDS_CONTINUUM_START_EXCEPTION =
        "Can not set a phase to start before its parent continuum"
    public static final String PHASE_END_AFTER_CONTINUUM_END_EXCEPTION =
        "Can not set a phase to end after its parent continuum"
    public static final String PHASE_START_PRECEEDS_PRECEEDING_PHASE_END_EXCEPTION =
        "Can not set a phase to start before the end of a prior phase end date"
    public static  final String PHASE_END_AFTER_NEXT_PHASE_START =
        "Can not set a phase to end after the start of the next phase"

    final PhaseType type
    String name
    BoundaryEvent entryBoundary
    BoundaryEvent exitBoundary

    Phase nextPhase
    Phase previousPhase

    private Date startDate
    private Date endDate

    Continuum parent

    Phase(Continuum parent, PhaseType phaseType, Phase previousPhase = null, Phase nextPhase = null) {
        this.parent = parent
        type = phaseType
        this.nextPhase = nextPhase
        this.previousPhase = previousPhase
        this.name = phaseType.name
    }

    void setStartDate(Date startDate, Phase coincidentPhase = this) {
        if(parent.getStartDate() && parent.getStartDate().after(startDate)) {
            throw new Exception (PHASE_START_PRECEEDS_CONTINUUM_START_EXCEPTION)
        }

        if (coincidentPhase != previousPhase && coincidentPhase != this) {
            throw new Exception("You can only make the previous phase coincident with this one")
        }

        if(previousPhase && coincidentPhase != previousPhase) {
            if (previousPhase.endDate && this.startDate != previousPhase.endDate) {
                if (previousPhase.endDate.after(startDate)) {
                    throw new Exception(PHASE_START_PRECEEDS_PRECEEDING_PHASE_END_EXCEPTION)
                }
            }
        }

        this.startDate = startDate
        if (coincidentPhase == previousPhase) {
            //if there is no start date or the dates are coincident, move the end date
            //if ((!previousPhase.endDate) || (previousPhase.endDate == startDate)) {
                previousPhase.setEndDate(startDate, coincidentPhase)
            //}
        }

        if (this.entryBoundary) {
            entryBoundary.boundaryTask.scheduledDate = startDate
        }
    }

    /**
     * Set the end date of the phase
     * Also, if there is a NEXT Phase, set that start date to the end date
     * IF...
     * 1) the start date of the next phase is null
     * 2) the current start date of the next phase is coincident with the
     *  current end date of this phase
     *
     * Do not allow the end Date to be set past the start date of the next phase
     *
     * @param endDate
     */
    void setEndDate(Date endDate, Phase coincidentPhase = this) {
        if(parent.getEndDate() && parent.getEndDate().before(endDate)) {
            throw new Exception (PHASE_END_AFTER_CONTINUUM_END_EXCEPTION)
        }

        if (coincidentPhase != nextPhase && coincidentPhase != this) {
            throw new Exception("You can only make the previous phase coincident with this one")
        }

        if(nextPhase && coincidentPhase != nextPhase) {
            if (nextPhase.startDate && this.endDate != nextPhase.startDate) {
                if (nextPhase.startDate.before(endDate)) {
                    throw new Exception(PHASE_END_AFTER_NEXT_PHASE_START)
                }
            }
        }

        this.endDate = endDate
        if (coincidentPhase == nextPhase) {
            //if there is no start date or the dates are coincident, move the end date
            //if ((!nextPhase.startDate) || (nextPhase.startDate == endDate)) {
                nextPhase.setStartDate(endDate, coincidentPhase)
            //}
        }

        if (this.exitBoundary) {
            exitBoundary.boundaryTask.scheduledDate = endDate
        }
    }


    Date getEndDate() {
        return endDate
    }

    Date getStartDate() {
        return startDate
    }

    String getName() {
        return name    }
}
