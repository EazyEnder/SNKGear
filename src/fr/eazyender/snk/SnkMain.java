package fr.eazyender.snk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SnkMain extends JavaPlugin {
	
	public static SnkMain instance;
	
	@Override
	public void onEnable() 
	{
		instance = this;
		PluginManager pm = getServer().getPluginManager();
		
		/** File reading and saving */
		SNKFileConfig file_pls = new SNKFileConfig();
		
		PlayerEventMovementGear movementgear = new PlayerEventMovementGear();
		pm.registerEvents(movementgear, this);
		
		for (Player p : Bukkit.getOnlinePlayers()) {

			movementgear.mainGear(p);
			
		}
	}
	
	@Override
	public void onDisable() 
	{
		SNKFileConfig.getSNKFileConfig().onDisable();
	}

}
