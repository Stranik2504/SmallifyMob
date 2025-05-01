package stranik.dev.smallifyMob;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SmallifyMob extends JavaPlugin {
    public static SmallifyMob getInstance() {
        return getPlugin(SmallifyMob.class);
    }
    
    @Override
    public void onEnable() {
        saveDefaultConfig();

        var pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new SmallifyEventHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("smallifyEventHandler.commands"))
            return false;

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();

            sender.sendMessage("Config successfully reloaded");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("enable")) {
            getConfig().set("enable", !getConfig().getBoolean("enable"));
            saveConfig();

            sender.sendMessage("Enable plugin state now is " + (getConfig().getBoolean("enable") ? "enabled" : "disabled"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("enableGrow")) {
            getConfig().set("enable-grow-after-rename", !getConfig().getBoolean("enable-grow-after-rename"));
            saveConfig();

            sender.sendMessage("Enable grow after rename now is " + (getConfig().getBoolean("enable-grow-after-rename") ? "enabled" : "disabled"));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("activationName")) {
            getConfig().set("activation-name", args[1]);
            saveConfig();

            sender.sendMessage("Activation disable grow set to : " + args[1] + " successfully");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(
                    "Enabled: " + getConfig().getBoolean("enable") + "\n" +
                    "Enable grow: " + getConfig().getBoolean("enable-grow-after-rename") + "\n" +
                    "Activate name: " + getConfig().getString("activation-name") + "\n"
            );
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].trim().isEmpty())
                return List.of("reload", "enable", "enableGrow", "activationName", "info");
            
            ArrayList<String> hints = new ArrayList<>();  

            if ("reload".startsWith(args[0]) && !"reload".equals(args[0]))
                hints.add("reload");

            if ("enable".startsWith(args[0]) && !"enable".equals(args[0]))
                hints.add("enable");

            if ("enableGrow".startsWith(args[0]) && !"enableGrow".equals(args[0]))
                hints.add("enableGrow");

            if ("activationName".startsWith(args[0]) && !"activationName".equals(args[0]))
                hints.add("activationName");

            if ("info".startsWith(args[0]) && !"info".equals(args[0]))
                hints.add("info");
            
            return hints;
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("activationName")) {
            return List.of("<nametag>");
        }

        return new ArrayList<>();
    }
}
