package com.mechzombie.continuum.monitoring

import com.google.common.eventbus.Subscribe

interface TaskSubscriber {

    @Subscribe
    public void handleTaskScheduleEvent(TaskScheduleEvent event)
}
