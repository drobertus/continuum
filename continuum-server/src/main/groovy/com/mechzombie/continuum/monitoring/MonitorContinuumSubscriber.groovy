package com.mechzombie.continuum.monitoring

import com.google.common.eventbus.Subscribe
import groovy.transform.CompileStatic

@CompileStatic
interface MonitorContinuumSubscriber {

    @Subscribe
    void subscribeToMonitorContinuumEvent(MonitorContinuumEvent event);
}
