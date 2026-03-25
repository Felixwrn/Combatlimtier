package de.deinname.nomacenostrength;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetStrengthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("nomace.setstrength")) {
            sender.sendMessage("§cKeine Berechtigung!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cBenutzung: /setstrength <level>");
            return true;
        }

        try {
            int level = Integer.parseInt(args[0]);

            if (level < 1) {
                sender.sendMessage("§cMinimum ist Stärke 1!");
                return true;
            }

            Main.maxStrengthLevel = level;
            sender.sendMessage("§aMaximale Stärke ist jetzt: " + level);

        } catch (NumberFormatException e) {
            sender.sendMessage("§cBitte eine Zahl eingeben!");
        }

        return true;
    }
}
