# Book Management System Makefile
# ไฟล์ Makefile สำหรับระบบจัดการหนังสือ

.PHONY: help build run test clean docker-up docker-down

# Default target
help:
	@echo "Book Management System - Available Commands:"
	@echo ""
	@echo "Development:"
	@echo "  build     - Build the application"
	@echo "  run       - Run the application with MySQL"
	@echo "  run-dev   - Run the application with H2 (development)"
	@echo "  test      - Run tests"
	@echo "  clean     - Clean build files"
	@echo ""
	@echo "Database:"
	@echo "  docker-up   - Start MySQL database with Docker"
	@echo "  docker-down - Stop MySQL database"
	@echo ""
	@echo "Documentation:"
	@echo "  docs       - Open Swagger UI in browser"

# Development commands
build:
	@echo "Building application..."
	./gradlew build

run:
	@echo "Starting application with MySQL..."
	./gradlew bootRun

run-dev:
	@echo "Starting application with H2 (development)..."
	./gradlew bootRun --args='--spring.profiles.active=dev'

test:
	@echo "Running tests..."
	./gradlew test

clean:
	@echo "Cleaning build files..."
	./gradlew clean

# Database commands
docker-up:
	@echo "Starting MySQL database..."
	docker-compose up -d mysql
	@echo "MySQL is running on localhost:3306"
	@echo "phpMyAdmin is available at http://localhost:8081"

docker-down:
	@echo "Stopping MySQL database..."
	docker-compose down

# Documentation
docs:
	@echo "Opening Swagger UI..."
	open http://localhost:8080/swagger-ui/index.html

# Full setup
setup: docker-up
	@echo "Waiting for database to be ready..."
	sleep 10
	@echo "Setup complete! You can now run 'make run' to start the application" 