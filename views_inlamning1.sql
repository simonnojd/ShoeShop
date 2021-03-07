create view bästa_månad as
select monthname(order_date) as Månad, sum(quantity * price) as 'Totala kostnaden' from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id       
join shoes on shoes.id = order_info.shoe_id
join prices on shoes.price_id = prices.id
group by månad
order by sum(quantity * price) desc 
limit 1;

create view kundlista_och_pengar as
select first_name as Förnamn, last_name as Efternamn, sum(quantity * price) as 'Totala kostnaden'from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id       
join shoes on shoes.id = order_info.shoe_id
join prices on shoes.price_id = prices.id
group by Förnamn;

create view mest_sålda_produkter as
select shoes.id as 'Sko ID', shoes.shoe_name as Namn, sum(order_info.quantity) as 'Antal sålda' from shoes
join order_info on shoes.id = order_info.shoe_id
group by shoes.id 
order by sum(order_info.quantity) desc
limit 5;

create view produkter_per_kategori as
select categories.name as Kategori, count(categories.name) as Antal from categories
join shoes_in_categories on shoes_in_categories.category_id = categories.id
group by Kategori;

create view svart_sandal_38_ecco as 
select first_name as Förnamn, last_name as Efternamn, shoe_id as 'Sko Id' from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id
join shoes on shoes.id = order_info.shoe_id
join sizes 
join colors
join categories
where sizes.size = 38 and colors.color_name = 'Svart' and shoes.brand_id = 102 and categories.name = 'Sandaler'; 

create view totalbeställning_ort as 
select locations.location_name as Ort, sum(price * quantity) as 'Handlat för' from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id       
join shoes on shoes.id = order_info.shoe_id
join prices on shoes.price_id = prices.id
join locations on locations.id = customers.location_id
group by location_id
having sum(price * quantity) > 1000;