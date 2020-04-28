# lovelystudy
lovely study, an sprint boot 2.0 base bbs system


## init db

CREATE USER 'sqluser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'sqluser'@'localhost';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS LovelyStudy DEFAULT CHARSET utf8 COLLATE utf8_general_ci; 

## fix elastic error 
https://discuss.elastic.co/t/elasticsearch-5-4-1-availableprocessors-is-already-set/88036/8



##开始准备代码生成
https://www.google.com/search?q=mybatis+代码生成器+github&oq=mybatis+代码生成器+github&aqs=chrome..69i57.8137j0j7&client=ubuntu&sourceid=chrome&ie=UTF-8
https://github.com/newpanjing/mybatis-generator/blob/master/src/main/java/com/qikenet/build/BuildHelper.java
https://github.com/aoeai/mybatis-mysql-generator/blob/master/src/main/java/com/aoeai/tools/mybatis/service/ServiceFileService.java
https://github.com/zouzg/mybatis-generator-gui
https://github.com/wu6660563/MybatisGenerator
https://gitee.com/free/Mybatis_Utils/blob/master/MybatisGeneator/MybatisGeneator.md
https://www.javacodegeeks.com/2012/11/mybatis-tutorial-crud-operations-and-mapping-relationships-part-1.html
https://my.oschina.net/scoder/blog/708391
MyBatis关联查询，查询主表列表数据时同时返回子表数据，只有一次查询 
https://blog.csdn.net/jiadajing267/article/details/79304602
https://blog.csdn.net/weixin_39142315/article/details/81488685
http://blog.chenqi.im/?p=663
