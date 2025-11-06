package scriptservice.uhc.loupgarou.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.joueur;
import scriptservice.uhc.loupgarou.classes.winResult;
import scriptservice.uhc.loupgarou.enums.camps;
import scriptservice.uhc.loupgarou.enums.roles;
import scriptservice.uhc.loupgarou.enums.states;
import scriptservice.uhc.loupgarou.resources.ScoreboardSign;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class gameUtils {
    private final Main main;

    public gameUtils(Main main) {
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

    //---- game stuff ----//
    public int mainTimerInt = -1;
    public int episodeInt = 0;
    public boolean cycle = false; // true = Jour, false = Nuit

    public HashMap<Player, joueur> playerJoueur = new HashMap<>();
    public HashMap<joueur, Player> joueurPlayer = new HashMap<>();
    //---- game stuff ----//

    //---- fonctions ----//
    public void startGame() {
        main.state = states.PREGAME;

        for (Player player : getServer().getOnlinePlayers()) {
            joueur joueur = new joueur(player, player.getUniqueId(), this.main);

            playerJoueur.put(player, joueur);
            joueurPlayer.put(joueur, player);

            ScoreboardSign scoreboard = main.playerUtils.makeScoreboard(player);
            joueur.setScoreboard(scoreboard);
        }

        main.timerUtils.mainTimer = new Timer();
        main.timerUtils.mainTimerTask = new TimerTask() {
            @Override
            public void run() {
                mainTimerInt = mainTimerInt + 1;

                for (joueur joueur : joueurPlayer.keySet()) {
                    joueur.getScoreboard().setLine(9, ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + String.format("%02d:%02d", mainTimerInt / 60, mainTimerInt % 60));

                    if (joueur.getPlayer() != null && joueur.getPlayer().getWorld() != null) {
                        joueur.getScoreboard().setLine(12, ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + (joueur.getPlayer().getWorld().getWorldBorder().getSize() / 2));
                    }
                }
            }
        };

        main.timerUtils.mainTimer.scheduleAtFixedRate(main.timerUtils.mainTimerTask, Calendar.getInstance().getTime(), 1000);

        main.timerUtils.cycleTimer = new Timer();
        main.timerUtils.cycleTimerTask = new TimerTask() {
            @Override
            public void run() {
                cycle = !cycle;

                if (cycle) {
                    if (episodeInt != 0) {
                        main.playerUtils.sendMessageToAll(_gold+_underline+"☀ LE JOUR SE LEVE ☀"); // j'ai prit trop de temps pour chercher les unicodes
                    }


                    for (joueur joueur : joueurPlayer.keySet()) {
                        // set scoreboard
                        joueur.getScoreboard().setLine(10, ChatColor.GOLD + "Cycle: " + ChatColor.YELLOW + "Jour");

                        // effets de jours
                        main.powerUtils.giveDay(joueur);

                        // le world time (si il y a)
                        if (main.world != null || main.worldName != null) {
                            World world = main.world;

                            if (world == null) {
                                world = Bukkit.getServer().getWorld(main.worldName);
                            }

                            if (world != null) {
                                world.setTime(6000);
                            }
                        }
                    }
                } else {
                    if (episodeInt != 0) {
                        main.playerUtils.sendMessageToAll(_blue+_underline+"☾ LA NUIT TOMBE ☽"); // j'ai prit trop de temps pour chercher les unicodes
                    }

                    for (joueur joueur : joueurPlayer.keySet()) {
                        // set scoreboard
                        joueur.getScoreboard().setLine(10, ChatColor.GOLD + "Cycle: " + ChatColor.YELLOW + "Nuit");

                        // effets de nuits
                        main.powerUtils.giveNight(joueur);

                        // le world time (si il y a)
                        if (main.world != null || main.worldName != null) {
                            World world = main.world;

                            if (world == null) {
                                world = Bukkit.getServer().getWorld(main.worldName);
                            }

                            if (world != null) {
                                world.setTime(18000);
                            }
                        }
                    }
                }
            }
        };

        main.timerUtils.cycleTimer.scheduleAtFixedRate(main.timerUtils.cycleTimerTask, Calendar.getInstance().getTime(), main.timerUtils.timeForCycle);

        main.timerUtils.episodeTimer = new Timer();
        main.timerUtils.episodeTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (episodeInt != 0) {
                    main.playerUtils.sendMessageToAll(ChatColor.AQUA + "-------- Fin Episode " + episodeInt + " --------");
                }

                episodeInt = episodeInt + 1;

                if (episodeInt == 2) {
                    main.state = states.GAME;
                    giveRoles();
                }

                if (episodeInt >= 2) {
                    for (joueur joueur : joueurPlayer.keySet()) {
                        joueur.hasSeen = false;
                        joueur.hasProtected = false;
                        joueur.isProtected = false;
                    }
                }

                for (joueur joueur : joueurPlayer.keySet()) {
                    joueur.getScoreboard().setLine(6, ChatColor.AQUA + "Episode " + ChatColor.GOLD + episodeInt);

                }
            }
        };

        main.timerUtils.episodeTimer.scheduleAtFixedRate(main.timerUtils.episodeTimerTask, Calendar.getInstance().getTime(), main.timerUtils.timeForEpisode); // 1200000 = 20 min
    }

    public void stopGame(boolean showMessage) {
        main.state = states.WAITING;

        episodeInt = 0;
        mainTimerInt = 0;
        cycle = false;

        if (showMessage) {
            main.playerUtils.sendTitleToAll(_red+"Arrêt de l'"+_gold+"UHC"+_red+".", "");
            main.playerUtils.sendMessageToAll(_red+"Arrêt de l'"+_gold+"UHC"+_red+".");
        }

        main.timerUtils.mainTimer.cancel();
        main.timerUtils.episodeTimer.cancel();
        main.timerUtils.cycleTimer.cancel();

        for (joueur joueur : joueurPlayer.keySet()) {
            joueur.getScoreboard().destroy();
            joueur.getPlayer().setMaxHealth(20);

            for (PotionEffect effect : joueur.getPlayer().getActivePotionEffects()) {
                joueur.getPlayer().removePotionEffect(effect.getType());
            }
        }

        joueurPlayer.clear();
        playerJoueur.clear();
    }

    public winResult endGame() {
        boolean shouldEndGame = true;
        camps aliveCamp = null; // rien pour l'instant
        String winMessage = "N/A"; // rien aussi

        for (joueur joueur : joueurPlayer.keySet()) {
            if (joueur.isAlive()) {

                if (aliveCamp == null) {
                    aliveCamp = joueur.getCamp(); // le 1er camps d'un type vivant (et oui jamy)

                } else if (aliveCamp == camps.Neutre && joueur.getCamp() == camps.Neutre) { // faut pas que tout les rôles solo win ensemble x)
                    shouldEndGame = false;
                } else if (aliveCamp != joueur.getCamp()) { // si le camp du 1er type vivant est diff du 1er type, alors personne n'a win :)
                    shouldEndGame = false;
                    break;

                }

            }
        }

        // ceci est bien, nan ?
        if (shouldEndGame && aliveCamp != null) {
            switch (aliveCamp) {
                case Couple:
                    winMessage = "" + ChatColor.DARK_PURPLE  + ChatColor.BOLD + "Le " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Couple" + ChatColor.DARK_PURPLE  + ChatColor.BOLD + " a gagné la partie !";
                    break;

                case Neutre:
                    winMessage = "" + ChatColor.YELLOW  + ChatColor.BOLD + "Un rôle " + ChatColor.GOLD + ChatColor.BOLD + "Solitaire" + ChatColor.YELLOW  + ChatColor.BOLD + " a gagné la partie !";
                    break;

                case LoupGarou:
                    winMessage = "" + ChatColor.DARK_RED  + ChatColor.BOLD + "Les " + ChatColor.RED + ChatColor.BOLD + "Loups-Garous" + ChatColor.DARK_RED  + ChatColor.BOLD + " ont gagné la partie !";

                    break;

                case Village:
                    winMessage = "" + ChatColor.DARK_GREEN  + ChatColor.BOLD + "Le " + ChatColor.GREEN + ChatColor.BOLD + "Village" + ChatColor.DARK_GREEN  + ChatColor.BOLD + " a gagné la partie !";
                    break;

                default:
                    winMessage = "bug ? (switch -> default?)";
                    break;
            }
        }

        return new winResult(shouldEndGame, winMessage, aliveCamp);
    }

    public void giveRoles() {
        main.playerUtils.sendMessageToAll(main.chatPrefix + "Attribution des rôles.");

        Random rng = new Random();
        for (roles role : roles.values()) {

            joueur joueurToGive = playerJoueur.values().stream().skip(rng.nextInt(playerJoueur.size())).findFirst().get();

            if (role.getNumber() > 0 && joueurToGive.getRole() == null) {
                role.setNumber(role.getNumber() - 1);

                // set role & camp
                joueurToGive.setRole(role);
                joueurToGive.setCamp(role.getCamp());

                // give effects
                main.powerUtils.givePermanent(joueurToGive);
                main.powerUtils.giveOneTime(joueurToGive);

                // petit sound
                if (role.getCamp() == camps.Village) {
                    main.playerUtils.playSound(joueurToGive.getPlayer(), Sound.VILLAGER_IDLE);
                } else if (role.getCamp() == camps.LoupGarou) {
                    main.playerUtils.playSound(joueurToGive.getPlayer(), Sound.WOLF_GROWL);
                } else if (role.getCamp() == camps.Neutre) {
                    main.playerUtils.playSound(joueurToGive.getPlayer(), Sound.ENDERDRAGON_GROWL);
                }

                // envoie les messages
                joueurToGive.getPlayer().sendMessage(new String[]{(main.chatPrefix + "Vous êtes " + role.getStrColor() + role.getName() + _white), (role.getDescription())});

            }


        }


    }

    public boolean isPlayerJoueur(Player player) {
        return (getJoueur(player) != null);
    }

    public joueur getJoueur(Player mainPlayer) {
        for (Player player : playerJoueur.keySet()) {
            if (player.getUniqueId().toString().equals( mainPlayer.getUniqueId().toString() )) {
                return playerJoueur.get(player);
            }
        }

        return null;
    }
    //---- fonctions ----//
}
