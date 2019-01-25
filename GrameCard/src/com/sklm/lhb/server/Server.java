package com.sklm.lhb.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.sklm.lhb.Dao.DBConnection;

public class Server {

	public static void insert(String cardType,String goodsType,String authCode,String command){
		Connection conn = new DBConnection().getConn();
		try {
			PreparedStatement ps = conn.prepareStatement("insert into playersGameCard (cardType,goodsType,authCode,command)values(?,?,?,?)");
			//PreparedStatement ps = conn.prepareStatement("insert into playersGameCard (cardType,goodsType cardCode)values(?,?,?)");
			
			ps.setString(1, cardType);
			ps.setString(2, goodsType);
			ps.setString(3, authCode);
			ps.setString(4, command);
			
			int row = ps.executeUpdate();
			if(row<=0){
				System.out.print("生成验证码失败");
			}
			if(ps!=null){
				ps.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean delete(String authCode){
		Connection conn = new DBConnection().getConn();
		boolean b = false;
		try {
			PreparedStatement ps = conn.prepareStatement("delete from playersGameCard where authCode='"+authCode+"'");
			//PreparedStatement ps = conn.prepareStatement("insert into playersGameCard (cardType,goodsType cardCode)values(?,?,?)");

			int row = ps.executeUpdate();
			if(row>=0){
				
				System.out.print("playersGameCard删除数据成功");
				return true;
			}
			if(ps!=null){
				ps.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return b;
	}
	
	/**
	 * @function 给玩家物品
	 * @param goodsType
	 * @param money
	 */
	public static void givePlayer(String authCode, String command) {
		Connection conn = new DBConnection().getConn();
		try {
			PreparedStatement ps = conn.prepareStatement("insert into player_give (authCode,command)values(?,?)");
			ps.setString(1, authCode);
			ps.setString(2, command);
			
			int row = ps.executeUpdate();
			if(row<=0){
				System.out.print("插入数据失败");
				
			}
			if(ps!=null){
				ps.close();
			}
			if(conn != null){
				conn.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean deleteCode(String authCode) {
		
		Connection conn = new DBConnection().getConn();
		String command=null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from player_give where authCode='"+authCode+"';");
			
			int i = ps.executeUpdate();
			if(i>=1) {
				Bukkit.getLogger().info("删除成功");
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
		
	}
	
	/**
	 * 查询对应物品执行的指令
	 * @param goodsType
	 * @return
	 */
	public static String getCommand(String authCode) {
		Connection conn = new DBConnection().getConn();
		String command=null;
		try {
			PreparedStatement ps = conn.prepareStatement("select command from player_give where authCode='"+authCode+"';");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				command = rs.getString("command");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return command;
		
	}
	
	/**
	 * 获得玩家vip等级
	 * @param player_name
	 * @param Key (playerID:获取玩家ID;TotalMoney:获取玩家充值总额;vipLevle:获得玩家vip等级;nextLevle:获取玩家距离升级还剩多少经验)
	 * @return
	 */
	public static Map getPlayerInfo(String player_name) {
		
		Connection conn = new DBConnection().getConn();
		Map playerInfoMap = new HashMap();
		
		try {
			PreparedStatement ps = conn.prepareStatement("select * from player_vip_Info where Player_name='"+player_name+"';");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String pm = rs.getString("Player_name");
				double pmt = rs.getDouble("Pay_money_total");
				int vip_levle = rs.getInt("VIP_levle");
				int next_levle = rs.getInt("Dis_next_levle");
				playerInfoMap.put("playerID", pm);
				playerInfoMap.put("TotalMoney", pmt);
				playerInfoMap.put("vipLevle", vip_levle);
				playerInfoMap.put("nextLevle", next_levle);
			}
			
		} catch (SQLException e) {
			System.out.println("该玩家不存在");
			e.printStackTrace();
		}
		
		return playerInfoMap;
		
	}
	
	/**
	 * 返回玩家充值总金额
	 * @param player_name
	 * @return
	 */
	public static double getPay_money_total(String player_name) {
		Connection conn = new DBConnection().getConn();
		double pmt = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("select Pay_money_total from player_vip_Info where Player_name='"+player_name+"';");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				pmt = rs.getDouble("Pay_money_total");
			}
			
		} catch (SQLException e) {
			pmt = 0.0;
			e.printStackTrace();
		}
		return pmt;
	}
	
	/**
	 * 返回vip玩家等级
	 * @param player_name
	 * @return
	 */
	public static int getVIP_levle(String player_name) {
		Connection conn = new DBConnection().getConn();
		int vip_levle = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("select VIP_levle from player_vip_Info where Player_name='"+player_name+"';");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				vip_levle = rs.getInt("VIP_levle");
			}
			
		} catch (SQLException e) {
			vip_levle = 0;
			e.printStackTrace();
		}
		return vip_levle;
	}
	
	/**
	 * 返回vip玩家距离下一级升级还需要的经验
	 * @param player_name
	 * @return
	 */
	public static double getDis_next_levle(String player_name) {
		Connection conn = new DBConnection().getConn();
		double next_levle = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("select Dis_next_levle from player_vip_Info where Player_name='"+player_name+"';");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				next_levle = rs.getDouble("Dis_next_levle");
			}
			
		} catch (SQLException e) {
			next_levle = 0.0;
			e.printStackTrace();
		}
		return next_levle;
	}
	
	
	/**
	 * 保存玩家信息
	 * @param player_name
	 * @param money
	 * @param vip_levle
	 * @param next_levle
	 */
	public static void saveVipInfo(String player_name, double money, int vip_levle, double next_levle) {
		
		Connection conn = new DBConnection().getConn();
		try {
			PreparedStatement ps = conn.prepareStatement("update player_vip_Info set Player_name='"+player_name+"',"
					+ "Pay_money_total="+money+","
					+ "VIP_levle="+vip_levle+","
					+ "Dis_next_levle="+next_levle+";");
			int i = ps.executeUpdate();
			if(i>0) {
				System.out.println("vip信息保存成功");
			}else {
				System.out.println("vip信息保存失败");
			}
			
		} catch (SQLException e) {
			System.out.println("该玩家不存在");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 添加vip玩家信息
	 * @param player_name
	 * @param money
	 * @param vip_levle
	 * @param next_levle
	 */
	public static void insertVipInfo(String player_name, double money, int vip_levle, double next_levle) {
		Connection conn = new DBConnection().getConn();
		try {
			PreparedStatement ps = conn.prepareStatement("insert into player_vip_Info (Player_name,Pay_money_total,VIP_levle,Dis_next_levle) values "
					+ "(?,?,?,?)");
			ps.setString(1, player_name);
			ps.setDouble(2, money);
			ps.setInt(3, vip_levle);
			ps.setDouble(4, next_levle);
			int i = ps.executeUpdate();
			if(i>0) {
				System.out.println("vip信息添加成功");
			}else {
				System.out.println("vip信息添加失败");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查找玩家，如果玩家存在返回true，反则返回false
	 * @param name
	 * @return
	 */
	public static boolean findPlayer(String name) {
		Connection conn = new DBConnection().getConn();
		try {
			PreparedStatement ps = conn.prepareStatement("select Player_name from player_vip_info where Player_name='"+name+"';");
			ResultSet rs = ps.executeQuery();
			String str = null;
			while(rs.next()) {		
					str = rs.getString("Player_name");	
			}
			if(str!=null) {
				if(str.equals(name)) 
					return true;
				else
					return false;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			System.out.println("该玩家不存在");
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * 返回对应等级所需要的经验
	 * @param vip_levle
	 * @return
	 */
	public static double getVip_levleInVIPLevle(int vip_levle) {
		Connection conn = new DBConnection().getConn();
		double Vip_Exp = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("select Vip_Exp from player_vip_levle where Vip_levle="+vip_levle+";");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Vip_Exp = rs.getDouble("Vip_Exp");
			}		
		} catch (SQLException e) {
			Vip_Exp = 0.0;
			e.printStackTrace();
		}
		
		return Vip_Exp;
	}
	
	/**
	 * 从数据库中获取VIP等级表
	 * @return 返回vip信息map表
	 */
	public static Map getVipLevle_table() {
		
		Map map = new HashMap();
		
		Connection conn = new DBConnection().getConn();
		double Vip_Exp = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("select * from player_vip_levle;");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				map.put(rs.getInt("Vip_levle"), rs.getDouble("Vip_Exp"));
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
}
