DELIMITER $$;

DROP PROCEDURE IF EXISTS sp_fetch_events $$

CREATE PROCEDURE sp_fetch_events (p_event_type CHAR(32), p_consumer_type CHAR(32), p_consumer_id CHAR(32), OUT p_batch_id INT)
    MODIFIES SQL DATA
BEGIN

DECLARE v_last_fetched_id INT;
DECLARE v_min_event_id INT;
DECLARE v_max_event_id INT;

SELECT last_fetched_id INTO v_last_fetched_id FROM event_dashboard 
   WHERE event_type = p_event_type AND consumer_type = p_consumer_type;

SELECT min(e.event_id),max(e.event_id) INTO v_min_event_id,v_max_event_id 
   FROM (SELECT event_id FROM event WHERE event_type = p_event_type AND event_id > v_last_fetched_id LIMIT 50) e;

IF v_max_event_id > 0 THEN
   UPDATE event_dashboard SET last_fetched_id=v_max_event_id,last_modified_date=SYSDATE()
     WHERE event_type = p_event_type AND consumer_type = p_consumer_type;

   INSERT INTO event_batch_log (event_type,consumer_type,start_event_id,end_event_id,consumer_id,creation_date,process_status)
      VALUES (p_event_type,p_consumer_type,v_min_event_id,v_max_event_id,p_consumer_id,SYSDATE(),1);

   SELECT LAST_INSERT_ID() INTO p_batch_id;

   SELECT event_id,payload,payload_type,ref_id,producer_type,producer_id,max_retry_times,schedule_date,creation_date FROM event 
      WHERE event_type = p_event_type AND event_id >= v_min_event_id AND event_id <= v_max_event_id;
END IF;

END$$

DELIMITER ;$$