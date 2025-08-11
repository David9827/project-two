export interface Building {
  id?: number;
  name: string;
  street: string;
  ward: string;
  districtId: number;
  structure?: string;
  numberFloor: number;
  floorArea: number;
  direction: string;
  level: string;
  rentPrice: number;
  rentPriceDescription?: string;
  managerName: string;
  managerPhoneNumber: string;
}

export interface BuildingDTO {
  id?: number; // Added ID field
  name: string;
  floorArea: number;
  districtName: string;
  districtId?: number; // Added for form handling
  ward: string;
  street: string;
  numberFloor: number;
  direction: string;
  level: string;
  rentAria: number;
  rentPrice: number;
  managerName: string;
  managerPhoneNumber: string;
  buildingRentType: string;
  structure?: string;
  rentPriceDescription?: string;
}

export interface BuildingSearchCriteria {
  name?: string;
  floorArea?: number;
  districtId?: number;
  districtName?: string;
  ward?: string;
  street?: string;
  numberFloor?: number;
  direction?: string;
  level?: string;
  rentPriceMin?: number;
  rentPriceMax?: number;
  rentAriaMin?: number;
  rentAriaMax?: number;
  managerName?: string;
  managerPhoneNumber?: string;
  buildingType?: string[];
  buildingRentType?: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
  role: 'ADMIN' | 'USER';
  firstName?: string;
  lastName?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

export interface AuthContextType {
  user: User | null;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
}