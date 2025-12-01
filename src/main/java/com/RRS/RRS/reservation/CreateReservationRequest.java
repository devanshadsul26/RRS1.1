package com.RRS.RRS.reservation;

public class CreateReservationRequest {

    private Integer customerId;
    private String date;              // e.g. "2025-11-30"
    private String time;              // e.g. "19:30"
    private Integer guestCount;
    private String locationPreference;
    private String specialRequests;

    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public Integer getGuestCount() {
        return guestCount;
    }
    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getLocationPreference() {
        return locationPreference;
    }
    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
}
