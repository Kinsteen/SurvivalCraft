package fr.pitiqui.survivalcraft;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class GameManager implements Listener
{
	static boolean invic = true;
	static int id = 0;
	Plugin main = Main.getPlugin(Main.class);
	
	public static void startGame()
	{
		Main.joinable = "ingame";
		
		ScoreboardManager.startCountdown();
	}
	
	public static void endGame()
	{
		Main.joinable = "endgame";
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.kickPlayer("Le jeu est terminé !");
		}
		
		Bukkit.getServer().unloadWorld("sg", true);
		deleteWorld(new File("sg/"));
		
		if(Main.restart == true)
			Bukkit.getServer().shutdown();
		else
			Bukkit.getServer().reload();
	}
	
	public static boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	
	public static void startCountdown()
	{
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers())
				{
					p.setScoreboard(ScoreboardManager.sb);
				}
			}
		}, 0L, 5L);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER)
		{
			if(invic)
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerFoodChange(FoodLevelChangeEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER)
		{
			if(invic)
			{
				e.setFoodLevel(20);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDead(PlayerDeathEvent e)
	{
		e.getEntity().kickPlayer("Vous avez perdu ! Bonne chance pour la prochaine partie !");
		for(Player p : Bukkit.getServer().getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10, 1);
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e)
	{
		if(Main.joinable != "ingame")
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent ev) {
		Location l = ev.getTo();
		Integer mapSize = Main.mapsize;
		Integer halfMapSize = (int) Math.floor(mapSize/2);
		Integer x = l.getBlockX();
		Integer z = l.getBlockZ();
		
		Location spawn = ev.getPlayer().getWorld().getSpawnLocation();
		Integer limitXInf = spawn.add(-halfMapSize, 0, 0).getBlockX();
		
		spawn = ev.getPlayer().getWorld().getSpawnLocation();
		Integer limitXSup = spawn.add(halfMapSize, 0, 0).getBlockX();
		
		spawn = ev.getPlayer().getWorld().getSpawnLocation();
		Integer limitZInf = spawn.add(0, 0, -halfMapSize).getBlockZ();
		
		spawn = ev.getPlayer().getWorld().getSpawnLocation();
		Integer limitZSup = spawn.add(0, 0, halfMapSize).getBlockZ();
		
		if (x < limitXInf || x > limitXSup || z < limitZInf || z > limitZSup) {
			ev.setCancelled(true);
		}
	}
}
