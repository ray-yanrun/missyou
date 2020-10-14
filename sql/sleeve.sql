/*
Navicat MySQL Data Transfer

Source Server         : 本地Mysql
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : sleeve

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-10-14 22:37:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(60) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `start_time` datetime(3) NOT NULL,
  `end_time` datetime(3) NOT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `remark` varchar(60) DEFAULT NULL,
  `online` tinyint(3) unsigned DEFAULT '1',
  `entrance_img` varchar(255) DEFAULT NULL,
  `internal_top_img` varchar(255) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity
-- ----------------------------

-- ----------------------------
-- Table structure for activity_category
-- ----------------------------
DROP TABLE IF EXISTS `activity_category`;
CREATE TABLE `activity_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` int(10) unsigned NOT NULL,
  `activity_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_category
-- ----------------------------

-- ----------------------------
-- Table structure for activity_coupon
-- ----------------------------
DROP TABLE IF EXISTS `activity_coupon`;
CREATE TABLE `activity_coupon` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_id` int(10) unsigned NOT NULL,
  `activity_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for activity_spu
-- ----------------------------
DROP TABLE IF EXISTS `activity_spu`;
CREATE TABLE `activity_spu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `activity_id` int(10) unsigned NOT NULL,
  `spu_id` int(10) unsigned NOT NULL,
  `participation` tinyint(3) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_spu
-- ----------------------------

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL COMMENT '部分banner可能有标题图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES ('3', 'b-1', '第一个banner的描述', '2020-09-24 16:50:07.228', '2020-09-24 16:50:07.228', null, 'banner1', 'img1');
INSERT INTO `banner` VALUES ('4', 'b-2', '第二个banner的描述', '2020-09-24 16:50:32.431', '2020-09-24 16:50:32.431', null, 'banner2', 'img2');

-- ----------------------------
-- Table structure for banner_item
-- ----------------------------
DROP TABLE IF EXISTS `banner_item`;
CREATE TABLE `banner_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `img` varchar(255) DEFAULT NULL,
  `keyword` varchar(50) DEFAULT NULL,
  `type` smallint(5) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `banner_id` int(10) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of banner_item
-- ----------------------------
INSERT INTO `banner_item` VALUES ('16', 'img3-1', 'name1-1', '0', '2020-09-24 16:50:51.392', '2020-09-24 16:52:17.034', null, '3', 'name1-1');
INSERT INTO `banner_item` VALUES ('17', 'img3-2', 'name1-2', '0', '2020-09-24 16:50:54.859', '2020-09-24 16:52:14.161', null, '3', 'name1-2');
INSERT INTO `banner_item` VALUES ('18', 'img4-1', 'name2-1', '0', '2020-09-24 16:50:58.291', '2020-09-24 16:52:20.729', null, '4', 'name2-1');
INSERT INTO `banner_item` VALUES ('19', 'img4-2', 'name2-2', '0', '2020-09-24 16:51:00.650', '2020-09-24 16:52:23.893', null, '4', 'name2-2');
INSERT INTO `banner_item` VALUES ('20', 'img4-3', 'name2-3', '0', '2020-09-24 16:51:06.657', '2020-09-24 16:52:27.675', null, '4', 'name2-3');

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of brand
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `is_root` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `parent_id` int(10) unsigned DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `index` int(10) unsigned DEFAULT NULL,
  `online` int(10) unsigned DEFAULT '1',
  `level` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `full_money` decimal(10,2) DEFAULT NULL,
  `minus` decimal(10,2) DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT NULL,
  `type` smallint(6) NOT NULL COMMENT '1. 满减券 2.折扣券 3.无门槛券 4.满金额折扣券',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `activity_id` int(10) unsigned DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `whole_store` tinyint(3) unsigned DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of coupon
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_category
-- ----------------------------
DROP TABLE IF EXISTS `coupon_category`;
CREATE TABLE `coupon_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` int(10) unsigned NOT NULL,
  `coupon_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of coupon_category
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_template
-- ----------------------------
DROP TABLE IF EXISTS `coupon_template`;
CREATE TABLE `coupon_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `full_money` decimal(10,2) DEFAULT NULL,
  `minus` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `type` smallint(6) NOT NULL COMMENT '1. 满减券 2.折扣券 3.无门槛券 4.满金额折扣券',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of coupon_template
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_type
-- ----------------------------
DROP TABLE IF EXISTS `coupon_type`;
CREATE TABLE `coupon_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  `code` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of coupon_type
-- ----------------------------

-- ----------------------------
-- Table structure for grid_category
-- ----------------------------
DROP TABLE IF EXISTS `grid_category`;
CREATE TABLE `grid_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `root_category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of grid_category
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_no` varchar(20) DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL COMMENT 'user表外键',
  `total_price` decimal(10,2) DEFAULT '0.00',
  `total_count` int(11) unsigned DEFAULT '0',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `expired_time` datetime(3) DEFAULT NULL,
  `placed_time` datetime(3) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `snap_img` varchar(255) DEFAULT NULL,
  `snap_title` varchar(255) DEFAULT NULL,
  `snap_items` json DEFAULT NULL,
  `snap_address` json DEFAULT NULL,
  `prepay_id` varchar(255) DEFAULT NULL,
  `final_total_price` decimal(10,2) DEFAULT NULL,
  `status` tinyint(3) unsigned DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for sale_explain
-- ----------------------------
DROP TABLE IF EXISTS `sale_explain`;
CREATE TABLE `sale_explain` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fixed` tinyint(3) unsigned DEFAULT '0',
  `text` varchar(100) NOT NULL,
  `spu_id` int(11) DEFAULT NULL,
  `index` int(10) unsigned DEFAULT NULL,
  `replace_id` int(10) unsigned DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sale_explain
-- ----------------------------

-- ----------------------------
-- Table structure for sku
-- ----------------------------
DROP TABLE IF EXISTS `sku`;
CREATE TABLE `sku` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `price` decimal(10,2) unsigned NOT NULL,
  `discount_price` decimal(10,2) unsigned DEFAULT NULL,
  `online` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `img` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `spu_id` int(10) unsigned NOT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `specs` text,
  `code` varchar(255) DEFAULT NULL,
  `stock` int(10) unsigned NOT NULL DEFAULT '0',
  `category_id` int(10) unsigned DEFAULT NULL,
  `root_category_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sku
-- ----------------------------
INSERT INTO `sku` VALUES ('52', '59.99', '59.99', '1', 'ada', '单品1', '29', '2020-09-27 10:44:23.392', '2020-09-29 19:18:55.018', null, '[{\"key\":\"颜色\",\"keyId\":1,\"value\":\"红\",\"valueId\":1}]', '1', '0', '1', '1');
INSERT INTO `sku` VALUES ('53', '89.99', '89.99', '1', 'fsad', '单品2', '29', '2020-09-27 10:44:54.552', '2020-09-29 19:20:43.413', null, '[{\"key\":\"颜色\",\"keyId\":1,\"value\":\"蓝\",\"valueId\":2},{\"key\":\"尺寸\",\"keyId\":2,\"value\":\"M\",\"valueId\":4}]', '1', '0', '1', '1');

-- ----------------------------
-- Table structure for sku_spec
-- ----------------------------
DROP TABLE IF EXISTS `sku_spec`;
CREATE TABLE `sku_spec` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `spu_id` int(10) unsigned NOT NULL,
  `sku_id` int(10) unsigned NOT NULL,
  `key_id` int(10) unsigned NOT NULL,
  `value_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sku_spec
-- ----------------------------

-- ----------------------------
-- Table structure for spec_key
-- ----------------------------
DROP TABLE IF EXISTS `spec_key`;
CREATE TABLE `spec_key` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `unit` varchar(30) DEFAULT NULL,
  `standard` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spec_key
-- ----------------------------

-- ----------------------------
-- Table structure for spec_value
-- ----------------------------
DROP TABLE IF EXISTS `spec_value`;
CREATE TABLE `spec_value` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL,
  `spec_id` int(10) unsigned NOT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `extend` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spec_value
-- ----------------------------

-- ----------------------------
-- Table structure for spu
-- ----------------------------
DROP TABLE IF EXISTS `spu`;
CREATE TABLE `spu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `subtitle` varchar(800) DEFAULT NULL,
  `category_id` int(10) unsigned NOT NULL,
  `root_category_id` int(11) DEFAULT NULL,
  `online` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `price` varchar(20) NOT NULL COMMENT '文本型价格，有时候SPU需要展示的是一个范围，或者自定义平均价格',
  `sketch_spec_id` int(10) unsigned DEFAULT NULL COMMENT '某种规格可以直接附加单品图片',
  `default_sku_id` int(11) DEFAULT NULL COMMENT '默认选中的sku',
  `img` varchar(255) DEFAULT NULL,
  `discount_price` varchar(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `tags` varchar(30) DEFAULT NULL,
  `is_test` tinyint(3) unsigned DEFAULT '0',
  `spu_theme_img` json DEFAULT NULL,
  `for_theme_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spu
-- ----------------------------
INSERT INTO `spu` VALUES ('29', '商品1', '一个商品', '1', '1', '1', '2020-09-27 10:42:46.942', '2020-09-27 10:42:46.942', null, '99.80', '1', '1', 'img', '9.99', '描述', '1', '0', null);

-- ----------------------------
-- Table structure for spu_detail_img
-- ----------------------------
DROP TABLE IF EXISTS `spu_detail_img`;
CREATE TABLE `spu_detail_img` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `img` varchar(255) NOT NULL,
  `spu_id` int(10) unsigned DEFAULT NULL,
  `index` int(10) unsigned NOT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spu_detail_img
-- ----------------------------
INSERT INTO `spu_detail_img` VALUES ('28', 'aaaa', '29', '1', '2020-09-27 10:43:29.754', '2020-09-27 10:43:29.754', null);

-- ----------------------------
-- Table structure for spu_img
-- ----------------------------
DROP TABLE IF EXISTS `spu_img`;
CREATE TABLE `spu_img` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `img` varchar(255) DEFAULT NULL,
  `spu_id` int(10) unsigned DEFAULT NULL,
  `delete_time` datetime(3) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spu_img
-- ----------------------------
INSERT INTO `spu_img` VALUES ('194', 'ssss', '29', null, '2020-09-27 10:43:10.912', '2020-09-27 10:43:10.912');
INSERT INTO `spu_img` VALUES ('195', 'qqq', '29', null, '2020-09-27 10:43:17.605', '2020-09-27 10:43:17.605');

-- ----------------------------
-- Table structure for spu_key
-- ----------------------------
DROP TABLE IF EXISTS `spu_key`;
CREATE TABLE `spu_key` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `spu_id` int(10) unsigned NOT NULL,
  `spec_key_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spu_key
-- ----------------------------

-- ----------------------------
-- Table structure for spu_tag
-- ----------------------------
DROP TABLE IF EXISTS `spu_tag`;
CREATE TABLE `spu_tag` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `spu_id` int(10) unsigned NOT NULL,
  `tag_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spu_tag
-- ----------------------------

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL COMMENT '中文限制6个，英文限制12个，由逻辑层控制',
  `description` varchar(255) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `highlight` tinyint(4) unsigned DEFAULT '0',
  `type` tinyint(3) unsigned DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for theme
-- ----------------------------
DROP TABLE IF EXISTS `theme`;
CREATE TABLE `theme` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(60) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `tpl_name` varchar(30) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  `entrance_img` varchar(255) DEFAULT NULL,
  `extend` varchar(255) DEFAULT NULL,
  `internal_top_img` varchar(255) DEFAULT NULL,
  `title_img` varchar(255) DEFAULT NULL,
  `online` tinyint(3) unsigned DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of theme
-- ----------------------------

-- ----------------------------
-- Table structure for theme_spu
-- ----------------------------
DROP TABLE IF EXISTS `theme_spu`;
CREATE TABLE `theme_spu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `theme_id` int(10) unsigned NOT NULL,
  `spu_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of theme_spu
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) DEFAULT NULL,
  `nickname` varchar(60) DEFAULT NULL,
  `unify_uid` int(10) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `mobile` varchar(30) DEFAULT NULL,
  `wx_profile` json DEFAULT NULL,
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_openid` (`openid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `coupon_id` int(10) unsigned NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1:未使用，2：已使用， 3：已过期',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `order_id` int(10) unsigned DEFAULT NULL,
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_user_coupon` (`user_id`,`coupon_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------
