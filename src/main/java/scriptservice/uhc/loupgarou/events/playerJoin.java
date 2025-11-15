package scriptservice.uhc.loupgarou.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.joueur;
import scriptservice.uhc.loupgarou.resources.ScoreboardSign;

public class playerJoin implements Listener {
    private final Main main;
    public playerJoin(Main main) {
        this.main = main;
    }

    //---- color strings ----//
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

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (main.gameUtils.isPlayerJoueur(player)) {
            final joueur joueur = main.gameUtils.getJoueur(player);

            // scoreboardsign sauvegarde pas -> create a chaque fois x)
            joueur.getScoreboard().destroy();
            ScoreboardSign scoreboard = main.playerUtils.makeScoreboard(player);
            joueur.setScoreboard(scoreboard);

            // on set les nouveaux trucs (tu connais)
            joueur.setUUID(player.getUniqueId());
            joueur.setPlayer();
            joueur.setName();

            // pas de join message :)
            event.setJoinMessage(null);
        }
    }


}
