insert into type_product (id,name) values (1,'candy');
insert into type_product (id,name) values (2,'stick of chocolate');
insert into product (description,image, name, price, product_id,id) values ('description','truf.jpg',  'Truffle candy', 5.4, 1,1);
insert into product (description,image, name, price, product_id,id) values ('description','stick_choc.jpg', 'Walnut', 12.8, 2,2);
insert into product (description,image, name, price, product_id,id) values ('description','jeele.jpg','Sweet cherry', 3.5, 1,3);
insert into users (user_id,username,password,role,enabled) values (1,'admin','$2a$10$9nGf1oiYesdzWl7RhDx6NOzi98k1exBSFhFK0.YDpIaj/Ko7GDShi','ROLE_ADMIN',1);
insert into users (user_id,username,password,role,enabled) values (2,'user','$2a$10$IfkZqki7OIxgvSpsI5LGPeHaCFC7bRpWu87r61QaZjS5HwU.ByNZi','ROLE_USER',1);