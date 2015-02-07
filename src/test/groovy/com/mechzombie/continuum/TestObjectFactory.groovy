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

        def agenda = ct.createBoundary('agenda', premeet,meet)
        def meetingNotes = ct.createBoundary('meeting-notes', meet, postMeet)
        return ct
    }
}
