package scriptservice.uhc.loupgarou.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.joueur;
import scriptservice.uhc.loupgarou.enums.states;

import java.util.UUID;

public class playerChat implements Listener {
    private final Main main;
    public playerChat(Main main) {
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
    public void OnChat(AsyncPlayerChatEvent event) {
        final UUID chamanUUID = main.gameUtils.chaman;

        if (main.state == states.WAITING) {
            event.setCancelled(false);
            return;
        }

        if (chamanUUID == null) { // uuid null
            event.getPlayer().sendMessage(main.chatPrefix_prive + "Le tchat est désactivé. "+_dgray+"[1]");
            event.setCancelled(true);
            return;
        }

        final Player chamanPlayer = Bukkit.getPlayer(chamanUUID);
        if (chamanPlayer == null) { // chaman déco
            event.getPlayer().sendMessage(main.chatPrefix_prive + "Le tchat est désactivé. "+_dgray+"[2]");
            event.setCancelled(true);
            return;
        }

        final joueur chaman = main.gameUtils.getJoueur(chamanPlayer);

        if (chaman == null) { // off
            event.getPlayer().sendMessage(main.chatPrefix_prive + "Le tchat est désactivé. "+_dgray+"[3]");
            event.setCancelled(true);
            return;
        }

        if (!chaman.isAlive()) { // mort
            event.getPlayer().sendMessage(main.chatPrefix_prive + "Le tchat est désactivé. "+_dgray+"[4]");
            event.setCancelled(true);
            return;
        } else {
            final Player player = event.getPlayer();
            if (main.gameUtils.isPlayerJoueur(player)) {
                joueur joueur = main.gameUtils.getJoueur(player);

                if (joueur.isAlive()) { // joueur vivant
                    event.getPlayer().sendMessage(main.chatPrefix_prive + "Le tchat est désactivé. "+_dgray+"[5]");
                    event.setCancelled(true);
                    return;
                }

                if (joueur.hasTextedChaman) { // a déjà envoyé un message au chaman
                    event.getPlayer().sendMessage(main.chatPrefix_prive + "Vous avez déjà envoyez un message au chaman.");
                    event.setCancelled(true);
                    return;
                }


                joueur.sendMessage(main.chatPrefix_prive + "Vous avez envoyé au chaman: " + event.getMessage());
                chaman.sendMessage(main.chatPrefix_prive + _dred+"["+_red+"☠ MORT ☠"+_dred+"]:"+_gray+event.getMessage());
                joueur.hasTextedChaman = true;
                event.setCancelled(true);
                return;
            }
        }

        return;
    }
}
