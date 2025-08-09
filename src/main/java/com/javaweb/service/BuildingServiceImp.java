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

        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql.toString())) {

            // Thiết lập các tham số vào PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i)); // Cài đặt các tham số vào PreparedStatement
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

        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi (log hoặc ném exception tùy theo yêu cầu)
        }

        return listDto;

    }

}