create table type_product (id bigint not null AUTO_INCREMENT, name varchar(255), primary key (id));
create table product (id bigint not null AUTO_INCREMENT, description clob, image clob, name varchar(255), price double, product_id bigint,FOREIGN KEY (product_id)  REFERENCES type_product (id), primary key (id));
