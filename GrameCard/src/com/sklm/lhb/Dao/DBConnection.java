package com.sklm.lhb.Dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.Bukkit;

import com.mysql.jdbc.Connection;

public class DBConnection {

	private final String path = System.getProperty("user.dir")+"/plugins/GameCode/db.properties";
	String sql;
	//获取数据库属性
	private String url; 
	private String user; 
	private String password;
	private String driver;
	
	public DBConnection() {
		setDbProperties();
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getDriver() {
		return driver;
	}

	public Connection getConn() {
		
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = (Connection) DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 加载db.properties
	 * @return
	 */
	private Properties loadProperties(String path) {
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(path);
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * 设置数据库属性
	 */
	private void setDbProperties(){
		Properties prop = loadProperties(path);
		Bukkit.getLogger().info("url = "+prop.getProperty("url")+"\n"
				+ "user = " + prop.getProperty("user")+"\n"
				+ "password = " + prop.getProperty("password")+"\n"
				+ "driver = " + prop.getProperty("driver"));
		this.setUrl(prop.getProperty("url"));
		this.setUser(prop.getProperty("user"));
		this.setPassword(prop.getProperty("password"));
		this.setDriver(prop.getProperty("driver"));
	}
	
}
