package com.eb.admin.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class JobFactory extends AdaptableJobFactory {
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
