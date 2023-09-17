## Script for deleting the database and retaining the schema
use ticketmaster;

delimiter $$
create procedure delete_all_db_info()
BEGIN
    declare total_table_count INT;
    declare cnt INT default 0;
    declare tablename varchar(1000);
    
	set foreign_key_checks = 0;
	set total_table_count = (select count(table_name) from information_schema.tables where table_schema = 'ticketmaster');
    while cnt < total_table_count DO
		set tablename = (select table_name from information_schema.tables where table_schema = 'ticketmaster' limit 1 offset cnt);
        set @truncate_stmt = concat("truncate table ticketmaster." , tablename); 
		prepare stmt from @truncate_stmt;
        execute stmt;
        deallocate prepare stmt;
        set cnt = cnt + 1;
	end while ;
    set foreign_key_checks = 1;
END $$
delimiter ;

call delete_all_db_info();