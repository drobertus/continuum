package com.mechzombie.continuum.services

import com.mechzombie.continuum.ContinuumType
import com.mechzombie.continuum.tasks.Task

interface ContinuumMonitor {

    /**
     * Add a task to be performed
     * @param task
     * @return
     */
    boolean addTask(Task task)

    /**
     * remove a task from the execution list
     * @param task
     * @return
     */
    boolean removeTask(Task task)

    /**
     * start processing Tasks in the list
     */
    void resumeMonitoring()

    /**
     * stop processing tasks in the list.
     * This call will block until the thread responsible for processing is no longer alive
     */
    void stopMonitoring()

    /**
     * Add a continuum to the monitoring service
     * @param continuum
     */
    //void monitorContinuum(Continuum continuum)

    /**
     * register a type of continuum to be monitored.
     * individual continuum with use the event bus to
     * handle their internal task registration
     * @param type
     */
    void registerContinuumType(ContinuumType type)

    boolean isMonitoring(String continuumName)

    void submitForExecution(Task task)
}