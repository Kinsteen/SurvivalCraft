package fr.pitiqui.survivalcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import me.confuser.barapi.BarAPI;

public class PreEventManager implements Listener
{
	@EventHandler
	public void onJoin(final PlayerJoinEvent e)
	{
		e.setJoinMessage(ChatColor.YELLOW + e.getPlayer().getDisplayName() + " has joined the game ! (" + ChatColor.AQUA + (ScoreboardManager.numPlayers + 1) + ChatColor.YELLOW + "/" + ChatColor.AQUA + Bukkit.getMaxPlayers() + ChatColor.YELLOW + ")");
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
		    @Override
		    public void run() {
				World sg = Bukkit.getServer().getWorld("sg");
				e.getPlayer().teleport(sg.getSpawnLocation());
				
		    	//TitleManager.sendTitle(e.getPlayer(), ChatColor.GREEN + "Welcome !", ChatColor.AQUA + "The game will start soon", 20);
		    	//TitleManager.setPlayerList(e.getPlayer(), "SurvivalCraft", "Good luck !");
		    	
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
					//e.getPlayer().kickPlayer("A game is in progress !");
					e.getPlayer().setScoreboard(ScoreboardManager.sb);
					
					e.getPlayer().getInventory().clear();
					e.getPlayer().getInventory().setHelmet(new ItemStack (Material.AIR));
					e.getPlayer().getInventory().setChestplate(new ItemStack (Material.AIR));
					e.getPlayer().getInventory().setLeggings(new ItemStack (Material.AIR));
					e.getPlayer().getInventory().setBoots(new ItemStack (Material.AIR));

					e.getPlayer().teleport(sg.getSpawnLocation());
					e.getPlayer().setGameMode(GameMode.SPECTATOR);
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You can't join the game, so you're in spectator mode.");
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You can tp to other players with /sg tp <player>");
					return;
				}

				e.getPlayer().setGameMode(GameMode.SURVIVAL);
				e.getPlayer().setScoreboard(ScoreboardManager.sb);
				
				e.getPlayer().getInventory().clear();
				e.getPlayer().getInventory().setHelmet(new ItemStack (Material.AIR));
				e.getPlayer().getInventory().setChestplate(new ItemStack (Material.AIR));
				e.getPlayer().getInventory().setLeggings(new ItemStack (Material.AIR));
				e.getPlayer().getInventory().setBoots(new ItemStack (Material.AIR));
		    }
		}, 10L);
		
		if(Bukkit.getServer().getMaxPlayers() == Bukkit.getServer().getOnlinePlayers().size())
		{
			GameManager.startGame();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		if(ScoreboardManager.numPlayers == 1 && Main.joinable == "ingame")
		{
			Player winner = null;
			
			for(Player temp : Bukkit.getServer().getOnlinePlayers())
			{
				if(!temp.isDead() && temp.getGameMode().equals(GameMode.SURVIVAL))
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
			e.setMotd("The game has been finished !");
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
