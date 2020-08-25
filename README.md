# lovelystudy
lovely study, an sprint boot 2.0 base bbs system


## init db

CREATE USER 'sqluser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'sqluser'@'localhost';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS LovelyStudy DEFAULT CHARSET utf8 COLLATE utf8_general_ci; 

## fix elastic error 
https://discuss.elastic.co/t/elasticsearch-5-4-1-availableprocessors-is-already-set/88036/8


sudo usermod -a -G group username
