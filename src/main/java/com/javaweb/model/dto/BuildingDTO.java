package com.javaweb.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class BuildingDTO {
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


}
