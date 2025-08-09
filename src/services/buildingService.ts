import { api } from './api';
import { BuildingDTO, BuildingSearchCriteria, Building } from '../types';

export const buildingService = {
  // Lấy danh sách building theo criteria
  getBuildings: async (criteria: BuildingSearchCriteria): Promise<BuildingDTO[]> => {
    try {
      // Sử dụng POST để search với criteria
      const response = await api.post<BuildingDTO[]>('/api/building/search', criteria);
      return response.data;
    } catch (error) {
      console.error('Error fetching buildings:', error);
      // Fallback to GET all buildings
      try {
        const fallbackResponse = await api.get<BuildingDTO[]>('/api/building');
        return fallbackResponse.data;
      } catch (fallbackError) {
        console.error('Fallback also failed:', fallbackError);
        throw error;
      }
    }
  },

  // Lấy building theo ID
  getBuildingById: async (id: number): Promise<Building> => {
    const response = await api.get<Building>(`/api/building/${id}`);
    return response.data;
  },

  // Tạo building mới
  createBuilding: async (building: Omit<Building, 'id'>): Promise<Building> => {
    const response = await api.post<Building>('/api/building', building);
    return response.data;
  },

  // Cập nhật building
  updateBuilding: async (id: number, building: Partial<Building>): Promise<Building> => {
    const response = await api.put<Building>(`/api/building/${id}`, building);
    return response.data;
  },

  // Xóa building
  deleteBuilding: async (id: number): Promise<void> => {
    await api.delete(`/api/building/${id}`);
  },

  // Tìm kiếm building
  searchBuildings: async (searchTerm: string): Promise<BuildingDTO[]> => {
    const response = await api.get<BuildingDTO[]>(`/api/building/search?q=${searchTerm}`);
    return response.data;
  }
};
