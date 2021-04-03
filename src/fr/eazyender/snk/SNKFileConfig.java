package fr.eazyender.snk;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class SNKFileConfig {
	
	public static SNKFileConfig sNKFileConfig;
	
	private static double power;
	private static int range, limit_gaz;
	private static float size_particle;
	private static ItemStack launcher;
	
	private static String language;
	
	  private static File file;
	  private static FileConfiguration configFile;
	
	  public SNKFileConfig() {
		  sNKFileConfig = this;
		    registerFile();
		    load();
	  }
	  
	  public void onDisable() {
		  ConfigurationSection s = configFile.getConfigurationSection("SNK_GEAR");
		  s.set("gear_power", power);
		  s.set("gear_range", range);
		  s.set("gear_limit_gaz", limit_gaz);
		  
		  s.set("gear_size_particle", size_particle);
		  s.set("gear_launcher", launcher);
		  
		  s.set("language", language);
		    saveFile();
		  }
	  
	  public static void create() {
		   
		    power = 0.185;
		    range = 80;
		    limit_gaz = 100;
		    size_particle = 0.4F;
		    
		    launcher = new ItemStack(Material.STICK,1);
		    
		    language = "EN";
		    
		    ConfigurationSection s = configFile.createSection("SNK_GEAR");  
		    s.set("gear_power", power);
			s.set("gear_range", range);
			s.set("gear_limit_gaz", limit_gaz);
			
			s.set("gear_size_particle", size_particle);
			s.set("gear_launcher", launcher);
			
			s.set("language", language);
		    
		    saveFile();
		  }
	  
	  
	 private void registerFile() {
		 file = new File(SnkMain.instance.getDataFolder(), "Config.yml");
		 configFile = YamlConfiguration.loadConfiguration(file);
		    saveFile();
		  }
	 
	 private static void saveFile() {
		    try {		    
		    	configFile.save(file);
		    } catch (IOException iOException) {}
		  }
	 
	 public void load() {
		    if (configFile.contains("SNK_GEAR")) {
		      ConfigurationSection s = configFile.getConfigurationSection("SNK_GEAR");
		      	power = s.getDouble("gear_power");
		      	range = s.getInt("gear_range");
		      	limit_gaz = s.getInt("gear_limit_gaz");
		      	size_particle =(float) s.getDouble("gear_size_particle");
		      	launcher = s.getItemStack("gear_launcher");
		      	
		      	if(!s.contains("language")) {
		      		language = "EN";
		      		s.set("language", language);
		      	}else {
		      		language = s.getString("language"); 
		      	}
			    
			
		    } else {
		    	create();
		    }
		  }
	 
	 
	 public static String getLanguage() {
			return language;
		}
	 
	 public static double getPower() {
		return power;
	}

	public static void setPower(double power) {
		SNKFileConfig.power = power;
	}

	public static int getRange() {
		return range;
	}

	public static void setRange(int range) {
		SNKFileConfig.range = range;
	}

	public static int getLimit_gaz() {
		return limit_gaz;
	}

	public static void setLimit_gaz(int limit_gaz) {
		SNKFileConfig.limit_gaz = limit_gaz;
	}

	public static float getSize_particle() {
		return size_particle;
	}

	public static void setSize_particle(float size_particle) {
		SNKFileConfig.size_particle = size_particle;
	}

	public static ItemStack getLauncher() {
		return launcher;
	}

	public static void setLauncher(ItemStack launcher) {
		SNKFileConfig.launcher = launcher;
	}

	public static SNKFileConfig getSNKFileConfig() { return sNKFileConfig;  }


}
