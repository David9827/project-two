package com.javaweb.beans;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
@Getter
@Setter
public class Building {
    private Integer id;
    private String name;
    private String street;
    private String ward;
    private Integer districtId;
    private String structure;
    private Integer numberFloor;
    private Double floorArea;
    private String direction;
    private String level;
    private BigDecimal rentPrice;
    private String rentPriceDesription;
    private String managerName;
    private String managerPhoneNumber;

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", ward='" + ward + '\'' +
                ", districtId=" + districtId +
                ", structure='" + structure + '\'' +
                ", numberFloor=" + numberFloor +
                ", floorArea=" + floorArea +
                ", direction='" + direction + '\'' +
                ", level='" + level + '\'' +
                ", rentPrice=" + rentPrice +
                ", rentPriceDesription='" + rentPriceDesription + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerPhoneNumber='" + managerPhoneNumber + '\'' +
                '}';
    }
}
