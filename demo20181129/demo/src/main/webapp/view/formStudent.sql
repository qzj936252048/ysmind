CREATE OR REPLACE ALGORITHM=MERGE VIEW form_student_view AS
select  
  `id` AS 'id', -- '编号',
  name as 'name',
  age as 'age',
  create_date AS 'createDate',
  create_by AS 'createById',
  get_user_name(create_by) AS 'createByName',
  update_by AS 'updateById',
  update_date AS 'updateDate',
  get_user_name(update_by) AS 'updateByName',
  `remarks` AS 'remarks', --  '备注信息',
  `del_flag` AS 'delFlag' --  '删除标记'
 from `form_student` where del_flag=0
 
 
 
 
 