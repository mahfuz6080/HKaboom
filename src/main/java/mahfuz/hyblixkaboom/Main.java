/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataOutput
 *  com.google.common.io.ByteStreams
 *  java.io.ByteArrayInputStream
 *  java.io.DataInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.bukkit.util.Vector
 */
package net.mahfuz.hyblixkaboom;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.lang.Object;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.mahfuz.hyblixkaboom.commands.CommandKaboom;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.util.Vector;

public class Main
extends JavaPlugin
implements Listener,
PluginMessageListener {
    private List<Player> list = new ArrayList();

    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("kaboom").setExecutor((CommandExecutor)new CommandKaboom(this));
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "Kaboom");
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "Kaboom", (PluginMessageListener)this);
    }

    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel((Plugin)this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel((Plugin)this);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player && this.list.contains((Object)(player = (Player)event.getEntity()))) {
            event.setCancelled(true);
            if (player.isOnGround()) {
                this.list.remove((Object)player);
            }
        }
    }

    public void kaboomPlayer(Player player, String name, boolean allowLightning) {
        player.setVelocity(new Vector(0.0, 64.0, 0.0));
        player.sendTitle(this.getConfig("Title"), this.getConfig("Sub-Title").replace((CharSequence)"%%player%%", (CharSequence)name));
        if (allowLightning) {
            player.getWorld().strikeLightningEffect(player.getLocation());
        }
        if (!this.list.contains((Object)player)) {
            this.list.add((Object)player);
        }
    }

    public String getConfig(String path) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)this.getConfig().getString(path));
    }

    public String getPrefix() {
        return this.getConfig("Prefix");
    }

    public void sendToBungee(String server, String name) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        if (server.equalsIgnoreCase("all")) {
            byteArrayDataOutput.writeUTF("%all");
        } else if (server.contains((CharSequence)",")) {
            for (String s : server.split(",")) {
                byteArrayDataOutput.writeUTF(s);
            }
        } else {
            byteArrayDataOutput.writeUTF(server);
        }
        byteArrayDataOutput.writeUTF(name);
        this.getServer().sendPluginMessage((Plugin)this, "Kaboom", byteArrayDataOutput.toByteArray());
    }

    public void onPluginMessageReceived(String channel, Player p, byte[] message) {
        if (!channel.equals((Object)"Kaboom")) {
            return;
        }
        DataInputStream dataInputStream = new DataInputStream((InputStream)new ByteArrayInputStream(message));
        try {
            String utf = dataInputStream.readUTF();
            String key = utf.split("[|]")[0];
            if (key.equals((Object)"%all")) {
                String value = utf.split("[|]")[1];
                for (Player player : this.getServer().getOnlinePlayers()) {
                    this.kaboomPlayer(player, value, true);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getList() {
        return this.list;
    }
}
