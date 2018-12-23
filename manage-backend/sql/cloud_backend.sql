/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : cloud_backend

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 23/12/2018 14:50:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for black_ip
-- ----------------------------
DROP TABLE IF EXISTS `black_ip`;
CREATE TABLE `black_ip`  (
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '黑名单ip',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ip`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'ip黑名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `parentId` int(11) NOT NULL COMMENT '父菜单id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单url',
  `css` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单样式',
  `sort` int(11) NOT NULL COMMENT '排序',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单目录 2018年12月17日添加',
  `component` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由视图 2018年12月17日添加',
  `nameFullPath` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由层级 2018年12月17日添加',
  `level` int(11) NULL DEFAULT NULL COMMENT '树的层级 2018年12月17日添加',
  `url1` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url1 2018年12月17日添加',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间',
  `updateTime` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 0, '系统设置', '', 'fa-gears', 1, NULL, NULL, NULL, 1, NULL, '2018-01-23 10:20:30', '2018-01-23 10:20:31');
INSERT INTO `menu` VALUES (2, 1, '菜单', 'pages/menu/menuList.html', 'fa-windows', 2, 'menu', 'MenuView', NULL, 2, 'menuList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (3, 1, '角色', 'pages/role/roleList.html', 'fa-cubes', 3, 'role', 'RoleView', NULL, 2, 'roleList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (4, 1, '权限', 'pages/permission/permissionList.html', 'fa-align-justify', 4, 'permission', 'PermissionView', NULL, 2, 'permissionList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (5, 0, '用户管理', '', 'fa-user', 4, NULL, NULL, NULL, 1, NULL, '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (6, 5, '用户查询', 'pages/user/userList.html', 'fa-user', 4, 'user', 'UserView', NULL, 2, 'userList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (7, 0, '文件查询', 'pages/file/fileList.html', 'fa-folder-open', 5, 'file', 'FileView', NULL, 1, 'fileList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (8, 0, '邮件管理', 'pages/mail/mailList.html', 'fa-envelope', 6, 'mail', 'MailView', NULL, 1, 'mailList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (9, 0, '注册中心', 'http://local.register.com:8761', 'fa-institution', 7, NULL, NULL, NULL, 1, NULL, '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (10, 0, '监控中心', 'http://local.monitor.com:9001', 'fa-spinner', 8, NULL, NULL, NULL, 1, NULL, '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (11, 0, 'swagger文档', 'pages/swagger/api-doc.html', 'fa-file-pdf-o', 8, 'swagger', 'SwaggerView', NULL, 1, 'api-doc', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (12, 0, '黑名单ip', 'pages/blackIP/blackIPList.html', 'fa-child', 9, 'blackIP', 'BlackIPView', NULL, 1, 'blackIPList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (13, 0, '日志查询', 'pages/log/logList.html', 'fa-reorder', 10, 'log', 'LogView', NULL, 1, 'logList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (14, 0, '短信历史查询', 'pages/sms/smsList.html', 'fa-reorder', 11, 'sms', 'SmsView', NULL, 1, 'smsList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (15, 1, 'client管理', 'pages/client/clientList.html', 'fa-tachometer', 13, 'client', 'ClientView', NULL, 0, 'clientList', '2018-01-23 14:04:40', '2018-01-23 14:04:43');
INSERT INTO `menu` VALUES (16, 0, '分布式事物监控中心', 'http://localhost:8899', 'fa-bell', 100, NULL, NULL, NULL, 1, NULL, '2018-12-21 19:26:09', '2018-12-22 11:43:52');
INSERT INTO `menu` VALUES (17, 0, '日志监控', 'http://localhost:5601', 'fa-reorder', 100, NULL, NULL, NULL, 1, NULL, '2018-12-21 23:38:44', '2018-12-21 23:38:44');
INSERT INTO `menu` VALUES (18, 0, '服务链路跟踪', 'http://localhost:9411/zipkin/', 'fa-openid', 101, NULL, NULL, NULL, 1, NULL, '2018-12-23 10:25:15', '2018-12-23 10:25:25');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `menuId` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`roleId`, `menuId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 0);
INSERT INTO `role_menu` VALUES (1, 1);
INSERT INTO `role_menu` VALUES (1, 2);
INSERT INTO `role_menu` VALUES (1, 3);
INSERT INTO `role_menu` VALUES (1, 4);
INSERT INTO `role_menu` VALUES (1, 5);
INSERT INTO `role_menu` VALUES (1, 6);
INSERT INTO `role_menu` VALUES (1, 7);
INSERT INTO `role_menu` VALUES (1, 8);
INSERT INTO `role_menu` VALUES (1, 9);
INSERT INTO `role_menu` VALUES (1, 10);
INSERT INTO `role_menu` VALUES (1, 11);
INSERT INTO `role_menu` VALUES (1, 12);
INSERT INTO `role_menu` VALUES (1, 13);
INSERT INTO `role_menu` VALUES (1, 14);
INSERT INTO `role_menu` VALUES (1, 15);
INSERT INTO `role_menu` VALUES (1, 16);
INSERT INTO `role_menu` VALUES (1, 17);
INSERT INTO `role_menu` VALUES (1, 18);
INSERT INTO `role_menu` VALUES (2, 0);
INSERT INTO `role_menu` VALUES (2, 1);
INSERT INTO `role_menu` VALUES (2, 2);
INSERT INTO `role_menu` VALUES (2, 3);
INSERT INTO `role_menu` VALUES (2, 4);
INSERT INTO `role_menu` VALUES (2, 15);
INSERT INTO `role_menu` VALUES (11, 2);

-- ----------------------------
-- Table structure for t_mail
-- ----------------------------
DROP TABLE IF EXISTS `t_mail`;
CREATE TABLE `t_mail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '发送人id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送人用户名',
  `toEmail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收件人邮件地址',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正文',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0草稿，1成功，2失败',
  `sendTime` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间',
  `updateTime` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `updateTime`(`updateTime`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件发送记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mail
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
