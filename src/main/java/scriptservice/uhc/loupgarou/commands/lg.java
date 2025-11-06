package scriptservice.uhc.loupgarou.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.classes.winResult;
import scriptservice.uhc.loupgarou.enums.states;
import scriptservice.uhc.loupgarou.utils.*;

public class lg implements CommandExecutor {
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
                    _dgray+"> "+_green+"composition",
                    _dgray+"> "+_yellow+"start",
                    _dgray+"> "+_green+"stop",
                    _dgray+"> "+_red+"end",
                    _dgray+"> "+_green+"map",
                    _dgray+"> "+_red+"liste",
                    _dgray+"> "+_purple+"test"
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

                    player.sendMessage("isWin: " + result.isWin());
                    player.sendMessage("winMessage: " + result.getWinMessage());
                    player.sendMessage("camp: " + result.getCamp().toString());
                }
            } else if (sousCommande.equalsIgnoreCase("test")) {
                if (!main.playerUtils.isOwner(player)) {
                    player.sendMessage("");
                }

            }
        }

        return true;
    }
}
