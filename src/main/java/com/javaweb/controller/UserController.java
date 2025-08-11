
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {

    private final BuildingServiceImp buildingService;

    public UserController() {
        this.buildingService = new BuildingServiceImp();
    }

    // ==================== SEARCH & READ OPERATIONS ====================

    @PostMapping("/building/search")
    public ResponseEntity<Map<String, Object>> searchBuildings(@RequestBody BuildingSearchCriteria searchCriteria) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔍 POST /api/building/search - Search request received");

            List<BuildingDTO> buildings = buildingService.getListBuilding(searchCriteria);

            response.put("success", true);
            response.put("data", buildings);
            response.put("count", buildings.size());
            response.put("message", "Search completed successfully");

            System.out.println("✅ Search completed - Found " + buildings.size() + " buildings");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Search error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Search failed");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/building")
    public ResponseEntity<Map<String, Object>> getAllBuildings() {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("📋 GET /api/building - Get all buildings");

            BuildingSearchCriteria emptyCriteria = new BuildingSearchCriteria();
            List<BuildingDTO> buildings = buildingService.getListBuilding(emptyCriteria);

            response.put("success", true);
            response.put("data", buildings);
            response.put("count", buildings.size());
            response.put("message", "Buildings retrieved successfully");

            System.out.println("✅ Retrieved " + buildings.size() + " buildings");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Get all buildings error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to retrieve buildings");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/building/{id}")
    public ResponseEntity<Map<String, Object>> getBuildingById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔍 GET /api/building/" + id + " - Get building by ID");

            BuildingDTO building = buildingService.getBuildingById(id);

            if (building != null) {
                response.put("success", true);
                response.put("data", building);
                response.put("message", "Building found");

                System.out.println("✅ Found building: " + building.getName());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Building not found");

                System.out.println("❌ Building not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            System.err.println("❌ Get building by ID error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to retrieve building");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== CREATE OPERATIONS ====================

    @PostMapping("/building")
    public ResponseEntity<Map<String, Object>> createBuilding(@RequestBody Building building) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("➕ POST /api/building - Create new building: " + building.getName());

            Building createdBuilding = buildingService.createBuilding(building);

            response.put("success", true);
            response.put("data", createdBuilding);
            response.put("message", "Building created successfully");

            System.out.println("✅ Created building with ID: " + createdBuilding.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation error: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Validation failed");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            System.err.println("❌ Create building error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to create building");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== UPDATE OPERATIONS ====================

    @PutMapping("/building/{id}")
    public ResponseEntity<Map<String, Object>> updateBuilding(@PathVariable Integer id, @RequestBody Building building) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("✏️ PUT /api/building/" + id + " - Update building");

            building.setId(id); // Ensure ID is set from path variable
            Building updatedBuilding = buildingService.updateBuilding(building);

            response.put("success", true);
            response.put("data", updatedBuilding);
            response.put("message", "Building updated successfully");

            System.out.println("✅ Updated building with ID: " + id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation error: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Validation failed or building not found");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            System.err.println("❌ Update building error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to update building");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== DELETE OPERATIONS ====================

    @DeleteMapping("/building/{id}")
    public ResponseEntity<Map<String, Object>> deleteBuilding(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🗑️ DELETE /api/building/" + id + " - Delete building");

            boolean deleted = buildingService.deleteBuilding(id);

            if (deleted) {
                response.put("success", true);
                response.put("message", "Building deleted successfully");

                System.out.println("✅ Deleted building with ID: " + id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete building");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Building not found: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Building not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            System.err.println("❌ Delete building error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to delete building");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== UTILITY ENDPOINTS ====================

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "Backend is running!");
        status.put("timestamp", System.currentTimeMillis());

        try {
            Connection connection = JDBCUtil.getConnection();
            if (connection != null) {
                status.put("database", "Connected successfully ✅");
                status.put("dbStatus", "CONNECTED");
                connection.close();
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
        return ResponseEntity.ok(status);
    }
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> result = new HashMap<>();

        try {
            System.out.println("🧪 Database test started...");

            BuildingSearchCriteria criteria = new BuildingSearchCriteria();
            List<BuildingDTO> buildings = buildingService.getListBuilding(criteria);

            result.put("success", true);
            result.put("message", "Database query successful");
            result.put("buildingCount", buildings.size());
            result.put("timestamp", System.currentTimeMillis());

            if (!buildings.isEmpty()) {
                BuildingDTO firstBuilding = buildings.get(0);
                Map<String, Object> sampleBuilding = new HashMap<>();
                sampleBuilding.put("name", firstBuilding.getName());
                sampleBuilding.put("district", firstBuilding.getDistrictName());
                sampleBuilding.put("price", firstBuilding.getRentPrice());
                sampleBuilding.put("area", firstBuilding.getFloorArea());
                result.put("sampleBuilding", sampleBuilding);
            }

            System.out.println("✅ Database test successful - Found " + buildings.size() + " buildings");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());

            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.ok(result);
        }
    }
}
