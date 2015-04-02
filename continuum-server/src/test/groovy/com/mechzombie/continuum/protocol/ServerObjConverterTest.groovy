package com.mechzombie.continuum.protocol

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.ContinuumType
import com.mechzombie.continuum.PhaseBoundaryTypeEnum
import com.mechzombie.continuum.protocol.MsgObjectConverter
import spock.lang.Specification

import static groovy.util.GroovyTestCase.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue


class ServerObjConverterTest extends Specification {

    ServerObjConverter converter = new ServerObjConverter()

    def "convert a continuumType" () {
        setup:
        def ct = 'Continuum Type'
        ContinuumType type = new ContinuumType(ct)
        def bus = Mock(EventBus)
        type.eventBus = bus
        def pt1 = type.appendPhaseType('phase 1')
        type.appendPhaseType('phase 2')
        type.createBoundary('end phase 1', pt1, PhaseBoundaryTypeEnum.EXIT)

        type.createContinuum('continuum instance 1')
        type.getOrCreateSupportedType('child type 1')

        when:
        def asMsg = converter.convertContinuumType(type)

        then:
        assertEquals asMsg.name, ct
        assertEquals type.boundaryTypes.size(), asMsg.boundaryTypes.size()
        assertEquals type.getPhaseTypeCount(), asMsg.phaseTypes.size()
        assertEquals type.instances.size(), asMsg.getInstances().size()
        assertEquals type.childTypes.size(), asMsg.getChildContinuumTypes().size()

        when: 'this is converted to a standardmsg'
            StandardMsg converted = converter.toStandardMsg(asMsg)
            String asString = converter.toStandardMsgString(asMsg)
          //  println "asString= ${asString}"
        //println new JsonBuilder(asMsg).toString()
        then:
            assertNotNull converted
        when: 'we convert back to a StandardMsg'
            StandardMsg reconverted = converter.getStdMsgFromString(asString)
            ContinuumTypeMsg body = reconverted.getMsgBody()

        then:
            assertTrue converted.msgBody instanceof ContinuumTypeMsg
            assertTrue body instanceof ContinuumTypeMsg
            ContinuumTypeMsg b1 = converted.msgBody
            ContinuumTypeMsg b2 = reconverted.msgBody
            assertEquals b1.name, b2.name
            assertEquals b1.boundaryTypes.size(), b2.boundaryTypes.size()
            assertEquals b1.phaseTypes.size(), b2.phaseTypes.size()
            assertEquals b1.instances.size(), b2.getInstances().size()
            assertEquals b1.childContinuumTypes.size(), b2.childContinuumTypes.size()

    }
}