package com.md.search.server.init;

import java.util.Arrays;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 环境变量的初始化
 * 
 * @author zhiwei.wen
 * @Date 2015年8月7日 下午2:26:11
 */
@SuppressWarnings("rawtypes")
public class ConfigBean implements InitializingBean, FactoryBean {

	private CompositeConfiguration configuration;

	private Configuration[] configurations;

	public ConfigBean() {

	}

	public ConfigBean(Configuration configuration) {
		this.configuration = new CompositeConfiguration(configuration);
	}

	public void afterPropertiesSet() {
		if (configuration == null
				&& (configurations == null || configurations.length == 0)) {
			throw new IllegalArgumentException(
					"no configuration object or location specified");
		}
		if (configuration == null) {
			configuration = new CompositeConfiguration();
		}
		if (configurations != null) {

			int beginIndex = 0;
			for (int i = beginIndex; i < configurations.length; i++) {
				configuration.addConfiguration(configurations[i]);
			}
		}
	}

	public void setConfigurations(Configuration[] cfgs) {
		if (cfgs == null) {
			this.configurations = new Configuration[0];
		} else {
			this.configurations = Arrays.copyOf(cfgs, cfgs.length);
		}
	}

	public Object getObject() {
		return (configuration != null) ? ConfigurationConverter
				.getProperties(configuration) : null;
	}

	public Class<?> getObjectType() {
		return java.util.Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Object getProperty(String key) {
		
		return configuration.getString(key);
	}

}
