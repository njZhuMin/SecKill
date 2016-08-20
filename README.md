# SecKill

基于SSM的高并发秒杀系统API实现

实现笔记：
http://sunnywr.com/blog/categories/project/SecKill/

## 技术选型
- 日志系统：slf4j，logback
- 数据库：MySQL，Druid
- DAO：MyBatis
- Servlet相关：JSTL，Jackson
- Spring：core，beans，context，jdbc，tx
- Spring Web：web，webmvc
- 测试组件：JUnit，spring-test
- Google Guava

## 具体实现
系统具体实现哪些功能：<br/>
1. 秒杀接口暴露<br/>
2. 执行秒杀<br/>
3. 相关查询功能<br/>

计划开发阶段：<br/>
1. 数据库设计<br/>
2. DAO层设计开发<br/>
3. Service层设计开发<br/>
4. Web前端设计开发<br/>
