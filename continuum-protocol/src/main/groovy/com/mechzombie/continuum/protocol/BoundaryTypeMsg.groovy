package com.mechzombie.continuum.protocol


class BoundaryTypeMsg implements JsonSerializable{
    @Override
    String getMessageType() {
        'BoundaryType'
    }
}
