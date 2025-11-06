package scriptservice.uhc.loupgarou.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import scriptservice.uhc.loupgarou.Main;
import scriptservice.uhc.loupgarou.enums.roles;

import java.util.Arrays;
import java.util.HashMap;

public class compoUtils {
    private final Main main;
    public HashMap<Integer, roles> roleListInt = new HashMap<>();

    public compoUtils(Main main) {
        this.main = main;
    }

    //---- fonctions ----//
    private ItemStack createDyeItem(final int dyeInt, final int roleAmount, final String itemName, final String... itemLore) {
        final ItemStack dyeItem = new ItemStack(Material.INK_SACK, roleAmount, (byte) dyeInt);
        final ItemMeta dyeMeta = dyeItem.getItemMeta();

        dyeMeta.setDisplayName(itemName);
        dyeMeta.setLore(Arrays.asList(itemLore));

        dyeItem.setItemMeta(dyeMeta);

        return dyeItem;
    }

    public void initItems(Inventory inventory) {
        if (inventory == null) {return;} // il n'y a pas d'inventaire ??
        inventory.clear();

        int roleInt = 0;
        for (roles role : roles.values()) {
            roleListInt.put(roleInt, role);
            roleInt = roleInt + 1;

            if (role.getNumber() > 0) {
                inventory.addItem(createDyeItem(role.getIntColor(), role.getNumber(), (role.getStrColor() + role.getName()), "Le role est activé :)"));
            } else {
                inventory.addItem(createDyeItem(8, 1, (role.getStrColor() + role.getName()), "le role n'est pas activé :("));
            }
        }
    }

    public void openMenu(Player player) {
        // nouveau inventaire
        Inventory menu = Bukkit.createInventory(player, 27, main.compoMenuName);

        // les items selectable
        initItems(menu);

        // on ouvre l'inventaire
        player.openInventory(menu);
    }
    //---- fonctions ----//
}
