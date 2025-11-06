package scriptservice.uhc.loupgarou.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.enums.roles;
import scriptservice.uhc.loupgarou.utils.compoUtils;

public class compoListener implements Listener {
    private final Main main;

    public compoListener(Main main) {
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

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {return;} // pas d'inventaire ?
        if (!(event.getWhoClicked() instanceof Player)) {return;} // le click ne viens pas d'un player
        if (event.getCurrentItem() == null) {return;} // l'item clické est null

        // bon inventaire ?
        if (event.getClickedInventory().getTitle().equalsIgnoreCase(main.compoMenuName)) {
            event.setCancelled(true);

            final ItemStack clickedItem = event.getCurrentItem();
            final Player player = (Player) event.getWhoClicked();
            final boolean isRightClick = event.isRightClick();
            final boolean isLeftClick = event.isLeftClick();
            final int slotClicked = event.getRawSlot();
            final roles roleFromSlot = main.compoUtils.roleListInt.get(slotClicked);

            if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) {return;}

            if (isRightClick) {
                if (roleFromSlot.getNumber() >= 1) {
                    roleFromSlot.setNumber(roleFromSlot.getNumber() - 1);
                    player.sendMessage(main.chatPrefix_debug + "Le rôle '" + _green + roleFromSlot.getName() + _gray + "' a bien été enlevé.");
                } else {
                    player.sendMessage(main.chatPrefix_debug + "Le rôle '" + _green + roleFromSlot.getName() + _gray + "' n'est pas dans la composition.");
                }
            } else if (isLeftClick) {
                if (roleFromSlot.getNumber()  <= 63) {
                    roleFromSlot.setNumber(roleFromSlot.getNumber() + 1);
                    player.sendMessage(main.chatPrefix_debug + "Le rôle '" + _green + roleFromSlot.getName() + _gray + "' a bien été ajouté.");
                } else {
                    player.sendMessage(main.chatPrefix_debug + "Le rôle '" + _green + roleFromSlot.getName() + _gray + "' n'a pas pu être ajouté.");
                }
            }

            // on update les items
            main.compoUtils.initItems(event.getClickedInventory());
        }
    }

    @EventHandler
    public void OnInventoryClick(InventoryDragEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(main.compoMenuName)) {
            event.setCancelled(true);
        }
    }
}
