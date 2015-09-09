package com.md.search.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.md.search.server.constant.ApplicationConstants;

public class PropertiesUtil {

	public static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	public  static String configPath = PropertiesUtil.class.getClassLoader().getResource("/").getPath() + "config/conf.properties";

	/**
	 * 记录binlog位置
	 * 
	 * @param pos
	 * @throws FileNotFoundException
	 */
	public static void writeBinLogPos() throws FileNotFoundException {
		Properties prop = new Properties();
		try {
			InputStream in = new FileInputStream(new File(configPath));
			prop.load(in);
			in.close();
			OutputStream out = new FileOutputStream(new File(configPath));
			prop.setProperty(
					"binlog.pos",
					String.valueOf(getRightPos(ApplicationConstants.POS_DATA_RESOURCE)));
			setTableBinLogPos(prop);
			prop.store(out, null);
			out.close();
			logger.info(
					"binlogpos updated,pos:{}",
					String.valueOf(getRightPos(ApplicationConstants.POS_DATA_RESOURCE)));
		} catch (IOException e) {
			logger.error("properties operate error:", e);
		}
	}

	/**
	 * 其他表偏移量设置
	 * 
	 * @param prop
	 */
	private static void setTableBinLogPos(Properties prop) {
		try {
			if (ApplicationConstants.DATA_RESOURCE_POS_CHANGE_FLAG) {
				prop.setProperty("pos.data_resource",
						String.valueOf(ApplicationConstants.POS_DATA_RESOURCE));
			}
		} catch (Exception e) {
			logger.error("set table binlog pos error:", e);
		}
	}

	/**
	 * 更新小区更新时间
	 * 
	 * @param date
	 * @throws FileNotFoundException
	 */
	public static void writeUpdownUpdateTime(Date date)
			throws FileNotFoundException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Properties prop = new Properties();
		try {
			InputStream in = PropertiesUtil.class.getClassLoader()
					.getResourceAsStream("config/conf.properties");
			prop.load(in);
			in.close();
			OutputStream out = new FileOutputStream(new File(configPath));
			prop.setProperty("updown_lastupdate_time", sdf.format(date));
			prop.store(out, null);
			out.close();
			logger.info("updown update time updated ,time:{}"
					+ sdf.format(date));
		} catch (IOException e) {
			logger.error("properties operate error:", e);
		}
	}


	/**
	 * 获取配置文件（在web目录）
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Properties getInnerConfigProperties() throws IOException {
		logger.info(configPath);
		
		InputStream in = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream("config/conf.properties");
		Properties prop = new Properties();
		prop.load(in);
		return prop;
	}

	/**
	 * 保存文件
	 * 
	 * @param fileName
	 * @param description
	 * @throws IOException
	 */
	public void saveFile(String fileName, String description)
			throws IOException {

		Properties propertie = new Properties();
		FileOutputStream outputFile;
		String filePath = getClass().getResource("/").getPath() + fileName;

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		try {
			outputFile = new FileOutputStream(file);
			propertie.store(outputFile, description);
			outputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 获取总的正确的pinlog偏移
	 * 
	 * @param poss
	 * @return
	 */
	private static long getRightPos(long... poss) {
		long right = 100000000000000l;
		for (long pos : poss) {
			if (pos < right) {
				right = pos;
			}
		}
		return right;
	}
}
