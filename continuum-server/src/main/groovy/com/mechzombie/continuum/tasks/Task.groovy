package com.mechzombie.continuum.tasks


class Task {

    def id
    Date scheduledDate
    Closure toExecute

    boolean isValid() {
        return scheduledDate && toExecute
    }
}
