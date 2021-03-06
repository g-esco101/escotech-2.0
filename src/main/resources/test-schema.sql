create table images (id bigint generated by default as identity, img1600 binary(255), img300 binary(255), img600 binary(255), name varchar(255), scaled100 binary(255), scaled1600 binary(255), scaled600 binary(255), primary key (id));
create table order_lines (id bigint generated by default as identity, brand varchar(50), category varchar(5), condition varchar(5), model varchar(50), product_id bigint, quantity integer not null, serial_number varchar(50), unit_cost decimal(19,2) not null, unit_shipping decimal(19,2) not null, primary key (id));
create table orders (id bigint generated by default as identity, address1 varchar(255), address2 varchar(255), city varchar(255), country integer, date date not null, email varchar(50), first_name varchar(50), last_name varchar(50), paypal_id varchar(255), paypal_status varchar(255), phone varchar(255), sale_method varchar(3), shipping_method varchar(6) not null, state varchar(255), tax decimal(19,2) not null, time time not null, zip varchar(255), primary key (id));
create table orders_order_lines (order_id bigint not null, order_lines_id bigint not null);
create table products (id bigint generated by default as identity, brand varchar(50), category varchar(5), condition varchar(5), description varchar(1500), inventory_count integer not null, model varchar(50), serial_number varchar(50), unit_cost decimal(19,2) not null, unit_shipping decimal(19,2) not null, primary key (id));
create table products_images (product_id bigint not null, images_id bigint not null);
alter table orders_order_lines drop constraint if exists ool_constraint;
alter table orders_order_lines add constraint ool_constraint unique (order_lines_id);
alter table products_images drop constraint if exists pi_constraint;
alter table products_images add constraint pi_constraint unique (images_id);
alter table orders_order_lines add constraint ool_constraint1 foreign key (order_lines_id) references order_lines;
alter table orders_order_lines add constraint ool_constraint2 foreign key (order_id) references orders;
alter table products_images add constraint pi_constraint1 foreign key (images_id) references images;
alter table products_images add constraint pi_constraint2 foreign key (product_id) references products;