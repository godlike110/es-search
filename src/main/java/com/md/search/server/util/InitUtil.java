package com.md.search.server.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.md.search.server.bean.DictCity;

/**
 * 初始化工具类
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午3:05:40
 */
public class InitUtil {

	public static Logger logger = LoggerFactory.getLogger(InitUtil.class);

	/**
	 * 商户表集合获取
	 * 
	 * @param citys
	 * @return
	 */
	public static Set<String> getMerchantTableSet(List<DictCity> citys) {
		Set<String> merchantTableSet = new HashSet<String>();
		for (DictCity city : citys) {
			merchantTableSet.add("data_resource_" + city.getId());
		}
		return merchantTableSet;
	}

}
