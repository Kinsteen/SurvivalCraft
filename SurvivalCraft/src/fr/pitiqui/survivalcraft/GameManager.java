package fr.pitiqui.survivalcraft;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
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
			p.kickPlayer("The game is finished !");
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
		ScoreboardManager.numPlayers--;
		
		for(Player p : Bukkit.getServer().getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 10, 1);
		}
		
		if(ScoreboardManager.numPlayers == 1)
		{
			Player winner = null;
			
			for(Player temp : Bukkit.getServer().getOnlinePlayers())
			{
				if(temp != e.getEntity())
				{
					winner = temp;
				}
			}
				winner.sendMessage(ChatColor.GREEN + "Congrats ! You win ! Good luck for the next game !");
				Firework fw = Bukkit.getWorld("sg").spawn(winner.getLocation(), Firework.class);
				FireworkMeta fwm = fw.getFireworkMeta();
	            Random r = new Random();   
	            
	            //Get the type
	            int rt = r.nextInt(4) + 1;
	            Type type = Type.BALL;       
	            if (rt == 1) type = Type.BALL;
	            if (rt == 2) type = Type.BALL_LARGE;
	            if (rt == 3) type = Type.BURST;
	            if (rt == 4) type = Type.CREEPER;
	            if (rt == 5) type = Type.STAR;
	           
	            //Get our random colours   
	            int r1i = r.nextInt(17) + 1;
	            int r2i = r.nextInt(17) + 1;
	            Color c1 = getColor(r1i);
	            Color c2 = getColor(r2i);
	           
	            //Create our effect with this
	            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
	           
	            //Then apply the effect to the meta
	            fwm.addEffect(effect);
	           
	            //Generate some random power and set it
	            int rp = r.nextInt(2) + 1;
	            fwm.setPower(rp);
	           
	            //Then apply this to our rocket
	            fw.setFireworkMeta(fwm);
	            
	            final Player winnertemp = winner;
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
					
					@Override
					public void run() {
						winnertemp.kickPlayer("Congrats !");
						endGame();
					}
				}, 200L);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		e.getPlayer().kickPlayer("You lose ! Good luck for the next game !");
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
	public void onBlockPlaceEvent(BlockPlaceEvent e)
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
	
	static Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}
}
