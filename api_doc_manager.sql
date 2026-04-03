/*
 Navicat Premium Dump SQL

 Source Server         : db(locallhost)
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : api_doc_manager

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 03/04/2026 09:52:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_endpoint
-- ----------------------------
DROP TABLE IF EXISTS `api_endpoint`;
CREATE TABLE `api_endpoint`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` bigint NOT NULL COMMENT '所属项目ID',
  `group_id` bigint NULL DEFAULT NULL COMMENT '所属分组ID',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方法：GET/POST/PUT/DELETE',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口路径（如 /api/users/{id}）',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口摘要',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口详细描述',
  `request_headers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求头（JSON格式）',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数（JSON格式）',
  `request_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求体示例（JSON格式）',
  `response_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '响应体示例（JSON格式）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-已完成 2-开发中 3-已废弃',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_project_method_path`(`project_id` ASC, `method` ASC, `path` ASC) USING BTREE,
  INDEX `idx_project_id`(`project_id` ASC) USING BTREE,
  INDEX `idx_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_endpoint
-- ----------------------------
INSERT INTO `api_endpoint` VALUES (1, 1, 1, 'POST', '/auth/login', '用户登录', '通过用户名密码登录获取JWT令牌', NULL, NULL, NULL, '{\"code\":200,\"msg\":\"success\",\"data\":{\"token\":\"eyJhbGciOiJIUzI1NiJ9...\"}}', 1, '2026-04-03 09:15:49', '2026-04-03 09:15:49');
INSERT INTO `api_endpoint` VALUES (2, 1, 1, 'GET', '/system/user/page', '分页查询用户', '分页查询系统用户列表', NULL, '[{\"name\":\"current\",\"type\":\"Integer\",\"required\":false,\"description\":\"当前页\",\"example\":\"1\"},{\"name\":\"size\",\"type\":\"Integer\",\"required\":false,\"description\":\"每页条数\",\"example\":\"10\"}]', NULL, '{\"code\":200,\"msg\":\"success\",\"data\":{\"total\":50,\"records\":[{\"id\":1,\"username\":\"admin\",\"realName\":\"管理员\"}]}}', 1, '2026-04-03 09:15:49', '2026-04-03 09:15:49');
INSERT INTO `api_endpoint` VALUES (3, 1, 2, 'GET', '/purchase/order/page', '分页查询采购订单', '按条件分页查询采购订单列表', NULL, '[{\"name\":\"poNo\",\"type\":\"String\",\"required\":false,\"description\":\"采购单号\"},{\"name\":\"status\",\"type\":\"String\",\"required\":false,\"description\":\"状态\"}]', NULL, '{\"code\":200,\"msg\":\"success\",\"data\":{\"total\":10,\"records\":[{\"id\":1,\"poNo\":\"PO20260401\",\"supplierName\":\"供应商A\",\"status\":\"DRAFT\"}]}}', 1, '2026-04-03 09:15:49', '2026-04-03 09:15:49');
INSERT INTO `api_endpoint` VALUES (4, 1, 2, 'POST', '/purchase/order', '新增采购订单', '创建新的采购订单', NULL, NULL, NULL, '{\"code\":200,\"msg\":\"新增成功\",\"data\":null}', 2, '2026-04-03 09:15:49', '2026-04-03 09:15:49');
INSERT INTO `api_endpoint` VALUES (5, 2, 4, 'POST', '/api/user/login', '学生登录', '学生通过学号和密码登录', NULL, NULL, NULL, '{\"code\":1,\"data\":{\"token\":\"xxx\"},\"msg\":\"登录成功\"}', 1, '2026-04-03 09:15:49', '2026-04-03 09:15:49');

-- ----------------------------
-- Table structure for api_group
-- ----------------------------
DROP TABLE IF EXISTS `api_group`;
CREATE TABLE `api_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` bigint NOT NULL COMMENT '所属项目ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称（如：用户模块、订单模块）',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序号',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_project_id`(`project_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '接口分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_group
-- ----------------------------
INSERT INTO `api_group` VALUES (1, 1, '用户管理', 1, '2026-04-03 09:15:49');
INSERT INTO `api_group` VALUES (2, 1, '采购管理', 2, '2026-04-03 09:15:49');
INSERT INTO `api_group` VALUES (3, 1, '库存管理', 3, '2026-04-03 09:15:49');
INSERT INTO `api_group` VALUES (4, 2, '用户模块', 1, '2026-04-03 09:15:49');
INSERT INTO `api_group` VALUES (5, 2, '订单模块', 2, '2026-04-03 09:15:49');

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目描述',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基础URL（如 https://api.example.com）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, 'ERP管理系统', '汽车配件出口企业ERP系统的后端API', 'http://localhost:8080', 1, '2026-04-03 09:15:49', '2026-04-03 09:15:49');
INSERT INTO `project` VALUES (2, '校园服务平台', '高校校园综合服务平台API', 'http://localhost:9090', 1, '2026-04-03 09:15:49', '2026-04-03 09:15:49');

SET FOREIGN_KEY_CHECKS = 1;
