package com.md.search.server.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识MyBatis的DAO,方便 {@link org.mybatis.spring.mapper.MapperScannerConfigurer}
 * 的扫描。 MyBatisRepository
 * 
 * @author chenchao
 * @date Jul 14, 2015 2:59:24 PM
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyBatisRepository {

}
