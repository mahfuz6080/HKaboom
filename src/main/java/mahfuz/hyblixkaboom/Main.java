package net.mahfuz.hyblixkaboom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.mahfuz.hyblixkaboom.commands.CommandKaboom;


public class Main extends JavaPlugin implements Listener {

	@Getter
	private List<Player> list = new ArrayList<>();

	@Override
	public void onEnable() {
		this.saveDefaultConfig();

		this.getCommand("kaboom").setExecutor(new CommandKaboom(this));
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (this.list.contains(player)) {
				event.setCancelled(true);
				this.list.remove(player);
			}
		}
	}

	public void kaboomPlayer(Player player, Player sender, boolean allowLightning) {
		player.setVelocity(new Vector(0.0D, 64.0D, 0.0D));
		player.sendTitle(this.getConfig("Title"), this.getConfig("Sub-Title").replace("%%player%%", sender.getName()));

		if (allowLightning) {
			player.getWorld().strikeLightningEffect(player.getLocation());
		}
	}

	public String getConfig(String path) {
		return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path));
	}

	public String getPrefix() {
		return this.getConfig("Prefix");
	}

}
