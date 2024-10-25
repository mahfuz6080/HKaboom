package net.mahfuz.hyblixkaboom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mahfuz.hyblixkaboom.Main;


public class CommandKaboom implements CommandExecutor {

	private Main plugin;

	public CommandKaboom(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("kaboom.command.admin")) {
			sender.sendMessage(this.plugin.getPrefix() + "§cYou don't have access to this command!");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			return;
		}

		if (args.length > 0) {
			if (args[0].contains(",")) {
				for (String target : args[0].split(",")) {
					Player player = this.plugin.getServer().getPlayer(target);
					if (player != null) {
						this.plugin.kaboomPlayer(player, (Player) sender, true);
					}
				}
				return true;
			}
			if (args[0].contains("@a")) {
				for (Player player : this.plugin.getServer().getOnlinePlayers()) {
					this.plugin.kaboomPlayer(player, (Player) sender, true);
					if (!this.plugin.getList().contains(player)) {
						this.plugin.getList().add(player);
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				this.plugin.reloadConfig();
				sender.sendMessage(this.plugin.getPrefix() + "§bConfig has been reloaded!");
				return true;
			}
			Player player = this.plugin.getServer().getPlayer(args[0]);
			if (player != null) {
				this.plugin.kaboomPlayer(player, (Player) sender, true);
				if (!this.plugin.getList().contains(player)) {
					this.plugin.getList().add(player);
				}
				return true;
			}
			sender.sendMessage(this.plugin.getPrefix() + "§cPlayer is not online!");
			return true;
		}
		sender.sendMessage(this.plugin.getPrefix() + "§cUsage: /kaboom <player>");
		return true;
	}

}
