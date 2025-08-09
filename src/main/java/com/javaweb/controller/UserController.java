package com.javaweb.controller;

import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;
import com.javaweb.service.BuildingServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @GetMapping(value = "api/building")
    public List<BuildingDTO> getBuilding(@RequestBody BuildingSearchCriteria buildingSearchCriteria){
        BuildingServiceImp buildingServiceImp = new BuildingServiceImp();
        buildingServiceImp.getListBuilding(buildingSearchCriteria);
        List<BuildingDTO> listb = new ArrayList<>(buildingServiceImp.getListBuilding(buildingSearchCriteria));
        return listb;
    }

}
