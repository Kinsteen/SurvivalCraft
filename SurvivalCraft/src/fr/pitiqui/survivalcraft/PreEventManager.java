package fr.pitiqui.survivalcraft;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class PreEventManager implements Listener
{
	@EventHandler
	public void onJoin(final PlayerJoinEvent e)
	{	
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
		    @Override
		    public void run() {
				if(Main.joinable == "startup")
				{
					e.getPlayer().kickPlayer("Attendez un peu que le serveur redémarre ;)");
					return;
				}
				
				if(Main.joinable == "endgame")
				{
					e.getPlayer().kickPlayer("La partie vient de terminer ! Il redémarre dans quelques secondes...");
					return;
				}
				
				if(Main.joinable == "ingame")
				{
					e.getPlayer().kickPlayer("Une partie est en cours !");
					return;
				}
				
				e.getPlayer().setScoreboard(ScoreboardManager.sb);
		    	
				World sg = Bukkit.getServer().getWorld("sg");
				
				e.getPlayer().getInventory().clear();
				e.getPlayer().getInventory().setHelmet(new ItemStack (Material.AIR));
				e.getPlayer().getInventory().setChestplate(new ItemStack (Material.AIR));
				e.getPlayer().getInventory().setLeggings(new ItemStack (Material.AIR));
				e.getPlayer().getInventory().setBoots(new ItemStack (Material.AIR));
				
				e.getPlayer().teleport(sg.getSpawnLocation().add(0, 200, 0));
		    }
		}, 10L);
		
		if(Bukkit.getServer().getMaxPlayers() == Bukkit.getServer().getOnlinePlayers().length)
		{
			GameManager.startGame();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		ScoreboardManager.numPlayers--;
		
		if(ScoreboardManager.numPlayers == 1)
		{
			Player winner = null;
			
			for(Player temp : Bukkit.getServer().getOnlinePlayers())
			{
				if(temp != e.getPlayer())
				{
					winner = temp;
				}
			}
				winner.sendMessage(ChatColor.GREEN + "Bravo ! Vous avez gagné ! Bonne chance pour la prochaine partie !");
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
	            Color c1 = GameManager.getColor(r1i);
	            Color c2 = GameManager.getColor(r2i);
	           
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
						winnertemp.kickPlayer("Bravo !");
						GameManager.endGame();
					}
				}, 200L);
		}
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e)
	{
		if(Main.joinable == "startup")
		{
			e.setMotd("Le serveur démarre !");
		}
		
		else if(Main.joinable == "endgame")
		{
			e.setMotd("La partie vient de terminer ! (chanceux)");
		}
		else if(Main.joinable == "ingame")
		{
			e.setMotd("La partie est en cours !");
		}
		
		else if(Main.joinable == "true")
		{
			e.setMotd("Venez vite ! Il reste " + (e.getMaxPlayers() - e.getNumPlayers()) + " place !");
		}
	}
}
