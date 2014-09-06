package fr.pitiqui.survivalcraft;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{	
	static String joinable = "startup";
	static int numPlayers = 0;
	
	static boolean restart;
	static int mapsize;
	
	public void onEnable()
	{
		setupConfig();
		
		numPlayers = Bukkit.getOnlinePlayers().length;
		
		ScoreboardManager.initScoreboard();
		
		GameManager.startCountdown();
		
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new PreEventManager(), this);
		Bukkit.getPluginManager().registerEvents(new ScoreboardManager(), this);
		Bukkit.getPluginManager().registerEvents(new GameManager(), this);
		
		WorldCreator wc = new WorldCreator("sg");
		wc.type(WorldType.NORMAL);
		wc.createWorld();
		
		Bukkit.getWorld("sg").setSpawnLocation(0, 0, 0);
		
		createBorders();
		
		joinable = "true";
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.setScoreboard(ScoreboardManager.sb);
		}
	}
	
	public void onDisable()
	{
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("sc"))
		{
			if(args.length == 0)
			{
				sender.sendMessage(ChatColor.GREEN + "SurvivalCraft v0.1, by pitiqui");
				
				return true;
			}
			
			else
			{
				if(args.length == 1)
				{
					if(args[0].equalsIgnoreCase("start"))
					{
						GameManager.startGame();
					}
					else if(args[0].equalsIgnoreCase("end"))
					{
						GameManager.endGame();
					}
				}
			}
		}
		
		return true;
	}
	
	public boolean createBorders()
	{
		try {
			Integer halfMapSize = (int) Math.floor(mapsize/2);
			Integer wallHeight = 255;
			Material wallBlock = Material.BEDROCK;
			World w = Bukkit.getWorld("sg");
			
			Location spawn = w.getSpawnLocation();
			Integer limitXInf = spawn.add(-halfMapSize, 0, 0).getBlockX();
			
			spawn = w.getSpawnLocation();
			Integer limitXSup = spawn.add(halfMapSize, 0, 0).getBlockX();
			
			spawn = w.getSpawnLocation();
			Integer limitZInf = spawn.add(0, 0, -halfMapSize).getBlockZ();
			
			spawn = w.getSpawnLocation();
			Integer limitZSup = spawn.add(0, 0, halfMapSize).getBlockZ();
			
			for (Integer x = limitXInf; x <= limitXSup; x++) {
				w.getBlockAt(x, 1, limitZInf).setType(Material.BEDROCK);
				w.getBlockAt(x, 1, limitZSup).setType(Material.BEDROCK);
				for (Integer y = 2; y <= wallHeight; y++) {
					w.getBlockAt(x, y, limitZInf).setType(wallBlock);
					w.getBlockAt(x, y, limitZSup).setType(wallBlock);
				}
			} 
			
			for (Integer z = limitZInf; z <= limitZSup; z++) {
				w.getBlockAt(limitXInf, 1, z).setType(Material.BEDROCK);
				w.getBlockAt(limitXSup, 1, z).setType(Material.BEDROCK);
				for (Integer y = 2; y <= wallHeight; y++) {
					w.getBlockAt(limitXInf, y, z).setType(wallBlock);
					w.getBlockAt(limitXSup, y, z).setType(wallBlock);
				}
			}
			
			System.out.println("Borders gen finished");
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	public void setupConfig()
	{
		this.getConfig().addDefault("restart", true);
		this.getConfig().addDefault("mapsize", 1000);
		this.saveDefaultConfig();
		restart = getConfig().getBoolean("restart");
		mapsize = getConfig().getInt("mapsize");
	}
}
