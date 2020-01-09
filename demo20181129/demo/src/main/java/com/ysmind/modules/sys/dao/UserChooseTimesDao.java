package com.ysmind.modules.sys.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.UserChooseTimes;

/**
 * DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class UserChooseTimesDao extends BaseDao<UserChooseTimes> {

	public List<UserChooseTimes> findAllList(String loginName,String chooseType){
		return find("from UserChooseTimes where delFlag=:p1 and chooseType=:p2 and id like :p3 order by chooseTimes desc ", new Parameter(UserChooseTimes.DEL_FLAG_NORMAL,chooseType,loginName+"_%"));
	}
}
