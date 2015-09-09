package com.md.search.server.dao;

import java.util.List;

import com.md.search.server.bean.DictCity;

/**
 * 城市信息
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午1:45:39
 */
@MyBatisRepository
public interface DictCityDao {

	List<DictCity> getCities();

}
