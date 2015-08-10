package fr.pitiqui.survivalcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
	static Score s3;
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
		s2 = obj.getScore("");
		s3 = obj.getScore(ChatColor.GREEN + "" + numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s1.setScore(Main.invictime);
		s2.setScore(-1);
		s3.setScore(-2);
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
					
					sb.resetScores(s1.getEntry());
					sb.resetScores(s2.getEntry());
					
					Bukkit.getScheduler().cancelTask(id);
				}
			}
		}, 220L, 20L);
	}
	
	public static void resetPlayersScore() {
		sb.resetScores(s3.getEntry());
		s3 = obj.getScore(ChatColor.GREEN + "" + numPlayers + "/" + Bukkit.getMaxPlayers() +  " players");
		s3.setScore(-1);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		if(!e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
			numPlayers--;
			resetPlayersScore();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(!e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
			numPlayers++;
			resetPlayersScore();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		numPlayers--;
		
		resetPlayersScore();
		
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
}
