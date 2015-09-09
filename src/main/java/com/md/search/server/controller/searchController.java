package com.md.search.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.search.server.bean.Merchant;
import com.md.search.server.bean.Updown;
import com.md.search.server.constant.Result;
import com.md.search.server.enums.ReturnType;
import com.md.search.server.service.EsService;
import com.md.search.server.service.LocationService;
import com.md.search.server.service.MerchantService;
import com.md.search.server.service.UpdownEsServer;
import com.md.search.server.service.UpdownService;

/**
 * 外用接口
 * 
 * @author zhiwei.wen 2015年7月6日
 */
@Controller
@RequestMapping("/")
public class searchController {

	public static Logger logger = LoggerFactory
			.getLogger(searchController.class);

	@Autowired
	private LocationService locationService;

	@Autowired
	private UpdownService updownService;

	@Autowired
	private UpdownEsServer esIndexService;

	@Autowired
	private UpdownEsServer updownEsServer;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private EsService esServie;

	@RequestMapping("index")
	@ResponseBody
	public String index() {
		return "hello md searcher";
	}

	/**
	 * 按名称搜索小区
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "searchUpdownByName", method = RequestMethod.GET)
	@ResponseBody
	public String searchUpdownByName(@RequestParam("name") String name,
			@RequestParam("city") String city) {
		long start = System.currentTimeMillis();
		Result<List> result = new Result<List>();
		try {
			result.setData(updownService.searchUpdownByName(name, city));
			result.setCode(ReturnType.SUCCESS.getType());
			result.setMsg(ReturnType.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("searchUpdownByName err:", e);
			result.setCode(ReturnType.FAILD.getType());
			result.setData(null);
			result.setMsg(ReturnType.FAILD.getMsg());
		} finally {
			logger.info("{}|{}|{}|{}", System.currentTimeMillis() - start,
					name, city, JSONObject.toJSONString(result));
		}
		return JSONObject.toJSONString(result);
	}

	/**
	 * 定位小区位置
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	@RequestMapping(value = "locate", method = RequestMethod.GET)
	@ResponseBody
	public String locate(@RequestParam("lon") double lon,
			@RequestParam("lat") double lat) {
		long start = System.currentTimeMillis();
		Result<Updown> result = new Result<Updown>();
		try {
			result.setData(locationService.location(lon, lat, 1l, "maydo",
					"community"));
			result.setCode(ReturnType.SUCCESS.getType());
			result.setMsg(ReturnType.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("locate err:", e);
			result.setCode(ReturnType.FAILD.getType());
			result.setData(null);
			result.setMsg(ReturnType.FAILD.getMsg());
		} finally {
			logger.info("{}|{}|{}|{}", System.currentTimeMillis() - start, lon,
					lat, JSONObject.toJSONString(result));
		}
		return JSONObject.toJSONString(result);
	}

	/**
	 * 安坐标搜索商户块
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("areas")
	@ResponseBody
	public String areas(@RequestParam("lon") double lon,
			@RequestParam("lat") double lat) throws Exception {
		long start = System.currentTimeMillis();
		Result<List> result = new Result<List>();
		try {
			List<Merchant> merchants = merchantService.search(lon, lat);
			result.setData(merchants);
			result.setCode(ReturnType.SUCCESS.getType());
			result.setMsg(ReturnType.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("areas search error:", e);
			result.setData(null);
			result.setMsg(ReturnType.FAILD.getMsg());
			result.setCode(ReturnType.FAILD.getType());
		} finally {
			logger.info("{}|{}|{}|{}", System.currentTimeMillis() - start, lon,
					lat, JSONObject.toJSONString(result));
		}
		return JSONObject.toJSONString(result);
	}

	/**
	 * 删除es文档入口
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("removeEsDoc")
	@ResponseBody
	public String remove(@RequestParam("id") String id,
			@RequestParam("index") String index,
			@RequestParam("type") String type) throws Exception {
		try {
			esServie.removeEsDoc(index, type, id);
			return "success";
		} catch (Exception e) {
			return "failed";
		}
	}

}
