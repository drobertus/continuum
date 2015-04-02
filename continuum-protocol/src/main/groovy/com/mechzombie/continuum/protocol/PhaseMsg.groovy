package com.mechzombie.continuum.protocol


class PhaseMsg implements JsonSerializable{
    @Override
    String getMessageType() {
        return 'Phase'
    }
}
