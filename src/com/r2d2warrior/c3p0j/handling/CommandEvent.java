package com.r2d2warrior.c3p0j.handling;

import java.util.List;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.types.GenericChannelUserEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.r2d2warrior.c3p0j.commands.GenericCommand;
import com.r2d2warrior.c3p0j.utils.Utils;

@Getter
public class CommandEvent<T extends PircBotX> extends Event<T> implements GenericMessageEvent<T>, GenericChannelUserEvent<T>
{
	protected Channel channel;
	protected User user;
	protected String message;
	protected String prefix;
	protected String commandName;
	protected String arguments;
	protected List<String> argumentsList;
	protected CommandInfo<GenericCommand> commandInfo;
	
	public CommandEvent(T bot, @Nullable Channel channel, @NonNull User user, @NonNull String message)
	{
		super(bot);
		this.user = user;
		this.channel = channel;
		this.message = message;
		
		List<String> msg = new StrTokenizer(message).getTokenList();
		this.arguments = (msg.size() > 1) ? StringUtils.split(message, " ", 2)[1] : "";
		this.argumentsList = new StrTokenizer(arguments).getTokenList();
		
		if (channel != null)
		{
			this.prefix = msg.get(0).substring(0, 1);
			this.commandName = msg.get(0).substring(1);
		}
		else
		{
			this.prefix = null;
			this.commandName = msg.get(0);
		}
		
		this.commandInfo = bot.getCommandRegistry().getCommandInfo(commandName);
	}

	@Override
	public void respond(String response)
	{
		if (prefix != null)
		{
			if (getBot().getConfiguration().getPrefixes().get(prefix).equals("NOTICE"))
				getUser().send().notice(response);
			else if (getBot().getConfiguration().getPrefixes().get(prefix).equals("MESSAGE"))
				getChannel().send().message(response);
		}
		else
		{
			getUser().send().message(response);
		}
	}
	
	public void respondToUser(String response)
	{
		getUser().send().notice(response);
	}
	
	public boolean hasNoArgs()
	{
		return StringUtils.isBlank(arguments);
	}
	
	public boolean hasChannelArg()
	{
		return !hasNoArgs() && getBot().getConfiguration().getChannelPrefixes().contains(argumentsList.get(0).substring(0, 1));
	}
	
	public String getArgRange(int start)
	{
		return Utils.getRange(argumentsList, start);
	}
	
	public String getArgRange(int start, int end)
	{
		return Utils.getRange(argumentsList, start, end);
	}
}
