package com.mechzombie.continuum

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.monitor.TestMonitorContinuumSubscriber
import spock.lang.Shared
import spock.lang.Specification

import static groovy.util.GroovyTestCase.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull


/**
 * Created by David on 2/1/2015.
 */
class ContinuumTypeSpec extends Specification {

    @Shared
    def eventBus = new EventBus()

    def "create a continuum based on a type"() {

        setup: "create a type as a template"
            def conType = new ContinuumType('Company')
            conType.setEventBus(eventBus)
            def continuumSubscriber = new TestMonitorContinuumSubscriber();
            eventBus.register(continuumSubscriber)
            // add a child type
            def salesTeam = conType.getOrCreateSupportedType('sales team')

        when: "phases types are added to the continuum type"
            // create phases
            PhaseType plan = conType.appendPhaseType('establish business plan')
            PhaseType operations = conType.appendPhaseType('start operations')
            PhaseType sales = conType.appendPhaseType('sales')

        then: "the phase type relations are established in the order they are added"
            assertEquals operations, plan.nextPhase
            assertEquals null, plan.previousPhase
            assertEquals sales, operations.nextPhase
            assertEquals null, sales.nextPhase

        and: "the glossary entries for the phase types are present"
            assertNotNull conType.getGlossaryEntry(operations.getName())
            assertNotNull conType.getGlossaryEntry(sales.getName())
            assertNotNull conType.getGlossaryEntry(plan.getName())
            assertNull conType.getGlossaryEntry('not a phase')

        when: "create a boundary between two of the phases"
            def bussPlan = conType.createBoundary("business-plan", plan, PhaseBoundary.EXIT)

        then: " the boundary should be bound to the phase types"
            assertEquals bussPlan, plan.exitBoundary
            assertEquals bussPlan, operations.entryBoundary

        when: "create a continuum from the type"
            Continuum companyX = conType.createContinuum('ABC Co.')

        then: " the created instance should inherit the glossary, child types, and phases"
            assertEquals 1, continuumSubscriber.receivedEvents.size()
            assertEquals companyX, continuumSubscriber.receivedEvents[0].continuum
            assertEquals conType.instances['ABC Co.'], companyX
            assertNotNull companyX.getGlossaryEntry('sales')
            assertNotNull companyX.getGlossaryEntry('Company')
            assertNotNull companyX.getGlossaryEntry('establish business plan')
            assertNotNull companyX.getGlossaryEntry('start operations')
            assertEquals salesTeam, companyX.getOrCreateSupportedType('sales team')

        and: " the phases from the type should be pulled over as well"
            assertEquals conType.phaseTypeCount, companyX.phaseTypeCount
            int phaseCount = conType.phaseTypeCount
            (0 .. phaseCount -1).each {
               assertEquals conType.phases.get(it), companyX.phases.get(it).type
            }

        when: " add a child type to the continuum type"
            def employee = conType.getOrCreateSupportedType('employee')

        then: " it should not appear in the created continuum instance"
            assertNotNull employee
            assertNotNull conType.getGlossaryEntry('employee')
            assertNull companyX.getGlossaryEntry('employee')

        when: " add a type to the created continuum instance"
            def division = companyX.getOrCreateSupportedType('division')

        then: " it should not appear in the type"
            assertNotNull division
            assertNotNull companyX.getGlossaryEntry('division')
            assertNull conType.getGlossaryEntry('division')

    }


}