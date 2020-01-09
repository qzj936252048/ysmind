package com.ysmind.modules.form.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.IpUtil;
import com.ysmind.common.utils.SessionInfo;
import com.ysmind.modules.form.dao.EmailOperationDao;
import com.ysmind.modules.sys.dao.UserDao;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class EmailOperationService extends BaseService{
	
	@Autowired  
	private  HttpServletRequest request;
	
	@Autowired
	private EmailOperationDao emailOperationDao;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 新增、删除MAILCENTER_S表
	 * @param sql
	 * @param obj
	 */
	@Transactional(readOnly = false)
	public void updateTo_MAILCENTER_S(String sql,Object[] obj){
		emailOperationDao.updateTo_MAILCENTER_S(sql,obj);
	}
	
	public void printRequest(String userId){
		//System.out.println("--------------"+request.getSession());
		User userDb = userDao.get(userId);
		//验证密码是否正确
		SessionInfo sessionInfo = new SessionInfo();
		BeanUtils.copyProperties(userDb, sessionInfo);
		sessionInfo.setIp(IpUtil.getIpAddr(request));
		request.getSession().setAttribute(Global.getSessionInfoName(), sessionInfo);
		sessionInfo.setResourceList(UserUtils.getMenuPermissionByUserId());
	}
	
	/**
	 * 新增、删除MAILCENTER_SD表
	 * @param sql
	 * @param obj
	 */
	@Transactional(readOnly = false)
	public void updateTo_MAILCENTER_SD(String sql,Object[] obj){
		emailOperationDao.updateTo_MAILCENTER_SD(sql,obj);
	}
	
	/**
	 * 新增、删除MAILCENTER_R表
	 * @param sql
	 * @param obj
	 */
	@Transactional(readOnly = false)
	public void updateTo_MAILCENTER_R(String sql,Object[] obj){
		emailOperationDao.updateTo_MAILCENTER_R(sql,obj);
	}
	
	/**
	 * 新增、删除MAILCENTER_R表
	 * @param sql
	 * @param obj
	 */
	@Transactional(readOnly = false)
	public void updateTo_MAILCENTER_RD(String sql,Object[] obj){
		emailOperationDao.updateTo_MAILCENTER_RD(sql,obj);
	}
	
	/**
	 * 定时任务扫描审批用户恢复的信息
	 * @param sql
	 * @param obj
	 */
	public List<Map<String, Object>> getAllFrom_MAILCENTER_R(String sql,Object[] obj){
		return emailOperationDao.getAllFrom_MAILCENTER_R(sql, obj);
	}
	
}
