package com.mechzombie.continuum.protocol


class BoundaryMsg implements JsonSerializable{
    @Override
    String getMessageType() {
        return 'Boundary'
    }
}
