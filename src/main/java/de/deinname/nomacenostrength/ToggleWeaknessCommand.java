package de.deinname.nomacenostrength;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleWeaknessCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("nomace.toggleweakness")) {
            sender.sendMessage("§cKeine Berechtigung!");
            return true;
        }

        Main.weaknessEnabled = !Main.weaknessEnabled;

        sender.sendMessage("§aSchwäche ist jetzt " + (Main.weaknessEnabled ? "aktiviert" : "deaktiviert"));

        return true;
    }
}
