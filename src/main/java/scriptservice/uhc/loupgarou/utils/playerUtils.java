package scriptservice.uhc.loupgarou.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.commandResult;
import scriptservice.uhc.loupgarou.classes.joueur;
import scriptservice.uhc.loupgarou.resources.ScoreboardSign;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class playerUtils {
    private final Main main;

    public playerUtils(Main main) {
        this.main = main;
    }

    //---- color strings ----//
    private final String _black = "§0";
    private final String _dblue = "§1";
    private final String _dgreen = "§2";
    private final String _daqua = "§3";
    private final String _dred = "§4";
    private final String _purple = "§5";
    private final String _gold = "§6";
    private final String _gray = "§7";
    private final String _dgray = "§8";
    private final String _blue = "§9";
    private final String _green = "§a";
    private final String _aqua = "§b";
    private final String _red = "§c";
    private final String _pink = "§d";
    private final String _yellow = "§e";
    private final String _white = "§f";

    private final String _random = "§k";
    private final String _bold = "§l";
    private final String _strikethrough = "§m";
    private final String _underline = "§n";
    private final String _italic = "§o";
    private final String _reset = "§r";
    //---- color strings ----//

    //---- fonctions ----//
    public boolean isOwner(Player player) {
        return player.getUniqueId().toString().equals("0fc289a2-8dda-429a-b727-7f1e9811d747");
    }

    public boolean isOwner(UUID uuid) {
        return uuid.toString().equals("0fc289a2-8dda-429a-b727-7f1e9811d747");
    }

    public commandResult hasPermission(Player player) {
        if (main.gameUtils.isHost(player)) {
            return new commandResult(true, "HOST");
        }

        if (main.gameUtils.isCohost(player)) {
            return new commandResult(true, "CO-HOST");
        }

        return new commandResult(false, "");
    }

    public void playSound(Player player, Sound soundPlayed, float v, float v1) {
        Bukkit.getScheduler().runTask(this.main, () -> player.getWorld().playSound(player.getLocation(), soundPlayed, v, v1));
    }

    public void playSound(Player player, Sound soundPlayed) {
        Bukkit.getScheduler().runTask(this.main, () -> player.getWorld().playSound(player.getLocation(), soundPlayed, 1, 1));
    }

    public void playSoundToAll(Sound soundPlayed, float v, float v1) {
        for (Player player: getServer().getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(this.main, () -> player.getWorld().playSound(player.getLocation(), soundPlayed, v, v1));
        }
    }

    public void playSoundToAll(Sound soundPlayed) {
        for (Player player: getServer().getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(this.main, () -> player.getWorld().playSound(player.getLocation(), soundPlayed, 1, 1));
        }
    }


    public ScoreboardSign makeScoreboard(Player player) {
        ScoreboardSign scoreboard = new ScoreboardSign(player, _bold+_underline+"LG UHC S1");
        scoreboard.create();

        int jrs = 0;
        for (joueur joueur : main.gameUtils.joueurPlayer.keySet()) {
            if (joueur.isAlive()) {
                jrs = jrs + 1;
            }
        }

        scoreboard.setLine(5," ");
        scoreboard.setLine(6, ChatColor.AQUA + "Episode " + ChatColor.GOLD + main.gameUtils.episodeInt);
        scoreboard.setLine(7, ChatColor.RED + "" + jrs + ChatColor.DARK_RED + ((jrs == 1) ? " Joueur" : " Joueurs"));
        scoreboard.setLine(8, "  ");
        scoreboard.setLine(9, ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + "00:00");
        scoreboard.setLine(10, ChatColor.GOLD + "Cycle: " + ChatColor.YELLOW + (main.gameUtils.cycle ? "Jour" : "Nuit"));
        scoreboard.setLine(11, "   ");
        scoreboard.setLine(12, ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + (player.getWorld().getWorldBorder().getSize() / 2));
        scoreboard.setLine(13, "    ");
        scoreboard.setLine(14,  ChatColor.UNDERLINE + "@scriptservice");

        return scoreboard;
    }

    @SuppressWarnings("deprecation")
    public void sendTitleToAll(String title, String subtitle) {
        for (Player player1 : getServer().getOnlinePlayers()) {
            player1.sendTitle(title, subtitle);
        }
    }

    public void sendMessageToAll(String[] messages) {
        for (Player player1: getServer().getOnlinePlayers()) {
            player1.sendMessage(messages);
        }
    }

    public void sendMessageToAll(String message) {
        for (Player player1: getServer().getOnlinePlayers()) {
            player1.sendMessage(message);
        }
    }
    //---- fonctions ----//
}
