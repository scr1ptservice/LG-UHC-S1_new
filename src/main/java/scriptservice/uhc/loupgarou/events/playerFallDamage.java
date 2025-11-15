package scriptservice.uhc.loupgarou.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.joueur;

public class playerFallDamage implements Listener {
    private final Main main;

    public playerFallDamage(Main main) {
        this.main = main;
    }

    @EventHandler
    public void OnFall(EntityDamageEvent event) {
        // fall check
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        // player check
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();

        // joueur check
        if (!main.gameUtils.isPlayerJoueur(player)) {
            return;
        }

        joueur joueur = main.gameUtils.getJoueur(player);

        // isProtected check
        if (joueur.isProtected || joueur.isProtected_once) {
            event.setCancelled(true);

            if (joueur.isProtected_once) {
                joueur.isProtected_once = false;
            }
        }

    }
}
