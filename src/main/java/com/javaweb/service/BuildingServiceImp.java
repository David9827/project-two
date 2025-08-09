package com.javaweb.service;

import com.javaweb.database.JDBCUtil;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;
import com.mysql.cj.jdbc.ConnectionImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildingServiceImp implements BuildingService {
    @Override
    public List<BuildingDTO> getListBuilding(BuildingSearchCriteria bdcri) {
        List<BuildingDTO> listDto = new ArrayList<>();
        
        try {
            Connection c = JDBCUtil.getConnection();
            
            // Nếu không có database connection, dùng mock data
            if (c == null) {
                System.out.println("📊 Using mock data for buildings...");
                return getMockBuildingData(bdcri);
            }
            
            // Code database thật (khi có connection)
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder("SELECT * FROM building b WHERE 1=1 ");
            
            if (bdcri.getName()!= null && !bdcri.getName().isEmpty()){
                sql.append("AND b.buildingname LIKE ? ");
                params.add("%"+bdcri.getName()+"%");
            }
            if (bdcri.getFloorArea()!=null){
                sql.append("AND b.floorarea LIKE ? ");
                params.add(bdcri.getFloorArea());
            }
            if (bdcri.getDistrictName()!= null && !bdcri.getDistrictName().isEmpty()){
                sql.append(";");
                sql.append(" SELECT * FROM district d JOIN building b ON d.id = b.districtid WHERE districtname LIKE ?");
                params.add("%" +bdcri.getDistrictName() +"%");
            }
            if (bdcri.getWard()!= null && !bdcri.getWard().isEmpty()){
                sql.append("AND b.ward LIKE ? ");
                params.add("%"+bdcri.getWard()+"%");
            }
            if (bdcri.getStreet()!= null && !bdcri.getStreet().isEmpty()){
                sql.append("AND b.street LIKE ? ");
                params.add("%"+bdcri.getStreet()+"%");
            }
            if (bdcri.getNumberFloor()!=null) {
                sql.append("AND b.numberfloor LIKE ? ");
                params.add(bdcri.getNumberFloor());
            }
            if (bdcri.getDirection()!= null && !bdcri.getDirection().isEmpty()){
                sql.append("AND b.direction LIKE ? ");
                params.add("%"+bdcri.getDirection()+"%");
            }
            if (bdcri.getLevel()!= null && !bdcri.getLevel().isEmpty()){
                sql.append("AND b.level LIKE ? ");
                params.add("%"+bdcri.getLevel()+"%");
            }
            if (bdcri.getRentAriaMin()!=null && bdcri.getRentAriaMax()!=null) {
                sql.append("AND rentaria BETWEEN ? AND ? ");
                params.add(bdcri.getRentAriaMin());
                params.add(bdcri.getRentAriaMax());
            }
            if (bdcri.getRentPriceMin()!=null && bdcri.getRentPriceMax()!=null) {
                sql.append("AND rentprice BETWEEN ? AND ? ");
                params.add(bdcri.getRentPriceMin());
                params.add(bdcri.getRentPriceMax());
            }
            if (bdcri.getManagerName()!= null && !bdcri.getManagerName().isEmpty()){
                sql.append("AND b.managername LIKE ? ");
                params.add("%"+bdcri.getManagerName()+"%");
            }
            if (bdcri.getManagerPhoneNumber()!= null && !bdcri.getManagerPhoneNumber().isEmpty()){
                sql.append("AND b.managerphonenumber LIKE ? ");
                params.add("%"+bdcri.getManagerPhoneNumber()+"%");
            }

            try (PreparedStatement pstmt = c.prepareStatement(sql.toString())) {
                // Thiết lập các tham số vào PreparedStatement
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                // Thực thi truy vấn
                ResultSet rs = pstmt.executeQuery();

                // Xử lý kết quả trả về
                while (rs.next()) {
                    BuildingDTO buildingDTO = new BuildingDTO();
                    buildingDTO.setName(rs.getString("buildingname"));
                    buildingDTO.setFloorArea(rs.getDouble("floorarea"));
                    //buildingDTO.setDistrictName(rs.getString("districtname"));
                    buildingDTO.setWard(rs.getString("ward"));
                    buildingDTO.setStreet(rs.getString("street"));
                    buildingDTO.setNumberFloor(rs.getInt("numberfloor"));
                    buildingDTO.setDirection(rs.getString("direction"));
                    buildingDTO.setLevel(rs.getString("level"));
                    buildingDTO.setRentPrice(rs.getBigDecimal("rentprice"));
                    buildingDTO.setManagerName(rs.getString("managername"));
                    buildingDTO.setManagerPhoneNumber(rs.getString("managerphonenumber"));
                    //buildingDTO.setBuildingRentType(rs.getString("buildingrenttype"));

                    listDto.add(buildingDTO);
                }
            }
            JDBCUtil.closeConnection(c);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            System.out.println("Falling back to mock data...");
            return getMockBuildingData(bdcri);
        }

        return listDto;
    }
    
    // Mock data method để test khi không có database
    private List<BuildingDTO> getMockBuildingData(BuildingSearchCriteria criteria) {
        List<BuildingDTO> mockData = new ArrayList<>();
        
        // Sample data
        BuildingDTO building1 = new BuildingDTO();
        building1.setName("Chung cư Vinhomes Central Park");
        building1.setFloorArea(120.0);
        building1.setDistrictName("Quận Bình Thạnh");
        building1.setWard("Phường 22");
        building1.setStreet("Đường Nguyễn Hữu Cảnh");
        building1.setNumberFloor(45);
        building1.setDirection("Đông Nam");
        building1.setLevel("Cao cấp");
        building1.setRentAria(java.math.BigDecimal.valueOf(120));
        building1.setRentPrice(java.math.BigDecimal.valueOf(25000000));
        building1.setManagerName("Nguyễn Văn A");
        building1.setManagerPhoneNumber("0901234567");
        building1.setBuildingRentType("Chung cư");
        
        BuildingDTO building2 = new BuildingDTO();
        building2.setName("Văn phòng Bitexco Financial Tower");
        building2.setFloorArea(200.0);
        building2.setDistrictName("Quận 1");
        building2.setWard("Phường Bến Nghé");
        building2.setStreet("Đường Hải Triều");
        building2.setNumberFloor(68);
        building2.setDirection("Nam");
        building2.setLevel("Hạng A");
        building2.setRentAria(java.math.BigDecimal.valueOf(200));
        building2.setRentPrice(java.math.BigDecimal.valueOf(50000000));
        building2.setManagerName("Trần Thị B");
        building2.setManagerPhoneNumber("0907654321");
        building2.setBuildingRentType("Văn phòng");
        
        BuildingDTO building3 = new BuildingDTO();
        building3.setName("Nhà phố Thảo Điền");
        building3.setFloorArea(300.0);
        building3.setDistrictName("Quận 2");
        building3.setWard("Phường Thảo Điền");
        building3.setStreet("Đường Nguyễn Văn Hưởng");
        building3.setNumberFloor(4);
        building3.setDirection("Đông");
        building3.setLevel("Cao cấp");
        building3.setRentAria(java.math.BigDecimal.valueOf(300));
        building3.setRentPrice(java.math.BigDecimal.valueOf(40000000));
        building3.setManagerName("Lê Văn C");
        building3.setManagerPhoneNumber("0912345678");
        building3.setBuildingRentType("Nhà phố");
        
        mockData.add(building1);
        mockData.add(building2);
        mockData.add(building3);
        
        // Simple filtering based on criteria
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            String searchName = criteria.getName().toLowerCase();
            mockData = mockData.stream()
                    .filter(b -> b.getName().toLowerCase().contains(searchName))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        System.out.println("📦 Returning " + mockData.size() + " mock buildings");
        return mockData;
    }

}
