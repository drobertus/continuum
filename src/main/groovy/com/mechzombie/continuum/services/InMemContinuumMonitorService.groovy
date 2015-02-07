package com.mechzombie.continuum.services

import com.mechzombie.continuum.Continuum
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
class InMemContinuumMonitorService implements ContinuumMonitor {

    // this is essentially an ordered queue of tasks to perform
    // events can be inserted into it or removed from it
    // each has a scheduled date/time.
    private final def listOfTasks = new CopyOnWriteArrayList<Task>()
    private final def newContinuum = new CopyOnWriteArrayList<Continuum>()
    private final def knownContinuum = new CopyOnWriteArrayList<Continuum>()
    private def monitoring = new AtomicBoolean(true)

    private final Thread monitorThread

    InMemContinuumMonitorService() {
        monitorThread = Thread.start monitor
    }

    boolean addTask(Task task) {

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

    @Override
    void monitorContinuum(Continuum continuum) {
        newContinuum.add continuum
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
                    if (boundary.boundaryTask) {
                        this.addTask(boundary.boundaryTask)
                    }
                }
                knownContinuum.add(c)
                newContinuum.remove(c)
            }
            sleep(50)
        }
    }
}