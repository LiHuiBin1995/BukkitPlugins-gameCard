package com.sklm.lhb.Cmd;

import java.util.Date;
import java.util.Map;

import javax.print.attribute.standard.Severity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sklm.lhb.Plugin.GameCode;
import com.sklm.lhb.generator.GeneratorAuthCode;
import com.sklm.lhb.server.Server;

public class PluginCommand implements CommandExecutor {

	public static String cardType = null;   //卡类型
	public static String goodsType = null;  //商品类型
	public static String authCodeTotal = null;  //生成验证码的数量
	public static String playerName = null;
	private String playerCode = null; //玩家购买的验证码
	private static int checkCount = 0;    //玩家输入验证码的次数
	public static String playerCardType = null; //玩家购买的卡的类型
	public static String playerGoodsType = null;//玩家购买的商品类型
	public static boolean isAgainGenerate = false;
	public static String cmdStr="";  //执行代码
	
	
	public PluginCommand() {
		
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.isOp()) {
				
				if(args.length>1 && command.getName().equalsIgnoreCase("cardcode")) {
					cardType = args[0];   //获取卡类型
					String str_tmp ="";
					for(int i=3;i<args.length;i++) {
						
						str_tmp = str_tmp+args[i]+" ";
					}
					cmdStr = str_tmp.substring(0, str_tmp.length()-1);
					//获取物品类型
					if((Integer.parseInt(args[1])>=0)&& (Integer.parseInt(args[1])<10)){
						goodsType = "00"+args[1];
					}
					if(((Integer.parseInt(args[1])>=10)&&(Integer.parseInt(args[1])<100))){
						goodsType = "0" + args[1];
					} 
					//获取验证码个数
					authCodeTotal = args[2];
					new GeneratorAuthCode().generateCode(authCodeTotal);
					sender.sendMessage("§7"+authCodeTotal+"条验证码生成完毕");
					return true;
				}
				if(args.length == 1) {
					checkCode((Player)sender,args);
				}
				return true;
			}
			if(command.getName().equalsIgnoreCase("cardcode")) {
				if((args.length == 1)) {
					
					Date date = new Date();
					GameCode.time = date.getMinutes();
					Bukkit.getLogger().info("玩家输入的次数是：" + PluginCommand.checkCount);
					PluginCommand.checkCount++;
					playerName = player.getName();
					if(((date.getMinutes()-GameCode.time)<=1)&&(PluginCommand.checkCount > 3)&&(!(player.isOp()))) {
			            //玩家掉线处理
						PluginCommand.checkCount = 0;
						player.kickPlayer("输入验证信息超过2次");
						return true;
					}
					checkCode(player,args);
					return true;		
						
				}else {
					sender.sendMessage(ChatColor.YELLOW+"请使用命令：/cardcode [验证码]");
					return false;
				}	
			}	
		}

		return true;
    }
	
	public boolean checkCode(Player player, String[] args) {
		String cmdgive="";
		playerCode = args[0];  //保存玩家输入的验证码
		playerCardType = splitMoney(playerCode);
		playerGoodsType = playerCode.substring(2,5); //分离出玩家的物品类型
		boolean b_ = Server.delete(playerCode);
		if(b_) {
			PluginCommand.isAgainGenerate = true;
			cmdgive = Server.getCommand(playerCode);
			
			if(cmdgive.contains("@")) {
				cmdgive = cmdgive.replaceAll("@a", player.getName());
			}
			if(!(player.isOp())) {
				player.setOp(true);
				player.performCommand(cmdgive);
				player.setOp(false);
			}
			if(player.isOp()) {
				player.performCommand(cmdgive);
			}
			boolean b = Server.deleteCode(playerCode);
			if(b){
			    player.sendMessage(ChatColor.YELLOW+"验证码输入成功");
			    String name = player.getName();
			    boolean hasplayer = Server.findPlayer(name);
			    if(hasplayer) {
			    	double money = Double.parseDouble(playerCardType);
			    	
			    	/************************调试****************************/
				    int vip_levle = Server.getVIP_levle(name);
				    double next_levle = Server.getDis_next_levle(name);
				    double Current_vipExp = Server.getVip_levleInVIPLevle(vip_levle);
				    double next_vipExp = Server.getVip_levleInVIPLevle(vip_levle+1);
				    System.out.println("\n name = "+name+"\n"
				    		+ "money = "+money+"\n"
				    	    + "vip_levle = "+vip_levle+"\n"
				    	    + "next_levle = "+next_levle+"\n"
				    	    + "Current_vipExp = "+Current_vipExp+"\n"
				    	    + "next_vipExp = "+next_vipExp);
				    /****************************************************/
				    
				    double preMoney_ = Server.getPay_money_total(name);
				    double moneytotal = money + preMoney_;
				    int size = GameCode.vipInfoMap.size();
				    int vip_levle_ = 0;
				    double next_viplevle_Exp;
				    for(int i=0;i<size;i++) {
				    	double vip_Exp = (double) GameCode.vipInfoMap.get(i);
				    	if(moneytotal>= vip_Exp) {
				    		continue;
				    	}else {
				    		vip_levle_ = i-1;
				    		break;
				    	}
				    }
				    next_viplevle_Exp = (double) GameCode.vipInfoMap.get(vip_levle_+1);
				    Server.saveVipInfo(name, moneytotal, vip_levle_, next_viplevle_Exp-moneytotal);
				  
			    }else {
			    	double player_cardType =  Double.parseDouble(playerCardType);
			    	int size = GameCode.vipInfoMap.size();
			    	int vip_levle = 0;
			    	for(int i=0;i<size;i++) {
			    		double vip_Exp = (double) GameCode.vipInfoMap.get(i);
			    		if(player_cardType>=vip_Exp) {
			    			vip_levle = i;
			    		}else {
			    			break;
			    		}
			    	}
			    	
			    	double Newnext_vipExp = Server.getVip_levleInVIPLevle(vip_levle+1);
			    	Server.insertVipInfo(name, player_cardType, vip_levle, Newnext_vipExp);
			    }
			    
			    return true;
			}

		}else {
			player.sendMessage(ChatColor.RED+"卡密错误,请重新输入(注意大小写)");
			return false;
		}
		return true;
	}
	
	/**
	 * 从玩家输入的验证码中分离出充值金额
	 * @param code 玩家输入的验证码
	 * @return 返回玩家充值的金额；String类型
	 */
	public String splitMoney(String code) {
		String money = null;
		
		int code_length = code.length();
		switch(code_length) {
		case 15:
			money = code.substring(0, 1);
			break;
		case 16:
			money = code.substring(0, 2);
			break;
		case 17:
			money = code.substring(0,3);
			break;
		case 18:
			money = code.substring(0, 4);
			break;
		}
		
		return money;
	}
	
}
