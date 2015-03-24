package com.mechzombie.continuum

import com.mechzombie.continuum.services.InMemContinuumMonitorService
import com.mechzombie.continuum.util.CountDown
import groovy.util.logging.Log
import spock.lang.Shared
import spock.lang.Specification

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue

@Log
class BoundaryEventSpec extends Specification {

    @Shared
    def cms = new InMemContinuumMonitorService()

    def "test create boundary permutations"() {
        when: ' setup a continuum with two phases'
            def twoPhaseCT = TestObjectFactory.newTwoPhaseContinuumType
            def beforePhase = twoPhaseCT.phases[0]
            def afterPhase = twoPhaseCT.phases[1]
        then: ' the two phases should be coincident'
            assertEquals beforePhase.nextPhase, afterPhase
            assertEquals afterPhase.previousPhase, beforePhase

        when: 'create a boundary on the entry to the first phase'
            def startBound = twoPhaseCT.createBoundary("start", beforePhase, PhaseBoundaryTypeEnum.ENTRY)

        then: 'the first phase should have an entry boundary'
            assertEquals startBound, beforePhase.entryBoundary

        when: 'create a boundary on the exit of the second phase'
            def endBound = twoPhaseCT.createBoundary("end", afterPhase, PhaseBoundaryTypeEnum.EXIT)

        then: 'the last phase should have an exit boundary'
            assertEquals endBound, afterPhase.exitBoundary

        when: ' a boundary is defined at the exit of the first phase'
            def inbetween = twoPhaseCT.createBoundary("middle", beforePhase, PhaseBoundaryTypeEnum.EXIT)

        then: ' the boundary should  appear at the exit of the first phase '
            assertEquals inbetween, beforePhase.exitBoundary
        and: ' as the entry boundary to the second phase'
            assertEquals inbetween, afterPhase.entryBoundary


    }

    //TODO: phase and boundary removal/replacement


    def "test creation of phase types with entry and exit boundaries"()
    {
        setup: 'setup a continuum type with 3 phases'
            def ct = new ContinuumType("Meeting")
            def premeet = ct.appendPhaseType('pre-meeting')
            def meet = ct.appendPhaseType('meeting')
            def postMeet = ct.appendPhaseType('post-meeting')

        when: "an entry boundary is set for the start"

            def agenda = ct.createBoundary('agenda', premeet, PhaseBoundaryTypeEnum.EXIT)
            def boundType = ct.boundaryTypes['agenda']
        then:
            assertNotNull agenda
            assertEquals agenda, boundType

        and:
            assertEquals premeet, agenda.precedingPhase
            assertEquals meet, agenda.succeedingPhase

            assertNull premeet.entryBoundary
            assertNull meet.exitBoundary

            assertEquals agenda, premeet.exitBoundary
            assertEquals agenda, meet.entryBoundary
    }

    def "test that when a phase boundary is traversed (or Boundary crossed) that the Task for the boundary is called"(){
        setup: "setup a continuum type with phase types and boundaries"
            def meetingType = TestObjectFactory.getNewMeetingContinuumType()
            cms.registerContinuumType(meetingType)
            def bounds = meetingType.boundaryTypes

        when: "construct a continuum from the continuum type"
            def aMeeting = meetingType.createContinuum("Quarterly Sales Meeting")

            def meetingName = aMeeting.getGlossaryEntry('meeting').name
            def agendaName = aMeeting.getGlossaryEntry('agenda').name

        then: "then the continuum should have appropriate matching boundaries and phases"
            assertEquals bounds.size(), aMeeting.getBoundaries().size()
            assertEquals aMeeting.phases[0].exitBoundary , aMeeting.phases[1].entryBoundary
            assertEquals aMeeting.phases[1].exitBoundary , aMeeting.phases[2].entryBoundary

            assertEquals aMeeting.boundaries.get(agendaName), aMeeting.phases[0].exitBoundary
            assertEquals aMeeting.boundaries.get('meeting-notes'), aMeeting.phases[1].exitBoundary

        when: "we set tasks on the boundaries"

        List<String> taskResults = []

        aMeeting.getBoundary(agendaName).setBoundaryTask({testClosure(agendaName, taskResults)})
        aMeeting.getBoundary('meeting-notes').setBoundaryTask({testClosure('meeting-notes', taskResults)})

        and: "set the times for the continuum and its phase changes "
            def cal = Calendar.getInstance(TimeZone.getDefault())
            aMeeting.begin(cal)
            //we will leave the end open ended

        then: "the meeting continuum should be being processed and tied to the monitor"

            def isMonitoring = cms.isMonitoring(aMeeting.name)
            def startMonitorTimer = new CountDown(500)
            while (!startMonitorTimer.hasExpired() && !isMonitoring) {
                sleep(25)
                isMonitoring = cms.isMonitoring(aMeeting.name)
            }

            assertTrue isMonitoring

        when: " we set the time for the phases of the meeting"

        cal.add(Calendar.SECOND, 3)
            def meetingStartTime = cal.getTime()
            cal.add(Calendar.SECOND, 3)
            def meetingEndTime = cal.getTime()

            aMeeting.setPhaseStartDate(meetingName, meetingStartTime)
            aMeeting.setPhaseEndDate(meetingName, meetingEndTime)

        then: "when the boundaries are passed (in time) the appropriate tasks are executed"

            //we should see the associated tasks

            def countDown = new CountDown(7000)
            while (!countDown.hasExpired() && taskResults.size() < 2) {
                sleep(100)
            }
            assertEquals 2, taskResults.size()
            def expected1 = taskResults[0].split('\\|')
            def expected2 = taskResults[1].split('\\|')

            assertEquals 'agenda', expected1[1]
            assertEquals 'meeting-notes', expected2[1]

        and: "there should be no tasks left to run when the continuum is completed"
            assertTrue cms.listOfTasks.isEmpty()
            assertTrue cms.newContinuum.isEmpty()
            assertTrue !cms.knownContinuum.isEmpty()
    }

    def testClosure = { name, results ->
        log.info "running a testClosure!"
        results.add "executed|${name}|${System.currentTimeMillis()}"
    }

}