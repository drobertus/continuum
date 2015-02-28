package com.mechzombie.continuum.services

import com.mechzombie.continuum.Task
import com.mechzombie.continuum.util.CountDown
import groovy.util.logging.Log
import spock.lang.Specification
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse


@Log
class ContinuumMonitorServiceSpec extends Specification {

    def "test that as tasks are added to the service they are arranged by date"() {
        setup: 'create a CMS'
            def cms = new InMemContinuumMonitorService()
            // we are testing the ordering mechanism- turn off the consumer
            cms.stopMonitoring()

            def resultQueue = []

            def msg1 = 'should be first'
            def msg2 = 'the middle'
            def msg3 = 'should be last'
            def msg4 = 'into the mix!'
        when: 'add three tasks with execution dates in the immediate future'

            def cal = Calendar.getInstance(TimeZone.getDefault())

            cal.add(Calendar.SECOND, 5)
            def shouldBeLast = new Task(scheduledDate: cal.getTime(), toExecute: {println 'last'; resultQueue.add msg3})

            cal.add(Calendar.SECOND, -4)
            def shouldBeFirst = new Task(scheduledDate: cal.getTime(), toExecute: {println 'first'; resultQueue.add msg1})

            cal.add(Calendar.SECOND, 2)
            def theMiddle = new Task(scheduledDate: cal.getTime(), toExecute: {println 'middle'; resultQueue.add msg2})

            cal.add(Calendar.SECOND, 1)
            def intoTheMix = new Task(scheduledDate: cal.getTime(), toExecute: {println 'MIXUP!'; resultQueue.add msg4})

            cms.addTask(theMiddle)
            cms.addTask(shouldBeLast)
            cms.addTask(shouldBeFirst)

        and: 'the consumer is activated and we toss a new element in as the list is being consumed'

            cms.resumeMonitoring()
            def timeout = new CountDown(7000)
            def addedToTheMix = false
            while(resultQueue.size() < 4 && !timeout.hasExpired()) {
               if(!addedToTheMix && resultQueue.size() > 0) {
                   cms.addTask(intoTheMix)
                   addedToTheMix = true
               }
                sleep 50
            }
        then: 'the results will appear in this order'
            assertEquals 4, resultQueue.size()

            assertEquals msg1, resultQueue[0]
            assertEquals msg2, resultQueue[1]
            assertEquals msg4, resultQueue[2]
            assertEquals msg3, resultQueue[3]

        when: "shutdown the monitor"
            cms.shutdown()

        then:
            assertFalse cms.monitorThread.isAlive()


    }


}