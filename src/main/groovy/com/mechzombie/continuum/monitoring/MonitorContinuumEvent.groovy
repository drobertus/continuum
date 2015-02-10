package com.mechzombie.continuum.monitoring

import com.mechzombie.continuum.Continuum

/**
 * Created by David on 2/9/2015.
 */
class MonitorContinuumEvent {

    def continuum
    MonitorContinuumEvent(Continuum toMonitor) {
        this.continuum = toMonitor
    }
}
