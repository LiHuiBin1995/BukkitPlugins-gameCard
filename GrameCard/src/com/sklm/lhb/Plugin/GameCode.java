package com.sklm.lhb.Plugin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.sklm.lhb.Cmd.PluginCommand;
import com.sklm.lhb.Cmd.VIPCMD;
import com.sklm.lhb.server.Server;
/**
 * 
 * @author Administrator
 *
 */
public class GameCode extends JavaPlugin {

	public static int time;
	public static Map vipInfoMap = new HashMap();
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("加载插件");
		this.saveResource("db.properties", false);
		vipInfoMap = Server.getVipLevle_table();
		System.out.println("/*******************************************************************\n"
				+ vipInfoMap+"\n"
			    + "****************************************************************************/");
		getCommand("cardcode").setExecutor(new PluginCommand());
		getCommand("vip").setExecutor(new VIPCMD(this));
	}
	
	@Override
	public void onDisable() {
		Bukkit.getLogger().info("卸载插件");
		
	}
	
}
