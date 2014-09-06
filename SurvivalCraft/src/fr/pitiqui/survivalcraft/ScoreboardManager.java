package fr.pitiqui.survivalcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager implements Listener
{
	static org.bukkit.scoreboard.ScoreboardManager sbmanager;
	static Scoreboard sb;
	static Objective obj;
	static Score s1;
	static Score s2;
	static int id = 0;
	static int numPlayers = Bukkit.getServer().getOnlinePlayers().length;
	
	public ScoreboardManager() {
		// TODO Auto-generated constructor stub
	}

	public static void initScoreboard()
	{
		sbmanager = Bukkit.getScoreboardManager();
		sb = sbmanager.getNewScoreboard();
		obj = sb.registerNewObjective("main", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.BOLD + "SurvivalCraft");
		s1 = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Invincibilit�"));
		s2 = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + Main.numPlayers + "/" + Bukkit.getMaxPlayers() +  " joueurs"));
		s1.setScore(60);
		s2.setScore(-1);
	}
	
	public static void startCountdown()
	{
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run()
			{
				s1.setScore(s1.getScore() - 1);
				
				if(s1.getScore() == 0)
				{
					GameManager.invic = false;
					
					sb.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Invincibilit�"));
					
					Bukkit.getScheduler().cancelTask(id);
				}
			}
		}, 0L, 20L);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		numPlayers++;
		sb.resetScores(s2.getPlayer());
		s2 = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + numPlayers + "/" + Bukkit.getMaxPlayers() +  " joueurs"));
		s2.setScore(-1);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		numPlayers--;
		sb.resetScores(s2.getPlayer());
		s2 = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + numPlayers + "/" + Bukkit.getMaxPlayers() +  " joueurs"));
		s2.setScore(-1);
	}
}
