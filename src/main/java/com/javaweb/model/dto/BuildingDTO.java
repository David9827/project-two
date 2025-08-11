package com.javaweb.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class BuildingDTO {
    private Integer id;
    private String buildingname;
    private Double floorarea;                 // DOUBLE
    private String ward;
    private String street;
    private Integer districtid;
    private Integer numberfloor;
    private String direction;
    private String level;                     // chú ý tên cột là `level`
    private BigDecimal rentaria;              // DECIMAL(15,2)
    private BigDecimal rentprice;             // DECIMAL(15,2)
    private String managername;
    private String managerphonenumber;
    private Instant createdAt;
    private Instant updatedAt;

}
