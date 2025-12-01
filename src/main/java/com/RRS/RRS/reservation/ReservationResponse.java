package com.RRS.RRS.reservation;

public class ReservationResponse {

    private Integer reservationId;
    private String status;
    private String message;

    public Integer getReservationId() {
        return reservationId;
    }
    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
