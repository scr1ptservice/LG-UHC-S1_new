package scriptservice.uhc.loupgarou.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import scriptservice.uhc.loupgarou.Main;

public class sethost implements CommandExecutor {
    private final Main main;
    public sethost(Main main) {
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

    // command
    private void _setHost(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.playerUtils.isOwner(player)) {
                main.gameUtils.host = player.getUniqueId();
                main.playerUtils.sendMessageToAll(main.configPrefix+_gold+player.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");
                return;
            }

            if (!main.gameUtils.isHost(player)) {
                player.sendMessage(main.configPrefix + "Vous n'êtes pas l'host de la partie.");
                return;
            }

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /sethost <pseudo>");
                return;
            }

            Player newHost = Bukkit.getPlayer(args[0]);
            if (newHost == null) {
                player.sendMessage(main.configPrefix+_red+"Le joueur n'existe pas ou n'est pas connecté.");
                return;
            }

            if (main.gameUtils.isHost(newHost)) {
                player.sendMessage(main.configPrefix+_red+"Le joueur est déjà host.");
                return;
            }

            main.gameUtils.host = newHost.getUniqueId();
            main.playerUtils.sendMessageToAll(main.configPrefix+_gold+newHost.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");


        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /sethost <pseudo>");
                return;
            }

            Player newHost = Bukkit.getPlayer(args[0]);
            if (newHost == null) {
                player.sendMessage(main.configPrefix + "Le joueur n'existe pas ou n'est pas connecte.");
                return;
            }

            if (main.gameUtils.isHost(newHost)) {
                player.sendMessage(main.configPrefix+_red+"Le joueur est deja host.");
                return;
            }

            main.gameUtils.host = newHost.getUniqueId();
            main.playerUtils.sendMessageToAll(main.configPrefix+_yellow+newHost.getName()+_white+" deviens desormais l'"+_pink+"host"+_white+" de la partie.");
            player.sendMessage(main.configPrefix+_yellow+newHost.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");


        } else {
            System.out.println("[LG-UHC-S1] '_setHost(sender)' sender has unhandled class: " + sender.getClass());
        }
    }

    // event
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        _setHost(sender, args);

        return true;
    }
}
