package com.mechzombie.continuum.monitoring

import com.mechzombie.continuum.Continuum
import groovy.transform.CompileStatic

/**
 * Created by David on 2/9/2015.
 */
@CompileStatic
class MonitorContinuumEvent {

    Continuum continuum

    MonitorContinuumEvent(Continuum toMonitor) {
        this.continuum = toMonitor
    }
}
