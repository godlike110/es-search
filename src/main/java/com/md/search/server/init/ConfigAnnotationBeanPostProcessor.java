package com.md.search.server.init;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.md.search.server.annotation.Config;
import com.md.search.server.bean.DictCity;
import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.dao.DictCityDao;
import com.md.search.server.service.FullDataService;
import com.md.search.server.service.OperateService;
import com.md.search.server.task.BinLogDelTask;
import com.md.search.server.task.BinLogUpdateTask;
import com.md.search.server.task.BinLogWriteTask;
import com.md.search.server.task.FullDataTask;
import com.md.search.server.task.TaskTrigger;
import com.md.search.server.util.InitUtil;
import com.md.search.server.util.PropertiesUtil;

/**
 * 属性值的一个注入
 * 
 * @author zhiwei.wen
 * @time 2015/07
 */
@Component
public class ConfigAnnotationBeanPostProcessor extends
		InstantiationAwareBeanPostProcessorAdapter implements
		ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = LoggerFactory
			.getLogger(ConfigAnnotationBeanPostProcessor.class);

	// 创建简单类型转换器
	private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

	@Autowired
	private ConfigBean configBean;

	@Autowired
	private DictCityDao dictCityDao;

	@Autowired
	private OperateService merchantService;

	@Autowired
	private FullDataService fullDataService;

	@Override
	public boolean postProcessAfterInstantiation(final Object bean,
			String beanName) throws BeansException {
		if ("applicationConstants".equals(beanName)) {
			findPropertyAutowiringMetadata(bean);
		}
		return true;
	}

	private void findPropertyAutowiringMetadata(final Object bean) {
		ReflectionUtils.doWithFields(bean.getClass(),
				new ReflectionUtils.FieldCallback() {
					public void doWith(Field field)
							throws IllegalAccessException {
						Config annotation = field.getAnnotation(Config.class);
						if (annotation != null) {
							Object strValue = configBean.getProperty(annotation
									.value());
							if (null != strValue) {
								Object value = typeConverter
										.convertIfNecessary(strValue,
												field.getType());
								ReflectionUtils.makeAccessible(field);
								field.set(null, value);
								logger.info("set field:" + field.getName()
										+ " value :" + value);
							}
						}
					}
				});
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("init system start!");
		try {
			Properties prop = PropertiesUtil.getInnerConfigProperties();
			if (prop == null) {
				logger.info("properties file error,null");
			}
			logger.info("config:{}", JSONObject.toJSONString(prop));
//			ApplicationConstants.BINLOGPOS = prop.getProperty("binlog.pos");
			ApplicationConstants.UPDATE_DATE = prop
					.getProperty("updown_lastupdate_time");
			ApplicationConstants.MYSQLHOST = prop.getProperty("db.host");
			ApplicationConstants.MYSQLPORT = prop.getProperty("db.port");
			ApplicationConstants.MYSQLUSER = prop.getProperty("db.user");
			ApplicationConstants.MYSQLPASSWD = prop.getProperty("db.pwd");
			ApplicationConstants.HAS_GROBAL_TASK = Boolean.parseBoolean(prop
					.getProperty("has_grobal_task"));
			ApplicationConstants.ALL_TASK_RUN = Boolean.parseBoolean(prop
					.getProperty("all_task_run"));
//			ApplicationConstants.POS_DATA_RESOURCE = Long.parseLong(prop
//					.getProperty("pos.data_resource"));
			ApplicationConstants.UPDOWN_UPDATE_OPEN = Boolean.parseBoolean(prop
					.getProperty("updown_update_open"));

			/**
			 * 所有开通的城市表
			 */
			List<DictCity> citys = dictCityDao.getCities();
			ApplicationConstants.MERCHANTTABLESET = InitUtil
					.getMerchantTableSet(citys);
			ApplicationConstants.BINLOGTABLESET = InitUtil
					.getMerchantTableSet(citys);

			BinLogDelTask.merchantService = this.merchantService;
			BinLogUpdateTask.merchantService = this.merchantService;
			BinLogWriteTask.merchantService = this.merchantService;
			FullDataTask.fullDataService = this.fullDataService;

			/**
			 * 初始化需要被扫描binlog的table
			 */
			if (StringUtils.isNotBlank(ApplicationConstants.BINLOGTABLES)) {
				for (String table : ApplicationConstants.BINLOGTABLES
						.split("##")) {
					ApplicationConstants.BINLOGTABLESET.add(table);
				}
			}

			/**
			 * 启动调度任务
			 */
			new Thread(new TaskTrigger()).start();
			logger.info("init finish,updownupdatetime:{}",
					prop.getProperty("updown_lastupdate_time"));
		} catch (IOException e) {
			logger.error("init sys error:", e);
			System.exit(1);
		}
	}

}
