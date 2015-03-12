package com.mechzombie.continuum

import com.google.common.eventbus.EventBus
import spock.lang.Specification

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

/**
 * PhaseType is a predefined set of phases, a "phase template",
 * associated to a ContinuumType.
 */
class PhaseSpec extends Specification {


    def eventBus = new EventBus()
    Continuum testC
    Phase before
    Phase after
    Calendar cal

    void setup() {
        def ct = TestObjectFactory.getNewTwoPhaseContinuumType()
        ct.setEventBus(eventBus)

        testC = ct.createContinuum("test_timing")

        before = testC.getPhase('before')
        after = testC.getPhase('after')

        cal = Calendar.instance
    }

    def "test that phases respect continuum boundaries"() {

        when: "we give the continuum a start time"
            //set the cal start to 10 days ago
            cal.add(Calendar.DATE, -10)
            testC.setStartDate(cal.getTime())
            def continuumStartOrig = cal.time

        then: 'the first phase should have a start time coincident with the continuum start time'

            assertEquals testC.getStartDate(), before.startDate

        when: " we should not be able to set the start time of either phase before the continuum start"
            cal.add(Calendar.DATE, -1)
            before.setStartDate(cal.time)

        then:
            Exception ex = thrown()
            assertEquals Phase.PHASE_START_PRECEEDS_CONTINUUM_START_EXCEPTION, ex.getMessage()

        when: "we set the start time of the phase to be after the start of the continuum"
            cal.add(Calendar.DATE, 2)
            before.setStartDate(cal.time)

        then: "the continuum start should stay the same and the phase be adjusted as set"
            assertEquals continuumStartOrig, testC.getStartDate()
            assertEquals before.startDate, cal.time

        when: "we give the continuum an end time"
            cal.add(Calendar.MONTH, 1)
            testC.setEndDate(cal.time)
            def continuumEndOrig = cal.time

        then: " the phases should not have been assigned end or start dates"
            assertNull after.endDate
            assertNull after.startDate
            assertEquals continuumEndOrig, testC.endDate

        when: "if we set a date for a phase past the end date of the continuum"
            cal.add(Calendar.DATE, 1)
            after.setEndDate(cal.time)

        then:  " we should not be able to set an end time for the phases past the continuum end"
            Exception ex2 = thrown()
            assertEquals Phase.PHASE_END_AFTER_CONTINUUM_END_EXCEPTION, ex2.getMessage()

        when: "we set the end of the last phase before the end of the continuum"
            cal.add(Calendar.DATE, -2)
            after.setEndDate(cal.time)
            def phaseEndTime = cal.time

        then: "then the continuum end should remain and the phase end be set"
            assertEquals phaseEndTime, after.getEndDate()
            assertEquals continuumEndOrig, testC.endDate

        and: "the end of the first phase and start of the second should still be null"
            assertNull before.endDate
            assertNull after.startDate

        when: "we set a date for the end of the first phase"
            cal.add(Calendar.DATE, -12)
            before.setEndDate(cal.time, after)
            def midTime = cal.time
        //then: "because this will create a coincident boundary it needs to be done via the continuum"
            //Exception ex3 = thrown()

        then: "the next phase should default to coincident"
            assertEquals midTime, before.endDate
            assertEquals midTime, after.startDate

    }

    def "test non-coincident phases"() {
        when: "end date of the first phase is set as non-coincident"
            before.setEndDate(cal.time)

        then: "the start date of the after phase should remain null"
            assertNull after.getStartDate()
            assertEquals cal.time, before.getEndDate()

        when: "start date of the after phase is set to a (non-coincident) date before the end of the before phase "
            cal.add(Calendar.DATE, -1)
            def yesterday = cal.time
            after.setStartDate(yesterday)

        then: " we should get an exception"
            Exception ex = thrown()
            assertEquals Phase.PHASE_START_PRECEEDS_PRECEEDING_PHASE_END_EXCEPTION, ex.getMessage()

        when: "makes the phases coincident via the setStartDate"
            after.setStartDate(yesterday, before)

        then: "the two phases should share a coincident (after) start and (before) end date"
            assertEquals yesterday, after.getStartDate()
            assertEquals yesterday, before.getEndDate()

        when: "make the phases non-coincident and non-overlapping"
            cal.add(Calendar.DATE, 3)
            def timeAhead = cal.time
            after.setStartDate(timeAhead)

        then: "the phases should be non-coincident"
            assertEquals yesterday, before.getEndDate()
            assertEquals timeAhead, after.getStartDate()

        when: "makes the phases coincident via the setEndDate"
            before.setEndDate(yesterday, after)

        then: "the two phases should share a coincident (after) start and (before) end date"
            assertEquals yesterday, after.getStartDate()
            assertEquals yesterday, before.getEndDate()
    }

    def "test that the phase order and end points recognize one another"() {
        when: "we try and make a later phase coincident with a start date"
            before.setStartDate(cal.time, after)

        then: " we get an error"
            Exception e = thrown()
        and: "the phase start should not be set"
            assertNull(before.getStartDate())
        when: " we try and make an earlier phase coincident with an end date"
            after.setEndDate(cal.time, before)
        then: " we get an error"
            Exception endErr = thrown()
        and: "the end date should not be set"
            assertNull after.getEndDate()

        when: "we try and make a start date coincident with null"
            after.setStartDate(cal.time, null)
        then: " we should get an error"
            Exception badStart = thrown()
        when: "we try and make an end date coincident with null"
            before.setEndDate(cal.time, null)
        then: " we should get an error"
            Exception badEnd = thrown()


    }


    //TODO: do we allow overlapping phases?  Or does what suggest that the continuums are
    //not defined with sufficient granularity?

    def "test that phases respect new continuum boundaries"() {

        when: "we give the 'before' phase a start time"
            cal.add(Calendar.MONTH, -1)
            def beforeStart = cal.time
            before.setStartDate(cal.time)

        and: "the 'after' phase an end time"
            cal.add(Calendar.MONTH, 2)
            def afterEnd = cal.time
            after.setEndDate(afterEnd)

        then: "the continuum should have neither start or end date set"
            assertNull testC.getEndDate()
            assertNull testC.getStartDate()

        when: "we set a start date to the continuum after the start date of the before phase"
            cal.setTime(beforeStart)
            cal.add(Calendar.DATE, 1)
            testC.setStartDate(cal.getTime())

        then: "we should get an exception that the continuum start date precedes the phase start"
            Exception preceedingPhaseException = thrown()

        when: " we set the continuum start to before the 'before' phase start "
            cal.add(Calendar.DATE, -3)
            testC.setStartDate(cal.getTime())
        then: "the 'begin' phase should remain the same as was originally set"
            assertEquals beforeStart, before.startDate

        and: "the continuum start should be defined"
            assertEquals cal.getTime(), testC.startDate
    }

}