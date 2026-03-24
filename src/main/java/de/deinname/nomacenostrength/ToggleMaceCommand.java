package de.deinname.nomacenostrength;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleMaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nomace.toggle")) {
            sender.sendMessage("§cDu hast keine Berechtigung dafür!");
            return true;
        }

        Main.maceEnabled = !Main.maceEnabled;

        sender.sendMessage("§aMaces sind jetzt " + (Main.maceEnabled ? "aktiviert" : "deaktiviert"));
        return true;
    }
}
