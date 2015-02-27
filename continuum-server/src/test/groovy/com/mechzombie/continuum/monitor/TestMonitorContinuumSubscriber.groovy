package com.mechzombie.continuum.monitor

import com.mechzombie.continuum.monitoring.MonitorContinuumEvent
import com.mechzombie.continuum.monitoring.MonitorContinuumSubscriber

/**
 * Created by David on 2/9/2015.
 */
class TestMonitorContinuumSubscriber implements MonitorContinuumSubscriber{

    def receivedEvents = []

    @Override
    void subscribeToMonitorContinuumEvent(MonitorContinuumEvent event) {
        receivedEvents << event
    }
}
