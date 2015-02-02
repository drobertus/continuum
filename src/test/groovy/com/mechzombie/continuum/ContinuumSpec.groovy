package com.mechzombie.continuum

import spock.lang.Specification

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

// this is a test to define the behavior of the
// data structure, its relation to glossary items
class ContinuumSpec extends Specification {

    //we are considering an example of a business
    def "test for a business with various parts"() {

        when: 'we create a new continuum'
            def abc_co = new Continuum('company', 'ABC Co.')

        then: 'it should include its type in its glossary'
            def entry = abc_co.getGlossaryEntry('company')
            assertNotNull entry

        when: 'add "employee" as a supported type'
            def type = abc_co.getOrCreateSupportedType('employee')
            entry = abc_co.getGlossaryEntry('employee')

        then: 'employee should appear in the glossary and as a valid type'
            assertNotNull(type)
            assertNotNull entry
            assertEquals(entry.getName(), type.getNameAsString())
    }

    def "test for getOrCrateNewChildType"() {

        setup:
            def testCont = new Continuum('company', "ABC Co.")
        when: "try to add a child type that has not been added as a supported type"
            def newChild = testCont.getOrCreateChildContinuum('employee', 'Caesar')
        then: " an exception should be thrown"
            Exception ex = thrown()
            assertEquals  ex.message , 'Type employee is not recognized.'

        when: " the child type is add to the supported set"
            def childType = testCont.getOrCreateSupportedType('employee')
        then: "supported types should include the new one"
            assertNotNull(childType)
        when: "a child type of the supported type is created"
            newChild = testCont.getOrCreateChildContinuum('employee', 'Caesar')
        then: "the child should be created"
            assertNotNull(newChild)

        and: "the two continuums should share the same type and glossary entry"
            assertEquals childType.getName(), newChild.getType().getName()
            assertEquals childType, newChild.getType()
            assertEquals 'Caesar', newChild.getName()
    }
}