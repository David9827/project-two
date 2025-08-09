package com.javaweb.model.gethttp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class BuildingSearchCriteria {
    private String name;
    private Double floorArea;
    private String districtName;
    private String ward;
    private String street;
    private Integer numberFloor;
    private String direction;
    private String level;
    private BigDecimal rentAriaMin;
    private BigDecimal rentAriaMax;
    private BigDecimal rentPriceMin;
    private BigDecimal rentPriceMax;
    private String managerName;
    private String managerPhoneNumber;
    private String buildingRentType;
}
