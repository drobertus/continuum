package com.mechzombie.continuum.protocol

class PhaseTypeMsg implements JsonSerializable{
    String name

    String nextPhase
    String previousPhase
    String continuumType

    @Override
    String getMessageType() {
        'PhaseType'
    }
}
