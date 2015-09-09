package com.md.search.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoShapeFilterBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.md.search.server.bean.Merchant;
import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.dao.MerchantDao;
import com.md.search.server.enums.MaydoType;
import com.vividsolutions.jts.geom.Coordinate;

@Service
public class MerchantService implements OperateService {

	public static Logger logger = LoggerFactory
			.getLogger(MerchantService.class);

	@Autowired
	private MerchantDao merchantDao;

	@Autowired
	private EsService esService;

	public List<Merchant> getMerchants(String table) {
		try {
			return merchantDao.getMerchants(table);
		} catch (Exception e) {
			logger.error("getMerchants error:", e);
			return null;
		}
	}

	public Merchant getMerchant(String table, String id) {
		try {
			return merchantDao.getMerchant(id, table);
		} catch (Exception e) {
			logger.error("getmerchant error:", e);
			return null;
		}
	}

	/**
	 * 插入文档
	 * 
	 * @param id
	 *            已经合成的id
	 * @param merchant
	 * @return
	 */
	public boolean insertMerchantToEs(String id, Merchant merchant) {
		try {
			return esService.insertEsDocByString(
					ApplicationConstants.INDEX,
					MaydoType.DATARESOURCE.name(),
					id,
					JSONObject.toJSONString(merchant).replaceAll("\\\\", "")
							.replace("geoMetry\":\"", "geoMetry\":")
							.replace("\",\"headPic", ",\"headPic"));
		} catch (Exception e) {
			logger.error("inserMerchant to es error!", e);
			return false;
		}
	}

	/**
	 * 删除文档
	 * 
	 * @param id
	 *            已经合成的id
	 * @return
	 */
	public boolean removeMechantFromEs(String id) {
		try {
			return esService.removeEsDoc(ApplicationConstants.INDEX,
					MaydoType.DATARESOURCE.name(), id);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 更新
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public boolean updateMerchantInEs(String table, String id) {
		boolean result = false;
		try {
			this.removeMerchantInEs(table, id);
			this.insertMerchantInEs(table, id);
			result = true;
		} catch (Exception e) {
			logger.error("update merchant error:", e);
		}
		return result;
	}

	/**
	 * 插入
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public boolean insertMerchantInEs(String table, String id) {
		try {
			Merchant merchant = merchantDao.getMerchant(id, table);
			if (null == merchant) {
				return false;
			}
			List<Merchant> merchants = getMerchantsFromMerchant(merchant);
			if (null == merchants || merchants.size()==0) {
				return false;
			}
			for (Merchant m : merchants) {
				this.insertMerchantToEs(table + id + m.getWhich(), m);
			}
			return true;
		} catch (Exception e) {
			logger.error("insert merchant errro:", e);
			return false;
		}
	}

	/**
	 * 删除
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public boolean removeMerchantInEs(String table, String id) {
		boolean result = false;
		try {
			Map<String, Object> map = esService.getDoc(
					ApplicationConstants.INDEX, MaydoType.DATARESOURCE.name(),
					table + id + 0);
			if (map != null && map.keySet().size() > 0) {
				int size = (int) map.get("areas");
				for (int i = 0; i < size; i++) {
					this.removeMechantFromEs(table + id + i);
				}
				result = true;
			}

		} catch (Exception e) {
			logger.error("remove merchant error:", e);
		}
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public boolean removeMerchantFromEs(String table, Merchant merchant) {
		if (null == merchant) {
			return false;
		}
		try {
			List<Merchant> merchants = getMerchantsFromMerchant(merchant);
			for (Merchant m : merchants) {
				this.removeMechantFromEs(table + m.getId() + m.getWhich());
			}
			return true;
		} catch (Exception e) {
			logger.error("removeMerchant from es error:", e);
			return false;
		}

	}

	public boolean insertMerchantFromEs(String table, Merchant merchant) {
		if (null == merchant) {
			return false;
		}
		try {
			List<Merchant> merchants = getMerchantsFromMerchant(merchant);
			if (null == merchants) {
				return true;
			}
			for (Merchant m : merchants) {
				this.insertMerchantToEs(table + m.getId() + m.getWhich(), m);
			}
			return true;
		} catch (Exception e) {
			logger.error("insertMerchant to es error:", e);
			return false;
		}

	}

	public List<Merchant> getMerchantsFromMerchant(Merchant merchant) {
		if (StringUtils.isBlank(merchant.getServiceArea()) || merchant.getServiceArea().length()<10) {
			return null;
		}
	    
		String sa = merchant.getServiceArea().replaceAll("\"\\{", "\\{")
				.replaceAll("\\}\"", "\\}").replaceAll("\\\\", "");
		logger.info("serive area:{}",sa);
		JSONObject jsonObject = null;
		List<Merchant> merList = new ArrayList<Merchant>();
		try {
			jsonObject = JSONObject.parseObject(sa);
			Iterator<Entry<String, Object>> it = jsonObject.entrySet()
					.iterator();
			merchant.setAreas(jsonObject.entrySet().size());
			JSONObject obj = null;
			JSONArray jaArray = null;
			int pos = 0;

			while (it.hasNext()) {

				Merchant m = (Merchant) merchant.clone();
				m.setWhich(pos++);
				Entry<String, Object> entry = it.next();
				obj = (JSONObject) entry.getValue();
				jaArray = obj.getJSONArray("node_location");
				Iterator<Object> itor = jaArray.iterator();
				int i = 0;
				Coordinate[] coordinates = new Coordinate[jaArray.size()];
				while (itor.hasNext()) {
					JSONObject object = (JSONObject) itor.next();
					coordinates[i++] = new Coordinate(object.getDouble("t"),
							object.getDouble("w"));
				}
				coordinates = sort(coordinates);
				m.setGeoMetry(getGeoMetry(coordinates));
				m.setServiceArea("");
				merList.add(m);
			}
		} catch (Exception e) {
			logger.error("get merchants from merchant error", e);
		}
		return merList;

	}

	@Override
	public void eventControl(EventType eventType, String id, String table,
			long offset) {
		/**
		 * binlog偏移量小的 统统略过
		 */
		if (offset <= ApplicationConstants.POS_DATA_RESOURCE) {
			return;
		}
		ApplicationConstants.DATA_RESOURCE_POS_CHANGE_FLAG = true;
		ApplicationConstants.POS_DATA_RESOURCE = offset;
		boolean result = false;
		try {
			switch (eventType) {
			case EXT_DELETE_ROWS:
				result = this.removeMerchantInEs(table, id);
				break;
			case EXT_WRITE_ROWS:
				result = this.insertMerchantInEs(table, id);
				break;
			case EXT_UPDATE_ROWS:
				result = this.updateMerchantInEs(table, id);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("merchant eventcontrol error:", e);
			result = false;
		} finally {
			logger.info("{} {} event {} {}", table, id, eventType.name(),
					result == false ? "failed" : "success");
		}

	}

	/**
	 * 点排序
	 * 
	 * @param coordinates
	 * @return
	 */
	public Coordinate[] sort(Coordinate[] coordinates) {
		
		Map<String, Coordinate> coMap = new HashMap<String, Coordinate>();
		
		for(int i=0;i<coordinates.length;i++) {
			coMap.put(String.valueOf(coordinates[i].x) + String.valueOf(coordinates[i].y), coordinates[i]);
		}
		Iterator<String> keyIterator = coMap.keySet().iterator();
		Coordinate[] b = new Coordinate[coMap.keySet().size() + 1];
		int index = 0;
		while(keyIterator.hasNext()) {
			b[index++] = coMap.get(keyIterator.next());
		}
		b[coMap.keySet().size()]=b[0];
		return b;
	}

	/**
	 * 获取矩形信息
	 * 
	 * @return
	 */
	public String getGeoMetry(Coordinate[] coordinates) {
		JSONObject geometry = new JSONObject();
		geometry.put("type", "polygon");
		JSONArray geoArray = new JSONArray();
		geometry.put("coordinates", geoArray);
		JSONArray geoArray2 = new JSONArray();
		geoArray.add(geoArray2);
		for (Coordinate coor : coordinates) {
			JSONArray geoArray3 = new JSONArray();
			geoArray3.add(coor.x);
			geoArray3.add(coor.y);
			geoArray2.add(geoArray3);
		}
		return geometry.toJSONString();
	}

	/**
	 * 搜索
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	public List<Merchant> search(double lon, double lat) {
		ShapeBuilder shape = ShapeBuilder.newPoint(new Coordinate(lon, lat));
		GeoShapeFilterBuilder geoShapeFilterBuilder = FilterBuilders
				.geoShapeFilter("geoMetry", shape, ShapeRelation.INTERSECTS);

		SearchResponse searchResponse = esService.filteredQuery(
				ApplicationConstants.INDEX, MaydoType.DATARESOURCE.name(),
				new FilteredQueryBuilder(new MatchAllQueryBuilder(),
						geoShapeFilterBuilder));
		if (null == searchResponse
				|| StringUtils.isBlank(searchResponse.toString())) {
			return null;
		}
		SearchHits searchHits = searchResponse.getHits();
		if (searchHits.getHits().length == 0) {
			return null;
		}
		Map<String, Merchant> merchantMap = new HashMap<String, Merchant>();
		for (SearchHit sh : searchHits.getHits()) {
			Merchant a = JSONObject.parseObject(sh.getSourceAsString(),
					Merchant.class);
			merchantMap.put((String) sh.getSource().get("id"), a);
		}
		List<Merchant> merchants = new ArrayList<Merchant>();
		Iterator<String> iterator = merchantMap.keySet().iterator();
		while (iterator.hasNext()) {
			merchants.add(merchantMap.get(iterator.next()));
		}
		return merchants;
	}
	
	
	/**
	 * 定时任务更新商户范围图形  10分钟
	 */
	public void updataTask() {
		logger.error("start run merchant full date update!");
		long start = System.currentTimeMillis();
		List<Merchant> merchants = null;
		for (String table : ApplicationConstants.MERCHANTTABLESET) {
			logger.info("run data {}", table);
			try {
				merchants = this.getMerchants(table);
				for (Merchant m : merchants) {
				    boolean result = false;
					List<Merchant> mers = this
							.getMerchantsFromMerchant(m);
					if (null == mers || mers.size()==0) {
						continue;
					}
					for (Merchant obj : mers) {
						result = this.insertMerchantToEs(table + obj.getId()
								+ obj.getWhich(), obj);
					}
					if(result) {
					logger.info(
							"run full data insert merchant success!{}|{}|{}",
							table, m.getId(), m.getWhich());
					} else {
						logger.info(
								"run full data insert merchant error!{}|{}|{}",
								table, m.getId(), m.getWhich());
					}
				}
			} catch (Exception e) {
				logger.error("fulldata error:", e);
				continue;
			} finally {
				logger.info("run merchant full date done!use time:{}",System.currentTimeMillis()-start);
			}
		}
	}
	}

