package com.discordplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscordPlugin extends JavaPlugin implements CommandExecutor, TabCompleter, Listener {

    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
    private BukkitTask broadcastTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        PluginCommand command = getCommand("discord");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }

        getServer().getPluginManager().registerEvents(this, this);
        startBroadcastTask();

        getLogger().info("DiscordPlugin has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (broadcastTask != null) {
            broadcastTask.cancel();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("link")) {
            if (sender instanceof Player && !sender.isOp() && !sender.hasPermission("discordplugin.admin")) {
                sender.sendMessage(serializer.deserialize("&cYou do not have permission to execute this command."));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(serializer.deserialize("&cUsage: /discord link <https://discord.gg/...>"));
                return true;
            }

            String newUrl = args[1];
            getConfig().set("discord-url", newUrl);
            saveConfig();

            sender.sendMessage(serializer.deserialize("&a[DiscordPlugin] Discord link successfully updated to: &b" + newUrl));
            getLogger().info("Discord link updated by " + sender.getName() + " to: " + newUrl);
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player && !sender.isOp() && !sender.hasPermission("discordplugin.admin")) {
                sender.sendMessage(serializer.deserialize("&cYou do not have permission to execute this command."));
                return true;
            }

            reloadConfig();
            startBroadcastTask();
            sender.sendMessage(serializer.deserialize("&a[DiscordPlugin] Configuration reloaded successfully!"));
            getLogger().info("Configuration reloaded by " + sender.getName());
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("broadcast")) {
            if (sender instanceof Player && !sender.isOp() && !sender.hasPermission("discordplugin.admin")) {
                sender.sendMessage(serializer.deserialize("&cYou do not have permission to execute this command."));
                return true;
            }

            broadcastDiscordMessage();
            sender.sendMessage(serializer.deserialize("&a[DiscordPlugin] Discord message successfully broadcasted to all online players!"));
            getLogger().info("Discord message broadcasted by " + sender.getName());
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only a player can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        sendDiscordMessage(player);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (sender.isOp() || sender.hasPermission("discordplugin.admin") || !(sender instanceof Player)) {
                List<String> completions = new ArrayList<>();
                String input = args[0].toLowerCase();
                
                if ("link".startsWith(input)) {
                    completions.add("link");
                }
                if ("reload".startsWith(input)) {
                    completions.add("reload");
                }
                if ("broadcast".startsWith(input)) {
                    completions.add("broadcast");
                }
                return completions;
            }
        }
        return Collections.emptyList();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (getConfig().getBoolean("first-join.enabled", false) && !player.hasPlayedBefore()) {
            sendDiscordMessage(player);
        } else if (getConfig().getBoolean("every-join.enabled", false)) {
            sendDiscordMessage(player);
        }
    }

    private void sendDiscordMessage(Player player) {
        String discordUrl = getConfig().getString("discord-url", "https://discord.gg/");

        String header = getConfig().getString("messages.header", "&8&m----------------------------------------");
        String title = getConfig().getString("messages.title", "&5&lDISCORD &7» Join our community!");
        String buttonText = getConfig().getString("messages.button-text", "&d&l[CLICK HERE TO JOIN]");
        String buttonHover = getConfig().getString("messages.button-hover", "&eClick to open the Discord link");
        String footer = getConfig().getString("messages.footer", "&8&m----------------------------------------");

        player.sendMessage(serializer.deserialize(header));
        player.sendMessage(serializer.deserialize(title));

        Component linkComponent = serializer.deserialize(buttonText)
                .clickEvent(ClickEvent.openUrl(discordUrl))
                .hoverEvent(HoverEvent.showText(serializer.deserialize(buttonHover)));

        player.sendMessage(linkComponent);
        player.sendMessage(serializer.deserialize(footer));
    }

    private void broadcastDiscordMessage() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendDiscordMessage(player);
        }
    }

    private void startBroadcastTask() {
        if (broadcastTask != null) {
            broadcastTask.cancel();
        }

        if (getConfig().getBoolean("broadcast.enabled", false)) {
            long minutes = getConfig().getLong("broadcast.interval-minutes", 60);
            long ticks = minutes * 60 * 20;

            broadcastTask = Bukkit.getScheduler().runTaskTimer(this, this::broadcastDiscordMessage, ticks, ticks);
        }
    }
}