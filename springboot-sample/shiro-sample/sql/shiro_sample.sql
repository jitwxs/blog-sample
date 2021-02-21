drop DATABASE if exists shiro_sample;

create DATABASE shiro_sample;

USE shiro_sample;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `create_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', 'create', '2018-03-20 10:48:17', null);
INSERT INTO `permission` VALUES ('2', 'delete', '2018-03-20 10:48:24', null);
INSERT INTO `permission` VALUES ('3', 'update', '2018-03-20 10:48:30', null);
INSERT INTO `permission` VALUES ('4', 'select', '2018-03-20 10:48:37', null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `create_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'admin', '2018-03-20 10:47:25', null);
INSERT INTO `role` VALUES ('2', 'teacher', '2018-03-20 10:47:59', null);
INSERT INTO `role` VALUES ('3', 'student', '2018-03-20 10:48:08', null);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` varchar(32) NOT NULL,
  `permission_id` varchar(32) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `fk_permission_id` (`permission_id`),
  CONSTRAINT `fk_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_role_id1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1', '2018-03-20 10:49:52');
INSERT INTO `role_permission` VALUES ('1', '2', '2018-03-20 10:49:58');
INSERT INTO `role_permission` VALUES ('1', '3', '2018-03-20 10:50:02');
INSERT INTO `role_permission` VALUES ('1', '4', '2018-03-20 10:50:07');
INSERT INTO `role_permission` VALUES ('2', '1', '2018-03-20 10:50:26');
INSERT INTO `role_permission` VALUES ('2', '4', '2018-03-20 10:50:31');
INSERT INTO `role_permission` VALUES ('3', '4', '2018-03-20 10:50:36');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(64) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'jitwxs', '123', '男', '2018-03-20 10:47:12', null);
INSERT INTO `user` VALUES ('2', 'zhangsan', '123', '男', '2018-03-20 21:02:55', null);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` varchar(32) NOT NULL,
  `role_id` varchar(32) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_role_id` (`role_id`),
  CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '2018-03-20 10:48:52');
INSERT INTO `user_role` VALUES ('2', '3', '2018-03-20 21:03:05');
