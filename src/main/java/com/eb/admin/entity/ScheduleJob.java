package com.eb.admin.entity;

public class ScheduleJob {
	/** 任务id */
	private long jobId;
	/** 任务名称 */
	private String jobName;
	/** 任务分组 */
	private String jobGroup;
	/** 任务状态 0禁用 1启用 2删除 */
	private String jobStatus;
	/** 任务运行时间表达式 */
	private String cronExpression;
	/** 定时任务处理类（全路径） */
	private String quartzClass;
	/** 任务描述 */
	private String description;
	/** 接收人 */
	private String receiver;


	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	
	public String getQuartzClass() {
		return quartzClass;
	}

	public void setQuartzClass(String quartzClass) {
		this.quartzClass = quartzClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}