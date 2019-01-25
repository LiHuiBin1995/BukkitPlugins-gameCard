package com.sklm.lhb.Cmd;

import java.util.Date;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sklm.lhb.Plugin.GameCode;
import com.sklm.lhb.server.Server;

public class VIPCMD implements CommandExecutor {

	private final GameCode plugin;
	private int payNum = 0;
	private final char[] color = {'3','6','9','a','b','c','d','e','f'};
	public VIPCMD(GameCode plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Key (playerID:获取玩家ID;TotalMoney:获取玩家充值总额;vipLevle:获得玩家vip等级;nextLevle:获取玩家距离升级还剩多少经验)
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	public void VIP_CMD(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		String vipCmd = cmd.getName();
			String player_name = player.getName();
			try {
				Map map = Server.getPlayerInfo(player_name);
				if((map.get("playerID"))!=null) {
					long time = System.currentTimeMillis();
	
						sender.sendMessage("§e§l━━━━━━━━━━VIP信息§e§l━━━━━━━━━\n"
								+ "● §"+color[(int) (Math.random()*9)]+"玩家ID: §"+color[(int) (Math.random()*9)]+map.get("playerID")+"\n"
								+ "● §"+color[(int) (Math.random()*9)]+"充值总金额: §"+color[(int) (Math.random()*9)]+map.get("TotalMoney")+"元\n"
								+ "● §"+color[(int) (Math.random()*9)]+"当前vip等级: VIP§"+color[(int) (Math.random()*9)]+map.get("vipLevle")+"\n"
							    + "● §"+color[(int) (Math.random()*9)]+"距离下一级VIP等级还需充值: §"+color[(int) (Math.random()*9)]+map.get("nextLevle")+"元\n"
							    + "§e§l━━━━━━━━━━━━━━━━━━━━━━━━━");	
				}else {
					sender.sendMessage("§c您还不是VIP");
					
				}
				
			} catch (NullPointerException e) {
				sender.sendMessage("§c您还不是VIP");
			}	
		
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			
			if(command.getName().equalsIgnoreCase("vip")) {
				VIP_CMD(sender,command,label,args);
				return true;
			}
		}
		return false;
	}
}
