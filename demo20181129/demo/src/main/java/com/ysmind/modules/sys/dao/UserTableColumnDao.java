package com.ysmind.modules.sys.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.UserTableColumn;

/**
 * DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class UserTableColumnDao extends BaseDao<UserTableColumn> {

	public List<UserTableColumn> findAllList(){
		return find("from UserTableColumn where delFlag=:p1 order by sort", new Parameter(UserTableColumn.DEL_FLAG_NORMAL));
	}
	
	public List<UserTableColumn> findListByType(String tableName,String type,String userId){
		if(null==userId)
		{
			return find("from UserTableColumn where delFlag=:p1 and tableName=:p2 and type=:p3 and user.id='1' order by sort", new Parameter(UserTableColumn.DEL_FLAG_NORMAL,tableName,type));
		}
		return find("from UserTableColumn where delFlag=:p1 and tableName=:p2 and type=:p3 and user.id=:p4 order by sort", new Parameter(UserTableColumn.DEL_FLAG_NORMAL,tableName,type,userId));
	}

}
