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
					e.getPlayer().kickPlayer("Wait before the server restart !");
					return;
				}
				
				if(Main.joinable == "endgame")
				{
					e.getPlayer().kickPlayer("The game has been finished !");
					return;
				}
				
				if(Main.joinable == "ingame")
				{
					e.getPlayer().kickPlayer("A game is in progress !");
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
		
		if(ScoreboardManager.numPlayers == 1 && Main.joinable == "ingame")
		{
			Player winner = null;
			
			for(Player temp : Bukkit.getServer().getOnlinePlayers())
			{
				if(temp != e.getPlayer())
				{
					winner = temp;
				}
			}
				
			GameManager.endGame(winner);
		}
		
		if(ScoreboardManager.numPlayers == 1 && Main.joinable == "pregame")
		{
			System.out.println("START CANCELED !!!");
			Main.joinable = "true";
			GameManager.cancelStart();
		}
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e)
	{
		if(Main.joinable == "startup")
		{
			e.setMotd("The server restart !");
		}
		
		else if(Main.joinable == "endgame")
		{
			e.setMotd("The game finished !");
		}
		else if(Main.joinable == "ingame")
		{
			e.setMotd("A game is in progress !");
		}
		
		else if(Main.joinable == "true")
		{
		}
	}
}
