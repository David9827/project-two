package com.javaweb.controller;

import com.javaweb.beans.Building;
import com.javaweb.database.JDBCUtil;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.gethttp.BuildingSearchCriteria;
import com.javaweb.service.BuildingServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:3000"})
public class UserController {
    @GetMapping(value = "api/building")
    public List<BuildingDTO> getBuilding(@RequestBody BuildingSearchCriteria buildingSearchCriteria){
        BuildingServiceImp buildingServiceImp = new BuildingServiceImp();
        buildingServiceImp.getListBuilding(buildingSearchCriteria);
        List<BuildingDTO> listb = new ArrayList<>(buildingServiceImp.getListBuilding(buildingSearchCriteria));
        return listb;
    }

    private final BuildingServiceImp buildingService;

    public UserController() {
        this.buildingService = new BuildingServiceImp();
        System.out.println("🚀 UserController initialized successfully");
    }

    // ==================== SEARCH & READ OPERATIONS ====================

    @PostMapping("/building/search")
    public ResponseEntity<Map<String, Object>> searchBuildings(
            @RequestBody(required = false) BuildingSearchCriteria searchCriteria) {

        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔍 POST /api/building/search - Search request received");

            // Handle null criteria
            if (searchCriteria == null) {
                searchCriteria = new BuildingSearchCriteria();
                System.out.println("⚠️ Search criteria is null, using empty criteria");
            }

            List<BuildingDTO> buildings = buildingService.getListBuilding(searchCriteria);

            response.put("success", true);
            response.put("data", buildings);
            response.put("count", buildings.size());
            response.put("message", "Search completed successfully");
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("✅ Search completed - Found " + buildings.size() + " buildings");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Search error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Search failed");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/building")
    public ResponseEntity<Map<String, Object>> getAllBuildings(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Integer ward,
            @RequestParam(required = false) Integer floorArea,
            @RequestParam(required = false) Integer rentPrice) {

        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("📋 GET /api/building - Get all buildings with filters");

            // Create criteria from query parameters
            BuildingSearchCriteria criteria = new BuildingSearchCriteria();
            criteria.setName(name);

            List<BuildingDTO> buildings = buildingService.getListBuilding(criteria);

            response.put("success", true);
            response.put("data", buildings);
            response.put("count", buildings.size());
            response.put("message", "Buildings retrieved successfully");
            response.put("filters", createFiltersMap(name, district, ward, floorArea, rentPrice));
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("✅ Retrieved " + buildings.size() + " buildings");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Get all buildings error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to retrieve buildings");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/building/{id}")
    public ResponseEntity<Map<String, Object>> getBuildingById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔍 GET /api/building/" + id + " - Get building by ID");

            if (id == null || id <= 0) {
                response.put("success", false);
                response.put("message", "Invalid building ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            BuildingDTO building = buildingService.getBuildingById(id);

            if (building != null) {
                response.put("success", true);
                response.put("data", building);
                response.put("message", "Building found");
                response.put("timestamp", System.currentTimeMillis());

                System.out.println("✅ Found building: " + building.getName());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Building not found with ID: " + id);
                response.put("timestamp", System.currentTimeMillis());

                System.out.println("❌ Building not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            System.err.println("❌ Get building by ID error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to retrieve building");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== CREATE OPERATIONS ====================

    @PostMapping("/building")
    public ResponseEntity<Map<String, Object>> createBuilding(@RequestBody Building building) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("➕ POST /api/building - Create new building: " +
                    (building != null ? building.getName() : "null"));

            if (building == null) {
                response.put("success", false);
                response.put("message", "Building data is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Basic validation
            if (building.getName() == null || building.getName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Building name is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Building createdBuilding = buildingService.createBuilding(building);

            response.put("success", true);
            response.put("data", createdBuilding);
            response.put("message", "Building created successfully");
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("✅ Created building with ID: " + createdBuilding.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation error: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Validation failed");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            System.err.println("❌ Create building error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to create building");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== UPDATE OPERATIONS ====================

    @PutMapping("/building/{id}")
    public ResponseEntity<Map<String, Object>> updateBuilding(
            @PathVariable Integer id, @RequestBody Building building) {

        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("✏️ PUT /api/building/" + id + " - Update building");

            if (id == null || id <= 0) {
                response.put("success", false);
                response.put("message", "Invalid building ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (building == null) {
                response.put("success", false);
                response.put("message", "Building data is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            building.setId(id); // Ensure ID matches path parameter
            Building updatedBuilding = buildingService.updateBuilding(building);

            response.put("success", true);
            response.put("data", updatedBuilding);
            response.put("message", "Building updated successfully");
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("✅ Updated building with ID: " + id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation error: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Validation failed or building not found");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            System.err.println("❌ Update building error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to update building");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== DELETE OPERATIONS ====================

    @DeleteMapping("/building/{id}")
    public ResponseEntity<Map<String, Object>> deleteBuilding(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🗑️ DELETE /api/building/" + id + " - Delete building");

            if (id == null || id <= 0) {
                response.put("success", false);
                response.put("message", "Invalid building ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean deleted = buildingService.deleteBuilding(id);

            if (deleted) {
                response.put("success", true);
                response.put("message", "Building deleted successfully");
                response.put("timestamp", System.currentTimeMillis());

                System.out.println("✅ Deleted building with ID: " + id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete building - building may not exist");
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Building not found: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Building not found");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            System.err.println("❌ Delete building error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to delete building");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== UTILITY ENDPOINTS ====================

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "Backend is running!");
        status.put("timestamp", System.currentTimeMillis());
        status.put("version", "1.0.0");

        try {
            Connection connection = JDBCUtil.getConnection();
            if (connection != null && !connection.isClosed()) {
                status.put("database", "Connected successfully ✅");
                status.put("dbStatus", "CONNECTED");

                // Test a simple query
                boolean canQuery = JDBCUtil.testConnection();
                status.put("dbQuery", canQuery ? "Query test passed ✅" : "Query test failed ❌");

                JDBCUtil.closeConnection(connection);
            } else {
                status.put("database", "Connection failed ❌");
                status.put("dbStatus", "ERROR");
            }
        } catch (Exception e) {
            status.put("database", "Connection failed: " + e.getMessage() + " ❌");
            status.put("dbStatus", "ERROR");
            status.put("error", e.getMessage());
        }

        System.out.println("🏥 Health check - DB Status: " + status.get("dbStatus"));

        // Return 200 even if DB is down (service might work with mock data)
        return ResponseEntity.ok(status);
    }

    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> result = new HashMap<>();

        try {
            System.out.println("🧪 Database test started...");

            // Test connection first
            Connection testConn = JDBCUtil.getConnection();
            if (testConn == null) {
                result.put("success", false);
                result.put("message", "Cannot establish database connection");
                result.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            }
            JDBCUtil.closeConnection(testConn);

            // Test service query
            BuildingSearchCriteria criteria = new BuildingSearchCriteria();
            List<BuildingDTO> buildings = buildingService.getListBuilding(criteria);

            result.put("success", true);
            result.put("message", "Database query successful");
            result.put("buildingCount", buildings.size());
            result.put("timestamp", System.currentTimeMillis());
            result.put("connectionInfo", JDBCUtil.getConnectionInfo());

            if (!buildings.isEmpty()) {
                BuildingDTO firstBuilding = buildings.get(0);
                Map<String, Object> sampleBuilding = new HashMap<>();
                sampleBuilding.put("id", firstBuilding.getId());
                sampleBuilding.put("name", firstBuilding.getName());
                sampleBuilding.put("district", firstBuilding.getDistrictName());
                sampleBuilding.put("price", firstBuilding.getRentPrice());
                sampleBuilding.put("area", firstBuilding.getFloorArea());
                result.put("sampleBuilding", sampleBuilding);
            } else {
                result.put("note", "No buildings found in database");
            }

            System.out.println("✅ Database test successful - Found " + buildings.size() + " buildings");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("message", "Database test failed");
            result.put("timestamp", System.currentTimeMillis());

            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.ok(result); // Still return 200 to show the error info
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("name", "Building Management API");
        info.put("version", "1.0.0");
        info.put("timestamp", System.currentTimeMillis());

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/health", "Health check");
        endpoints.put("GET /api/test-db", "Database test");
        endpoints.put("GET /api/building", "Get all buildings (with optional filters)");
        endpoints.put("POST /api/building/search", "Search buildings");
        endpoints.put("GET /api/building/{id}", "Get building by ID");
        endpoints.put("POST /api/building", "Create building");
        endpoints.put("PUT /api/building/{id}", "Update building");
        endpoints.put("DELETE /api/building/{id}", "Delete building");

        info.put("endpoints", endpoints);
        info.put("database", JDBCUtil.getConnectionInfo());

        return ResponseEntity.ok(info);
    }

    // ==================== HELPER METHODS ====================

    private Map<String, Object> createFiltersMap(String name, String district, Integer ward,
                                                 Integer floorArea, Integer rentPrice) {
        Map<String, Object> filters = new HashMap<>();
        if (name != null) filters.put("name", name);
        if (district != null) filters.put("district", district);
        if (ward != null) filters.put("ward", ward);
        if (floorArea != null) filters.put("floorArea", floorArea);
        if (rentPrice != null) filters.put("rentPrice", rentPrice);
        return filters;
    }
}