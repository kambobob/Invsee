package dev.foliaviewer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;

public final class FoliaViewer extends JavaPlugin implements CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        getCommand("invsee").setExecutor(this);
        getCommand("invsee").setTabCompleter(this);
        getCommand("ec").setExecutor(this);
        getCommand("ec").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player viewer)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            viewer.sendMessage(Component.text("Usage: /" + label + " <player>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            viewer.sendMessage(Component.text("That player is not online.", NamedTextColor.RED));
            return true;
        }

        // Commands run on the sender's region thread in Folia, so opening
        // an inventory for the sender here is safe.
        switch (command.getName().toLowerCase(Locale.ROOT)) {
            case "invsee" -> viewer.openInventory(target.getInventory());
            case "ec" -> viewer.openInventory(target.getEnderChest());
            default -> { return false; }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) return List.of();
        String prefix = args[0].toLowerCase(Locale.ROOT);
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(prefix))
                .toList();
    }
}
