package scriptservice.uhc.loupgarou.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.*;
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

    // commands
    private void _cmdlist(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

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
            System.out.println("[LG-UHC-S1] '_cmdlist(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _composition(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            main.compoUtils.openMenu(player);

        } else {
            System.out.println("[LG-UHC-S1] '_composition(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _map(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            main.mapUtils.setMainMap(player);

        } else {
            System.out.println("[LG-UHC-S1] '_map(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _start(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.state == states.WAITING) {
                main.timerUtils.launch_cdUHC();
            } else {
                player.sendMessage(main.chatPrefix_debug + "l'UHC a déjà commencé.");
            }

        } else {
            System.out.println("[LG-UHC-S1] '_start(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _stop(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.state == states.WAITING) {
                player.sendMessage(main.chatPrefix_debug + "l'UHC n'a pas encore commencé.");
                return;
            } else if (main.state == states.STARTING) {
                main.timerUtils.stop_cdUHC();
            } else {
                main.gameUtils.stopGame(true);
            }

        } else {
            System.out.println("[LG-UHC-S1] '_stop(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _end(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.state == states.WAITING || main.state == states.PREGAME) {
                player.sendMessage(main.chatPrefix_debug + "l'UHC n'a pas encore commencé / n'a pas encore attribué les rôles.");
            } else {
                winResult result = main.gameUtils.endGame();

                player.sendMessage(_yellow+"isWin: " + (result.isWin() ? (_green+"true") : (_red+"false")));
                player.sendMessage(_yellow+"winMessage: " + result.getWinMessage());
                player.sendMessage(_yellow+"camp: " +_white+ result.getCamp().toString());
            }

        } else {
            System.out.println("[LG-UHC-S1] '_end(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _test(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.playerUtils.isOwner(player)) {
                player.sendMessage(main.chatPrefix_debug + "il n'y a pas de test " + _purple + "(fin, qui sait ^^')");
                return;
            }

            player.sendMessage(main.chatPrefix_debug+"test effectué"+_purple+" :^)");

            Location respawnLocation = new Location(player.getWorld(), (-300 + Math.random() * 600), 100, (-300 + Math.random() * 600));
            player.spigot().respawn();
            player.teleport(respawnLocation);

        } else {
            System.out.println("[LG-UHC-S1] '_test(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _choisirES(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.gameUtils.enfantSauvage.toString().equals(player.getUniqueId().toString())) {
                return;
            }// le joueur est enfant sauvage

            if (main.gameUtils.enfantSauvageTarget != null) {
                player.sendMessage(main.chatPrefix_prive+_red+"Vous avez déjà choisi votre modèle.");
                return;
            } // il n'a pas encore choisi de modele

            if (args.length != 1) {
                player.sendMessage(main.chatPrefix_prive+_red+"Usage: /lg choisir <pseudo>.");
                return;
            } // il a bien ecrit un pseudo en plus

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(main.chatPrefix_prive+_red+"Le joueur choisi n'existe pas ou n'est pas connecté.");
                return;
            } // le joueur visé existe et est connecté

            if (target.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                player.sendMessage(main.chatPrefix_prive+_red+"Vous ne pouvez pas vous choisir vous-même. :/");
                return;
            } // ce n'est pas lui même

            main.gameUtils.enfantSauvageTarget = target.getUniqueId();
            player.sendMessage(main.chatPrefix_prive+"Vous venez de choisir "+_yellow+target.getName()+_white+" comme modèle, si celui-ci vient à périr, vous deviendrez alors "+_red+"Loup-Garou"+_white+" et devrez gagner avec "+_red+"eux"+_white+".");

        } else {
            System.out.println("[LG-UHC-S1] '_choisirES(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _voirVOYANTE(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.gameUtils.isPlayerJoueur(player)) {
                return;
            } // le player est un joueur

            joueur joueur = main.gameUtils.getJoueur(player);
            if (joueur.getRole() != roles.Voyante_Bavarde) {
                return;
            } // il a le role voyante bavarde

            if (joueur.hasSeen) {
                joueur.sendPMessage(_red+"Vous avez déjà regardé le rôle d'un joueur cette épisode.");
                return;
            } // il n'a pas spec cette episode

            if (args.length != 1) {
                joueur.sendPMessage(_red+"Usage: /lg voir <pseudo>.");
                return;
            } // il a bien ecrit un pseudo en plus

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                joueur.sendPMessage(_red+"Le player choisi n'existe pas ou n'est pas connecté.");
                return;
            } // le player visé existe et est connecté

            joueur targetJoueur = main.gameUtils.getJoueur(target);
            if (targetJoueur == null) {
                joueur.sendPMessage(_red+"Le joueur choisi n'existe pas ou n'est pas connecté.");
                return;
            } // le joueur visé existe et est connecté

            if (targetJoueur.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                joueur.sendPMessage(_red+"Vous ne pouvez pas regarder votre propre rôle. :/");
                return;
            } // il ne vise pas lui-même

            joueur.hasSeen = true;
            main.playerUtils.sendMessageToAll(main.chatPrefix + "La " + _green + "Voyante Bavarde" + _white + " a espionné un joueur et son rôle est : " + targetJoueur.getRole().getStrColor() + targetJoueur.getRole().getName() + _white + ".");

        } else {
            System.out.println("[LG-UHC-S1] '_voirVOYANTE(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _salvaterSALVA(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.gameUtils.isPlayerJoueur(player)) {
                return;
            } // le player est un joueur

            joueur joueur = main.gameUtils.getJoueur(player);
            if (joueur.getRole() != roles.Salvateur) {
                return;
            } // il a le role salvateur

            if (joueur.hasProtected) {
                joueur.sendPMessage(_red+"Vous avez déjà salvaté un joueur cette épisode.");
                return;
            } // il n'a pas protegé cette episode

            if (args.length != 1) {
                joueur.sendPMessage(_red+"Usage: /lg salvater <pseudo>.");
                return;
            } // il a bien ecrit un pseudo en plus

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                joueur.sendPMessage(_red+"Le player choisi n'existe pas ou n'est pas connecté.");
                return;
            } // le player visé existe et est connecté

            joueur targetJoueur = main.gameUtils.getJoueur(target);
            if (targetJoueur == null) {
                joueur.sendPMessage(_red+"Le joueur choisi n'existe pas ou n'est pas connecté.");
                return;
            } // le joueur visé existe et est connecté

            if (joueur.lastProtected == target) {
                joueur.sendPMessage(_red+"Le joueur choisi a déjà été salvaté l'épisode dernier.");
                return;
            }

            joueur.hasProtected = true;
            targetJoueur.isProtected = true;
            joueur.lastProtected = target;

            PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (main.timerUtils.timeForEpisode / 50), 0, false, false);
            main.powerUtils.giveEffect(targetJoueur, resistance);

            targetJoueur.sendPMessage("Le "+_green+"Salvateur"+_gray+" vous a salvaté! Vous disposez de l'effet "+_white+"Resistance"+_gray+" ainsi que "+_white+"NoFall"+_gray+" pendant cette épisode.");

        } else {
            System.out.println("[LG-UHC-S1] '_salvaterSALVA(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _tirerCHASSEUR(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.gameUtils.isPlayerJoueur(player)) {
                return;
            } // le player est un joueur

            joueur joueur = main.gameUtils.getJoueur(player);
            if (joueur.getRole() != roles.Chasseur) {
                return;
            } // il a le role chasseur

            if (joueur.isAlive()) {
                joueur.sendPMessage(_red+"Vous êtes encore en vie.. "+_dred+":/");
                return;
            } // il est mort

            if (!joueur.canFire) {
                joueur.sendPMessage(_red+"Vous ne pouvez pas tirer.");
                return;
            } // il peut tirer

            if (args.length != 1) {
                joueur.sendPMessage(_red+"Usage: /lg tirer <pseudo>.");
                return;
            } // il a bien ecrit un pseudo

            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                joueur.sendPMessage(_red+"Le player visé n'existe pas ou n'est pas connecté.");
                return;
            } // il a bien visé un player

            joueur targetJoueur = main.gameUtils.getJoueur(targetPlayer);
            if (targetJoueur == null) {
                joueur.sendPMessage(_red+"Le joueur visé n'existe pas ou n'est pas connecté.");
                return;
            } // il a bien visé un joueur

            targetPlayer.setHealth((targetPlayer.getHealth() / 2));
            main.playerUtils.sendMessageToAll(main.chatPrefix + "Le chasseur a tiré sur "+_yellow+targetPlayer.getName()+_white+", il perd donc la "+_red+"moitié"+_white+" de sa vie effective.");
            main.playerUtils.playSoundToAll(Sound.SHOOT_ARROW);

        } else {
            System.out.println("[LG-UHC-S1] '_tirerCHASSEUR(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    // event
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            _cmdlist(sender, args);
        } else {
            String sousCommande = args[0];

            if (sousCommande.equalsIgnoreCase("composition") || sousCommande.equalsIgnoreCase("compo") || sousCommande.equalsIgnoreCase("c")) {
                _composition(sender, args);
            } else if (sousCommande.equalsIgnoreCase("map")) {
                _map(sender, args);
            } else if (sousCommande.equalsIgnoreCase("start")) {
                _start(sender, args);
            } else if (sousCommande.equalsIgnoreCase("stop")) {
                _stop(sender, args);
            } else if (sousCommande.equalsIgnoreCase("end")) {
                _end(sender, args);
            } else if (sousCommande.equalsIgnoreCase("test")) {
                _test(sender, args);
            } else if (sousCommande.equalsIgnoreCase("choisir")) {
                _choisirES(sender, args);
            } else if (sousCommande.equalsIgnoreCase("voir")) {
                _voirVOYANTE(sender, args);
            } else if (sousCommande.equalsIgnoreCase("salvater")) {
                _salvaterSALVA(sender, args);
            } else if (sousCommande.equalsIgnoreCase("tirer")) {
                _tirerCHASSEUR(sender, args);
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
