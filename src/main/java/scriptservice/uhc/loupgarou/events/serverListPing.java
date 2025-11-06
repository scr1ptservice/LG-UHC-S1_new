package scriptservice.uhc.loupgarou.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import scriptservice.uhc.loupgarou.Main;

import java.util.Random;

public class serverListPing implements Listener {
    private final Main main;

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

    private final Random rand = new Random();

    public serverListPing(Main main) {
        this.main = main;
    }

    // https://www.spigotmc.org/threads/center-motds-and-messages.354209/ + moi
    private String centerText(String text, int lineLength) {
        char[] chars = text.toCharArray(); // Get a list of all characters in text
        boolean isBold = false;
        double length = 0;
        ChatColor pholder = null;
        for (int i = 0; i < chars.length; i++) { // Loop through all characters
            // Check if the character is a ColorCode..
            if (chars[i] == '§' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
                    isBold = (chars[i + 1] == 'l'); // Setting bold  to true or false, depending on if the ChatColor is Bold.
                    length-=2; // Removing TWO from the length, of the string, because we don't wanna count color codes. (c'est 2 chars, j'ai fix)
                    i += isBold ? 1 : 0;
                }
            } else {
                // If the character is not a color code:
                length++; // Adding a space
                length += (isBold ? (chars[i] != ' ' ? 0.1555555555555556 : 0) : 0); // Adding 0.156 spaces if the character is bold.
            }
        }

        double spaces = (lineLength - length) / 2; // Getting the spaces to add by (max line length - length) / 2

        // Adding the spaces
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            builder.append(' ');
        }
        String copy = builder.toString();
        builder.append(text).append(copy);

        return builder.toString();
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        int randomNum = rand.nextInt(100 - 1 + 1) + 1; // +1 makes it inclusive of y

        String message = centerText(_dgray+_bold+"✮  "+_purple+_bold+"scriptservice"+_white+_bold+" UHC"+_dgray+" - "+_white+"1.8.9"+_dgray+_bold+"  ✮", 44)+"\n";
        String afterMessage;

        if (randomNum == 50) {
            message = centerText(_dgray+_bold+"✦  "+_pink+_bold+"femboyprime"+_white+_bold+" UHC"+_dgray+" - "+_white+"1.8.9"+_dgray+_bold+"  ✦", 42)+"\n";
        }

        switch (main.state) {
            case WAITING:
                afterMessage = centerText(_blue+"Loup-Garou S1"+_dgray+_bold+" | "+_white+"En attente de joueur.",50);
                break;
            case STARTING:
                afterMessage = centerText(_blue+"Loup-Garou S1"+_dgray+_bold+" | "+_white+"Demarrage de la partie.",50);
                break;
            case PREGAME:
            case GAME:
                afterMessage = centerText(_blue+"Loup-Garou S1"+_dgray+_bold+" | "+_white+"En partie.",50);
                break;
            case FINISH:
                afterMessage = centerText(_blue+"Loup-Garou S1"+_dgray+_bold+" | "+_white+"Partie fini.",50);
                break;
            default:
                afterMessage = centerText(_blue+"Loup-Garou S1"+_dgray+_bold+" | "+_white+"blah blah blah.",50);
                break;

        }

        event.setMotd(message + afterMessage);
    }


}
