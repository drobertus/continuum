package com.mechzombie.continuum.protocol

import com.mechzombie.continuum.ContinuumType

/**
 * Created by David on 4/2/2015.
 */
class ServerObjConverter extends MsgObjectConverter {

    ContinuumTypeMsg convertContinuumType(ContinuumType convert) {
        def msg = new ContinuumTypeMsg()
        msg.name = convert.getNameAsString()
        msg.setChildContinuumTypes(convert.childTypes.keySet())
        msg.setBoundaryTypes(convert.getBoundaryTypes().keySet())
        msg.setInstances(convert.getInstances().keySet())
        msg.setPhaseTypes(convert.getPhases().collect {it.name})

        return msg
    }

}
