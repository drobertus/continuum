package com.mechzombie.continuum.protocol


class TaskMsg implements JsonSerializable{
    @Override
    String getMessageType() {
        'Task'
    }
}
