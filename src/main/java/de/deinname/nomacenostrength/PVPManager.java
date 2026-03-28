package de.deinname.nomacenostrength;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PVPManager {

    public static HashMap<UUID, UUID> requests = new HashMap<>();
    public static HashMap<UUID, UUID> fights = new HashMap<>();

    public static void sendRequest(Player sender, Player target) {
        requests.put(target.getUniqueId(), sender.getUniqueId());

        sender.sendMessage("§aDu hast " + target.getName() + " herausgefordert!");
        target.sendMessage("§e" + sender.getName() + " fordert dich heraus! §a/accept");
    }

    public static void accept(Player player) {
    UUID playerUUID = player.getUniqueId();

    if (!requests.containsKey(playerUUID)) {
        player.sendMessage("§cDu hast keine offene Anfrage!");
        return;
    }

    UUID senderUUID = requests.get(playerUUID);
    Player sender = player.getServer().getPlayer(senderUUID);

    if (sender == null) {
        player.sendMessage("§cDer Spieler ist nicht mehr online!");
        requests.remove(playerUUID);
        return;
    }

    // Fight starten
    fights.put(playerUUID, senderUUID);
    fights.put(senderUUID, playerUUID);

    requests.remove(playerUUID);

    Main plugin = Main.instance;

    // Teleport
    player.teleport(plugin.arenaSpawn1);
    sender.teleport(plugin.arenaSpawn2);

    player.sendMessage("§aKampf gestartet gegen " + sender.getName());
    sender.sendMessage("§aKampf gestartet gegen " + player.getName());
}

    public static void endFight(Player loser) {
        if (!fights.containsKey(loser.getUniqueId())) return;

        UUID opponentUUID = fights.get(loser.getUniqueId());
        Player winner = loser.getServer().getPlayer(opponentUUID);

        fights.remove(loser.getUniqueId());
        fights.remove(opponentUUID);

        Main plugin = Main.instance;

        if (winner != null) {
            plugin.addWin(winner.getUniqueId());
            plugin.addLoss(loser.getUniqueId());

            winner.sendMessage("§aWin! (" + plugin.getWins(winner.getUniqueId()) + ")");
            loser.sendMessage("§cLose! (" + plugin.getLosses(loser.getUniqueId()) + ")");
        }
    }

    public static boolean isInFight(Player player) {
        return fights.containsKey(player.getUniqueId());
    }
}
