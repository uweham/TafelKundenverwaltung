#remove on update clause
#when update with mariadb client the timestamp storniertAm was faulty set

alter table einkauf MODIFY COLUMN storniertAm  TIMESTAMP DEFAULT null