package com.ysmind.modules.sys.entity;

import java.util.Date;
import javax.persistence.Entity;  
import javax.persistence.Table;  
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.model.QuartzEntityModel;

@Entity
@Table(name = "sys_quartz_entity")
@DynamicInsert @DynamicUpdate
public class QuartzEntity extends IdEntity<QuartzEntity>  {
    private static final long serialVersionUID = 1L;
    private String title;  // 标题    
    private String triggerName;  // 设置trigger名称    
    private String cronExpression;  //设置表达式 
    private String jobDetailName;  //设置Job名称  
    private String targetObject;  //任务类名 
    private String methodName;  //类名对应的方法名  
    private String conCurrent;  //设置是否并发启动任务 0是false 非0是true  
    private String state;   // 如果计划任务不存则为1 存在则为0 ，1为可用，0为不可用
    private String isSpringBean;  //是否是已经存在的springBean 1是  0 否
    private String jobStatus;//有没有从任务中去除。当把一个任务取消的时候，避免每次都判断，每次实际从任务取消的时候修改状态
    private Date startTime;//如果设了有效期，有效期的开始时间
    private Date endTime;//如果设了有效期，有效期的结束时间
    
    private String serialNumber;//编号
    private String jobType;//任务类型：每天、每周、每个月
    private String runTime;//执行时间，与jobType共同定义表达式
    private String emailList;//邮件通知列表
    
    private String jobTypePerHour;//每隔几小时
    private String jobTypeWeek;//当任务类型为每周的时候选中的星期
    private String jobTypeMonth;//当任务类型为每月的时候选中的日
    private String jobTypePointDay;//当任务类型为制定日期的时候选中的日期
    
    private String relativeTables;//同步的表
    private String conditionDesc;//表达式的中文
    
    
    
    public static final String state_active = "1";
    public static final String state_unactive = "0";
    
    //等待/进行/完成/错误
    public static final String JOBSTATUS_WAIT = "wait";
    public static final String JOBSTATUS_RUNNING = "running";
    public static final String JOBSTATUS_FINISHED = "finished";
    public static final String JOBSTATUS_ERROR = "error";
    
    public QuartzEntity() {  
    }

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getJobDetailName() {
		return jobDetailName;
	}

	public void setJobDetailName(String jobDetailName) {
		this.jobDetailName = jobDetailName;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getConCurrent() {
		return conCurrent;
	}

	public void setConCurrent(String conCurrent) {
		this.conCurrent = conCurrent;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIsSpringBean() {
		return isSpringBean;
	}

	public void setIsSpringBean(String isSpringBean) {
		this.isSpringBean = isSpringBean;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getEmailList() {
		return emailList;
	}

	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}

	public String getJobTypePerHour() {
		return jobTypePerHour;
	}

	public void setJobTypePerHour(String jobTypePerHour) {
		this.jobTypePerHour = jobTypePerHour;
	}
	
	public String getJobTypeWeek() {
		return jobTypeWeek;
	}

	public void setJobTypeWeek(String jobTypeWeek) {
		this.jobTypeWeek = jobTypeWeek;
	}

	public String getJobTypeMonth() {
		return jobTypeMonth;
	}

	public void setJobTypeMonth(String jobTypeMonth) {
		this.jobTypeMonth = jobTypeMonth;
	}

	public String getJobTypePointDay() {
		return jobTypePointDay;
	}

	public void setJobTypePointDay(String jobTypePointDay) {
		this.jobTypePointDay = jobTypePointDay;
	}

	public String getRelativeTables() {
		return relativeTables;
	}

	public void setRelativeTables(String relativeTables) {
		this.relativeTables = relativeTables;
	}

	@Transient
	public String getConditionDesc() {
		return conditionDesc;
	}

	public void setConditionDesc(String conditionDesc) {
		this.conditionDesc = conditionDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	@Transient
    public String getCondition(QuartzEntity entity){
    	if(null != entity){
    		String jobType = entity.getJobType();
    		if(StringUtils.isNotBlank(jobType))
    		{
    			if("day".equals(jobType))
    			{
    				return "每天（"+entity.getRunTime()+"）";
    			}
    			else if("week".equals(jobType))
    			{
    				String jobTypeWeek = entity.getJobTypeWeek();
    				if(StringUtils.isNotBlank(jobTypeWeek))
    				{
    					return jobTypeWeek.replace("MON", "星期一").replace("TUE", "星期二").replace("WED", "星期三").
    							replace("THU", "星期四").replace("FRI", "星期五").replace("SAT", "星期六").replace("SUN", "星期日")+"（"+entity.getRunTime()+"）";
    				}
    			}
    			else if("month".equals(jobType))
    			{
    				return "每月"+entity.getJobTypeMonth()+"日（"+entity.getRunTime()+"）";
    			}
    			else if("month".equals(jobType))
    			{
    				return entity.getJobTypePointDay()+"（"+entity.getRunTime()+"）";
    			}
    		}
    	}
    	return "";
    }

	@Transient
    public static String getCondition(QuartzEntityModel entity){
    	if(null != entity){
    		String jobType = entity.getJobType();
    		if(StringUtils.isNotBlank(jobType))
    		{
    			if("day".equals(jobType))
    			{
    				return "每天（"+entity.getRunTime()+"）";
    			}
    			else if("week".equals(jobType))
    			{
    				String jobTypeWeek = entity.getJobTypeWeek();
    				if(StringUtils.isNotBlank(jobTypeWeek))
    				{
    					return jobTypeWeek.replace("MON", "星期一").replace("TUE", "星期二").replace("WED", "星期三").
    							replace("THU", "星期四").replace("FRI", "星期五").replace("SAT", "星期六").replace("SUN", "星期日")+"（"+entity.getRunTime()+"）";
    				}
    			}
    			else if("month".equals(jobType))
    			{
    				return "每月"+entity.getJobTypeMonth()+"日（"+entity.getRunTime()+"）";
    			}
    			else if("month".equals(jobType))
    			{
    				return entity.getJobTypePointDay()+"（"+entity.getRunTime()+"）";
    			}
    		}
    	}
    	return "";
    }
	
}  