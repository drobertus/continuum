package com.mechzombie.continuum.util

class CountDown {

    def startTime
    def endTime
    final def waitTime

    CountDown(waitTime) {
        this.waitTime = waitTime
    }

    void start() {
        startTime = System.currentTimeMillis()
        endTime = startTime + waitTime
    }
    boolean hasExpired() {
        if(!startTime) {
            start()
        }
        return  System.currentTimeMillis() >= endTime
    }
}
