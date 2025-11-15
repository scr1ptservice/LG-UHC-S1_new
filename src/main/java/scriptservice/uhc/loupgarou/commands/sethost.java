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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        System.out.println(sender.getClass());

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.playerUtils.isOwner(player)) {
                main.gameUtils.host = player.getUniqueId();
                main.playerUtils.sendMessageToAll(main.configPrefix+_gold+player.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");
                return true;
            }

            if (!main.gameUtils.isHost(player)) {
                player.sendMessage(main.configPrefix + "Vous n'êtes pas l'host de la partie.");
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /sethost <pseudo>");
                return true;
            }

            Player newHost = Bukkit.getPlayer(args[0]);
            if (newHost == null) {
                player.sendMessage(main.configPrefix+_red+"Le joueur n'existe pas ou n'est pas connecté.");
                return true;
            }

            if (newHost.getUniqueId().toString().equals(main.gameUtils.host.toString())) {
                player.sendMessage(main.configPrefix+_red+"Le joueur est déjà host.");
                return true;
            }

            main.gameUtils.host = newHost.getUniqueId();
            main.playerUtils.sendMessageToAll(main.configPrefix+_gold+newHost.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");

        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

            if (args.length != 1) {
                player.sendMessage(main.configPrefix+_red+"Usage: /sethost <pseudo>");
                return true;
            }

            Player newHost = Bukkit.getPlayer(args[0]);
            if (newHost == null) {
                player.sendMessage(main.configPrefix + "Le joueur n'existe pas ou n'est pas connecté.");
                return true;
            }

            if (newHost.getUniqueId().toString().equals(main.gameUtils.host.toString())) {
                player.sendMessage(main.configPrefix+_red+"Le joueur est déjà host.");
                return true;
            }

            main.gameUtils.host = newHost.getUniqueId();
            main.playerUtils.sendMessageToAll(main.configPrefix+_yellow+newHost.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");
            player.sendMessage(main.configPrefix+_yellow+newHost.getName()+_white+" deviens désormais l'"+_pink+"host"+_white+" de la partie.");
        }

        return true;
    }
}
