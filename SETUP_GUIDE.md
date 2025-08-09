# 🚀 Hướng dẫn Setup Dự án Real Estate

## 📋 Yêu cầu hệ thống

### Bắt buộc:
- **Java JDK 17 trở lên** (Backend)
- **Node.js 16 trở lên** (Frontend)

### Tùy chọn:
- **Maven 3.6+** (hoặc dùng Maven wrapper có sẵn)
- **MySQL 8.0+** hoặc **Oracle Database** (nếu muốn dùng database)

## 🔧 Cài đặt phần mềm

### 1. Cài đặt Java JDK
```bash
# Kiểm tra Java hiện tại
java -version

# Nếu chưa có, tải từ:
# https://www.oracle.com/java/technologies/downloads/
# hoặc OpenJDK: https://adoptopenjdk.net/
```

### 2. Cài đặt Node.js
```bash
# Kiểm tra Node.js hiện tại
node -v
npm -v

# Nếu chưa có, tải từ:
# https://nodejs.org/
```

### 3. Cài đặt Maven (Tùy chọn)
```bash
# Kiểm tra Maven hiện tại
mvn -version

# Nếu chưa có, tải từ:
# https://maven.apache.org/download.cgi
# Hoặc dùng Maven wrapper có sẵn trong project (mvnw.cmd)
```

## 🚀 Cách chạy dự án

### ⚠️ **QUAN TRỌNG: Cài đặt Node.js trước**
Nếu chưa có Node.js:
1. **Tải về**: https://nodejs.org/ (chọn LTS version)
2. **Cài đặt** và restart command prompt/PowerShell
3. **Kiểm tra**: `node -v` và `npm -v`

### Cách 1: Chạy tự động (Khuyến nghị)
```bash
# Command Prompt
start-full-app.bat

# PowerShell
.\start-frontend.ps1
```

### Cách 2: Chạy từng phần

#### Frontend (React):
```bash
# Command Prompt
start.bat

# PowerShell  
.\start-frontend.ps1

# Manual
npm install
npm start
```

#### Backend (Spring Boot):
```bash
# Cách 1: Dùng script có sẵn
setup-backend.bat

# Cách 2: Dùng Maven
mvn spring-boot:run

# Cách 3: Dùng Maven wrapper
mvnw.cmd spring-boot:run
```

## 🌐 Truy cập ứng dụng

- **Frontend**: http://localhost:3000
- **Backend**: http://localhost:8080
- **Health Check**: http://localhost:8080/api/health

## 🔐 Tài khoản demo

- **Admin**: `admin` / `admin`
- **User**: `user` / `user`

## 🗄️ Cấu hình Database (Tùy chọn)

### MySQL:
1. Tạo database `real_estate_db`
2. Uncomment MySQL config trong `application.properties`
3. Cập nhật username/password

### Oracle:
1. Chuẩn bị Oracle database
2. Uncomment Oracle config trong `application.properties`
3. Cập nhật connection string

## 🛠️ Troubleshooting

### Backend không chạy được:
1. **Kiểm tra Java**: `java -version`
2. **Kiểm tra port 8080**: Đảm bảo không bị chiếm
3. **Xem log lỗi** trong console

### Frontend không chạy được:
1. **Kiểm tra Node.js**: `node -v`
2. **Cài lại dependencies**: `npm install`
3. **Xóa cache**: `npm start --reset-cache`

### CORS Error:
- Đảm bảo backend chạy trên port 8080
- Frontend chạy trên port 3000
- CORS đã được cấu hình trong backend

### Database Error:
- Kiểm tra connection string
- Đảm bảo database service đang chạy
- Nếu không dùng database, comment lại database configs

## 📁 Cấu trúc project

```
project-two/
├── src/main/java/          # Backend Spring Boot
├── src/main/resources/     # Backend configs
├── src/                    # Frontend React
├── public/                 # Frontend public files
├── pom.xml                # Maven dependencies
├── package.json           # NPM dependencies
└── *.bat                  # Setup scripts
```

## 🔄 Development Workflow

1. **Backend**: Code trong `src/main/java/`
2. **Frontend**: Code trong `src/components/`
3. **API**: Update trong `controller/` và `service/`
4. **Database**: Update trong `application.properties`

## 📞 Hỗ trợ

Nếu gặp vấn đề:
1. Kiểm tra console logs
2. Đảm bảo tất cả requirements đã cài đặt
3. Restart cả backend và frontend
4. Kiểm tra firewall/antivirus

## 🚀 Next Steps

- [ ] Setup database thật
- [ ] Implement authentication APIs
- [ ] Add image upload
- [ ] Deploy to production
