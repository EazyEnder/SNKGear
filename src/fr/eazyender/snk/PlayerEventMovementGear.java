package fr.eazyender.snk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class PlayerEventMovementGear implements Listener {
	
	public static int limit_gaz = SNKFileConfig.getLimit_gaz();
	public static int range = SNKFileConfig.getRange();
	public static String language = SNKFileConfig.getLanguage();
	
	public static Map<UUID, Location[]> gear_targets = new HashMap<UUID, Location[]>();
	public static Map<UUID, Boolean> gear_active = new HashMap<UUID, Boolean>();
	
	public static Map<UUID, BossBar> gaz_bar = new HashMap<UUID, BossBar>();
	public static Map<UUID, Double> gaz_player = new HashMap<UUID, Double>();
	public static Map<UUID, Integer> gaz_timer = new HashMap<UUID, Integer>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		mainGear(p);
	}
	
	public void mainGear(Player p) {
		double power = SNKFileConfig.getPower();
		double len = 0.20D;
		if(!gear_active.containsKey(p.getUniqueId())) {gear_active.put(p.getUniqueId(), false);}
		if(!gear_targets.containsKey(p.getUniqueId())) {Location[] d = {null,null};gear_targets.put(p.getUniqueId(), d);}
		if(!gaz_player.containsKey(p.getUniqueId())) {gaz_player.put(p.getUniqueId(), 100.0);}
		if(!gaz_timer.containsKey(p.getUniqueId())) {gaz_timer.put(p.getUniqueId(), 5*10);}
		if(!gaz_bar.containsKey(p.getUniqueId())) {
			BossBar bossBar;
			if(language.contains("FR")) {
			bossBar = Bukkit.createBossBar("Gaz : "+ gaz_player.get(p.getUniqueId()) + "/" + limit_gaz, BarColor.WHITE, BarStyle.SOLID);
			}else {
			bossBar = Bukkit.createBossBar("Fuel : "+ gaz_player.get(p.getUniqueId()) + "/" + limit_gaz, BarColor.WHITE, BarStyle.SOLID);	
			}
			bossBar.addPlayer(p);
			bossBar.setProgress((double)(gaz_player.get(p.getUniqueId())/limit_gaz));
			bossBar.setVisible(true);
			gaz_bar.put(p.getUniqueId(), bossBar);}
		
		new BukkitRunnable() {
			
			Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 5.0F);
			Particle.DustOptions d2 = new Particle.DustOptions(Color.BLACK, SNKFileConfig.getSize_particle());
			@Override
			public void run() {
				
				if(!p.isOnline()) {this.cancel();}
				
				if(gaz_bar.containsKey(p.getUniqueId())) {
					BossBar bossBar = gaz_bar.get(p.getUniqueId());
					bossBar.setProgress((double)(gaz_player.get(p.getUniqueId())/limit_gaz));
					if(gear_active.get(p.getUniqueId())) {
						if(language.contains("FR")) {
					bossBar.setTitle("§2Actif§r | Gaz : "+ gaz_player.get(p.getUniqueId()) + "/" + limit_gaz);
						}else {
					bossBar.setTitle("§2Enabled§r | Fuel : "+ gaz_player.get(p.getUniqueId()) + "/" + limit_gaz);		
						}
					}
					else {
						if(language.contains("FR")) {bossBar.setTitle("§cInactif§r | Gaz : "+ gaz_player.get(p.getUniqueId()) + "/" + limit_gaz);}
						else{bossBar.setTitle("§cDisabled§r | Fuel : "+ gaz_player.get(p.getUniqueId()) + "/" + limit_gaz);}}
					if(gaz_player.get(p.getUniqueId()) / limit_gaz > 0.5) {bossBar.setColor(BarColor.WHITE);}
					else if(gaz_player.get(p.getUniqueId()) / limit_gaz > 0.25) {bossBar.setColor(BarColor.YELLOW);}
					else if(gaz_player.get(p.getUniqueId()) / limit_gaz > 0.0) {bossBar.setColor(BarColor.RED);}
				}
				
				if(gear_targets.get(p.getUniqueId())[0]!=null) {
					Vector t1 = gear_targets.get(p.getUniqueId())[0].toVector().clone().subtract(p.getLocation().toVector()).normalize().multiply(power);
					double length = 0.0D;
					for (double k = 0; length < p.getLocation().distance(gear_targets.get(p.getUniqueId())[0]);) {
						Vector v0 = p.getLocation().toVector();
						for (double j = 0; j < k; j+=len) {
						v0.add(t1.clone().multiply(1/power).multiply(len));
						}
						p.getWorld().spawnParticle(Particle.REDSTONE, v0.getX(),v0.getY(), v0.getZ() , 0, 0D, 0D, 0D, d2);
						k += len;
						length += len;
					}
					if(gear_targets.get(p.getUniqueId())[0].distance(p.getLocation()) > range) {
						Location[] gr = gear_targets.get(p.getUniqueId());
						gr[0] = null;
						gear_targets.replace(p.getUniqueId(), gr);
						p.playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 1, 1);
					}
				}
				if(gear_targets.get(p.getUniqueId())[1]!=null) {
					Vector t2 = gear_targets.get(p.getUniqueId())[1].toVector().clone().subtract(p.getLocation().toVector()).normalize().multiply(power);
					double length = 0.0D;
					for (double k = 0; length < p.getLocation().distance(gear_targets.get(p.getUniqueId())[1]);) {
						Vector v0 = p.getLocation().toVector();
						for (double j = 0; j < k; j += len) {
						v0.add(t2.clone().multiply(1/power).multiply(len));
						}
						p.getWorld().spawnParticle(Particle.REDSTONE, v0.getX(),v0.getY(), v0.getZ() , 0, 0D, 0D, 0D, d2);
						k += len;
						length += len;
					}
					if(gear_targets.get(p.getUniqueId())[1].distance(p.getLocation()) > range) {
						Location[] gr = gear_targets.get(p.getUniqueId());
						gr[1] = null;
						gear_targets.replace(p.getUniqueId(), gr);
						p.playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 1, 1);
					}
				}
				
				if(gear_active.get(p.getUniqueId()) && gaz_player.get(p.getUniqueId()) > 0) {
					gaz_timer.replace(p.getUniqueId(), 5*10);
				Vector velocity = p.getVelocity();
				Vector t1 = new Vector(0,0,0),t2 = new Vector(0,0,0);
				if(gear_targets.get(p.getUniqueId())[0]!=null) {
					t1 = gear_targets.get(p.getUniqueId())[0].toVector().clone().subtract(p.getLocation().toVector()).normalize().multiply(power);	
				}if(gear_targets.get(p.getUniqueId())[1]!=null) {
					t2 = gear_targets.get(p.getUniqueId())[1].toVector().clone().subtract(p.getLocation().toVector()).normalize().multiply(power);
				}
				t1.add(t2);
				Vector t3 = t1.clone();
				velocity = velocity.add(t3);
				p.setVelocity(velocity);
				
				if(gear_targets.get(p.getUniqueId())[0]!=null) {gaz_player.replace(p.getUniqueId(), gaz_player.get(p.getUniqueId()) - 0.125);}
				if(gear_targets.get(p.getUniqueId())[1]!=null) {gaz_player.replace(p.getUniqueId(), gaz_player.get(p.getUniqueId()) - 0.125);}
				
				p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() , 0, 0D, 0D, 0D, dustOptions);
				
				if(p.isSprinting() && gear_targets.get(p.getUniqueId())[1]!=null && gear_targets.get(p.getUniqueId())[0]!=null) {
					p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
					double power = 0.5;
					Location target = p.getTargetBlock(null, 10).getLocation();
					Vector acceleration = target.toVector().clone().subtract(p.getLocation().toVector()).normalize().multiply(power);	
					p.setVelocity(velocity.add(acceleration));
					
					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() , 2, 0D, 0D, 0D);
					gaz_player.replace(p.getUniqueId(), gaz_player.get(p.getUniqueId()) - 1);
				}
				if(gaz_player.get(p.getUniqueId()) < 0) {gaz_player.replace(p.getUniqueId(), 0.0);}
				}else {
					if(gaz_timer.get(p.getUniqueId()) > 0) {
					gaz_timer.replace(p.getUniqueId(), gaz_timer.get(p.getUniqueId())-1);
					}else {
						if(gaz_player.get(p.getUniqueId()) < limit_gaz) {
							gaz_player.replace(p.getUniqueId(), gaz_player.get(p.getUniqueId()) + 0.5);
							if(gaz_player.get(p.getUniqueId()) > limit_gaz) {gaz_player.replace(p.getUniqueId(), (double) limit_gaz);}
						}
					}
				}
				
				
			}
			
		}.runTaskTimer(SnkMain.instance, 0, 2);
	}
	
	@EventHandler
	public void onPlayerResetGear(PlayerSwapHandItemsEvent e){
		Player p = e.getPlayer();
		if((e.getMainHandItem() != null && e.getMainHandItem().getType() == SNKFileConfig.getLauncher().getType()) || (e.getOffHandItem() != null && e.getOffHandItem().getType()  == SNKFileConfig.getLauncher().getType())) {
			Location[] nloc = {null,null};
			gear_targets.replace(p.getUniqueId(), nloc);
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 1, 1);
		}
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		  
		  	Player p = e.getPlayer();
		  	if(e.getItem().getType() == SNKFileConfig.getLauncher().getType()) {
		  		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
		  			if(!gear_targets.containsKey(p.getUniqueId())) {Location[] d = {null,null};gear_targets.put(p.getUniqueId(), d);}
		  			Location target = p.getTargetBlock(null, range).getLocation();
		  			if(target.getBlock().getType()!=Material.AIR) {
		  			Location[] l = gear_targets.get(p.getUniqueId());
		  			l[1] = target;
		  			gear_targets.replace(p.getUniqueId(), l);
		  			p.playSound(p.getLocation(), Sound.ITEM_CROSSBOW_SHOOT, 1, 1);
		  			}
		  			
		  		}
		  		else if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
		  			if(!gear_targets.containsKey(p.getUniqueId())) {Location[] d = {null,null};gear_targets.put(p.getUniqueId(), d);}
		  			Location target = p.getTargetBlock(null, range).getLocation();
		  			if(target.getBlock().getType()!=Material.AIR) {
		  			Location[] l = gear_targets.get(p.getUniqueId());
		  			l[0] = target;
		  			gear_targets.replace(p.getUniqueId(), l);
		  			p.playSound(p.getLocation(), Sound.ITEM_CROSSBOW_SHOOT, 1, 1);
		  			}
		  		}
		  	}
	  } 
	
	@EventHandler
    public void onPlayerActiveGear(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if(!gear_targets.containsKey(p.getUniqueId())) {Location[] d = {null,null};gear_targets.put(p.getUniqueId(), d);}
		if(p.getItemInHand().getType() == SNKFileConfig.getLauncher().getType() && !p.isSneaking()) {
		if(gear_active.get(p.getUniqueId())) {
			if(!gear_active.containsKey(p.getUniqueId())) {gear_active.put(p.getUniqueId(), false);}
			gear_active.replace(p.getUniqueId(), false);
			}
		else if(!gear_active.get(p.getUniqueId())) {
			if(!gear_active.containsKey(p.getUniqueId())) {gear_active.put(p.getUniqueId(), false);}
			gear_active.replace(p.getUniqueId(), true);

			}
		}
	}
	


}
