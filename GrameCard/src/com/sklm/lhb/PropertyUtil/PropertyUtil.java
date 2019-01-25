package com.sklm.lhb.PropertyUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bukkit.Bukkit;

/**
 * 该类存在bug禁止使用
 * 功能：properties文件获取工具类,
 * @author Administrator
 *
 */
public class PropertyUtil {

	private static Properties props;
	static {
		loadProps();
	}
	/**
	 * 功能：加载属性文件
	 */
	synchronized static private void loadProps() {
		props = new Properties();
		InputStream in = null;
		try {
			in = PropertyUtil.class.getClassLoader().getResourceAsStream("./db.properties");
			props.load(in);
		} catch (FileNotFoundException e) {
			Bukkit.getLogger().info("db.properties文件未找到");
			e.printStackTrace();
		} catch (IOException e) {
			Bukkit.getLogger().info("db.properties文件打开错误");
			e.printStackTrace();
		} finally {
			try {
				if(in != null) {
					in.close();
				}
			} catch (Exception e2) {
				Bukkit.getLogger().info("db.properties文件流关闭出现异常");
			}
		}
	}
	/**
	 * @function 获得指定key值的属性值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		if(props == null) {
			loadProps();
		}
		return props.getProperty(key);
	}
	/**
	 * @function 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
    public static String getProperty(String key, String defaultValue) {
		if(props == null) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}
}
