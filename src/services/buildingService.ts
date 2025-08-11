import api from "./api";

export type BuildingStatus = "AVAILABLE" | "RENTED" | "MAINTENANCE";

export interface Building {
  id?: number | string;
  name: string;
  address: string;
  type?: string;
  area?: number;
  price?: number;
  status: BuildingStatus;
  createdAt?: string;
  updatedAt?: string;
}

export interface BuildingQuery {
  q?: string;
  status?: BuildingStatus | "";
  page?: number;
  size?: number;
}

export async function listBuildings(params: BuildingQuery) {
  // Gợi ý API: GET /api/building?page=&size=&q=&status=
  const res = await api.get("/api/building", { params });
  // Chiều theo nhiều dạng response (Pageable hoặc custom)
  const items = res.data?.content ?? res.data?.items ?? res.data ?? [];
  const total =
      res.data?.totalElements ?? res.data?.total ?? (Array.isArray(items) ? items.length : 0);
  return { items, total };
}

export async function createBuilding(payload: Partial<Building>) {
  const { data } = await api.post("/api/building", payload);
  return data;
}

export async function updateBuilding(id: number | string, payload: Partial<Building>) {
  const { data } = await api.put(`/api/building/${id}`, payload);
  return data;
}

export async function deleteBuilding(id: number | string) {
  await api.delete(`/api/building/${id}`);
}
