package com.md.search.server.constant;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.md.search.server.annotation.Config;

/**
 * 运用程序常量配置
 * 
 * @author zhiwei.wen
 * @time 2015年4月28日下午2:52:55
 */
@Component
public class ApplicationConstants {

	@Config("test.url")
	public static String TEST_URL;

	@Config("ip.list")
	public static String IP_LIST;

	/**
	 * 小区更新时间
	 */
	public static String UPDATE_DATE;

	/**
	 * binlog位置记录
	 */
	public static String BINLOGPOS;

	public static String MYSQLHOST;

	public static String MYSQLPORT;

	public static String MYSQLUSER;

	public static String MYSQLPASSWD;

	/**
	 * 商户信息信息表set
	 */
	public static Set<String> MERCHANTTABLESET;

	/**
	 * binlog扫描表集合(不包含data_resource_*)
	 */
	@Config("tables")
	public static String BINLOGTABLES;

	/**
	 * 所有需要扫描表set(包含data_resource_*)
	 */
	public static Set<String> BINLOGTABLESET = new HashSet<String>();
	/**
	 * 需要处理的数据库
	 */
	public static String BINGLOGDB = "mdsh";

	/**
	 * es各端口ip分割
	 */
	public static final String ES_IP_SPLIT = "##";

	/**
	 * es index
	 */
	public static final String INDEX = "maydo";

	/**
	 * data_resource binLog偏移
	 */
	public static long POS_DATA_RESOURCE;
	/**
	 * data_resource binlog是否被修改
	 */
	public static volatile boolean DATA_RESOURCE_POS_CHANGE_FLAG = false;

	/**
	 * 是否有全量更新任务
	 */
	public static volatile boolean HAS_GROBAL_TASK;

	/**
	 * 是否允许跑任务
	 */
	public static volatile boolean ALL_TASK_RUN;
	
	/**
	 * 小区是否更新
	 */
	public static volatile boolean UPDOWN_UPDATE_OPEN;

}
