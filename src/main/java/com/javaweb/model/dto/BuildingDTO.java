package com.javaweb.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class BuildingDTO {
    private Integer id;
    private String name;
    private Double floorArea;
    private String districtName;
    private String ward;
    private String street;
    private Integer numberFloor;
    private String direction;
    private String level;
    private BigDecimal rentAria;
    private BigDecimal rentPrice;
    private String managerName;
    private String managerPhoneNumber;
    private String buildingRentType;

    private Integer districtId; // For easier form handling
    private String structure;   // If needed
    private String rentPriceDescription;


}