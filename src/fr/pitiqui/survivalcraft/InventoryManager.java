package fr.pitiqui.survivalcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryManager implements Listener {
	public static void createMenu(Player p) {
		double slot = Bukkit.getOnlinePlayers().size();
		slot /= 9;
		slot = Math.ceil(slot);
		slot *= 9;
        Inventory inv = Bukkit.createInventory(null, (int) slot, ChatColor.GRAY + "Teleport");
        
        for(Player loop : Bukkit.getOnlinePlayers()) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headm = (SkullMeta) head.getItemMeta();

            headm.setOwner(loop.getName());
            headm.setDisplayName(loop.getName());
            head.setItemMeta(headm);
           
            inv.addItem(head);
        }

        p.openInventory(inv);
	}
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void InventoryClick(InventoryClickEvent e){
            if(ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Teleport")){
                    //Player p = (Player) e.getWhoClicked();
                    e.setCancelled(true);
            }
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
                    return;
            }
           
            switch(e.getCurrentItem().getType()){
            case SKULL_ITEM:
                    Player p = (Player) e.getWhoClicked();
                    p.sendMessage(ChatColor.GREEN + "You have been teleported to " + Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getDisplayName());
                    p.teleport(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getLocation());
                    p.closeInventory();
                    break;
            case POTION:
                    Player p2 = (Player) e.getWhoClicked();
                    p2.closeInventory();
                    break;
            case BED:
                    Player p3 = (Player) e.getWhoClicked();
                    p3.closeInventory();
                    break;
           
            default:
                    break;
            }
    }
}
