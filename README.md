# Hệ thống Quản lý Bất động sản - Frontend

Ứng dụng React TypeScript cho hệ thống quản lý và thuê bất động sản.

## Tính năng chính

### 🏠 Trang người dùng
- Tìm kiếm và lọc bất động sản theo nhiều tiêu chí
- Xem thông tin chi tiết bất động sản
- Giao diện thân thiện và responsive
- Liên hệ với người quản lý

### 🔐 Hệ thống đăng nhập
- Đăng nhập cho admin và người dùng
- Bảo mật với token-based authentication
- Protected routes theo vai trò

### 👨‍💼 Trang quản trị (Admin)
- Dashboard với thống kê tổng quan
- Quản lý bất động sản (CRUD operations)
- Giao diện admin với sidebar navigation
- Responsive design

## Công nghệ sử dụng

- **React 18** với TypeScript
- **Material-UI (MUI)** cho UI components
- **React Router** cho navigation
- **Axios** cho API calls
- **Context API** cho state management

## Cài đặt và chạy

### Yêu cầu
- Node.js 16+ và npm
- Spring Boot backend đang chạy trên port 8080

### Cài đặt dependencies
```bash
npm install
```

### Chạy ứng dụng
```bash
npm start
```

Ứng dụng sẽ chạy trên: http://localhost:3000

### Build cho production
```bash
npm run build
```

## Tài khoản demo

Để test hệ thống, sử dụng các tài khoản sau:

**Admin:**
- Username: `admin`
- Password: `admin`

**User:**
- Username: `user`
- Password: `user`

## Cấu trúc thư mục

```
src/
├── components/
│   ├── admin/              # Admin components
│   │   ├── Dashboard.tsx
│   │   └── BuildingManagement.tsx
│   ├── auth/               # Authentication components
│   │   ├── LoginForm.tsx
│   │   └── ProtectedRoute.tsx
│   ├── layout/             # Layout components
│   │   └── AdminLayout.tsx
│   └── user/               # User components
│       └── UserHomePage.tsx
├── context/
│   └── AuthContext.tsx     # Authentication context
├── services/
│   ├── api.ts              # Axios configuration
│   ├── authService.ts      # Auth API calls
│   └── buildingService.ts  # Building API calls
├── types/
│   └── index.ts            # TypeScript interfaces
├── App.tsx                 # Main app component
└── index.tsx              # Entry point
```

## API Integration

Ứng dụng được thiết kế để tích hợp với Spring Boot backend:

- **Base URL**: `http://localhost:8080`
- **Auth endpoints**: `/api/auth/*`
- **Building endpoints**: `/api/building/*`

Hiện tại sử dụng mock data để demo, có thể dễ dàng thay thế bằng real API calls.

## Tính năng sẽ phát triển

- [ ] Quản lý người dùng
- [ ] Upload và quản lý hình ảnh
- [ ] Hệ thống thông báo
- [ ] Chat/messaging
- [ ] Payment integration
- [ ] Advanced search với map
- [ ] Mobile app với React Native

## Development

### Code style
- Sử dụng TypeScript strict mode
- Material-UI design system
- Responsive first approach
- Component-based architecture

### State Management
- React Context cho authentication
- Local state cho component data
- Service layer cho API integration

## Hỗ trợ

Nếu có vấn đề hoặc câu hỏi, vui lòng tạo issue hoặc liên hệ team phát triển.
