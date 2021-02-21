drop DATABASE if exists alipay_sample;

create DATABASE alipay_sample;

USE alipay_sample;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `subject` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单名称',
  `body` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单描述',
  `money` float NULL DEFAULT NULL COMMENT '付款金额',
  `seller_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户UID',
  `alipay_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝订单号',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态（与官方统一）【WAIT_BUYER_PAY：交易创建，等待买家付款；TRADE_CLOSED：未付款交易超时关闭，或支付完成后全额退款；TRADE_SUCCESS：交易支付成功；TRADE_FINISHED：交易结束，不可退款】',
  `refund_money` float NULL DEFAULT 0 COMMENT '总计退款金额',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('152810603232866', '测试订单', '测试订单描述', 2, '2088102175634311', '2018060421001004880200500828', 'TRADE_CLOSED', 2, '2018-06-04 17:53:52', '2018-06-04 21:36:03');
INSERT INTO `order_info` VALUES ('152811273724868', '测试订单', '测试订单描述', 3, '2088102175634311', '2018060421001004880200500829', 'TRADE_CLOSED', 3, '2018-06-04 19:45:37', '2018-06-04 21:48:58');
INSERT INTO `order_info` VALUES ('152812453754242', '购买飞机', '波音747', 1024.5, '2088102175634311', NULL, 'WAIT_BUYER_PAY', 0, '2018-06-04 23:02:17', NULL);
INSERT INTO `order_info` VALUES ('152812469139783', '测试订单', '测试订单描述', 2, '2088102175634311', NULL, 'WAIT_BUYER_PAY', 0, '2018-06-04 23:04:51', NULL);
INSERT INTO `order_info` VALUES ('152812486041626', '测试订单', '测试订单描述', 2, '2088102175634311', NULL, 'WAIT_BUYER_PAY', 0, '2018-06-04 23:07:40', NULL);
INSERT INTO `order_info` VALUES ('152812497150420', '测试订单', '测试订单描述', 2, '2088102175634311', NULL, 'WAIT_BUYER_PAY', 0, '2018-06-04 23:09:31', NULL);
INSERT INTO `order_info` VALUES ('152812504631966', '测试订单', '测试订单描述', 2, '2088102175634311', '2018060421001004880200500505', 'TRADE_CLOSED', 2, '2018-06-04 23:10:46', '2018-06-04 23:23:41');
INSERT INTO `order_info` VALUES ('152812543127604', '购买飞机', '波音747', 0.01, '2088102175634311', NULL, 'TRADE_CLOSED', 0, '2018-06-04 23:17:11', '2018-06-04 23:17:52');
INSERT INTO `order_info` VALUES ('152812832132431', '购买飞机', '波音747', 23333, '2088102175634311', '2018060521001004880200500507', 'TRADE_CLOSED', 23333, '2018-06-05 00:05:21', '2018-06-05 00:06:57');

-- ----------------------------
-- Table structure for order_refund
-- ----------------------------
DROP TABLE IF EXISTS `order_refund`;
CREATE TABLE `order_refund`  (
  `refund_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '退款号',
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `money` float NULL DEFAULT NULL COMMENT '退款金额',
  `account` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款账户',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  `refund_date` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款时间',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单退款' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_refund
-- ----------------------------
INSERT INTO `order_refund` VALUES ('152811924045798', '152810603232866', 1, 'uce***@sandbox.com', '任性退款', '2018-06-04 21:34:00');
INSERT INTO `order_refund` VALUES ('152811926652968', '152810603232866', 0.55, 'uce***@sandbox.com', '任性退款', '2018-06-04 21:34:26');
INSERT INTO `order_refund` VALUES ('152811936160675', '152810603232866', 0.45, 'uce***@sandbox.com', '任性退款', '2018-06-04 21:36:02');
INSERT INTO `order_refund` VALUES ('152812013680939', '152811273724868', 3, 'uce***@sandbox.com', '任性退款', '2018-06-04 21:48:57');
INSERT INTO `order_refund` VALUES ('152812575214239', '152812504631966', 1.4, 'uce***@sandbox.com', '测试退款', '2018-06-04 23:22:32');
INSERT INTO `order_refund` VALUES ('152812581149652', '152812504631966', 0.3, 'uce***@sandbox.com', '测试退款', '2018-06-04 23:23:31');
INSERT INTO `order_refund` VALUES ('152812581997104', '152812504631966', 0.3, 'uce***@sandbox.com', '测试退款', '2018-06-04 23:23:39');
INSERT INTO `order_refund` VALUES ('152812840923518', '152812832132431', 20000, 'uce***@sandbox.com', '不想买了，太贵了', '2018-06-05 00:06:49');
INSERT INTO `order_refund` VALUES ('152812841665717', '152812832132431', 3333, 'uce***@sandbox.com', '不想买了，太贵了', '2018-06-05 00:06:56');

SET FOREIGN_KEY_CHECKS = 1;
