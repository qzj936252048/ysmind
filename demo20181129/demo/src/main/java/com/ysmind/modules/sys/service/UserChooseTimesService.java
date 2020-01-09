package com.ysmind.modules.sys.service;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.sys.dao.UserChooseTimesDao;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.entity.UserChooseTimes;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * Service
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class UserChooseTimesService extends BaseService {

	@Autowired
	private UserChooseTimesDao userChooseTimesDao;
	
	
	public UserChooseTimes get(String id) {
		return userChooseTimesDao.get(id);
	}
	
	public List<UserChooseTimes> findAllList(String loginName,String chooseType){
		return userChooseTimesDao.findAllList(loginName,chooseType);
	}
	
	@Transactional(readOnly = false)
	public void save(UserChooseTimes userChooseTimes, HttpServletRequest request) throws Exception{
		
		String userId = request.getParameter("userId");
		if(StringUtils.isNotBlank(userId))
		{
			User u = UserUtils.getUserById(userId);
			String loginName = UserUtils.getUser().getLoginName();
			UserChooseTimes ucr = userChooseTimesDao.get(loginName+"_"+u.getLoginName());
			if(null==ucr || !userId.equals(ucr.getChooseUser().getId()))
			{
				ucr = new UserChooseTimes();
				ucr.setId(loginName+"_"+u.getLoginName());
				ucr.setCreateBy(UserUtils.getUser());
				ucr.setCreateDate(new Date());
				String addChooseTimesType = request.getParameter("addChooseTimesType");
				ucr.setChooseType(addChooseTimesType);
				ucr.setChooseTimes(1);
			}
			else
			{
				int times = ucr.getChooseTimes();
				ucr.setChooseTimes(times+1);
			}
			ucr.setChooseUser(u);
			userChooseTimesDao.save(ucr);
			userChooseTimesDao.flush();
		}
	}

}
