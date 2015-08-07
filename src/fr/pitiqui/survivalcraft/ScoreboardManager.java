package fr.pitiqui.survivalcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	static int numPlayers = Bukkit.getServer().getOnlinePlayers().size();
	
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
		s1 = obj.getScore(ChatColor.GREEN + "Invincibility");
		s2 = obj.getScore(ChatColor.GREEN + "" + Main.numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s1.setScore(Main.invictime);
		s2.setScore(-1);
	}
	
	public static void startCountdown()
	{
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run()
			{
				s1.setScore(s1.getScore() -1);
				
				if(s1.getScore() == 0)
				{
					GameManager.invic = false;
					
					sb.resetScores(ChatColor.GREEN + "Invincibility");
					
					Bukkit.getScheduler().cancelTask(id);
				}
			}
		}, 220L, 20L);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		sb.resetScores(ChatColor.GREEN + "" + Main.numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s2 = obj.getScore(ChatColor.GREEN + "" + numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s2.setScore(-1);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		numPlayers++;
		sb.resetScores(ChatColor.GREEN + "" + Main.numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s2 = obj.getScore(ChatColor.GREEN + "" + numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s2.setScore(-1);
	}
}
