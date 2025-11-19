package scriptservice.uhc.loupgarou.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import scriptservice.uhc.loupgarou.Main;

import java.util.UUID;

public class cohost implements CommandExecutor {
    private final Main main;
    public cohost(Main main) {
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
    private void _liste(CommandSender sender, String[] args) {
        StringBuilder message = new StringBuilder((main.configPrefix + "Co-hosts: " + _red));

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.gameUtils.cohosts.isEmpty()) {
                message.append(_red).append("Aucun.");
            } else {
                for (UUID id : main.gameUtils.cohosts) {
                    Player cohost = Bukkit.getPlayer(id);
                    if (cohost == null) {
                        continue;
                    }

                    message.append(_red).append(cohost.getName()).append(", ");
                }
            }

            player.sendMessage(message.substring(0, message.length() - (main.gameUtils.cohosts.isEmpty() ? 0 : 2))  );
        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

            if (main.gameUtils.cohosts.isEmpty()) {
                message.append(_red).append("Aucun.");
            } else {
                for (UUID id : main.gameUtils.cohosts) {
                    Player cohost = Bukkit.getPlayer(id);
                    if (cohost == null) {
                        continue;
                    }

                    message.append(_red).append(cohost.getName()).append(", ");
                }
            }

            player.sendMessage(message.substring(0, message.length() - (main.gameUtils.cohosts.isEmpty() ? 0 : 2))  );
        } else {
            System.out.println("[LG-UHC-S1] '_liste(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _add(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.playerUtils.isOwner(player) || !main.gameUtils.isHost(player)) {
                player.sendMessage(main.chatPrefix + "Vous n'êtes pas l'host de la partie.");
                return;
            }

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /cohost add <pseudo>");
                return;
            }

            Player cohost = Bukkit.getPlayer(args[0]);
            if (cohost == null) {
                player.sendMessage(main.configPrefix + "Le joueur n'existe pas ou n'est pas connecté.");
                return;
            }

            if (main.gameUtils.cohosts.contains(cohost.getUniqueId())) {
                player.sendMessage(main.configPrefix + "Le joueur est déjà co-host.");
                return;
            }

            main.gameUtils.cohosts.add(cohost.getUniqueId());
            player.sendMessage(main.configPrefix + _yellow + cohost.getName() + " est désormais "+_pink+"co-host"+_white+".");
            cohost.sendMessage(main.configPrefix + "Vous êtes desormais "+_pink+"co-host"+_white+".");

        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /cohost add <pseudo>");
                return;
            }

            Player cohost = Bukkit.getPlayer(args[0]);
            if (cohost == null) {
                player.sendMessage(main.configPrefix + "Le joueur n'existe pas ou n'est pas connecté.");
                return;
            }

            if (main.gameUtils.cohosts.contains(cohost.getUniqueId())) {
                player.sendMessage(main.configPrefix + "Le joueur est déjà co-host.");
                return;
            }

            main.gameUtils.cohosts.add(cohost.getUniqueId());
            player.sendMessage(main.configPrefix + _yellow + cohost.getName() + " est désormais "+_pink+"co-host"+_white+".");
            cohost.sendMessage(main.configPrefix + "Vous êtes desormais "+_pink+"co-host"+_white+".");

        } else {
            System.out.println("[LG-UHC-S1] '_add(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _remove(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.playerUtils.isOwner(player) || !main.gameUtils.isHost(player)) {
                player.sendMessage(main.chatPrefix + "Vous n'êtes pas l'host de la partie.");
                return;
            }

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /cohost remove <pseudo>");
                return;
            }

            Player cohost = Bukkit.getPlayer(args[0]);
            if (cohost == null) {
                player.sendMessage(main.configPrefix + "Le joueur n'existe pas ou n'est pas connecté.");
                return;
            }

            if (!main.gameUtils.cohosts.contains(cohost.getUniqueId())) {
                player.sendMessage(main.configPrefix + "Le joueur n'est pas co-host.");
                return;
            }

            main.gameUtils.cohosts.add(cohost.getUniqueId());
            player.sendMessage(main.configPrefix + _yellow + cohost.getName() + " n'est plus "+_pink+"co-host"+_white+".");
            cohost.sendMessage(main.configPrefix + "Vous n'êtes plus "+_pink+"co-host"+_white+".");

        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /cohost remove <pseudo>");
                return;
            }

            Player cohost = Bukkit.getPlayer(args[0]);
            if (cohost == null) {
                player.sendMessage(main.configPrefix + "Le joueur n'existe pas ou n'est pas connecté.");
                return;
            }

            if (!main.gameUtils.cohosts.contains(cohost.getUniqueId())) {
                player.sendMessage(main.configPrefix + "Le joueur n'est pas co-host.");
                return;
            }

            main.gameUtils.cohosts.add(cohost.getUniqueId());
            player.sendMessage(main.configPrefix + _yellow + cohost.getName() + " n'est plus "+_pink+"co-host"+_white+".");
            cohost.sendMessage(main.configPrefix + "Vous n'êtes plus "+_pink+"co-host"+_white+".");

        } else {
            System.out.println("[LG-UHC-S1] '_remove(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    private void _cmdlist(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

        } else {
            System.out.println("[LG-UHC-S1] '_cmdlist(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    // event
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            _cmdlist(sender, args);
        } else {
            String sousCommande = args[0];

            if (sousCommande.equalsIgnoreCase("liste") || sousCommande.equalsIgnoreCase("list")) {
                _liste(sender, args);
            } else if (sousCommande.equalsIgnoreCase("add")) {
                _add(sender, args);
            } else if (sousCommande.equalsIgnoreCase("remove")) {
                _remove(sender, args);
            }
        }

        return true;
    }
}
