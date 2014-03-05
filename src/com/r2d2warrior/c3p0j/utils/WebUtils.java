package com.r2d2warrior.c3p0j.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class WebUtils
{
	public static List<Map<String, String>> getUrbanDictionDefinitions(String searchTerm)
	{
		try
		{
			searchTerm = searchTerm.replace(' ', '+');
			HttpURLConnection conn = (HttpURLConnection) new URL("http://api.urbandictionary.com/v0/define?term=" + searchTerm).openConnection();
			
			Scanner in = new Scanner(conn.getInputStream());
			
			String json = "";
			while (in.hasNext())
				json += in.nextLine() + "\n";
			
			JSONParser parser = new JSONParser();
			Map<?, ?> jMap = ((Map<?, ?>)parser.parse(json));
			
			@SuppressWarnings("unchecked")
			List<Map<String, String>> definitionMapList = (List<Map<String, String>>)jMap.get("list");
			
			in.close();
			return definitionMapList;
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Map<String, String> getLocationData(String ip) throws IOException, ParseException
	{
		String address = "http://geo.liamstanley.io/json/" + ip;
		HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
		JSONObject data =
				(JSONObject)new JSONParser().parse(new InputStreamReader(conn.getInputStream()));
		
		Map<String, String> results = new HashMap<>();
		for (Object o : data.keySet())
		{
			String key = o.toString();
			String val = data.get(key).toString();
			if (val.equals("0") || StringUtils.isBlank(val))
				results.put(key, "N/A");
			else
				results.put(key, Utils.toTitleCase(val));
		}
		return results;
	}
	
	public static String getRandomFML()
	{
		String address = "http://www.fmylife.com/random";
		
		try
		{
			Element element = Jsoup.connect(address).get().select("li[id]").first().select("p").first();
			String fml = StringEscapeUtils.unescapeHtml4(element.html()) + ".";
			return fml;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}