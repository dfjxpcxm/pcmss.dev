
DROP TRIGGER IF EXISTS `tri_insert_bi_warn_info`;
DELIMITER ;;
CREATE TRIGGER `tri_insert_res_info` AFTER INSERT ON `bi_warning_log` FOR EACH ROW begin
    INSERT INTO portaldb.res_info( resource_id, resource_name,apply_user,resource_status,
    oper_desc,check_status,resource_type,cre_time,upd_time)
		VALUES (new.mea_id, new.mea_value,'admin',1,new.log_detail,'0',2,
		new.log_time,new.log_time);
end
;;
DELIMITER ;