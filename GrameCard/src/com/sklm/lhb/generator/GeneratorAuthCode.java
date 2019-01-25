package com.sklm.lhb.generator;

import com.sklm.lhb.Cmd.PluginCommand;
import com.sklm.lhb.server.Server;

public class GeneratorAuthCode {

	//验证码集合
	private final char[] codeArray= {
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
	};
	private String cardType = PluginCommand.cardType;   //卡类型
	private int authCodeLength = 16;
	private String goodsType = PluginCommand.goodsType; //物品类型
	
	public void generateCode(String authCodeTotal) {
		
		for(int j=0;j<Integer.parseInt(authCodeTotal);j++) {
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<authCodeLength-5;i++) {
				int random = (int) (Math.random()*61);
				sb.append(codeArray[random]);
			}
			Server server = new Server();

			String str = PluginCommand.cardType+PluginCommand.goodsType+sb.toString();
			server.insert(PluginCommand.cardType, PluginCommand.goodsType, str,PluginCommand.cmdStr.toString());
			server.givePlayer(str, PluginCommand.cmdStr.toString());
			
			//DBServer.insertData(Integer.parseInt(cardType), Integer.parseInt(goodsType), str);
		    
		    
		}
		
	}
	
	
}
