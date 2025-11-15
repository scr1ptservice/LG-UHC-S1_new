package scriptservice.uhc.loupgarou.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.joueur;
import scriptservice.uhc.loupgarou.classes.winResult;
import scriptservice.uhc.loupgarou.enums.roles;
import scriptservice.uhc.loupgarou.enums.states;
import scriptservice.uhc.loupgarou.utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class lg implements CommandExecutor, TabExecutor {
    private final Main main;
    public lg(Main main) {
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(new String[]{
                    main.chatPrefix+_bold+"Sous-commandes "+_dgray+"("+_gray+"/lg ..."+_dgray+")",
                    _dgray+"> "+_green+"composition "+_dgray+"/"+_green+" compo "+_dgray+"/"+_green+" c",
                    _dgray+"> "+_green+"map",
                    _dgray+"> "+_yellow+"start",
                    _dgray+"> "+_green+"stop",
                    _dgray+"> "+_red+"end",
                    _dgray+"> "+_red+"liste",
                    _dgray+"> "+_red+"role"+_dgray+" / "+_red+"me",

                    _dgray+"> "+_purple+"test",
            });
        } else {
            String sousCommande = args[0];

            if (sousCommande.equalsIgnoreCase("composition") || sousCommande.equalsIgnoreCase("compo") || sousCommande.equalsIgnoreCase("c")) {
                main.compoUtils.openMenu(player);
            } else if (sousCommande.equalsIgnoreCase("map")) {
                main.mapUtils.setMainMap(player);
            } else if (sousCommande.equalsIgnoreCase("start")) {
                if (main.state == states.WAITING) {
                    main.timerUtils.launch_cdUHC();
                } else {
                    player.sendMessage(main.chatPrefix_debug + "l'UHC a déjà commencé."); 
                }
            } else if (sousCommande.equalsIgnoreCase("stop")) {
                if (main.state == states.WAITING) {
                    player.sendMessage(main.chatPrefix_debug + "l'UHC n'a pas encore commencé.");
                    return true;
                } else if (main.state == states.STARTING) {
                    main.timerUtils.stop_cdUHC();
                } else {
                    main.gameUtils.stopGame(true);
                }
            } else if (sousCommande.equalsIgnoreCase("end")) {
                if (main.state == states.WAITING || main.state == states.PREGAME) {
                    player.sendMessage(main.chatPrefix_debug + "l'UHC n'a pas encore commencé / n'a pas encore attribué les rôles.");
                } else {
                    winResult result = main.gameUtils.endGame();

                    player.sendMessage(_yellow+"isWin: " + (result.isWin() ? (_green+"true") : (_red+"false")));
                    player.sendMessage(_yellow+"winMessage: " + result.getWinMessage());
                    player.sendMessage(_yellow+"camp: " +_white+ result.getCamp().toString());
                }
            } else if (sousCommande.equalsIgnoreCase("test")) {
                player.sendMessage(main.chatPrefix_debug+"rien a test "+_purple+":^)");


            // commandes pour les rôles.
            } else if (sousCommande.equalsIgnoreCase("choisir")) {
                if (!main.gameUtils.enfantSauvage.toString().equals(player.getUniqueId().toString())) {
                    return true;
                }// le joueur est enfant sauvage

                if (main.gameUtils.enfantSauvageTarget != null) {
                    player.sendMessage(main.chatPrefix_prive+_red+"Vous avez déjà choisi votre modèle.");
                    return true;
                } // il n'a pas encore choisi de modele

                if (args.length != 1) {
                    player.sendMessage(main.chatPrefix_prive+_red+"Usage: /lg choisir <pseudo>.");
                    return true;
                } // il a bien ecrit un pseudo en plus

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(main.chatPrefix_prive+_red+"Le joueur choisi n'existe pas ou n'est pas connecté.");
                    return true;
                } // le joueur visé existe et est connecté

                if (target.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    player.sendMessage(main.chatPrefix_prive+_red+"Vous ne pouvez pas vous choisir vous-même. :/");
                    return true;
                } // ce n'est pas lui même

                main.gameUtils.enfantSauvageTarget = target.getUniqueId();
                player.sendMessage(main.chatPrefix_prive+"Vous venez de choisir "+_yellow+target.getName()+_white+" comme modèle, si celui-ci vient à périr, vous deviendrez alors "+_red+"Loup-Garou"+_white+" et devrez gagner avec "+_red+"eux"+_white+".");
            } else if (sousCommande.equalsIgnoreCase("voir")) {
                if (!main.gameUtils.isPlayerJoueur(player)) {
                    return true;
                } // le player est un joueur

                joueur joueur = main.gameUtils.getJoueur(player);
                if (joueur.getRole() != roles.Voyante_Bavarde) {
                    return true;
                } // il a le role voyante bavarde

                if (joueur.hasSeen) {
                    joueur.sendPMessage(_red+"Vous avez déjà regardé le rôle d'un joueur cette épisode.");
                    return true;
                } // il n'a pas spec cette episode

                if (args.length != 1) {
                    joueur.sendPMessage(_red+"Usage: /lg voir <pseudo>.");
                    return true;
                } // il a bien ecrit un pseudo en plus

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    joueur.sendPMessage(_red+"Le player choisi n'existe pas ou n'est pas connecté.");
                    return true;
                } // le player visé existe et est connecté

                joueur targetJoueur = main.gameUtils.getJoueur(target);
                if (targetJoueur == null) {
                    joueur.sendPMessage(_red+"Le joueur choisi n'existe pas ou n'est pas connecté.");
                    return true;
                } // le joueur visé existe et est connecté

                if (targetJoueur.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    joueur.sendPMessage(_red+"Vous ne pouvez pas regarder votre propre rôle. :/");
                    return true;
                } // il ne vise pas lui-même

                joueur.hasSeen = true;
                main.playerUtils.sendMessageToAll(main.chatPrefix + "La " + _green + "Voyante Bavarde" + _white + " a espionné un joueur et son rôle est : " + targetJoueur.getRole().getStrColor() + targetJoueur.getRole().getName() + _white + ".");
            } else if (sousCommande.equalsIgnoreCase("salvater")) {
                if (!main.gameUtils.isPlayerJoueur(player)) {
                    return true;
                } // le player est un joueur

                joueur joueur = main.gameUtils.getJoueur(player);
                if (joueur.getRole() != roles.Salvateur) {
                    return true;
                } // il a le role salvateur

                if (joueur.hasProtected) {
                    joueur.sendPMessage(_red+"Vous avez déjà salvaté un joueur cette épisode.");
                    return true;
                } // il n'a pas protegé cette episode

                if (args.length != 1) {
                    joueur.sendPMessage(_red+"Usage: /lg salvater <pseudo>.");
                    return true;
                } // il a bien ecrit un pseudo en plus

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    joueur.sendPMessage(_red+"Le player choisi n'existe pas ou n'est pas connecté.");
                    return true;
                } // le player visé existe et est connecté

                joueur targetJoueur = main.gameUtils.getJoueur(target);
                if (targetJoueur == null) {
                    joueur.sendPMessage(_red+"Le joueur choisi n'existe pas ou n'est pas connecté.");
                    return true;
                } // le joueur visé existe et est connecté

                if (joueur.lastProtected == target) {
                    joueur.sendPMessage(_red+"Le joueur choisi a déjà été salvaté l'épisode dernier.");
                    return true;
                }

                joueur.hasProtected = true;
                targetJoueur.isProtected = true;
                joueur.lastProtected = target;

                PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (main.timerUtils.timeForEpisode / 50), 0, false, false);
                main.powerUtils.giveEffect(targetJoueur, resistance);

                targetJoueur.sendPMessage("Le "+_green+"Salvateur"+_white+" vous a salvaté! Vous disposez de l'effet "+_gray+"Resistance"+_white+" ainsi que "+_gray+"NoFall"+_white+" pendant cette épisode.");
            } else if (sousCommande.equalsIgnoreCase("tirer")) {
                if (!main.gameUtils.isPlayerJoueur(player)) {
                    return true;
                } // le player est un joueur

                joueur joueur = main.gameUtils.getJoueur(player);
                if (joueur.getRole() != roles.Chasseur) {
                    return true;
                } // il a le role voyante bavarde

                if (!joueur.canFire) {

                    return true;
                } // il peut tirer


            }
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (main.gameUtils.isHost(player) || main.playerUtils.isOwner(player)) {
                    arguments.add("start");
                    arguments.add("stop");
                }

                if (main.gameUtils.isHost(player) || main.gameUtils.isCohost(player) || main.playerUtils.isOwner(player)) {
                    arguments.add("composition");
                    arguments.add("map");
                }

                if (main.gameUtils.isPlayerJoueur(player)) {
                    joueur joueur = main.gameUtils.getJoueur(player);

                    if (joueur.getRole() != null) {
                        arguments.add("me");
                        arguments.add("role");
                    }

                    if (joueur.isLoup_Liste()) {
                        arguments.add("liste");
                    }

                    switch (joueur.getRole()) {
                        case Enfant_Sauvage:
                            arguments.add("choisir"); // cbon
                            break;
                        case Voyante_Bavarde:
                            arguments.add("voir"); // cbon
                            break;
                        case Salvateur:
                            arguments.add("salvater"); // cbon
                            break;
                        case Chasseur:
                            arguments.add("tirer"); // cbon
                            break;
                    }
                }
            }

            arguments.add("end");
            arguments.add("test");
            return arguments;
        } else if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();

            for (joueur joueur : main.gameUtils.joueurPlayer.keySet()) {
                playerNames.add(joueur.getName());
            }

            return playerNames;
        }

        return Collections.emptyList();
    }
}
