# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ApiDocManager is an API documentation management tool with:

- **Backend**: Spring Boot 3.x + MyBatis-Plus + MySQL 8.0
- **Frontend**: Vue 3 + Element Plus + Axios + Vue Router
- **Database**: `api_doc_manager` on port 3306
- **Ports**: Backend 8081, Frontend 5173

## Commands

### Backend (apidoc-backend)

```bash
cd apidoc-backend
mvn spring-boot:run        # Start backend
mvn clean compile          # Compile only
```

### Frontend (apidoc-frontend)

```bash
cd apidoc-frontend
npm run dev               # Start dev server (http://localhost:5173)
npm run build             # Production build
npm run preview           # Preview production build
```

## Architecture

### Backend Layer Structure

```
controller -> service -> mapper -> database
```

- **Controller**: Handles HTTP requests, returns `Result<T>` wrapper
- **Service**: Business logic, validation, cascade operations
- **Mapper**: MyBatis-Plus `BaseMapper` for CRUD

### Frontend Page Flow

```
ProjectList -> ApiList (select project) -> ApiDetail (select endpoint)
```

- **ProjectList.vue**: Main entry, lists all projects
- **ApiList.vue**: Left tree (groups) + Right table (endpoints)
- **ApiDetail.vue**: Full endpoint details with JSON display

### API Response Format

All APIs return `Result<T>` with structure: `{ code: 200, msg: "success", data: T }`

- `code != 200` indicates error
- Use `BusinessException` for business logic errors (code defaults to 400)

### Key Conventions

**Database**: No logical deletion. Cascade hard delete for project/group hierarchy.

**Unique Constraints**:

- Project: `name` unique
- ApiEndpoint: `project_id + method + path` unique (excludes deleted rows)

**Entity Fields**: Use `@TableName` with actual DB table names. Follow snake_case to camelCase mapping configured in MyBatis-Plus.

**Frontend API**: Axios instance at `src/api/axios.js` with baseURL `http://localhost:8081`. Response interceptor extracts `res.data` from `Result` wrapper.

## Database Tables

| Table        | Key Columns                                                                                                                                                | Notes                       |
| ------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------- |
| project      | id, name, description, base_url, status, created_at, updated_at                                                                                            | name is unique              |
| api_group    | id, project_id, name, sort_order, created_at                                                                                                               | No updated_at column        |
| api_endpoint | id, project_id, group_id, method, path, summary, description, request_headers, request_params, request_body, response_body, status, created_at, updated_at | Unique: project+method+path |

## Important Notes

- **Do NOT add logical deletion** (deleted column) - this project uses hard deletes only
- **Do NOT modify api_doc_manager.sql** - it's the reference for actual database schema
- Align entity fields exactly with database columns - field mismatch causes SQL errors



严格按照openspec进行开发  并且不能修改数据库 数据库参考api_doc_manager.sql文件
