package scriptservice.uhc.loupgarou;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import scriptservice.uhc.loupgarou.commands.*;
import scriptservice.uhc.loupgarou.enums.*;
import scriptservice.uhc.loupgarou.events.*;
import scriptservice.uhc.loupgarou.utils.*;

public final class Main extends JavaPlugin {
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

    //---- noms des inventaires ----//
    public String compoMenuName = (_gold+_bold+"Composition de la partie.");
    //---- noms des inventaires ----//

    //---- prefix pour le chat, etc ----//
    public String chatPrefix = (_dgray+"["+_yellow+_bold+"LGUHC"+_dgray+"] "+_white);
    public String chatPrefix_debug = (_dgray+"["+_red+_bold+"LGUHC"+_dgray+"] "+_gray);
    //---- prefix pour le chat, etc ----//

    //---- uhc stuff ----//
    public String worldName = null;
    public World world = null;
    public states state = states.WAITING;
    public powerUtils powerUtils = new powerUtils(this);
    public playerUtils playerUtils = new playerUtils(this);
    public timerUtils timerUtils = new timerUtils(this);
    public gameUtils gameUtils = new gameUtils(this);
    public compoUtils compoUtils = new compoUtils(this);
    public mapUtils mapUtils = new mapUtils(this);
    //---- uhc stuff ----//

    @Override
    public void onEnable() {
        // on register la commande (l'unique)
        getCommand("lg").setExecutor(new lg(this));

        // on register tout les events
        getServer().getPluginManager().registerEvents(new compoListener(this), this);
        getServer().getPluginManager().registerEvents(new playerDeath(this), this);
        getServer().getPluginManager().registerEvents(new playerFallDamage(this), this);
        getServer().getPluginManager().registerEvents(new serverListPing(this), this);

        // les descriptions des differents roles
        roles.Petite_Fille.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Vous disposez de l'effet " + _dblue + "Night Vision" + _blue + " en permanence, ainsi que des effets "+_gray+"Invisibility I"+_blue+" et " +_dgray+ "Weakness I" + _blue + " la nuit. Vous disposez également de 2 potions de " + _white + "Speed I" + _blue + ". Au crépuscule et au milieu de la nuit, vous connaîtrez les pseudos des joueurs situés dans un rayon de 100 blocks autour de vous.");
        roles.Sorciere.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Pour ce faire, vous disposez de 3 potions splash d'"+_pink+"Instant Health I"+_blue+", d'une potion splash de "+_purple+"Regeneration I"+_blue+" et de 3 potions splash d'"+_dred+"Instant Damage I"+_blue+". Vous avez le pouvoir de ressuciter un joueur une fois dans la partie, à l'aide de la commande "+_white+"/lg sauver <pseudo>"+_blue+".");
        roles.Voyante_Bavarde.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Vous disposez de l'effet " + _dblue + "Night Vision" + _blue + ", de 4 bibliothèques et de 4 blocks d'obsidienne. A chaque début de journée, vous pourrez connaître le rôle d'un joueur à l'aide de la commande "+_white+"/lg voir <pseudo>"+_blue+".");
        roles.Chasseur.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Pour ce faire, vous disposez d'un livre Power IV, 128 flèches, 3 oeufs de loup et de 15 os. A votre mort, vous pourrez tirer sur une personne pour lui faire perdre la moitié de sa vie effective avec la commande "+_white+"/lg tirer <pseudo>"+_blue+".");
        roles.Salvateur.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Pour ce faire, vous disposez de 2 potions splash d'"+_pink+"Instant Health I"+_blue+". A chaque début de journée, vous pouvez choisir un joueur à qui vous conférez "+_gray+"NoFall"+_blue+" et "+_gray+"Resistance"+_blue+" jusqu'a que vous changez de joueur avec la commande "+_white+"/lg salvater <pseudo>"+_blue+".");
        roles.Ancien.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Vous disposez de l'effet "+_gray+"Resistance I"+_blue+" et d'une canne à peche enchanté Luck of the Sea V. A votre mort, si vous êtes tué par un "+_red+"Loup-Garou"+_blue+", vous ressucitez, mais vous perdez l'effet "+_gray+"Résistance"+_blue+", aussi non, vous mourez, et votre tueur perdra la moitié de sa vie effective.");
        roles.Pyromane.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Vous disposez de l'effet "+_gold+"Fire Resistance"+_blue+" en permanence, ainsi qu'un livre "+_gold+"Flame I"+_blue+", un livre "+_gold+"Fire Aspect I"+_blue+", 2 sceaux de lave et d'un briquet.");
        roles.Chaman.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". A chaque mort, le joueur mort pourra vous envoyer un message §4anonyme"+_blue+" que seul vous pouvez voir.");
        roles.Simple_Villageois.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Vous ne disposez d'aucun pouvoirs à votre disposition. §4(petite merde :p)");

        roles.Enfant_Sauvage.setDescription(_blue + "Votre objectif est de gagner avec les " + _green + "Villageois" + _blue + ". Vous choisissez un modèle parmi les joueurs (commande : " + _white + " /lg choisir" + _blue + "). Si celui-ci meurt, vous devenez un " + _red + "Loup-Garou" + _blue + " et devez gagner avec eux.");

        roles.LG_Simple.setDescription(_blue + "Votre objectif est de gagner avec les " + _red + "Loups-Garous" + _blue + ". Pour ce faire, vous disposez des effets " + _red + "Strength I" + _blue + " et " + _dblue + "Night Vision" + _blue + ". A chaque kill, vous gagnez 1 minute de "+_white+"Speed" + _blue + " et 4 coeurs d'"+_yellow+"Absorption" + _blue + " pendant 4 minutes.");
        roles.LG_Blanc.setDescription(_blue + "Votre objectif est de gagner " + _gold + "seul" + _blue + ". Pour ce faire, vous disposez des effets " + _red + "Strength I" + _blue + " et " + _dblue + "Night Vision" + _blue + ", ainsi que d'une deuxième barre de vie. A chaque kill, vous gagnez 1 minute de "+_white+"Speed" + _blue + " et 4 coeurs d'"+_yellow+"Absorption" + _blue + " pendant 4 minutes.");
    }

    @Override
    public void onDisable() {
        System.out.println("c'est ciao :(");
    }
}
