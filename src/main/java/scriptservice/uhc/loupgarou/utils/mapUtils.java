package scriptservice.uhc.loupgarou.utils;

import org.bukkit.entity.Player;
import scriptservice.uhc.loupgarou.Main;

public class mapUtils {
    private final Main main;
    public mapUtils(Main main) {
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

    //---- fonctions ----//
    public void setMainMap(Player player) {
        main.world = player.getWorld();
        main.worldName = player.getWorld().getName();

        player.sendMessage(main.chatPrefix_debug + "Set main map to "+_green+main.worldName+_gray+".");
    }
    //---- fonctions ----//
}
