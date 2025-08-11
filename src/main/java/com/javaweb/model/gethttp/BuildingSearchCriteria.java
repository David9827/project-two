package com.javaweb.model.gethttp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class BuildingSearchCriteria {
    private String q;                 // tìm theo buildingname/ward/street
    private Integer districtid;
    private BigDecimal minRentprice;
    private BigDecimal maxRentprice;
    private Double minFloorarea;
    private Double maxFloorarea;
    private String level;
    private String direction;
}
