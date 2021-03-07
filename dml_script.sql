-- Lista antalet produkter per kategori. Listningen ska innehålla kategori-namn och antalet produkter. 
select categories.name as Kategori, count(categories.name) as Antal from categories
join shoes_in_categories on shoes_in_categories.category_id = categories.id
group by Kategori;


-- Skapa en kundlista med den totala summan pengar som varje kund har handlat för. Kundens
-- för- och efternamn, samt det totala värdet som varje person har shoppats för, skall visas.
select first_name as Förnamn, last_name as Efternamn, sum(quantity * price) as 'Totala kostnaden'from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id       
join shoes on shoes.id = order_info.shoe_id
join prices on shoes.price_id = prices.id
group by Förnamn;


-- Vilka kunder har köpt svarta sandaler i storlek 38 av märket Ecco? Lista deras namn.
select first_name as Förnamn, last_name as Efternamn, shoe_id as 'Sko Id' from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id
join shoes on shoes.id = order_info.shoe_id
join sizes 
join colors
join categories
where sizes.size = 38 and colors.color_name = 'Svart' and shoes.brand_id = 102 and categories.name = 'Sandaler'; 


-- Skriv ut en lista på det totala beställningsvärdet per ort där beställningsvärdet är större än
-- 1000 kr. Ortnamn och värde ska visas. (det måste finnas orter i databasen där det har
-- handlats för mindre än 1000 kr för att visa att frågan är korrekt formulerad)
select locations.location_name as Ort, sum(price * quantity) as 'Handlat för' from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id       
join shoes on shoes.id = order_info.shoe_id
join prices on shoes.price_id = prices.id
join locations on locations.id = customers.location_id
group by location_id
having sum(price * quantity) > 1000;


-- Skapa en topp-5 lista av de mest sålda produkterna. 
select shoes.id as 'Sko ID', shoes.shoe_name as Namn, sum(order_info.quantity) as 'Antal sålda' from shoes
join order_info on shoes.id = order_info.shoe_id
group by shoes.id 
order by sum(order_info.quantity) desc
limit 5;


-- Vilken månad hade du den största försäljningen? (det måste finnas data som anger
-- försäljning för mer än en månad i databasen för att visa att frågan är korrekt formulerad)
select monthname(order_date) as Månad, sum(quantity * price) as 'Totala kostnaden' from customers
join orders on orders.customer_id = customers.id
join order_info on order_info.order_id = orders.id       
join shoes on shoes.id = order_info.shoe_id
join prices on shoes.price_id = prices.id
group by månad
order by sum(quantity * price) desc 
limit 1;