package scriptservice.uhc.loupgarou.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.joueur;
import scriptservice.uhc.loupgarou.classes.winResult;
import scriptservice.uhc.loupgarou.enums.camps;
import scriptservice.uhc.loupgarou.enums.roles;
import scriptservice.uhc.loupgarou.enums.states;

import java.util.Timer;
import java.util.TimerTask;

public class playerDeath implements Listener {
    private final Main main;
    public playerDeath(Main main) { 
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

    // pour l'enfant sauvage & le chasseur (pour l'instant)
    private void tryDeathEvents(Player playerMort, joueur joueurMort) {

        if (joueurMort.getRole() == roles.Chasseur) {
            joueurMort.canFire = true;
            joueurMort.sendPMessage("Vous êtes mort, vous avez une minute pour tirer sur une personne grâce a la commande "+_yellow+"/lg tirer <pseudo>"+_white+".");

            TimerTask offTask = new TimerTask() {
                @Override
                public void run() {
                    if (joueurMort.canFire) {
                        joueurMort.sendPMessage("Vous ne pouvez plus tirer.");
                    }

                    joueurMort.canFire = false;
                }
            };

            new Timer().schedule(offTask, 60*1000); // timeForCycle = 10mn, c'est au millieu de la nuit

        } else if (joueurMort.getRole() == roles.Enfant_Sauvage) {
            
        }
    }

    private void _respawn(Player player, joueur joueurMort) {
        if (joueurMort.getRole() == roles.Ancien) {
            joueurMort.getPlayer().sendMessage(main.chatPrefix+"Vous avez été tué par un "+_red+"Loup-Garou"+_white+"! Vous ressuscitez mais perdez votre effet de "+_gray+"résistance"+_white+".");
        }


        Location respawnLocation = new Location(player.getWorld(), (-300 + Math.random() * 600), 100, (-300 + Math.random() * 600)); // MAKE RANDOM
        joueurMort.hasRespawned = true;
        joueurMort.isProtected_once = true;

        player.spigot().respawn();
        player.teleport(respawnLocation);
    }

    private void _kill(Player playerMort, joueur joueurMort) {
        joueurMort.setMort(true); // CIAAAAAAAAOOOOOO !!!

        // give les effets de potions pour les kills des lg
        if (playerMort.getKiller() != null && playerMort.getKiller().getType() == EntityType.PLAYER && main.gameUtils.isPlayerJoueur(playerMort.getKiller())) {
            Player killer = playerMort.getKiller();
            joueur joueurKiller = main.gameUtils.getJoueur(killer);

            if (joueurKiller.getUUID().toString().equals(killer.getUniqueId().toString()) && joueurKiller.isLoup_Effect() ) {
                main.powerUtils.giveEffects(joueurKiller, main.powerUtils.lg_kill_effects);
            }
        }

        // update sur les scoreboards
        int jrs = 0;
        for (joueur joueur : main.gameUtils.joueurPlayer.keySet()) {
            if (joueur.isAlive()) {
                jrs = jrs + 1;

                // nique sa race x)
                joueur.getScoreboard().setLine(7, ChatColor.RED + "" + jrs + ChatColor.DARK_RED + (jrs > 1 ? " Joueurs" : " Joueur"));
            }
        }

        // les events de mort
        tryDeathEvents(playerMort, joueurMort);

        // le message
        main.playerUtils.sendMessageToAll(new String[]{ChatColor.RED + "=========☠==========","§2Le village a perdu un de ses membres : " + joueurMort.getRole().getStrColor() + "§l" + joueurMort.getName() + "§r§2, qui était " + joueurMort.getRole().getStrColor() + "§o" + joueurMort.getRole().getName() + "§r§2.", "§c===================="});
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        boolean isPlaying = main.gameUtils.isPlayerJoueur(player);

        if (isPlaying && main.gameUtils.episodeInt >= 2) {
            event.setDeathMessage(null);

            joueur joueurMort = main.gameUtils.getJoueur(player);

            // l'ancien est mort
            if (joueurMort.getRole() == roles.Ancien && !joueurMort.hasRespawned) {
                // l'ancien se fait tuer par qqn
                if (player.getKiller() != null && player.getKiller().getType() == EntityType.PLAYER && main.gameUtils.isPlayerJoueur(player.getKiller())) {
                    Player killer = player.getKiller();
                    joueur joueurKiller = main.gameUtils.getJoueur(killer);

                    // si l'ancien est mort par un lg
                    if (joueurKiller.isLoup_Kill()) {
                        event.setKeepInventory(true);
                        _respawn(player, joueurMort);
                    } else {
                        // aussinon x) (sur les prochaines saisons -> check si le type est solo ou non)
                        killer.setHealth((killer.getHealth() / 2));
                        joueurKiller.getPlayer().sendMessage(main.chatPrefix+"Vous avez tué l'"+_green+"Ancien"+_white+", vous perdez donc la moitié de votre vie effective.");

                        event.setKeepInventory(false);
                        _kill(player, joueurMort);
                    }
                } else {
                    event.setKeepInventory(false);
                    _kill(player, joueurMort);
                }
            } else {
                event.setKeepInventory(false);
                _kill(player, joueurMort);
            }

            // fin de game
            winResult result = main.gameUtils.endGame();

            if (result.isWin() && result.getCamp() != null) {
                camps winCamp = result.getCamp();
                String winMessage = result.getWinMessage();

                // on modifie l'etat de la game
                main.state = states.FINISH;

                // on stop la game, sans montrer le message (oui faut que je change pour les kills & pseudo mais flm)
                main.gameUtils.stopGame(false);

                // on envoie les gagnants
                main.playerUtils.sendTitleToAll(_gold+"FIN DE GAME", _yellow+"GG");
                main.playerUtils.sendMessageToAll(winMessage);

                // on joue un petit sound
                if (winCamp == camps.Village) {
                    main.playerUtils.playSoundToAll(Sound.VILLAGER_IDLE, 1, 1);
                } else if (winCamp == camps.LoupGarou) {
                    main.playerUtils.playSoundToAll(Sound.WOLF_GROWL, 1, 1);
                } else if (winCamp == camps.Neutre) {
                    main.playerUtils.playSoundToAll(Sound.ENDERDRAGON_GROWL, 1, 1);
                } else if (winCamp == camps.Couple) {
                    main.playerUtils.playSoundToAll(Sound.VILLAGER_IDLE, 1, 1); // faut changer un bruit de fleche, même qu'uhcworld ^^ (pas sur apres reflexion mais bon)
                }
            }
        } else {
            event.setDeathMessage("§b[§c☠§b] §3§l" + player.getName() + "§r§3 est mort.");
            event.setKeepInventory(true);
        }

    }
}
