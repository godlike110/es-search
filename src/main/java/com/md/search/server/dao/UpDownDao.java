package com.md.search.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.md.search.server.bean.Updown;

/**
 * 小区dao
 * 
 * @author zhiwei.wen
 * @Date 2015年8月5日 下午3:36:08
 */
@MyBatisRepository
public interface UpDownDao {

	/**
	 * 批量获取
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Updown> getUpdowns(@Param("offset") int offset,
			@Param("limit") int limit);

	/**
	 * 根据城市和楼盘名查找
	 * 
	 * @param name
	 * @param city
	 * @return
	 */
	public Updown getUpdownsByName(@Param("name") String name,
			@Param("city") String city);

	/**
	 * 根据id查找
	 * 
	 * @param id
	 * @return
	 */
	public Updown getUpdown(@Param("id") int id);

	/**
	 * 获取总数
	 * 
	 * @return
	 */
	public int getCounts();

}
