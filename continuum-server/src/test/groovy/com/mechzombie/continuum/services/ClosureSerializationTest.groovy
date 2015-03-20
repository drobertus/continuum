package com.mechzombie.continuum.services

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.TestObjectFactory
import spock.lang.Specification
import static org.junit.Assert.assertEquals


/**
 * Created by David on 3/1/2015.
 */
class ClosureSerializationTest extends Specification {

    def "test how well closures serialize and de-serialize"() {

        setup: 'create a closure'
        def variableToChange = 5
        def closure = { variableToChange += 5 }

        when: ' we dehydrate the closure'
        def hydrated = closure.dehydrate()

        and: 're-hydrate it and run it'
        def rehyd = hydrated.rehydrate(null, null, null)
        def val = rehyd()

        then:
        assertEquals 10, val
    }

    def "test closures with arguments"() {

        setup: 'create a closure'
        def variableToChange = 2
        def closure = { it -> it + 5 }

        when: ' we dehydrate the closure'
        def hydrated = closure.dehydrate()

        and: 're-hydrate it and run it'
        def rehyd = hydrated.rehydrate(null, null, null)
        def val = rehyd(variableToChange)

        then:
        assertEquals 7, val
    }

    def "store objects and variables in dehydrated form"() {

        setup: 'put an object into a closure'
            def ct = TestObjectFactory.newTwoPhaseContinuumType
            def bus = new EventBus()
            ct.setEventBus(bus)
            def testC = ct.createContinuum('twoPhase')
            def toHyrate = {testC.getPhases()[0].setEndDate(it)}
            def dehyd = toHyrate.dehydrate()
            println dehyd.toString()

        when:
            def rehyd = dehyd.rehydrate(null, null, null)
            def cal = Calendar.instance
            cal.add(Calendar.MONTH, 2)
            def runDate = cal.time
            rehyd(runDate)

        then:
            assertEquals(runDate, testC.getPhases()[0].getEndDate())

    }


}