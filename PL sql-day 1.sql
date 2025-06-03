-- create database
create database fsd_practice;
use fsd_practice;

-- create the table 
create table employee(
eid int primary key,
ename varchar(100) not null,
ebranch varchar(255),
edeparment varchar(255),
esalary double default 0
);
describe employee;

INSERT INTO EMPLOYEE (eid, ename, ebranch, edeparment, esalary) VALUES
(1, 'Amit Sharma', 'Mumbai', 'HR', 75000.00),
(2, 'Priya Patel', 'Delhi', 'IT', 82000.50),
(3, 'Rajesh Kumar', 'Bangalore', 'Finance', 90000.75),
(4, 'Neha Verma', 'Mumbai', 'HR', 78000.25),
(5, 'Vikram Singh', 'Delhi', 'IT', 85000.00),
(6, 'Anjali Gupta', 'Bangalore', 'Finance', 92000.40),
(7, 'Manish Tiwari', 'Mumbai', 'HR', 76000.90),
(8, 'Kavita Reddy', 'Chennai', 'IT', 89000.30),
(9, 'Arjun Nair', 'Bangalore', 'Finance', 94000.20),
(10, 'Sneha Iyer', 'Chennai', 'HR', 77000.10),
(11, 'Suresh Pillai', 'Mumbai', 'IT', 81000.60),
(12, 'Divya Menon', 'Delhi', 'Finance', 93000.80),
(13, 'Ravi Shankar', 'Chennai', 'HR', 74000.55),
(14, 'Pooja Deshmukh', 'Bangalore', 'IT', 86000.95),
(15, 'Karan Mehta', 'Mumbai', 'Finance', 97000.70),
(16, 'Meera Joshi', 'Delhi', 'HR', 73000.15),
(17, 'Akash Bansal', 'Chennai', 'IT', 88000.45),
(18, 'Rohan Agarwal', 'Delhi', 'Finance', 95000.60),
(19, 'Swati Saxena', 'Bangalore', 'HR', 72000.85),
(20, 'Vivek Choudhary', 'Chennai', 'Finance', 96000.90);
 
select * from employee;

/*PL SQL : Procedur Lanaguage*/

/* PROC:1 Display list of employee*/
delimiter $$;
create procedure proc_emp_list()
begin
select * from employee;
end;

drop procedure proc_emp_list;

-- call the procedure 
call proc_emp_list();

-- too see all the procedure in db
show procedure status where db='fsd_practice';


/* PROC:2 Display list of employee by branch*/
DELIMITER $$;
create procedure proc_emp_by_branch(in pbranch varchar(255))
begin
	select * from employee where ebranch=pbranch;
end;

call proc_emp_by_branch('mumbai');


/* PROC:3 Compute percentage based on total_marks,marks_scored*/
DELIMITER $$;
-- we pass 2 input and procedure gives 1 output
create procedure proc_compute_percent(in p_total_marks double,p_marks_scored double,out percent double)
begin
	if p_marks_scored > p_total_marks then
		SET percent=0;
	else
		SET percent=(p_marks_scored / p_total_marks)*100;
	end if;
end;

-- create a global/session variable to save this OUT param value of the procedure
set @percent=0;
call proc_compute_percent(500,450,@percent);
call proc_compute_percent(500,550,@percent);
select @percent as "percentage scored";


/* Sample study proc: Compute consumption bills by taking units and rate as per below calculation*/
DELIMITER &&;
create procedure proc_compute_bill(in p_unit double,out bill double)
begin
    if p_unit < 10 then
		SET p_unit=25 *8;
    elseif p_unit > 10 and p_unit <= 200 then
		SET bill=p_unit * 8;
	else
		SET bill=(200 * 8) + ((p_unit-200) * 10);
	end if;
end; 

drop procedure proc_compute_bill;

SET @bill=0;
call proc_compute_bill(100,@bill);
select @bill as "Total amount";



/*Loops: Basic loops,while loops*/

-- procs to display the loops
DELIMITER &&;
create procedure proc_basic_loop(in final_num int)
begin
	declare i int default 1;
    declare result varchar(255) default "";
    loop_lbl: -- loop label
    LOOP
		SET result=concat(result," ",i);
        SET i=i+1;
        
        If i > final_num then
			leave loop_lbl;
		END IF;
    END LOOP loop_lbl;
    select result;
end;

call proc_basic_loop(5);


/*Proc for while loop*/
DELIMITER &&;
create procedure proc_while_loop(in final_num int)
begin
	declare i int default 1;
    declare result varchar(255) default "";
    
    WHILE i <= final_num do
		SET result=concat(result," ",i);
        set i=i+1;
	END WHILE;
	select result;
end;

call proc_while_loop(5);

-- fetch all the ids from the table
DELIMITER $$;
create procedure proc_all_ids()
BEGIN
	DECLARE i int default 0;
	DECLARE total_rows int default 0;
	DECLARE ids varchar(50) default "";
	DECLARE result varchar(50) default "";
 
	-- count number of records in a table
	select count (eid) into total_rows from employee;
 
	WHILE i <= total_rows DO
		SELECT eid into ids from employee LIMIT i,1;
		set result =concat(result," ",ids);
		set i=i+1;
	end while;
    select result;
END;

call proc_all_ids();

/*3 types of parmeters
	IN
    OUT
    INOUT
*/

-- proc return salary of an employee based on id using INOUT
-- create procedure proc_salary_by_id(IN p_id int,OUT p_salary double)

DELIMITER $$;
create procedure proc_salary_by_id(INOUT myvar varchar(255))
begin
	select esalary into myvar from employee where eid=myvar;
end;
SET @x=5;
call proc_salary_by_id(@x);
select @x as "Current Salary";

/* Proc: procedure to give increments to employees based on their department. 
	take department and percentage as input. 
    update salaries accordingly. 
	
    Fetch all Ids from the table satisfying department criteria 1,4,7,9,13,17 
    Since I will be inside the loop, 
    I can call update on these IDs and increment Salary. 
*/
DELIMITER $$
create procedure proc_emp_update_sal_v2(IN p_dept varchar(255), IN p_incr_percent double)
BEGIN
	declare num_rows INT default 0; 
    declare i INT default 0; 
    declare p_id INT default 0; 
    
    select count(eid) into num_rows
    from employee 
    where edeparment = p_dept; 
    
    -- num_rows = 5
    while i<=num_rows DO  -- this loop will be executed as per total records belonging to given department 
		select eid into p_id
        from employee 
        where edeparment = p_dept
        limit i,1; 
      
        -- update this record using p_id
        update employee 
        set esalary = esalary + (esalary * (p_incr_percent/100)) 
        where eid = p_id; 
        
        SET i = i+1; 
    END WHILE; 
END; 

drop procedure proc_emp_update_sal_v2;

CALL proc_emp_update_sal_v2("IT", 35); 


/* Day-2 */

/*Triggers 
create trigger trg_employee_update

create trigger <trigger_name>
BEFORE | AFTER    INSERT | DELETE | UPDATE    ON <table_name>
FOR EACH ROw
BEGIN

END
*/

create table employee_log(
	id int primary key auto_increment,
    old_salary double,
    new_salary double,
    date_of_op date,
    username varchar(255),
    eid int
);
-- update employee set salary=5600000 where id=6; NEW.salary=5600000  OLD.salary=92000
DELIMITER $$
create TRIGGER trg_employee_update
BEFORE UPDATE ON employee  
FOR EACH ROW
BEGIN
	insert into employee_log(old_salary,new_salary,date_of_op,username,eid)
    values (OLD.esalary,NEW.esalary,now(),user(),OLd.eid);
END;
-- lets do an update op on employee and get trigger going automatically
update employee set esalary=200000 where eid=6;
select * from employee;

/*Trigger for query validation*/

-- salary should not beyond 500000 while inserting the new employee
insert into employee(eid,ename,ebranch,edeparment,esalary)
values(21,"John doe","Mumbai","Finance",600000);

DELIMITER $$
create trigger trg_employee_insert
BEFORE INSERT ON employee
FOR EACH ROW
BEGIN
	IF NEW.esalary > 500000 then
		SIGNAL SQLSTATE '45000'
        SET message_text="Error: Salary should not be beyond 50000 while inserting new employee";
	END IF;
END;

/*Create view for showing employee state based on department*/
create VIEW view_employee_department_states
AS 
select edeparment,count(eid) AS employee_count from employee group by edeparment;

select * from view_employee_department_states;


/*Create view for showing employee state based on department with analysis simulation
views are also created for customized projections*/
create VIEW view_department_states
AS 
select ebranch,count(eid) AS employee_count from employee group by ebranch;

select * from view_department_states;


/*Functions
	Proceudres cannot return a value(workaround-use OUT param),but func can and must return a value.
*/

-- function to fetch employee salary based on ids

select DELIMITER $$
create function emp_sal_func(p_id int)
returns double
deterministic
begin
	declare sal double;
    select esalary into sal from employee where eid=p_id;
    return sal;
end;
drop function emp_sal_func;
select emp_sal_func(6);

/*Procedures
	-Anonymous procedure
    -stored procedure-IMP
    3 types of PARAMS
		-If-ELSE
        -Loops:Basic,while
        -SQL Ops by iteration i.e.bulk updates
    Triggers:
		-triggers for loops and validation
	views:
		- view to safeguard info
        -view for states
	Functions:
		-Determinstic
	Work on Connected tables(rev)
    EER diagrams
*/