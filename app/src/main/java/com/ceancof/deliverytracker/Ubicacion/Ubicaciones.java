package com.ceancof.deliverytracker.Ubicacion;

public class Ubicaciones {
    Double latitude;
    Double longitude;

    public Ubicaciones() {
    }
    public Ubicaciones(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
