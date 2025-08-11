package com.javaweb.respository;

import com.javaweb.beans.Building;
import com.javaweb.database.JDBCUtil;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {

    public List<BuildingDTO> findBuildingsWithCriteria(BuildingSearchCriteria criteria) {
        List<BuildingDTO> buildings = new ArrayList<>();
        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Could not establish database connection");
            }

            StringBuilder sql = new StringBuilder(
                    "SELECT b.*, d.districtname " +
                            "FROM building b " +
                            "LEFT JOIN district d ON b.districtid = d.id " +
                            "WHERE 1=1 "
            );

            List<Object> params = new ArrayList<>();

            // Dynamic query building
            if (criteria.getId() != null ) {
                sql.append("AND b.id >= ? ");
                params.add(criteria.getId());
            }

            if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
                sql.append("AND b.buildingname LIKE ? ");
                params.add("%" + criteria.getName().trim() + "%");
            }

            if (criteria.getFloorArea() != null) {
                sql.append("AND b.floorarea >= ? ");
                params.add(criteria.getFloorArea());
            }

            if (criteria.getDistrictName() != null && !criteria.getDistrictName().trim().isEmpty()) {
                sql.append("AND d.districtname LIKE ? ");
                params.add("%" + criteria.getDistrictName().trim() + "%");
            }

            if (criteria.getWard() != null && !criteria.getWard().trim().isEmpty()) {
                sql.append("AND b.ward LIKE ? ");
                params.add("%" + criteria.getWard().trim() + "%");
            }

            if (criteria.getStreet() != null && !criteria.getStreet().trim().isEmpty()) {
                sql.append("AND b.street LIKE ? ");
                params.add("%" + criteria.getStreet().trim() + "%");
            }

            if (criteria.getNumberFloor() != null) {
                sql.append("AND b.numberfloor >= ? ");
                params.add(criteria.getNumberFloor());
            }

            if (criteria.getDirection() != null && !criteria.getDirection().trim().isEmpty()) {
                sql.append("AND b.direction LIKE ? ");
                params.add("%" + criteria.getDirection().trim() + "%");
            }

            if (criteria.getLevel() != null && !criteria.getLevel().trim().isEmpty()) {
                sql.append("AND b.level LIKE ? ");
                params.add("%" + criteria.getLevel().trim() + "%");
            }

            if (criteria.getRentPriceMin() != null && criteria.getRentPriceMax() != null) {
                sql.append("AND b.rentprice BETWEEN ? AND ? ");
                params.add(criteria.getRentPriceMin());
                params.add(criteria.getRentPriceMax());
            } else if (criteria.getRentPriceMin() != null) {
                sql.append("AND b.rentprice >= ? ");
                params.add(criteria.getRentPriceMin());
            } else if (criteria.getRentPriceMax() != null) {
                sql.append("AND b.rentprice <= ? ");
                params.add(criteria.getRentPriceMax());
            }

            if (criteria.getRentAriaMin() != null && criteria.getRentAriaMax() != null) {
                sql.append("AND b.rentaria BETWEEN ? AND ? ");
                params.add(criteria.getRentAriaMin());
                params.add(criteria.getRentAriaMax());
            }

            if (criteria.getManagerName() != null && !criteria.getManagerName().trim().isEmpty()) {
                sql.append("AND b.managername LIKE ? ");
                params.add("%" + criteria.getManagerName().trim() + "%");
            }

            if (criteria.getManagerPhoneNumber() != null && !criteria.getManagerPhoneNumber().trim().isEmpty()) {
                sql.append("AND b.managerphonenumber LIKE ? ");
                params.add("%" + criteria.getManagerPhoneNumber().trim() + "%");
            }

            sql.append("ORDER BY b.id DESC");

            try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
                // Set parameters
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                System.out.println("🔍 Executing query: " + sql.toString());
                System.out.println("📋 Parameters: " + params);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        BuildingDTO building = mapResultSetToDTO(rs);
                        buildings.add(building);
                    }
                }
            }

            System.out.println("✅ Successfully loaded " + buildings.size() + " buildings from database");

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database query failed", e);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

        return buildings;
    }

    public BuildingDTO findById(Integer id) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Could not establish database connection");
            }

            String sql = "SELECT b.*, d.districtname FROM building b " +
                    "LEFT JOIN district d ON b.districtid = d.id " +
                    "WHERE b.id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToDTO(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding building by ID: " + e.getMessage());
            throw new RuntimeException("Failed to find building", e);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

        return null;
    }

    public Building save(Building building) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Could not establish database connection");
            }

            String sql = "INSERT INTO building (buildingname, floorarea, ward, street, districtid, " +
                    "numberfloor, direction, level, rentaria, rentprice, managername, managerphonenumber) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, building.getId());
                stmt.setString(2, building.getName());
                stmt.setDouble(3, building.getFloorArea());
                stmt.setString(4, building.getWard());
                stmt.setString(5, building.getStreet());
                stmt.setInt(6, building.getDistrictId());
                stmt.setInt(7, building.getNumberFloor());
                stmt.setString(8, building.getDirection());
                stmt.setString(9, building.getLevel());
                stmt.setBigDecimal(10, building.getRentPrice()); // assuming rentaria = rentprice for now
                stmt.setBigDecimal(11, building.getRentPrice());
                stmt.setString(12, building.getManagerName());
                stmt.setString(13, building.getManagerPhoneNumber());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating building failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        building.setId(generatedKeys.getInt(1));
                    }
                }

                System.out.println("✅ Successfully created building: " + building.getName());
                return building;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error saving building: " + e.getMessage());
            throw new RuntimeException("Failed to save building", e);
        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    public Building update(Building building) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Could not establish database connection");
            }

            String sql = "UPDATE building SET buildingname=?, floorarea=?, ward=?, street=?, districtid=?, " +
                    "numberfloor=?, direction=?, level=?, rentaria=?, rentprice=?, managername=?, managerphonenumber=? " +
                    "WHERE id=?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, building.getId());
                stmt.setString(2, building.getName());
                stmt.setDouble(3, building.getFloorArea());
                stmt.setString(4, building.getWard());
                stmt.setString(5, building.getStreet());
                stmt.setInt(6, building.getDistrictId());
                stmt.setInt(7, building.getNumberFloor());
                stmt.setString(8, building.getDirection());
                stmt.setString(9, building.getLevel());
                stmt.setBigDecimal(10, building.getRentPrice()); // assuming rentaria = rentprice for now
                stmt.setBigDecimal(11, building.getRentPrice());
                stmt.setString(12, building.getManagerName());
                stmt.setString(13, building.getManagerPhoneNumber());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating building failed, no rows affected.");
                }

                System.out.println("✅ Successfully updated building: " + building.getName());
                return building;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating building: " + e.getMessage());
            throw new RuntimeException("Failed to update building", e);
        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    public boolean deleteById(Integer id) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Could not establish database connection");
            }

            String sql = "DELETE FROM building WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("✅ Successfully deleted building with ID: " + id);
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting building: " + e.getMessage());
            throw new RuntimeException("Failed to delete building", e);
        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    private BuildingDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(rs.getInt("id")); // Include ID for frontend operations
        dto.setName(rs.getString("buildingname"));
        dto.setFloorArea(rs.getDouble("floorarea"));
        dto.setDistrictName(rs.getString("districtname"));
        dto.setDistrictId(rs.getInt("districtid")); // Include district ID
        dto.setWard(rs.getString("ward"));
        dto.setStreet(rs.getString("street"));
        dto.setNumberFloor(rs.getInt("numberfloor"));
        dto.setDirection(rs.getString("direction"));
        dto.setLevel(rs.getString("level"));
        dto.setRentAria(rs.getBigDecimal("rentaria"));
        dto.setRentPrice(rs.getBigDecimal("rentprice"));
        dto.setManagerName(rs.getString("managername"));
        dto.setManagerPhoneNumber(rs.getString("managerphonenumber"));

        // Determine building type based on level or other criteria
        String level = rs.getString("level");
        if (level != null) {
            if (level.toLowerCase().contains("văn phòng") || level.toLowerCase().contains("office")) {
                dto.setBuildingRentType("Văn phòng");
            } else if (level.toLowerCase().contains("chung cư") || level.toLowerCase().contains("apartment")) {
                dto.setBuildingRentType("Chung cư");
            } else if (level.toLowerCase().contains("nhà phố") || level.toLowerCase().contains("house")) {
                dto.setBuildingRentType("Nhà phố");
            } else if (level.toLowerCase().contains("thương mại") || level.toLowerCase().contains("commercial")) {
                dto.setBuildingRentType("Mặt bằng");
            } else if (level.toLowerCase().contains("studio")) {
                dto.setBuildingRentType("Studio");
            } else {
                dto.setBuildingRentType("Khác");
            }
        } else {
            dto.setBuildingRentType("Không xác định");
        }

        return dto;
    }
}