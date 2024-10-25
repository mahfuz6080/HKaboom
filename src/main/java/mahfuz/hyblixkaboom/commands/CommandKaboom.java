/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.simplyrin.simplekaboom.commands;

import net.simplyrin.simplekaboom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKaboom
implements CommandExecutor {
    private Main plugin;

    public CommandKaboom(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simplekaboom.command.admin")) {
            sender.sendMessage(this.plugin.getPrefix() + "\u00a7cYou don't have access to this command!");
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length > 0) {
            if (args[0].contains((CharSequence)",")) {
                for (String target : args[0].split(",")) {
                    Player player = this.plugin.getServer().getPlayer(target);
                    if (player == null) continue;
                    this.plugin.kaboomPlayer(player, sender.getName(), true);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("@a")) {
                for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.kaboomPlayer(player, sender.getName(), true);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("bungee")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("all")) {
                        this.plugin.sendToBungee("all", sender.getName());
                        sender.sendMessage(this.plugin.getPrefix() + "\u00a7bKaboom all servers...");
                        return true;
                    }
                    this.plugin.sendToBungee(args[1], sender.getName());
                    sender.sendMessage(this.plugin.getPrefix() + "\u00a7bKaboom to " + args[1] + "...");
                    return true;
                }
                sender.sendMessage(this.plugin.getPrefix() + "\u00a7cUsage: /kaboom bungee <serverName(,)|all>");
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                this.plugin.reloadConfig();
                sender.sendMessage(this.plugin.getPrefix() + "\u00a7bConfig has been reloaded!");
                return true;
            }
            Player player = this.plugin.getServer().getPlayer(args[0]);
            if (player != null) {
                this.plugin.kaboomPlayer(player, sender.getName(), true);
                return true;
            }
            sender.sendMessage(this.plugin.getPrefix() + "\u00a7cPlayer is not online!");
            return true;
        }
        sender.sendMessage(this.plugin.getPrefix() + "\u00a7cUsage: /kaboom <player(,)|@a|bungee>");
        return true;
    }
}
