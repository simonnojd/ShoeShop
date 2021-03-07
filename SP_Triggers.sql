DELIMITER $$
drop trigger if exists OOS_trigger;
CREATE TRIGGER OOS_trigger
        AFTER UPDATE ON stock
        FOR EACH ROW
    BEGIN
        IF (NEW.quantity < 1) THEN 
            INSERT INTO out_of_stock(shoe_id, oos_date) VALUES (OLD.shoe_id, CURRENT_DATE());
        END IF;

    END $$
DELIMITER ;

DELIMITER $$
drop procedure if exists add_to_cart;
CREATE PROCEDURE add_to_cart(IN _customers_id INT, INOUT _orders_id INT, IN _shoes_id INT, IN _quantity INT)
BEGIN

DECLARE lastID int default 0;
DECLARE shoeID int default 0;
DECLARE orderExists int default 0;
DECLARE shoeExists int default 0;


DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
	ROLLBACK;
	RESIGNAL SET MESSAGE_TEXT = 'Något gick fel';
END;

SELECT count(*) FROM orders WHERE orders.id = _orders_id INTO orderExists;
	
    IF (orderExists = 0) THEN
		SET autocommit = 0;
			START TRANSACTION;
				INSERT INTO orders (customer_id) VALUES (_customers_id);
                SELECT LAST_INSERT_ID() INTO _orders_id;
		
			COMMIT;
			SET autocommit = 1;
            
         ELSE
	
            SELECT Count(*) FROM order_info WHERE order_info.shoe_id = _shoes_id AND order_info.order_id = _orders_id INTO shoeExists;
            END IF;
            
            
		IF (shoeExists = 0) THEN
			INSERT INTO order_info(order_id, quantity, shoe_id, order_date) VALUES (_orders_id, _quantity, _shoes_id, CURRENT_DATE()); 
	
		ELSE
            UPDATE order_info
			SET order_info.quantity = order_info.quantity + _quantity
			WHERE order_info.shoe_id = _shoes_id AND order_info.order_id = _orders_id;
        END IF;
	
    UPDATE stock
    SET stock.quantity = stock.quantity - _quantity
    WHERE stock.shoe_id = _shoes_id;
END$$
DELIMITER ;

DELIMITER $$
drop function if exists average_rating;
CREATE FUNCTION average_rating(_shoeID INT) 
RETURNS INTEGER
READS SQL DATA
BEGIN 
	DECLARE avrg_rating int default 0;
    SELECT avg(reviews.number_rating) 
    FROM reviews 
    WHERE reviews.shoe_id = _shoeID 
    INTO avrg_rating;
    RETURN avrg_rating;
END $$
DELIMITER ;

drop view if exists average_number_rating;
CREATE VIEW average_number_rating AS
SELECT shoes.id as ID, (SELECT average_rating(shoes.id)) as 'Average Rating', reviews.rating
FROM shoes
LEFT JOIN reviews on reviews.id = (SELECT average_rating(shoes.id)) 
ORDER BY shoe_name;


DELIMITER $$
drop procedure if exists rate;
CREATE PROCEDURE rate(_customerID INT, _shoeID INT, _nmbr_grade INT, _enumRating INT, _comment varchar(50))
BEGIN
INSERT INTO reviews(customer_id, shoe_id, number_rating, rating, comment) VALUES (_customerID, _shoeID, _nmbr_grade, _enumRating, _comment);
END $$
DELIMITER ;

  -- SET @testorderid = 0;

SELECT * FROM orders;
SELECT * FROM order_info;
SELECT * FROM stock;
SELECT * FROM out_of_stock;
SELECT * FROM reviews;
SELECT * FROM shoe_has_color;