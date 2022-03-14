-- CREATE SCHEMA IF NOT EXISTS SCHEMA1;
CREATE SCHEMA IF NOT EXISTS customers;
-- SET SCHEMA SCHEMA1 ;
-- SET SCHEMA customers ;

-- DROP ALL OBJECTS;

-- DROP TABLE IF EXISTS customer ;
-- DROP TABLE IF EXISTS country ;

CREATE TABLE customers.customer (
	code VARCHAR(5),
	country_code VARCHAR(2) NOT NULL,
	first_name VARCHAR(40) DEFAULT ' a zer' COMMENT 'my comment',
	last_name VARCHAR(40),
	score DECIMAL(  5, 2 ) ,
	login VARCHAR(20) NOT NULL,
	password VARCHAR(20),
	age INTEGER,
	city VARCHAR(45),
	zip_code INTEGER,
	phone VARCHAR(20),
	reviewer SMALLINT,
	PRIMARY KEY(code)
);

CREATE TABLE customers.country ( 
	code VARCHAR(2),
	name VARCHAR(45),
	PRIMARY KEY(code)
);
