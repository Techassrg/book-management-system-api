# Book Management System API

[English](#english) | [ภาษาไทย](#thai)

---

## English

### Overview
A RESTful API for managing books built with Kotlin and Spring Boot. This API allows users to manage a list of books in a MySQL database with authentication.

### Features
- ✅ Get books by author
- ✅ Save new books
- ✅ Buddhist calendar date support
- ✅ Basic authentication
- ✅ Swagger UI documentation
- ✅ Integration tests
- ✅ Input validation
- ✅ Health Check endpoints
- ✅ Database indexing for performance
- ✅ Docker support

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher (or Docker)
- Gradle 7.0 or higher

### Quick Start

#### Option 1: Using Docker (Recommended)
```bash
# Clone repository
git clone <repository-url>
cd Book_Management_System

# Start MySQL database
make docker-up

# Run application
make run
```

#### Option 2: Manual Setup
```bash
# Clone repository
git clone <repository-url>
cd Book_Management_System

# Create MySQL database
mysql -u root -p
CREATE DATABASE book_management_system;

# Run application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Database Setup

The application will automatically create the `books` table with the following structure:
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_date DATE NOT NULL,
    INDEX idx_author (author),
    INDEX idx_published_date (published_date)
);
```

### Configuration

#### For Development (H2 Database)
######## During local development and testing, I have used the H2 in-memory database instead of MySQL due to a current issue with MySQL drivers on my MacBook.

However, the application is designed to support both H2 (for local testing) and MySQL (for production usage), and can easily switch between the two via environment-specific configuration.
For development only, you can use H2 in-memory database:
```properties
spring.datasource.url=jdbc:h2:mem:bookdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

#### For Production (MySQL - Required by Assignment)
The application uses MySQL database as required by the assignment:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookdb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Or use make command
make test
```

### API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

### Health Monitoring

The application includes health check endpoints:

- **Custom Health Check**: `http://localhost:8080/api/health`
- **Simple Health Check**: `http://localhost:8080/api/health/simple`
- **Actuator Health**: `http://localhost:8080/actuator/health`

**Example Health Check Response:**
```json
{
  "status": "UP",
  "timestamp": "2025-08-05 00:27:57",
  "message": "Book Management System is running",
  "version": "1.0.0",
  "database": "MySQL"
}
```

### API Endpoints

#### Authentication
All endpoints require Basic Authentication:
- Username: `user`
- Password: `password`

#### 1. Get Books by Author
```
GET /books?author={authorName}
```

**Example Request:**
```bash
curl -X GET "http://localhost:8080/books?author=Techattara%20Srijaisuk" \
  -u user:password
```

**Example Response:**
```json
[
  {
    "id": 1,
    "title": "Kotlin Spring Boot",
    "author": "Techattara Srijaisuk",
    "publishedDate": "2024-06-15"
  }
]
```

#### 2. Save Book
```
POST /books
```

**Example Request:**
```json
{
  "title": "Kotlin Programming",
  "author": "Techasit Srirueng",
  "publishedDate": "2568-01-01"
}
```

**Example Response:**
```json
{
  "id": 1,
  "title": "Kotlin Programming",
  "author": "Techasit Srirueng",
  "publishedDate": "2025-01-01"
}
```

### Validation Rules
- Title and author must not be empty
- Published date must be in format `yyyy-MM-dd`
- Year must be > 1000 and <= current year
- Buddhist calendar dates are automatically converted to Gregorian calendar (subtract 543 years)

### Useful Commands

```bash
# View all available commands
make help

# Build project
make build

# Run application
make run

# Run tests
make test

# Start database
make docker-up

# Stop database
make docker-down

# Open Swagger UI
make docs

# Setup everything
make setup
```

---

## ภาษาไทย

### ภาพรวม
API สำหรับจัดการหนังสือที่สร้างด้วย Kotlin และ Spring Boot API นี้ช่วยให้ผู้ใช้สามารถจัดการรายการหนังสือในฐานข้อมูล MySQL พร้อมระบบยืนยันตัวตน

### คุณสมบัติ
- ✅ ดึงหนังสือตามผู้เขียน
- ✅ บันทึกหนังสือใหม่
- ✅ รองรับวันที่แบบพุทธศักราช
- ✅ การยืนยันตัวตนแบบ Basic Authentication
- ✅ เอกสาร API ผ่าน Swagger UI
- ✅ การทดสอบแบบ Integration
- ✅ การตรวจสอบข้อมูลนำเข้า
- ✅ Health Check endpoints
- ✅ การ optimize query ด้วย index
- ✅ รองรับ Docker

### ความต้องการของระบบ
- Java 17 หรือสูงกว่า
- MySQL 8.0 หรือสูงกว่า (หรือ Docker)
- Gradle 7.0 หรือสูงกว่า

### การเริ่มต้นใช้งาน

#### วิธีที่ 1: ใช้ Docker (แนะนำ)
```bash
# Clone repository
git clone <repository-url>
cd Book_Management_System

# เริ่มฐานข้อมูล MySQL
make docker-up

# รันแอปพลิเคชัน
make run
```

#### วิธีที่ 2: ติดตั้งเอง
```bash
# Clone repository
git clone <repository-url>
cd Book_Management_System

# สร้างฐานข้อมูล MySQL
mysql -u root -p
CREATE DATABASE book_management_system;

# รันแอปพลิเคชัน
./gradlew bootRun
```

แอปพลิเคชันจะเริ่มทำงานที่ `http://localhost:8080`

### การตั้งค่าฐานข้อมูล

แอปพลิเคชันจะสร้างตาราง `books` อัตโนมัติด้วยโครงสร้างดังนี้:
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_date DATE NOT NULL,
    INDEX idx_author (author),
    INDEX idx_published_date (published_date)
);
```

### การกำหนดค่า

#### สำหรับการพัฒนา (H2 Database)
######ในการพัฒนาและทดสอบระบบภายในเครื่อง ผมเลือกใช้ฐานข้อมูล H2 แทน MySQL เนื่องจากปัจจุบันเครื่อง MacBook ของผมมีปัญหาเรื่อง Driver ของ MySQL ซึ่งส่งผลให้ไม่สามารถเชื่อมต่อกับ MySQL ได้ชั่วคราว

อย่างไรก็ตาม ระบบถูกออกแบบให้สามารถใช้งานได้ทั้ง H2 (ใน local) และ MySQL (ใน production) โดยสามารถสลับการใช้งานผ่านไฟล์ environment configuration ได้
แอปพลิเคชันใช้ H2 in-memory database เป็นค่าเริ่มต้นสำหรับการพัฒนาที่ง่าย:
```properties
spring.datasource.url=jdbc:h2:mem:bookdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

#### สำหรับ Production (MySQL)
อัปเดต `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/book_management_system
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### การรันการทดสอบ

```bash
# รันการทดสอบทั้งหมด
./gradlew test

# หรือใช้ make command
make test
```

### เอกสาร API

เข้าถึง Swagger UI ได้ที่: `http://localhost:8080/swagger-ui/index.html`

### การตรวจสอบสถานะ

แอปพลิเคชันมี health check endpoints:

- **Health Check แบบละเอียด**: `http://localhost:8080/api/health`
- **Health Check แบบง่าย**: `http://localhost:8080/api/health/simple`
- **Actuator Health**: `http://localhost:8080/actuator/health`

**ตัวอย่าง Health Check Response:**
```json
{
  "status": "UP",
  "timestamp": "2025-08-05 00:27:57",
  "message": "Book Management System is running",
  "version": "1.0.0",
  "database": "H2 (In-Memory)"
}
```

### API Endpoints

#### การยืนยันตัวตน
ทุก endpoint ต้องการ Basic Authentication:
- Username: `user`
- Password: `password`

#### 1. ดึงหนังสือตามผู้เขียน
```
GET /books?author={authorName}
```

**ตัวอย่างการร้องขอ:**
```bash
curl -X GET "http://localhost:8080/books?author=Techattara%20Srijaisuk" \
  -u user:password
```

**ตัวอย่างการตอบกลับ:**
```json
[
  {
    "id": 1,
    "title": "Kotlin Spring Boot",
    "author": "Techattara Srijaisuk",
    "publishedDate": "2024-06-15"
  }
]
```

#### 2. บันทึกหนังสือ
```
POST /books
```

**ตัวอย่างการร้องขอ:**
```json
{
  "title": "Kotlin Programming",
  "author": "Techasit Srirueng",
  "publishedDate": "2568-01-01"
}
```

**ตัวอย่างการตอบกลับ:**
```json
{
  "id": 1,
  "title": "Kotlin Programming",
  "author": "Techasit Srirueng",
  "publishedDate": "2025-01-01"
}
```

### กฎการตรวจสอบข้อมูล
- ชื่อหนังสือและผู้เขียนต้องไม่เป็นค่าว่าง
- วันที่พิมพ์ต้องเป็นรูปแบบ `yyyy-MM-dd`
- ปีต้อง > 1000 และ <= ปีปัจจุบัน
- วันที่แบบพุทธศักราชจะถูกแปลงเป็นคริสต์ศักราชอัตโนมัติ (ลบ 543 ปี)

### คำสั่งที่มีประโยชน์

```bash
# ดูคำสั่งทั้งหมด
make help

# สร้างโปรเจค
make build

# รันแอปพลิเคชัน
make run

# รันการทดสอบ
make test

# เริ่มฐานข้อมูล
make docker-up

# หยุดฐานข้อมูล
make docker-down

# เปิด Swagger UI
make docs

# ติดตั้งทั้งหมด
make setup
```

### โครงสร้างโปรเจค
```
Book_Management_System/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/asmt/Book_Management_System/
│   │   │       ├── controller/     # REST API endpoints
│   │   │       ├── service/        # Business logic
│   │   │       ├── repository/     # Data access layer
│   │   │       ├── entity/         # Database entities
│   │   │       ├── dto/            # Data transfer objects
│   │   │       └── config/         # Configuration classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-local.properties
│   └── test/
│       └── kotlin/                 # Integration tests
├── database/
│   └── schema.sql                  # Database schema
├── docker-compose.yml              # Docker configuration
├── Makefile                        # Build automation
└── README.md                       # Documentation
```

### การพัฒนา

#### การตั้งค่าสำหรับการพัฒนา
```bash
# ใช้ profile local
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### การเพิ่มข้อมูลทดสอบ
```sql
INSERT INTO books (title, author, published_date) VALUES
('Kotlin Programming', 'Techasit Srirueng', '2025-01-01'),
('Spring Boot Guide', 'Techattara Srijaisuk', '2024-06-15'),
('Database Design', 'Test Author', '2024-01-01');
```

### การแก้ไขปัญหา

#### ปัญหาการเชื่อมต่อฐานข้อมูล
1. ตรวจสอบว่า MySQL กำลังทำงาน
2. ตรวจสอบ username และ password ใน application.properties
3. ตรวจสอบว่า database `book_management_system` มีอยู่

#### ปัญหาการรันแอปพลิเคชัน
1. ตรวจสอบว่า Java 17 ติดตั้งแล้ว
2. ตรวจสอบว่า Gradle wrapper มีอยู่
3. ลองรัน `./gradlew clean build`

### การปรับปรุงประสิทธิภาพ
- ใช้ index บนคอลัมน์ author และ published_date
- ใช้ optimized query ใน repository
- ใช้ connection pooling
- ใช้ caching สำหรับข้อมูลที่ใช้บ่อย

### การติดต่อ
หากมีคำถามหรือปัญหา กรุณาสร้าง issue ใน GitHub repository 
