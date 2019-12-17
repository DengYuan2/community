#Spring Boot实战练习   
##学习网站：   
[哔哩哔哩](https://www.bilibili.com/video/av65117012?p=1)   
##资料   
[Spring文档](https://spring.io/guides)   
[Bootstrap](https://v3.bootcss.com/getting-started/#download)   
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)   
[MySQL](https://www.runoob.com/mysql/mysql-tutorial.html)  
[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html) 
##工具   
lombok
postman   
##脚本
```sql
create table USER
(
    ID           int auto_increment primary key not null ,
    NAME         VARCHAR(50),
    ACCOUNT_ID   VARCHAR(100),
    TOKEN        VARCHAR(50),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
);
```
```sql
ALTER TABLE USER add bio VARCHAR(256) NULL;
```
对表的改动：
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
####

