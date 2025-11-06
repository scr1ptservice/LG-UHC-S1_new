package scriptservice.uhc.loupgarou.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.enums.roles;
import scriptservice.uhc.loupgarou.enums.states;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class timerUtils {
    private final Main main;

    public timerUtils(Main main) {
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

    //---- timer stuff ----//
    public int timeForEpisode = (1_200_000 / 40); // 1_200_000 = 20 mns
    public int timeForCycle = (timeForEpisode / 2); // 10mns Jour / 10mns Nuit

    public Timer launchTimer;
    public TimerTask launchTimerTask;

    public Timer mainTimer;
    public TimerTask mainTimerTask;

    public Timer episodeTimer;
    public TimerTask episodeTimerTask;

    public Timer cycleTimer;
    public TimerTask cycleTimerTask;
    //---- timer stuff ----//

    //---- fonctions ----//
    public void stop_cdUHC() {
        main.playerUtils.sendTitleToAll(_red+"Arrêt de l'"+_gold+"UHC"+_red+".", "");
        main.playerUtils.sendMessageToAll(_red+"Arrêt de l'"+_gold+"UHC"+_red+".");

        main.state = states.WAITING;
        launchTimer.cancel();
    }

    public void launch_cdUHC() {
        int nombreRoles = 0;
        for (roles role : roles.values()) {
            nombreRoles = nombreRoles + role.getNumber();
        }

        if (nombreRoles != Bukkit.getOnlinePlayers().size()) {
            if (nombreRoles > Bukkit.getOnlinePlayers().size()) {
                main.playerUtils.sendMessageToAll(main.chatPrefix + "Il n'y a pas assez de "+_green+"joueurs"+_white+" pour commencer la partie.");
            } else {
                main.playerUtils.sendMessageToAll(main.chatPrefix + "Il n'y a pas assez de "+_green+"rôles"+_white+" pour commencer la partie.");
            }

            main.state = states.WAITING;
            return;
        }

        main.state = states.STARTING;
        launchTimer = new Timer();
        launchTimerTask = new TimerTask() {
            int counter = 10; // le temps avant que l'UHC se lance.

            @Override
            public void run() {
                main.playerUtils.playSoundToAll(Sound.ORB_PICKUP, 1, 1);

                if (counter > 0) {
                    // le counter n'est pas a 0.
                    main.playerUtils.sendTitleToAll(_yellow+"Lançement de l'"+_underline+_gold+"UHC"+_reset+_yellow+".", (_green + counter + "s"));

                    counter = counter - 1;
                } else {
                    // le counter est a 0.
                    main.playerUtils.sendTitleToAll(_yellow+"Bon jeux!", "");

                    main.gameUtils.startGame();
                    launchTimer.cancel();
                }

            }

        };

        launchTimer.scheduleAtFixedRate(launchTimerTask, Calendar.getInstance().getTime(), (1 * 1000));
    }
    //---- fonctions ----//
}
