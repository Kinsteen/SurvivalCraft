package fr.pitiqui.survivalcraft;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class GameManager implements Listener
{
	static boolean invic = true;
	static int id = 0;
	static int id10= 0;
	static int id9 = 0;
	static int id8 = 0;
	static int id7 = 0;
	static int id6 = 0;
	static int id5 = 0;
	static int id4 = 0;
	static int id3 = 0;
	static int id2 = 0;
	static int id1 = 0;
	static int id0 = 0;
	Plugin main = Main.getPlugin(Main.class);
	
	public static void startGame()
	{
		ScoreboardManager.startCountdown();
		
		Main.joinable = "pregame";
		
		for(final Player p : Bukkit.getServer().getOnlinePlayers())
		{
			id10 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 10...");
				}
			}, 20L);
			id9 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 9...");
				}
			}, 40L);
			id8 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 8...");
				}
			}, 60L);
			id7 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 7...");
				}
			}, 80L);
			id6 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 6...");
				}
			}, 100L);
			id5 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 5...");
				}
			}, 120L);
			id4 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 4...");
				}
			}, 140L);
			id3 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 3...");
				}
			}, 160L);
			id2 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 2...");
				}
			}, 180L);
			id1 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game start in 1...");
				}
			}, 200L);
			id0 = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.RED + "The game has started !");
					Main.joinable = "ingame";
					p.getInventory().addItem(new ItemStack(Material.COMPASS));
				}
			}, 220L);
		}
	}
	
	public static void endGame(Player winner)
	{
		Main.joinable = "endgame";
		
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
		}, 400L);
	}
	
	public static void cancelStart()
	{
		Main.joinable = "true";
		
		Bukkit.getScheduler().cancelTask(id10);
		Bukkit.getScheduler().cancelTask(id9);
		Bukkit.getScheduler().cancelTask(id8);
		Bukkit.getScheduler().cancelTask(id7);
		Bukkit.getScheduler().cancelTask(id6);
		Bukkit.getScheduler().cancelTask(id5);
		Bukkit.getScheduler().cancelTask(id4);
		Bukkit.getScheduler().cancelTask(id3);
		Bukkit.getScheduler().cancelTask(id2);
		Bukkit.getScheduler().cancelTask(id1);
		Bukkit.getScheduler().cancelTask(id0);
		Bukkit.getScheduler().cancelTask(ScoreboardManager.id);
		
		for(Player p : Bukkit.getServer().getOnlinePlayers())
		{
			p.sendMessage(ChatColor.DARK_RED + "START CANCELED !");
		}
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
		e.setDeathMessage(ChatColor.DARK_RED + e.getEntity().getName().toUpperCase() + ChatColor.RED +" IS DEAD ! ONLY " + ChatColor.DARK_RED + ScoreboardManager.numPlayers + ChatColor.RED + " PLAYERS REMAINING ! ");
		
		if(ScoreboardManager.numPlayers == 1)
		{
			Player winner = null;
			
			for(Player temp : Bukkit.getServer().getOnlinePlayers())
			{
				if(temp != e.getEntity())
				{
					winner = temp;
					endGame(winner);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		if(Main.joinable == "ingame" || Main.joinable == "endgame")
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
	
	@EventHandler
	public void onItemRightClick(PlayerInteractEvent e)
	{
			if(e.getPlayer().getItemInHand().getType() == Material.COMPASS && Main.joinable == "ingame" && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
			{
				Player nearest = null;
				Double distance = 9999999.0D;
				for (Player pl2 : Bukkit.getWorld("sg").getPlayers())
				{
					Double calc = e.getPlayer().getLocation().distance(pl2.getLocation());
					if (calc > 1 && calc < distance && pl2 != e.getPlayer())
					{
						distance = calc;
						nearest = pl2;
						System.out.println("Distance -- !");
					}
				}
				
				if(nearest != null)
				{
					e.getPlayer().sendMessage(ChatColor.GREEN + "Your compass now points to " + ChatColor.DARK_GREEN + nearest.getName());
					e.getPlayer().setCompassTarget(nearest.getLocation());
				}
				else
				{
					e.getPlayer().sendMessage("Nope.");
				}
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
