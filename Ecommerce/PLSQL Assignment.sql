create database lib_db;
use lib_db;

create table book(
	id int primary key auto_increment,
    title varchar(255) not null,
    price double not null,
    author varchar(255),
    publication_house enum('Mcgraw Hill','DreamFolks','Warner Bros'),
    category enum('Fiction','War','Comedy','Sports'),
    book_count int default 1,
    status enum('In_Stock','Out_of_Stock')default 'In_Stock'
);

insert into book(title,price,author,publication_house,category,book_count,status) values
	('Book1',250.00,'Author X','Mcgraw Hill','Fiction',5,'In_Stock'),
    ('Book2',350.00,'Author Y','DreamFolks','War',3,'In_Stock'),
    ('Book3',500.00,'Author Z','Warner Bros','Comedy',7,'In_Stock');
    
select * from book;


-- fetch all in stock books under given price
DELIMITER $$
create procedure get_in_stock(in max_price double)
begin
	select * from book where status='In_Stock' and price < max_price;
end;
call get_in_stock(500);


-- delete books from a given publication house
set sql_safe_updates=0;
DELIMITER $$
create procedure delete_by_publication(in pub_name varchar(255))
begin
	delete from book where publication_house=pub_name;
end;
call delete_by_publication('Mcgraw Hill');


-- update price of books by percentage for a given category
DELIMITER $$
create procedure update_price_category(in cat varchar(255),in percent double)
begin
	update book 
    set price= price + (price * percent / 100)
    where category=cat;
end;

call update_price_category('War',25);
select * from book;
