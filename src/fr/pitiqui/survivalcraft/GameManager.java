package fr.pitiqui.survivalcraft;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class GameManager implements Listener
{
	static boolean invic = true;
	private static int cd;
	static int cdInt = 10;
	Plugin main = Main.getPlugin(Main.class);
	
	public static void startGame()
	{
		ScoreboardManager.startCountdown();
		
		Main.joinable = "pregame";
		

		cd = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run()
			{
				for(final Player p : Bukkit.getOnlinePlayers())
				{
					p.sendMessage(ChatColor.RED + "The game start in " + cdInt + "...");
				}
				
				cdInt--;
				
				if(cdInt == 0) {
					Main.joinable = "ingame";
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(ChatColor.RED + "The game has started !");
						
						p.getInventory().clear();
						p.setGameMode(GameMode.SURVIVAL);
						if(Main.compass) {
							p.getInventory().addItem(new ItemStack(Material.COMPASS));
						}
					}
					
					Bukkit.getScheduler().cancelTask(cd);
				}
			}
		}, 0L, 20L);
	}

	
	public static void endGame(Player winner)
	{
		Main.joinable = "endgame";
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!p.getName().equals(winner.getName())) {
				p.sendMessage(ChatColor.GREEN + "Congrats ! The player " + winner.getName() + " won !");
			}
		}
		
		winner.sendMessage(ChatColor.GREEN + "Congrats ! You win ! Good luck for the next game !");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				Firework fw = Bukkit.getWorld("sg").spawn(winner.getLocation(), Firework.class);
				FireworkMeta fwm = fw.getFireworkMeta();
		        Random r = new Random();   
		        
		        //Get the type
		        int rt = r.nextInt(5) + 1;
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
			}
		}, 0L, 20L);
        
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
		}, (Main.endkicktime) * 20);
	}
	
	public static void forceEndGame() {
		Main.joinable = "endgame";
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage("Congrats ! The game has been forced to shutdown");
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers())
				{
					p.kickPlayer("The game is finished !");
				}
				
				Bukkit.getServer().unloadWorld("sg", true);
				deleteWorld(new File("sg/"));
				
				Bukkit.getServer().unloadWorld("sg_nether", true);
				deleteWorld(new File("sg_nether/"));
				
				Bukkit.getServer().unloadWorld("sg_the_end", true);
				deleteWorld(new File("sg_the_end/"));
				
				if(Main.restart == true)
					Bukkit.getServer().shutdown();
				else
					Bukkit.getServer().reload();
			}
		}, (Main.endkicktime) * 20);
	}
	
	public static void cancelStart()
	{
		Main.joinable = "true";
		
		Bukkit.getScheduler().cancelTask(cd);
		
		for(Player p : Bukkit.getOnlinePlayers())
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
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers())
				{
					p.setScoreboard(ScoreboardManager.sb);
				}
			}
		}, 0L, 5L);
	}
	
	public static void forceQuit(Player sender) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!p.getName().equals(sender.getName())) {
				p.sendMessage(ChatColor.RED + "THE PLAYER " + ChatColor.DARK_RED + sender.getName() + ChatColor.RED + " HAS SURRENDER !");
			}
		}
		
		//sender.teleport(sender.getBedSpawnLocation());
		sender.setGameMode(GameMode.SPECTATOR);
		sender.sendMessage("GG ! You're now in spec mode !");
		ScoreboardManager.numPlayers--;
		ScoreboardManager.resetPlayersScore();
		
		if(ScoreboardManager.numPlayers == 1)
		{
			Player winner = null;
			
			for(Player temp : Bukkit.getOnlinePlayers())
			{
				if(!temp.isDead() && temp.getGameMode().equals(GameMode.SURVIVAL))
				{
					winner = temp;
					GameManager.endGame(winner);
				}
			}
		}
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
		
		if(Main.joinable != "ingame" && e.getCause() == DamageCause.ENTITY_ATTACK)
			e.setCancelled(true);
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
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 10, 1);
		}
		if(ScoreboardManager.numPlayers != 1) {
			e.setDeathMessage(ChatColor.DARK_RED + e.getEntity().getName().toUpperCase() + ChatColor.RED +" IS DEAD ! ONLY " + ChatColor.DARK_RED + ScoreboardManager.numPlayers + ChatColor.RED + " PLAYERS REMAINING ! ");
		} else {
			e.setDeathMessage("");
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		if(Main.joinable == "ingame" || Main.joinable == "endgame") {
			e.getPlayer().setGameMode(GameMode.SPECTATOR);
			e.getPlayer().sendMessage("Congratulations ! You're now in spectator mode");
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			    @Override
			    public void run() {
					World sg = Bukkit.getServer().getWorld("sg");
					e.getPlayer().teleport(sg.getSpawnLocation());
			    }
			}, 10L);
		}
		
		if(Main.joinable == "true") {
			e.getPlayer().setGameMode(GameMode.SURVIVAL);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			    @Override
			    public void run() {
					World sg = Bukkit.getServer().getWorld("sg");
					e.getPlayer().teleport(sg.getSpawnLocation());
			    }
			}, 10L);
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
					e.getPlayer().sendMessage("No players has been found !");
				}
			}
	}
	
	@EventHandler //Teleport player to world nether
	public void onPlayerPortal(PlayerPortalEvent e) {
		TravelAgent ta = e.getPortalTravelAgent();
		Player caller = e.getPlayer();
		boolean inow = caller.getWorld().getEnvironment().equals(World.Environment.NORMAL);
		boolean innether = caller.getWorld().getEnvironment().equals(World.Environment.NETHER);
		if (inow && e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
		Location netherloc = caller.getLocation().clone();
		netherloc.setWorld(Bukkit.getWorld("sg_nether"));
		netherloc.multiply(1d / 8d);
		ta.setSearchRadius(5);
		caller.teleport(ta.findOrCreate(netherloc));
		}
		if (innether && e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
		Location owloc = caller.getLocation().clone();
		owloc.setWorld(Bukkit.getWorld("sg"));
		owloc.multiply(8d);
		caller.teleport(ta.findOrCreate(owloc));
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
