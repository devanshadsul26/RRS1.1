package com.RRS.RRS.table;

import jakarta.persistence.*;

@Entity
@Table(name = "tables")
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tableid")
    private Integer id;

    @Column(name = "tablenumber")
    private Integer tableNumber;

    @Column(name = "seatingcapacity")
    private Integer seatingCapacity;

    @Column(name = "locationpreference")
    private String locationPreference;   // "Indoor" or "Outdoor"

    @Column(name = "isactive")
    private Boolean isActive;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }
    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getLocationPreference() {
        return locationPreference;
    }
    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
