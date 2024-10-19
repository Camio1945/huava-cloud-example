-- 管理端微服务
drop database if exists huava_cloud_admin;
drop user if exists 'huava_cloud_admin'@'%';
create database huava_cloud_admin default character set utf8mb4 collate utf8mb4_general_ci;
use huava_cloud_admin;
create user 'huava_cloud_admin'@'%' identified by 'eFS0H6_0_pkVm__o';
grant all privileges on huava_cloud_admin.* to 'huava_cloud_admin'@'%';
flush privileges;

-- 商品微服务
drop database if exists huava_cloud_goods;
drop user if exists 'huava_cloud_goods'@'%';
create database huava_cloud_goods default character set utf8mb4 collate utf8mb4_general_ci;
use huava_cloud_goods;
create user 'huava_cloud_goods'@'%' identified by 'eFS0H6_0_pkVm__o';
grant all privileges on huava_cloud_goods.* to 'huava_cloud_goods'@'%';
flush privileges;

-- 用户微服务
drop database if exists huava_cloud_member;
drop user if exists 'huava_cloud_member'@'%';
create database huava_cloud_member default character set utf8mb4 collate utf8mb4_general_ci;
use huava_cloud_member;
create user 'huava_cloud_member'@'%' identified by 'eFS0H6_0_pkVm__o';
grant all privileges on huava_cloud_member.* to 'huava_cloud_member'@'%';
flush privileges;

-- 订单微服务
drop database if exists huava_cloud_order;
drop user if exists 'huava_cloud_order'@'%';
create database huava_cloud_order default character set utf8mb4 collate utf8mb4_general_ci;
use huava_cloud_order;
create user 'huava_cloud_order'@'%' identified by 'eFS0H6_0_pkVm__o';
grant all privileges on huava_cloud_order.* to 'huava_cloud_order'@'%';
flush privileges;

