package com.javaweb.service;

import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;

import java.util.List;
import java.util.Objects;

public interface BuildingService {
    List<BuildingDTO> getListBuilding(BuildingSearchCriteria buildingcri);

}
