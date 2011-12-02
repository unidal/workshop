// check if there are transactions where amount_paid was changed
select distinct t1.id,t1.order_id,t1.amount_paid,t1.adjustment_amount from transaction t1, transaction t2 where t1.order_id=t2.order_id and (t1.amount_paid+t1.adjustment_amount<>t2.amount_paid+t2.adjustment_amount) and t1.order_id>0;

// update it if have

// prepare orders table
insert into orders select distinct order_id,amount_paid,adjustment_amount,'',0,now(),now() from transaction where order_id >0;

// prepare item table
insert item select distinct item_id,item_title,0,now(),now() from transaction

// check if there are transactions where buyer name changed
select t1.buyer_account,t1.buyer_name from transaction t1,transaction t2 where t1.buyer_account=t2.buyer_account and t1.buyer_name<>t2.buyer_name

// update it if have

// prepare buyer table
insert into buyer select distinct buyer_account,buyer_name,buyer_email,null,now(),now() from transaction

// prepare shipping_address table
insert into shipping_address select distinct payment_id,buyer_account,buyer_name,street1,street2,city_name,state_Or_Province,postal_code,phone,null,country_name,now(),now() from transaction where payment_id is not null
