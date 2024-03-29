package com.r2d2warrior.c3p0j.commands;

import org.pircbotx.PircBotX;

import bsh.EvalError;
import bsh.Interpreter;

import com.r2d2warrior.c3p0j.handling.CommandEvent;
import com.r2d2warrior.c3p0j.utils.Utils;

@Command(name="exec", desc="Execute a method within PircBotX", syntax="exec <code>", requiresArgs=true, minGroup="admin")
public class Exec extends GenericCommand
{
	
	public Exec(CommandEvent<PircBotX> event)
	{
		super(event);
	}
	
	public void execute()
	{	
		String exec = event.getArguments();

		try
		{
				Interpreter i = Utils.createDefaultInterpreter(event);
				event.respondToUser("Trying to run \"" + exec + "\"");
				i.eval(exec);
			
		}
		catch (EvalError e)
		{
			e.printStackTrace();
			event.respondToUser("Error while running \"" + exec + "\"");
		}
	}
}
