package com.javaweb.service;

import com.javaweb.database.JDBCUtil;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BuildingServiceImp {

    // ======= SEARCH + PAGINATION =======
    public List<BuildingDTO> search(BuildingSearchCriteria c, int page, int size) {
        List<BuildingDTO> out = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT b.id, b.buildingname, b.floorarea, b.ward, b.street, b.districtid, " +
                        "       b.numberfloor, b.direction, b.`level`, b.rentaria, b.rentprice, " +
                        "       b.managername, b.managerphonenumber, b.created_at, b.updated_at " +
                        "FROM building b WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (c != null) {
            if (c.getQ() != null && !c.getQ().isBlank()) {
                sql.append(" AND (LOWER(b.buildingname) LIKE ? OR LOWER(b.ward) LIKE ? OR LOWER(b.street) LIKE ?)");
                String like = "%" + c.getQ().toLowerCase() + "%";
                params.add(like); params.add(like); params.add(like);
            }
            if (c.getDistrictid() != null) {
                sql.append(" AND b.districtid = ?");
                params.add(c.getDistrictid());
            }
            if (c.getMinRentprice() != null) {
                sql.append(" AND b.rentprice >= ?");
                params.add(c.getMinRentprice());
            }
            if (c.getMaxRentprice() != null) {
                sql.append(" AND b.rentprice <= ?");
                params.add(c.getMaxRentprice());
            }
            if (c.getMinFloorarea() != null) {
                sql.append(" AND b.floorarea >= ?");
                params.add(c.getMinFloorarea());
            }
            if (c.getMaxFloorarea() != null) {
                sql.append(" AND b.floorarea <= ?");
                params.add(c.getMaxFloorarea());
            }
            if (c.getLevel() != null && !c.getLevel().isBlank()) {
                sql.append(" AND b.`level` = ?");
                params.add(c.getLevel());
            }
            if (c.getDirection() != null && !c.getDirection().isBlank()) {
                sql.append(" AND b.direction = ?");
                params.add(c.getDirection());
            }
        }

        sql.append(" ORDER BY b.id DESC LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Search building failed", e);
        }
        return out;
    }

    public int count(BuildingSearchCriteria c) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM building b WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (c != null) {
            if (c.getQ() != null && !c.getQ().isBlank()) {
                sql.append(" AND (LOWER(b.buildingname) LIKE ? OR LOWER(b.ward) LIKE ? OR LOWER(b.street) LIKE ?)");
                String like = "%" + c.getQ().toLowerCase() + "%";
                params.add(like); params.add(like); params.add(like);
            }
            if (c.getDistrictid() != null) {
                sql.append(" AND b.districtid = ?");
                params.add(c.getDistrictid());
            }
            if (c.getMinRentprice() != null) {
                sql.append(" AND b.rentprice >= ?");
                params.add(c.getMinRentprice());
            }
            if (c.getMaxRentprice() != null) {
                sql.append(" AND b.rentprice <= ?");
                params.add(c.getMaxRentprice());
            }
            if (c.getMinFloorarea() != null) {
                sql.append(" AND b.floorarea >= ?");
                params.add(c.getMinFloorarea());
            }
            if (c.getMaxFloorarea() != null) {
                sql.append(" AND b.floorarea <= ?");
                params.add(c.getMaxFloorarea());
            }
            if (c.getLevel() != null && !c.getLevel().isBlank()) {
                sql.append(" AND b.`level` = ?");
                params.add(c.getLevel());
            }
            if (c.getDirection() != null && !c.getDirection().isBlank()) {
                sql.append(" AND b.direction = ?");
                params.add(c.getDirection());
            }
        }

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Count building failed", e);
        }
    }

    // ======= CRUD =======

    public BuildingDTO findById(int id) {
        String sql = "SELECT b.id, b.buildingname, b.floorarea, b.ward, b.street, b.districtid, " +
                "       b.numberfloor, b.direction, b.`level`, b.rentaria, b.rentprice, " +
                "       b.managername, b.managerphonenumber, b.created_at, b.updated_at " +
                "FROM building b WHERE b.id = ?";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find building failed", e);
        }
    }

    public BuildingDTO create(BuildingDTO dto) {
        String sql = "INSERT INTO building (" +
                "buildingname, floorarea, ward, street, districtid, numberfloor, direction, `level`, " +
                "rentaria, rentprice, managername, managerphonenumber, created_at, updated_at" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bindUpsertParams(ps, dto);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dto.setId(keys.getInt(1));
            }
            dto.setCreatedAt(Instant.now());
            dto.setUpdatedAt(dto.getCreatedAt());
            return dto;
        } catch (SQLException e) {
            throw new RuntimeException("Create building failed", e);
        }
    }

    public BuildingDTO update(int id, BuildingDTO dto) {
        String sql = "UPDATE building SET " +
                "buildingname=?, floorarea=?, ward=?, street=?, districtid=?, numberfloor=?, " +
                "direction=?, `level`=?, rentaria=?, rentprice=?, managername=?, managerphonenumber=?, " +
                "updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            bindUpsertParams(ps, dto);
            ps.setInt(13, id); // tham số cuối cho WHERE id=?

            int n = ps.executeUpdate();
            if (n == 0) throw new RuntimeException("Building not found id=" + id);
            dto.setId(id);
            dto.setUpdatedAt(Instant.now());
            return dto;
        } catch (SQLException e) {
            throw new RuntimeException("Update building failed", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM building WHERE id = ?";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete building failed", e);
        }
    }

    // ======= Helpers =======

    private void bindUpsertParams(PreparedStatement ps, BuildingDTO dto) throws SQLException {
        // 1 buildingname
        ps.setString(1, dto.getBuildingname());

        // 2 floorarea (DOUBLE) - cho phép null
        if (dto.getFloorarea() == null) ps.setNull(2, Types.DOUBLE);
        else ps.setDouble(2, dto.getFloorarea());

        // 3 ward
        ps.setString(3, dto.getWard());

        // 4 street
        ps.setString(4, dto.getStreet());

        // 5 districtid (INT) - cho phép null
        if (dto.getDistrictid() == null) ps.setNull(5, Types.INTEGER);
        else ps.setInt(5, dto.getDistrictid());

        // 6 numberfloor (INT) - cho phép null
        if (dto.getNumberfloor() == null) ps.setNull(6, Types.INTEGER);
        else ps.setInt(6, dto.getNumberfloor());

        // 7 direction
        ps.setString(7, dto.getDirection());

        // 8 level (từ khóa nhạy cảm -> đã backtick trong SQL)
        ps.setString(8, dto.getLevel());

        // 9 rentaria (DECIMAL)
        if (dto.getRentaria() == null) ps.setNull(9, Types.DECIMAL);
        else ps.setBigDecimal(9, dto.getRentaria());

        // 10 rentprice (DECIMAL)
        if (dto.getRentprice() == null) ps.setNull(10, Types.DECIMAL);
        else ps.setBigDecimal(10, dto.getRentprice());

        // 11 managername
        ps.setString(11, dto.getManagername());

        // 12 managerphonenumber
        ps.setString(12, dto.getManagerphonenumber());
    }

    private BuildingDTO mapRow(ResultSet rs) throws SQLException {
        BuildingDTO b = new BuildingDTO();
        b.setId(rs.getInt("id"));

        b.setBuildingname(rs.getString("buildingname"));

        double fa = rs.getDouble("floorarea");
        b.setFloorarea(rs.wasNull() ? null : fa);

        b.setWard(rs.getString("ward"));
        b.setStreet(rs.getString("street"));

        int did = rs.getInt("districtid");
        b.setDistrictid(rs.wasNull() ? null : did);

        int nf = rs.getInt("numberfloor");
        b.setNumberfloor(rs.wasNull() ? null : nf);

        b.setDirection(rs.getString("direction"));
        b.setLevel(rs.getString("level"));

        b.setRentaria(rs.getBigDecimal("rentaria"));
        b.setRentprice(rs.getBigDecimal("rentprice"));

        b.setManagername(rs.getString("managername"));
        b.setManagerphonenumber(rs.getString("managerphonenumber"));

        Timestamp cAt = rs.getTimestamp("created_at");
        Timestamp uAt = rs.getTimestamp("updated_at");
        b.setCreatedAt(cAt != null ? cAt.toInstant() : null);
        b.setUpdatedAt(uAt != null ? uAt.toInstant() : null);
        return b;
    }
}
