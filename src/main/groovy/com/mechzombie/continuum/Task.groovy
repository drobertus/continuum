package com.mechzombie.continuum


class Task {

    def id
    Date scheduledDate
    Closure toExecute

    boolean isValid() {
        return scheduledDate && toExecute
    }
}
