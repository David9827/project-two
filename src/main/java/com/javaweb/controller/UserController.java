package com.javaweb.controller;

import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;
import com.javaweb.service.BuildingServiceImp;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Cho phép React app truy cập
public class UserController {
    
    // Thay đổi từ @RequestBody thành @RequestParam hoặc @PostMapping
    @PostMapping(value = "/api/building/search")
    public List<BuildingDTO> searchBuildings(@RequestBody BuildingSearchCriteria buildingSearchCriteria){
        BuildingServiceImp buildingServiceImp = new BuildingServiceImp();
        List<BuildingDTO> listb = new ArrayList<>(buildingServiceImp.getListBuilding(buildingSearchCriteria));
        return listb;
    }
    
    // Thêm endpoint GET đơn giản để test
    @GetMapping(value = "/api/building")
    public List<BuildingDTO> getAllBuildings(){
        BuildingServiceImp buildingServiceImp = new BuildingServiceImp();
        // Tạo criteria rỗng để lấy tất cả
        BuildingSearchCriteria criteria = new BuildingSearchCriteria();
        List<BuildingDTO> listb = new ArrayList<>(buildingServiceImp.getListBuilding(criteria));
        return listb;
    }
    
    // Endpoint health check
    @GetMapping(value = "/api/health")
    public String healthCheck(){
        return "Backend is running!";
    }

}
