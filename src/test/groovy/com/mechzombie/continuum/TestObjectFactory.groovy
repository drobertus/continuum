package com.mechzombie.continuum

/**
 * Created by David on 2/6/2015.
 */
class TestObjectFactory {

    static ContinuumType getNewMeetingContinuumType() {
        def ct = new ContinuumType("Meeting")
        def premeet = ct.appendPhaseType('pre-meeting')
        def meet = ct.appendPhaseType('meeting')
        def postMeet = ct.appendPhaseType('post-meeting')

        def agenda = ct.createBoundary('agenda', premeet, PhaseBoundary.EXIT)
        def meetingNotes = ct.createBoundary('meeting-notes', meet, PhaseBoundary.EXIT)
        return ct
    }

    static ContinuumType getNewTwoPhaseContinuumType() {
        def ct = new ContinuumType("BeforeAfter")
        def before = ct.appendPhaseType("before")
        def after = ct.appendPhaseType("after")

        return ct
    }
}
