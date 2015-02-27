package com.mechzombie.continuum.monitoring

import com.mechzombie.continuum.Task

/**
 * Created by David on 2/9/2015.
 */
class TaskScheduleEvent {

    final Task task

    TaskScheduleEvent(Task task) {
        this.task = task
    }
}
