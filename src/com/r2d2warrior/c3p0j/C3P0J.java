package com.r2d2warrior.c3p0j;

import java.io.File;
import java.nio.charset.Charset;

import org.pircbotx.PircBotX;
import org.pircbotx.Configuration;
import org.pircbotx.cap.EnableCapHandler;

import com.google.common.io.Files;
import com.r2d2warrior.c3p0j.listeners.CommandListener;
import com.r2d2warrior.c3p0j.listeners.InviteJoin;

public class C3P0J
{

	public static void main(String[] args) throws Exception
	{
		
		// TODO config file?
		String password = Files.toString(new File("pswrd.txt"), Charset.defaultCharset());
		
 		Configuration<PircBotX> config = new Configuration.Builder<PircBotX>()
 				
 				//Login info
			.setName("C3P0J")
			.setLogin("R2D2")
			.setRealName("C3P0J (PircBotX) - by R2D2Warrior")
			
				//Booleans
			.setAutoNickChange(true)
			.setCapEnabled(true)
			
			.addCapHandler(new EnableCapHandler("extended-join", true))
			.addCapHandler(new EnableCapHandler("account-notify", true))
			
				//Listeners
			.addListener(new CommandListener())
			.addListener(new InviteJoin())
			
				//Command management
			.addAdminAccounts("R2D2Warrior", "CHCMATT", "Vgr255")
			.addPrefix(".", "MESSAGE")
			.addPrefix("@", "NOTICE")
			
				//Server info
			.setServerHostname("irc.esper.net")
			.setChannelPrefixes("#")
			
			.addAutoJoinChannel("#C3P0")
			
			.addBlockedChannels("#help", "#lobby")
			
			.setNickservPassword(password)
			
			.buildConfiguration();
 		
        PircBotX bot = new PircBotX(config);
                
        try
        {
        	bot.startBot();
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
	}
}