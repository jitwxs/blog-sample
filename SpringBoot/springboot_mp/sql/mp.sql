/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : mp

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 30/11/2018 19:43:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(5) NULL DEFAULT NULL,
  `create_date` datetime(0) NOT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_role_id`(`age`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('168850b0e6564544a58340a7515d7802', '张三', '男', 20, '2018-03-19 20:20:22', '2018-03-20 16:04:35');
INSERT INTO `user` VALUES ('16d664a133914d03a1cfae3bf87ace33', '里大根', '男', 41, '2018-03-19 20:22:19', NULL);
INSERT INTO `user` VALUES ('2b65bf6de3c74dfab49bdd453d4edd49', '吴祥生', '男', 21, '2018-03-19 19:12:17', '2018-03-19 20:21:17');
INSERT INTO `user` VALUES ('525d1f1548c34fd4ad7af850e187816f', '小红', '男', 12, '2018-03-19 20:21:58', NULL);
INSERT INTO `user` VALUES ('6178da8b1aa94264a4d198f66d7fdbff', '张全蛋', '男', 26, '2018-03-19 20:25:52', NULL);
INSERT INTO `user` VALUES ('86b5d86b34e742768268e6d50b65d604', '张老师', '女', 22, '2018-03-19 20:25:36', NULL);
INSERT INTO `user` VALUES ('9673e6e090f84b4383c7e0de897b2062', '张小丽', '女', 26, '2018-03-19 20:22:06', NULL);
INSERT INTO `user` VALUES ('97db0ec083be4057ada8e4660b1c1a53', '刘畅', '女', 22, '2018-03-19 19:25:24', '2018-03-19 20:21:31');
INSERT INTO `user` VALUES ('9a3683cdcb954f4a8b066116d52d2a70', '牛头人', '男', 51, '2018-03-19 20:23:04', NULL);
INSERT INTO `user` VALUES ('ba8875ebf3234e3d9f1c5228f4fb8466', '李四', '男', 18, '2018-03-19 20:21:50', NULL);
INSERT INTO `user` VALUES ('cca10f99eff0406a9fa8def1345dc809', '盒子怪', '男', 81, '2018-03-19 20:22:43', NULL);

SET FOREIGN_KEY_CHECKS = 1;
