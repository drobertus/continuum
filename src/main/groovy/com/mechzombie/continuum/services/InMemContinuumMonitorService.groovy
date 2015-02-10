package com.mechzombie.continuum.services

import com.google.common.eventbus.EventBus
import com.mechzombie.continuum.Continuum
import com.mechzombie.continuum.ContinuumType
import com.mechzombie.continuum.Task
import com.mechzombie.continuum.monitoring.MonitorContinuumEvent
import com.mechzombie.continuum.monitoring.MonitorContinuumSubscriber
import com.mechzombie.continuum.monitoring.TaskScheduleEvent
import com.mechzombie.continuum.monitoring.TaskSubscriber
import groovy.util.logging.Log

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This is basically a thread pool.
 * It monitors the continuums it is aware of,
 * waits for scheduled events such as start and end of
 * continuums and phases, looks for associated boundaries
 * and marshalls the necessary resources needed to
 *
 */
@Log
class InMemContinuumMonitorService implements ContinuumMonitor, TaskSubscriber, MonitorContinuumSubscriber {

    // this is essentially an ordered queue of tasks to perform
    // events can be inserted into it or removed from it
    // each has a scheduled date/time.
    private final def listOfTasks = new CopyOnWriteArrayList<Task>()
    private final def newContinuum = new CopyOnWriteArrayList<Continuum>()
    private final def knownContinuum = new CopyOnWriteArrayList<Continuum>()
    private def monitoring = new AtomicBoolean(true)

    EventBus eventBus// = new EventBus();
    //TaskSubscriber taskSubscriber

    private final Thread monitorThread

    InMemContinuumMonitorService () {
        eventBus = new EventBus()
       // taskSubscriber = new TaskSubscriber()
        eventBus.register(this)
        monitorThread = Thread.start monitor
    }

    boolean addTask(Task task) {

        if (!task.scheduledDate) {
            throw new Exception ('Task has no Scheduled Date')
        }
        if (!task.toExecute) {
            //TODO - can we allow a task to be scheduled that has a null action?
            throw new Exception ("Task has nothing to run ")
        }

        if (listOfTasks.empty) {
            return listOfTasks.add(task)
        }
        int pos = 0;

        def iter = listOfTasks.iterator()

        while (iter.hasNext()) {
            def aTask = iter.next()
            if (aTask.scheduledDate.after(task.scheduledDate)) {
                listOfTasks.add(pos, task)
                return true
            }
            pos++
        }
        return listOfTasks.add(task)

    }

    boolean removeTask(Task task) {
        listOfTasks.remove(task)
    }

    void shutdown() {
        stopMonitoring()
    }

    void resumeMonitoring() {
        log.info('resuming monitoring')
        if (!monitoring.get()) {
            monitoring.set(true)
            this.monitorThread.start(monitor)
        }
    }

    void stopMonitoring() {
        log.info('stop monitoring')

        monitoring.set(false)
        if (!monitorThread.alive) {
            return
        }
        this.monitorThread.interrupt()
        while (monitorThread.alive) {
            println("thread alive= ${monitorThread.alive}, ${monitoring.get()}")
            sleep(100)
        }
    }
//
//    @Override
//    void monitorContinuum(Continuum continuum) {
//        //continuum.setMonitor this
//        newContinuum.add continuum
//    }

    @Override
    void registerContinuumType(ContinuumType type) {
        type.setEventBus(this.eventBus)
    }

    @Override
    boolean isMonitoring(String continuumName) {
        for(def c : knownContinuum) {
            if (continuumName.equals(c.name)) {
                return true
            }
        }
        return false
    }

    private def monitor = {
        while (monitoring.get()) {
            if (!listOfTasks.empty) {
                def task = listOfTasks[0]
                def now = new Date()
                if (now.after(task.scheduledDate)) {
                    log.info "Task runTime= ${task.scheduledDate.getTime()}, currTime = ${now.getTime()}, delta= ${now.getTime() - task.scheduledDate.getTime()}"
                    listOfTasks.remove(task)
                    Thread.start task.toExecute
                }
            }
            if (!newContinuum.empty) {
                def c = newContinuum[0]
                c.boundaries.values().each() { boundary ->
                    if (boundary.hasValidTask()) {
                        this.addTask(boundary.getBoundaryTask())
                    }
                }
                knownContinuum.add(c)
                newContinuum.remove(c)
            }
            sleep(50)
        }
    }

    @Override
    void handleTaskScheduleEvent(TaskScheduleEvent event) {
        log.info("Got TaskScheduleEvent ${event}")
        this.addTask(event.task)
    }

    @Override
    void subscribeToMonitorContinuumEvent(MonitorContinuumEvent event) {
        log.info("Got MonitorContinuumEvent ${event}")
        newContinuum << event.continuum
    }
}