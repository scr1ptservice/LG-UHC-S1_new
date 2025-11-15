package scriptservice.uhc.loupgarou.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.enums.*;
import scriptservice.uhc.loupgarou.resources.ScoreboardSign;

import java.util.UUID;

public class joueur {
    // private stuff
    private final Main main;

    // default stuff
    private Player player;
    private String username;
    private UUID uuid;
    private boolean mort = false;
    private ScoreboardSign scoreboard = null;

    // lg global stuff
    private camps camp;
    private roles role = null;
    private joueur couple = null;

    public boolean canVote = true;
    public joueur votedFor;

    public boolean isProtected_once = false; // used for respawning

    //-// role stuff
    // sorciere
    public boolean canRevive = true;
    // vb
    public boolean hasSeen = false;
    // chasseur
    public boolean canFire = true;
    // salva
    public boolean hasProtected = false;
    public boolean isProtected = false;
    public Player lastProtected = null;
    // ancien
    public boolean hasRespawned = false;
    // es
    public boolean isTransfo = false;
    // chaman
    public boolean hasTextedChaman = false;

    //--// MAIN
    public joueur(Player player, UUID uuid, Main main) {
        // setup this
        this.uuid = uuid;
        this.player = player;

        // server stuff
        setName();

        // setup main
        this.main = main;
    }
    //-// MAIN

    // "set" methods
    public void setPlayer() {
        this.player = Bukkit.getPlayer(uuid);
    }

    public void setName() {
        if (player == null) { setPlayer(); }

        this.username = player.getName();
    }

    public void setRole(roles role) {
        this.role = role;
    }

    public void setCamp(camps camp) {
        this.camp = camp;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setScoreboard(ScoreboardSign scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setMort(boolean bool) {
        this.mort = bool;
    }

    // "get" methods
    public Player getPlayer() { return player; }
    public String getName() { return username; }
    public roles getRole() { return role; }
    public camps getCamp() { return camp; }
    public UUID getUUID() { return uuid; }
    public UUID getUniqueId() { return uuid; }
    public ScoreboardSign getScoreboard() { return scoreboard; }

    // "is" methods
    public boolean isCouple() {return hasCouple(); }
    public boolean isAlive() { return (!mort); }

    public boolean isLoup_Effect() {
        if (getRole()  == roles.LG_Blanc) {
            return true;
        } else if (getRole()  == roles.LG_Simple) {
            return true;
        } else if (getRole() == roles.Enfant_Sauvage && isTransfo) {
            return true;
        } else if (getCamp() == camps.LoupGarou) { //-// omg l'infection (ya pas en saison 1 ...)
            return true;
        } else {
            return false;
        }
    }

    public boolean isLoup_Kill() {
        if (getRole()  == roles.LG_Blanc) {
            return true;
        } else if (getRole()  == roles.LG_Simple) {
            return true;
        } else if (getRole() == roles.Enfant_Sauvage && isTransfo) {
            return true;
        } else if (getCamp() == camps.LoupGarou) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLoup_Liste() {
        if (getRole()  == roles.LG_Blanc) {
            return true;
        } else if (getRole()  == roles.LG_Simple) {
            return true;
        } else if (getRole() == roles.Enfant_Sauvage && isTransfo) {
            return true;
        } else if (getCamp() == camps.LoupGarou) {
            return true;
        } else {
            return false;
        }
    }

    // "has" methods
    public boolean hasCouple() {
        return (couple != null);
    }

    // other methods
    public void sendMessage(String s) {
        if (getPlayer() == null) { return; }

        getPlayer().sendMessage(s);
    }

    public void sendMessage(String[] strings) {
        if (getPlayer() == null) { return; }

        getPlayer().sendMessage(strings);
    }

    public void sendPMessage(String s) {
        if (getPlayer() == null) { return; }

        getPlayer().sendMessage(main.chatPrefix_prive + s);
    }
}
