package com.mechzombie.continuum.monitoring

import com.google.common.eventbus.Subscribe

interface MonitorContinuumSubscriber {

    @Subscribe
    void subscribeToMonitorContinuumEvent(MonitorContinuumEvent event);
}
