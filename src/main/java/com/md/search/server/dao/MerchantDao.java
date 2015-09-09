package com.md.search.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.md.search.server.bean.Merchant;

@MyBatisRepository
public interface MerchantDao {

	public List<Merchant> getMerchants(@Param("table") String table);

	public Merchant getMerchant(@Param("id") String id,
			@Param("table") String table);

}
