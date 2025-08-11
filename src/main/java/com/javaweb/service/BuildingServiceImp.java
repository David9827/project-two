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
import java.sql.*;
import com.javaweb.beans.Building;

public class BuildingServiceImp implements BuildingService {
    @Override
    public List<BuildingDTO> getListBuilding(BuildingSearchCriteria bdcri) {
        List<BuildingDTO> listDto = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM building b WHERE 1=1 ");
        if (bdcri.getName() != null && !bdcri.getName().isEmpty()) {
            sql.append("AND b.buildingname LIKE ? ");
            params.add("%" + bdcri.getName() + "%");
        }
        if (bdcri.getFloorArea() != null) {
            sql.append("AND b.floorarea LIKE ? ");
            params.add(bdcri.getFloorArea());
        }
        if (bdcri.getDistrictName() != null && !bdcri.getDistrictName().isEmpty()) {
            sql.append(";");
            sql.append(" SELECT * FROM district d JOIN building b ON d.id = b.districtid WHERE districtname LIKE ?");
            params.add("%" + bdcri.getDistrictName() + "%");
        }
        if (bdcri.getWard() != null && !bdcri.getWard().isEmpty()) {
            sql.append("AND b.ward LIKE ? ");
            params.add("%" + bdcri.getWard() + "%");
        }
        if (bdcri.getStreet() != null && !bdcri.getStreet().isEmpty()) {
            sql.append("AND b.street LIKE ? ");
            params.add("%" + bdcri.getStreet() + "%");
        }
        if (bdcri.getNumberFloor() != null) {
            sql.append("AND b.numberfloor LIKE ? ");
            params.add(bdcri.getNumberFloor());
        }
        if (bdcri.getDirection() != null && !bdcri.getDirection().isEmpty()) {
            sql.append("AND b.direction LIKE ? ");
            params.add("%" + bdcri.getDirection() + "%");
        }
        if (bdcri.getLevel() != null && !bdcri.getLevel().isEmpty()) {
            sql.append("AND b.level LIKE ? ");
            params.add("%" + bdcri.getLevel() + "%");
        }
        if (bdcri.getRentAriaMin() != null && bdcri.getRentAriaMax() != null) {
            sql.append("AND rentaria BETWEEN ? AND ? ");
            params.add(bdcri.getRentAriaMin());
            params.add(bdcri.getRentAriaMax());
        }
        if (bdcri.getRentPriceMin() != null && bdcri.getRentPriceMax() != null) {
            sql.append("AND rentprice BETWEEN ? AND ? ");
            params.add(bdcri.getRentPriceMin());
            params.add(bdcri.getRentPriceMax());
        }
        if (bdcri.getManagerName() != null && !bdcri.getManagerName().isEmpty()) {
            sql.append("AND b.managername LIKE ? ");
            params.add("%" + bdcri.getManagerName() + "%");
        }
        if (bdcri.getManagerPhoneNumber() != null && !bdcri.getManagerPhoneNumber().isEmpty()) {
            sql.append("AND b.managerphonenumber LIKE ? ");
            params.add("%" + bdcri.getManagerPhoneNumber() + "%");
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
                buildingDTO.setId(rs.getInt("id"));
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
    // Thêm các method này vào class BuildingServiceImp

    /**
     * Lấy building theo ID
     */
    public BuildingDTO getBuildingById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Building ID cannot be null");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtil.getConnection();
            String sql = """
            
                    SELECT b.id, b.name, b.street, b.ward, b.district, b.structure, 
                   b.numberofbasement, b.floorarea, b.direction, b.level, 
                   b.rentprice, b.rentpricedescription, b.servicefee, 
                   b.carfee, b.motofee, b.overtimefee, b.waterfee, 
                   b.electricityfee, b.deposit, b.payment, b.renttime, 
                   b.decorationtime, b.brokeragefee, b.note, b.linkofbuilding, 
                   b.map, b.avatar, b.createddate, b.modifieddate, 
                   b.createdby, b.modifiedby, b.managername, b.managerphone,
                   d.name as district_name
            FROM building b
            LEFT JOIN district d ON b.district = d.code
            WHERE b.id = ?
            """;

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BuildingDTO building = new BuildingDTO();
                building.setId(resultSet.getInt("id"));
                building.setName(resultSet.getString("name"));
                building.setStreet(resultSet.getString("street"));
                building.setWard(resultSet.getString("ward"));
                building.setDistrictName(resultSet.getString("district_name"));
                building.setStructure(resultSet.getString("structure"));

                building.setDirection(resultSet.getString("direction"));
                building.setLevel(resultSet.getString("level"));
                building.setRentPriceDescription(resultSet.getString("rentpricedescription"));


                return building;
            }

            return null; // Không tìm thấy building

        } catch (Exception e) {
            throw new RuntimeException("Error getting building by ID: " + e.getMessage(), e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }

    /**
     * Tạo building mới
     */
    public Building createBuilding(Building building) {
        if (building == null) {
            throw new IllegalArgumentException("Building cannot be null");
        }

        // Validate required fields
        if (building.getName() == null || building.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Building name is required");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = JDBCUtil.getConnection();
            String sql =
                    """
            INSERT INTO building (name, street, ward, district, structure, 
                                numberofbasement, floorarea,
                    direction, level, 
                                rentprice,
                    rentpricedescription, servicefee,
                                carfee, motofee, overtimefee, waterfee, 
                                electricityfee,
                    deposit, payment, renttime,
                                decorationtime, brokeragefee, note, linkofbuilding, 
                                map, avatar, createddate,
                    createdby, managername, managerphone)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?)
            """;

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, building.getName());
            statement.setString(2, building.getStreet());
            statement.setString(3, building.getWard());
            statement.setString(5, building.getStructure());
            statement.setObject(7, building.getFloorArea());
            statement.setString(8, building.getDirection());
            statement.setString(9, building.getLevel());
            statement.setObject(10, building.getRentPrice());

            statement.setString(27, "system"); // createdby
            statement.setString(28, building.getManagerName());


            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Creating building failed, no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                building.setId(generatedKeys.getInt(1));
            } else {
                throw new RuntimeException("Creating building failed, no ID obtained.");
            }

            return building;

        } catch (Exception e) {
            throw new RuntimeException("Error creating building: " + e.getMessage(), e);
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }

    /**
     * Cập nhật building
     */
    public Building updateBuilding(Building building) {
        if (building == null || building.getId() == null) {
            throw new IllegalArgumentException("Building and Building ID cannot be null");
        }

        // Validate required fields
        if (building.getName() == null || building.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Building name is required");
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection =
                    JDBCUtil.
                    getConnection();
            String sql = """
            UPDATE building SET 
                name = ?, street = ?, ward = ?, district = ?, structure = ?, 
                numberofbasement = ?, floorarea = ?, direction = ?, level
                    = ?, 
                rentprice = ?, rentpricedescription = ?
                    , servicefee = ?, 
                carfee = ?, motofee = ?,
                    overtimefee = ?, waterfee = ?, 
                electricityfee = ?, deposit = ?, payment = ?, renttime = ?, 
                decorationtime = ?,
                    brokeragefee = ?, note = ?, linkofbuilding = ?
                   
                           map = ?, avatar = ?, modifieddate = NOW(), modifiedby = ?, 
                managername = ?, managerphone = ?
            WHERE id = ?
            """;

            statement = connection.prepareStatement(sql);

            statement.setString(1, building.getName());
            statement.setString(2, building.getStreet());
            statement.setString(3, building.getWard());
            statement.setString(5, building.getStructure());
            statement.setObject(7, building.getFloorArea());
            statement.setString(8, building.getDirection());
            statement.setString(9, building.getLevel());
            statement.setObject(10, building.getRentPrice());

            statement.setString(27, "system"); // modifiedby
            statement.setString(28, building.getManagerName());
            statement.setInt(30, building.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Building not found with ID: " + building.getId());
            }

            return building;

        } catch (Exception e) {
            throw new RuntimeException("Error updating building: " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }

    /**
     * Xóa building
     */
    public boolean deleteBuilding(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Building ID cannot be null");
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = JDBCUtil.getConnection();
            String sql = "DELETE FROM building WHERE id = ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Building not found with ID: " + id);
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException("Error deleting building: " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }
}
