drop database inlamning1;
create database inlamning1;
use inlamning1; 
set sql_safe_updates = 0;

create table brands
(id int not null auto_increment,
name varchar(20) not null,
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id));
	
create table prices
(id int not null auto_increment,
price double not null,
primary key (id));

create table sizes
(id int not null auto_increment,
size double not null,
primary key (id));

create table colors
(id int not null auto_increment,
color_name varchar(30) not null,
primary key (id));

create table shoes
(id int not null auto_increment,
price_id int,
size_id int,
brand_id int not null,
shoe_name varchar(50) not null,
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key(brand_id) references brands(id) on delete cascade on update cascade, -- Om ID uppdateras eller tas bort kommer det att ändras här också
foreign key(size_id) references sizes(id) on delete set null on update cascade,   -- Om ett pris eller en storlek försvinner så ska inte skon och om det uppdateras så sker uppdateringen här med
foreign key(price_id) references prices(id) on delete set null on update cascade);

create table shoe_has_color
(id int not null auto_increment,
shoe_id int not null,
color_id int not null,
primary key (id), 
foreign key (shoe_id) references shoes(id) on delete cascade on update cascade,-- Om ID uppdateras kommer det att ändras här med, om skon tas bort så tas den även bort från denna tabell
foreign key (color_id) references colors(id) on delete cascade on update cascade); 

create table categories
(id int not null auto_increment,
name varchar(30) not null,
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key (id));

create table shoes_in_categories
(id int not null auto_increment,
shoe_id int not null,
category_id int not null,
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key(shoe_id) references shoes(id) on delete cascade on update cascade, -- Om ID uppdateras kommer det att ändras här med, om skon tas bort så tas den även bort från denna tabell
foreign key(category_id) references categories(id) on delete cascade on update cascade);

create table locations
(id int not null auto_increment,
location_name varchar(50),
primary key(id));

create table customers
(id int not null auto_increment,
first_name varchar(20) not null,
last_name varchar(20) not null,
location_id int not null,
password varchar(50) not null,
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key(location_id) references locations(id) on delete cascade on update cascade); -- Om ortens ID uppdateras kommer det att ändras här med, om orten tas bort från databasen så tas den även bort från denna tabell

create table reviews
(id int not null auto_increment,
customer_id int,
shoe_id int not null,
number_rating int,
rating enum ('MYCKET NÖJD', 'GANSKA NÖJD', 'NÖJD','MISSNÖJD'),
comment varchar(100),
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key(customer_id) references customers(id) on delete set null on update cascade, -- Om en kund försvinner ur systemet så sätts detta till null så att reviewn fortfarande är kvar.
foreign key(shoe_id) references shoes(id) on delete cascade on update cascade); -- Ifall en sko försvinner ur systemet så försvinner den här med och om ID uppdateras kommer det att ändras här med

create table orders
(id int not null auto_increment,
customer_id int,
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key(customer_id) references customers(id) on delete set null);

create table order_info
(id int not null auto_increment,
order_id int not null,
quantity int not null,
shoe_id int,
order_date date not null, 
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key (order_id) references orders(id) on delete cascade on update cascade,
foreign key (shoe_id) references shoes(id) on delete set null on update cascade);

-- Inlämning 2
create table out_of_stock
(id int not null auto_increment,
shoe_id int not null,
oos_date date not null, 
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key (shoe_id) references shoes(id) on delete cascade on update cascade);
-- Inlämning 2

create table stock
(id int not null auto_increment,
shoe_id int not null,
quantity int not null, 
created timestamp default current_timestamp,
lastUpdated timestamp default current_timestamp,
primary key(id),
foreign key (shoe_id) references shoes(id) on delete cascade on update cascade);
-- Inlämning 2




insert into locations(id, location_name) values (50, 'Stockholm');
insert into locations(location_name) values ('Göteborg');
insert into locations(location_name) values ('Luleå');

insert into customers(first_name, last_name, location_id, password) values ('Liliana', 'Pitra', 50, '123');
insert into customers(first_name, last_name, location_id, password) values ('Simon', 'Nöjd', 50, 'hejhej');
insert into customers(first_name, last_name, location_id, password) values ('Valle', 'Rudhag', 51, '567');
insert into customers(first_name, last_name, location_id, password) values ('Obi', 'One', 51, '0123');
insert into customers(first_name, last_name, location_id, password) values ('Sara', 'Svensson', 52, 'katt');

insert into brands(name, id) values ('Nike', 100);
insert into brands(name) values ('Adidas');
insert into brands(name) values ('Ecco');
insert into brands(name) values ('Moonboot');
insert into brands(name) values ('Fila');
insert into brands(name) values ('Gucci');

insert into prices(id, price) values (2000, 350);
insert into prices(price) values (700);
insert into prices(price) values (500);
insert into prices(price) values (1000);
insert into prices(price) values (1500);
insert into prices(price) values (300);
insert into prices(price) values (150);
insert into prices(price) values (550);
insert into prices(price) values (750);

insert into sizes(id, size) values (3000, 30);
insert into sizes(size) values (38);
insert into sizes(size) values (42);
insert into sizes(size) values (36);
insert into sizes(size) values (39);
insert into sizes(size) values (37);

insert into colors(id, color_name) values (4000, 'Blå');
insert into colors(color_name) values ('Svart');
insert into colors(color_name) values ('Vit');
insert into colors(color_name) values ('Rosa');
insert into colors(color_name) values ('Röd');
insert into colors(color_name) values ('Grön');
insert into colors(color_name) values ('Lila');

insert into shoes(shoe_name, price_id, size_id, brand_id, id) values ('Flax', 2000, 3000, 104, 300);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Fot fetish', 2000, 3000, 104);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Sandalari', 2001, 3001, 102); -- SVART 38 EKKO
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Fin sko', 2002, 3002, 101);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Bekväm men ful', 2003, 3003, 100);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('En sko', 2004, 3000, 103);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Spring Snabbt', 2005, 3004, 102);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Obekväm Men Snygg', 2006, 3005, 104);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Jävligt ful', 2007, 3005, 101);
insert into shoes(shoe_name, price_id, size_id, brand_id) values ('Sko', 2008, 3005, 103);

insert into shoe_has_color(id, shoe_id, color_id) values (5000, 300, 4000);
insert into shoe_has_color(shoe_id, color_id) values (301, 4000);
insert into shoe_has_color(shoe_id, color_id) values (302, 4001);
insert into shoe_has_color(shoe_id, color_id) values (303, 4001);
insert into shoe_has_color(shoe_id, color_id) values (304, 4002);
insert into shoe_has_color(shoe_id, color_id) values (305, 4003);
insert into shoe_has_color(shoe_id, color_id) values (306, 4002);
insert into shoe_has_color(shoe_id, color_id) values (307, 4004);
insert into shoe_has_color(shoe_id, color_id) values (308, 4005);
insert into shoe_has_color(shoe_id, color_id) values (309, 4006);

insert into categories(name, id) values ('Sport', 500);
insert into categories(name) values ('Sneakers');
insert into categories(name) values ('Sandaler');
insert into categories(name) values ('Vinter');
insert into categories(name) values ('Terräng');

insert into shoes_in_categories(shoe_id, category_id) values (300, 500);
insert into shoes_in_categories(shoe_id, category_id) values (300, 501);
insert into shoes_in_categories(shoe_id, category_id) values (301, 502);
insert into shoes_in_categories(shoe_id, category_id) values (302, 502);
insert into shoes_in_categories(shoe_id, category_id) values (303, 503);
insert into shoes_in_categories(shoe_id, category_id) values (303, 504);
insert into shoes_in_categories(shoe_id, category_id) values (304, 501);
insert into shoes_in_categories(shoe_id, category_id) values (305, 502);
insert into shoes_in_categories(shoe_id, category_id) values (306, 503);
insert into shoes_in_categories(shoe_id, category_id) values (307, 500);
insert into shoes_in_categories(shoe_id, category_id) values (308, 503);
insert into shoes_in_categories(shoe_id, category_id) values (308, 504);
insert into shoes_in_categories(shoe_id, category_id) values (309, 504);

insert into orders(id, customer_id) values (1000, 1);
insert into orders(customer_id) values (2);
insert into orders(customer_id) values (3);
insert into orders(customer_id) values (4);
insert into orders(customer_id) values (5);
insert into orders(customer_id) values (1);

insert into order_info(order_id, quantity, shoe_id, order_date, id) values (1000, 3, 300, '2020-06-28', 20);
insert into order_info(order_id, quantity, shoe_id, order_date) values (1001, 5, 302, '2019-03-21');
insert into order_info(order_id, quantity, shoe_id, order_date) values (1002, 2, 301, '2021-01-16');
insert into order_info(order_id, quantity, shoe_id, order_date) values (1002, 3, 307, '2021-01-16');
insert into order_info(order_id, quantity, shoe_id, order_date) values (1003, 1, 302, '2019-03-05');
insert into order_info(order_id, quantity, shoe_id, order_date) values (1004, 1, 307, '2019-03-17');
insert into order_info(order_id, quantity, shoe_id, order_date) values (1005, 3, 309, '2020-02-19');

insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (1, 300, 4, 4, 'very good');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (1, 300, 2, 2, 'very good');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (2, 302, 3, 3, 'nice');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (3, 301, 1, 1, 'Do not like');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (3, 307, 4, 4, 'Comfortable');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (3, 307, 3, 3, 'Comfortable');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (3, 307, 3, 3, 'Comfortable');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (3, 308, 2, 2, 'Comfortable');
insert into reviews(customer_id, shoe_id, number_rating, rating, comment) values (3, 309, 4, 4, 'Comfortable');



insert into stock(id, shoe_id, quantity) values (800, 300, 50);
insert into stock(shoe_id, quantity) values (301, 50);
insert into stock(shoe_id, quantity) values (302, 50);
insert into stock(shoe_id, quantity) values (303, 50);
insert into stock(shoe_id, quantity) values (304, 50);
insert into stock(shoe_id, quantity) values (305, 50);
insert into stock(shoe_id, quantity) values (306, 50);
insert into stock(shoe_id, quantity) values (307, 50);
insert into stock(shoe_id, quantity) values (308, 50);
insert into stock(shoe_id, quantity) values (309, 50);


-- Vi indexerar sko namn och namn på märket för att det är förmodligen det datat som kommer att sökas igenom mest.
-- Vi tänker utifrån oss själva när vi är inne på en webbutik och letar efter nya skor så brukar man oftast söka på märken och/eller namn på skon
create index IX_brand_name on brands(name);
create index IX_shoe_name on shoes(shoe_name);


select * from customers;
select * from brands;
select * from shoes;
select * from orders;
select * from order_info;
select * from shoes_in_categories;
select * from categories;
select * from reviews;
select * from stock;

show indexes from brands;
show indexes from shoes;