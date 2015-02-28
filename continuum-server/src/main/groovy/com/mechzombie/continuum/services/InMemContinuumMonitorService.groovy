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
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This is basically a thread pool.
 * It monitors the continuums it is aware of,
 * waits for scheduled events such as start and end of
 * continuums and phases, looks for associated boundaries
 * and marshalls the necessary resources needed to process
 *
 */
@Log
class InMemContinuumMonitorService implements ContinuumMonitor, TaskSubscriber, MonitorContinuumSubscriber {

    // this is essentially an ordered queue of tasks to perform
    // events can be inserted into it or removed from it
    // each has a scheduled date/time.
    private final def listOfTasks = new ConcurrentSkipListMap<Date, Task>()
    private final def newContinuum = new CopyOnWriteArrayList<Continuum>()
    private final def knownContinuum = new CopyOnWriteArrayList<Continuum>()
    private def monitoring = new AtomicBoolean(true)

    EventBus eventBus
    private final Thread monitorThread

    InMemContinuumMonitorService () {
        eventBus = new EventBus()
        eventBus.register(this)
        monitorThread = Thread.start monitor
    }

    boolean addTask(Task task) {
        return listOfTasks.put(task.scheduledDate, task)
    }

    boolean removeTask(Task task) {
        listOfTasks.remove(task.scheduledDate, task)
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

            if (listOfTasks.size() > 0) {
                def entry = listOfTasks.firstKey()
                def now = new Date()
                if (now.after(entry)) {
                    //TODO: behaviour with multiple values at the same date
                    log.info "Task runTime= ${entry.getTime()}, currTime = ${now.getTime()}, delta= ${now.getTime() - entry.getTime()}"
                    def toRun = listOfTasks.get(entry)
                    listOfTasks.remove(entry, toRun)
                    Thread.start toRun.toExecute
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