package scriptservice.uhc.loupgarou;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import scriptservice.uhc.loupgarou.commands.*;
import scriptservice.uhc.loupgarou.enums.*;
import scriptservice.uhc.loupgarou.events.*;
import scriptservice.uhc.loupgarou.utils.*;

/*
    private void _test(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = (ConsoleCommandSender) sender;

        } else {
            System.out.println("[LG-UHC-S1] '_test(sender)' sender has unhandled class: " + sender.getClass());
        }
    }
 */

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
    public String chatPrefix_prive = (_dgray+"["+_red+_bold+"LGUHC-PRIVÉ"+_dgray+"] "+_gray);
    public String chatPrefix_debug = (_dgray+"["+_dred+_bold+"LGUHC"+_dgray+"] "+_gray);

    public String configPrefix = (_dgray+"["+_gold+_bold+"CONFIG"+_dgray+"]: "+_white);

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
        // on register les commandes
        getCommand("lg").setExecutor(new lg(this));
        getCommand("sethost").setExecutor(new sethost(this)); // je devrais switch c'est commande dans un uhc classique, mais flemme :)
        getCommand("cohost").setExecutor(new cohost(this)); // la même

        // on register tout les events
        getServer().getPluginManager().registerEvents(new compoListener(this), this);
        getServer().getPluginManager().registerEvents(new playerDeath(this), this);
        getServer().getPluginManager().registerEvents(new playerFallDamage(this), this);
        getServer().getPluginManager().registerEvents(new serverListPing(this), this);
        getServer().getPluginManager().registerEvents(new playerChat(this), this);
        getServer().getPluginManager().registerEvents(new playerJoinLeave(this), this);

        // les descriptions des differents roles
        roles.Petite_Fille.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Vous disposez de l'effet " + _dblue + "Night Vision" + _gray + " en permanence, ainsi que des effets "+_gray+"Invisibility I"+_gray+" et " +_dgray+ "Weakness I" + _gray + " la nuit. Vous disposez également de 2 potions de " + _white + "Speed I" + _gray + ". Au crépuscule et au milieu de la nuit, vous connaîtrez les pseudos des joueurs situés dans un rayon de 100 blocks autour de vous.");
        roles.Sorciere.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Pour ce faire, vous disposez de 3 potions splash d'"+_pink+"Instant Health I"+_gray+", d'une potion splash de "+_purple+"Regeneration I"+_gray+" et de 3 potions splash d'"+_dred+"Instant Damage I"+_gray+". Vous avez le pouvoir de ressuciter un joueur une fois dans la partie, à l'aide de la commande "+_white+"/lg sauver <pseudo>"+_gray+".");
        roles.Voyante_Bavarde.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Vous disposez de l'effet " + _dblue + "Night Vision" + _gray + ", de 4 bibliothèques et de 4 blocks d'obsidienne. A chaque début de journée, vous pourrez connaître le rôle d'un joueur à l'aide de la commande "+_white+"/lg voir <pseudo>"+_gray+".");
        roles.Chasseur.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Pour ce faire, vous disposez d'un livre Power IV, 128 flèches, 3 oeufs de loup et de 15 os. A votre mort, vous pourrez tirer sur une personne pour lui faire perdre la moitié de sa vie effective avec la commande "+_white+"/lg tirer <pseudo>"+_gray+".");
        roles.Salvateur.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Pour ce faire, vous disposez de 2 potions splash d'"+_pink+"Instant Health I"+_gray+". A chaque début de journée, vous pouvez choisir un joueur à qui vous conférez "+_gray+"NoFall"+_gray+" et "+_gray+"Resistance"+_gray+" jusqu'a que vous changez de joueur avec la commande "+_white+"/lg salvater <pseudo>"+_gray+".");
        roles.Ancien.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Vous disposez de l'effet "+_gray+"Resistance I"+_gray+" et d'une canne à peche enchanté Luck of the Sea V. A votre mort, si vous êtes tué par un "+_red+"Loup-Garou"+_gray+", vous ressucitez, mais vous perdez l'effet "+_gray+"Résistance"+_gray+", aussi non, vous mourez, et votre tueur perdra la moitié de sa vie effective.");
        roles.Pyromane.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Vous disposez de l'effet "+_gold+"Fire Resistance"+_gray+" en permanence, ainsi qu'un livre "+_gold+"Flame I"+_gray+", un livre "+_gold+"Fire Aspect I"+_gray+", 2 sceaux de lave et d'un briquet.");
        roles.Chaman.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". A chaque mort, le joueur mort pourra vous envoyer un message §4anonyme"+_gray+" que seul vous pouvez voir.");
        roles.Simple_Villageois.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Vous ne disposez d'aucun pouvoirs à votre disposition. §4(petite merde :p)");

        roles.Enfant_Sauvage.setDescription(_gray + "Votre objectif est de gagner avec les " + _green + "Villageois" + _gray + ". Vous choisissez un modèle parmi les joueurs (commande : " + _white + " /lg choisir <pseudo>" + _gray + "). Si celui-ci meurt, vous devenez un " + _red + "Loup-Garou" + _gray + " et devez gagner avec eux.");

        roles.LG_Simple.setDescription(_gray + "Votre objectif est de gagner avec les " + _red + "Loups-Garous" + _gray + ". Pour ce faire, vous disposez des effets " + _red + "Strength I" + _gray + " et " + _dblue + "Night Vision" + _gray + ". A chaque kill, vous gagnez 1 minute de "+_white+"Speed" + _gray + " et 4 coeurs d'"+_yellow+"Absorption" + _gray + " pendant 4 minutes.");
        roles.LG_Blanc.setDescription(_gray + "Votre objectif est de gagner " + _gold + "seul" + _gray + ". Pour ce faire, vous disposez des effets " + _red + "Strength I" + _gray + " et " + _dblue + "Night Vision" + _gray + ", ainsi que d'une deuxième barre de vie. A chaque kill, vous gagnez 1 minute de "+_white+"Speed" + _gray + " et 4 coeurs d'"+_yellow+"Absorption" + _gray + " pendant 4 minutes.");

        System.out.println("[LG-UHC-S1] Enabled plugin successfully !");
    }

    @Override
    public void onDisable() {
        System.out.println("[LG-UHC-S1] Disabled plugin successfully !");
    }
}
