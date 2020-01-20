# 新闻资讯分享

## 项目介绍
这是一个新闻资讯分享项目，每位用户都可以通过发表新闻资讯。用户可以对资讯进行赞踩、评论。还具有站内信功能，用户之间可以进行通信。

## 技术选型
后端框架使用SpringBoot+Mybatis为基础，数据库方面采用Mysql存储关系强，需要事务支持的表。使用redis作为缓存数据库，并用其完成消息列
表、点赞功能的设计。使用OSS存储头像图片。使用javax.mail实现邮件发送服务。


## 头条新闻数据库设计

###  数据库字段设计

| 用户(id)         | 站内信（Message）        | 资讯（News）              | 评论（Comment）          | 登陆              |
| ---------------- | ------------------------ | ------------------------- | ------------------------ | ----------------- |
| id(主键)         | id                       | id                        | id                       | id                |
| name             | fromid（来自）           | title（标题）             | content（内容）          | user_id           |
| password         | toid（发向人）           | link（超链接）            | user_id（评论人）        | ticket            |
| salt             | content（内容）          | image（首图）             | created_date（评论时间） | expired(过期时间) |
| head_url（头像） | conversation_id          | like_count（赞评数量）    | news_id（属于资讯）      | status(状态)      |
|                  | created_date（发送时间） | comment_count（评论数量） |                          |                   |
|                  |                          | user_id（某个人发的）     |                          |                   |
|                  |                          | created_date（发送时间）  |                          |                   |



### 创建表结构
#### user

```sql
CREATE TABLE `user` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `name` varchar(64) DEFAULT NULL,
   `password` varchar(128) DEFAULT NULL,
   `salt` varchar(32) DEFAULT NULL,
   `head_url` varchar(256) DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `name_UNIQUE` (`name`)
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### news

```sql
CREATE TABLE `news` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `title` varchar(128) DEFAULT NULL,
   `link` varchar(256) DEFAULT NULL,
   `image` varchar(256) DEFAULT NULL,
   `like_count` int(11) DEFAULT NULL,
   `comment_count` int(11) DEFAULT NULL,
   `created_date` datetime DEFAULT NULL,
   `user_id` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### ticket

```sql
CREATE TABLE `login_ticket` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `user_id` varchar(45) DEFAULT NULL,
   `ticket` varchar(256) DEFAULT NULL,
   `expired` datetime DEFAULT NULL,
   `status` int(11) DEFAULT '0',
   PRIMARY KEY (`id`),
   UNIQUE KEY `ticket_UNIQUE` (`ticket`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```



备注： 文档并不详细，后期会进行补充，这只是其中的一部分。