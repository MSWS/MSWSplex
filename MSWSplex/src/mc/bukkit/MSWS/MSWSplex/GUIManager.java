package mc.bukkit.MSWS.MSWSplex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GUIManager {
	@SuppressWarnings("deprecation")
	public static void makeInventory(Player player, String name) {
		for (String res : Main.guis.getKeys(false)) {
			if (res.equals(name)) {
				ConfigurationSection gItem = Main.guis.getConfigurationSection(name);
				Inventory gui = Bukkit.createInventory(null, Main.guis.getInt(res + ".size"),
						Main.cST(Main.color(Main.guis.getString(res + ".name")), player));
				for (String items : gItem.getKeys(false)) {
					if (gItem.contains(items + ".icon")) {
						ItemStack tempItem = new ItemStack(Material.valueOf(gItem.getString(items + ".icon")),
								Math.max(gItem.getInt(items + ".amount"), 1));
						ItemMeta meta = tempItem.getItemMeta();
						if (gItem.contains(items + ".data"))
							tempItem.setDurability((short) gItem.getInt(items + ".data"));
						if (gItem.contains(items + ".skullowner")) {
							SkullMeta skullMeta = (SkullMeta) tempItem.getItemMeta();
							skullMeta.setOwner(Main.cST(gItem.getString(items + ".skullowner"), player));
							tempItem.setItemMeta(skullMeta);
							meta = tempItem.getItemMeta();
						}

						if (gItem.contains(items + ".lore")) {
							List<String> newLore = new ArrayList<String>();
							int amo = 0;
							for (World world : Bukkit.getWorlds()) {
								if (world.getName().toLowerCase().contains(items.toLowerCase())) {
									amo += world.getPlayers().size();
								}
							}
							for (String loreLine : gItem.getStringList(items + ".lore"))
								newLore.add(Main.color("&r" + Main.cST(loreLine, player).replace("%enabled%",
										Main.TorF(Main.getPref(player.getUniqueId(), items)))));
							if (name.matches("(?i)(gamemenu)")) {
								newLore.add("");
								newLore.add(Main.color("&rJoin &a" + amo + " &rother players!"));
							}
							meta.setLore(newLore);
						}
						meta.setDisplayName(Main.color(Main.cST(gItem.getString(items + ".name"), player)));
						tempItem.setItemMeta(meta);
						if (gItem.contains(items + ".rank")) {
							if (Main.getRankID(player.getUniqueId()).matches(gItem.getString(items + ".rank"))) {
								gui.setItem(gItem.getInt(items + ".slot"), tempItem);
							}
						} else {
							gui.setItem(gItem.getInt(items + ".slot"), tempItem);
						}
					}
				}
				ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 3);
				ItemMeta meta = glass.getItemMeta();
				meta.setDisplayName(" ");
				glass.setItemMeta(meta);
				if (Main.guis.getBoolean(res + ".GlassPanes")) {
					for (int i = 0; i < gui.getSize(); i++) {
						if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR)
							gui.setItem(i, glass);
					}
				}
				player.openInventory(gui);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static Inventory getInventory(Player player, String name) {
		for (String res : Main.guis.getKeys(false)) {
			if (res.equals(name)) {
				ConfigurationSection gItem = Main.guis.getConfigurationSection(name);
				Inventory gui = Bukkit.createInventory(null, Main.guis.getInt(res + ".size"),
						Main.cST(Main.color(Main.guis.getString(res + ".name")), player));
				for (String items : gItem.getKeys(false)) {
					if (gItem.contains(items + ".icon")) {
						ItemStack tempItem = new ItemStack(Material.valueOf(gItem.getString(items + ".icon")),
								Math.max(gItem.getInt(items + ".amount"), 1));
						ItemMeta meta = tempItem.getItemMeta();
						if (gItem.contains(items + ".data"))
							tempItem.setDurability((short) gItem.getInt(items + ".data"));
						if (gItem.contains(items + ".skullowner")) {
							SkullMeta skullMeta = (SkullMeta) tempItem.getItemMeta();
							skullMeta.setOwner(Main.cST(gItem.getString(items + ".skullowner"), player));
							tempItem.setItemMeta(skullMeta);
							meta = tempItem.getItemMeta();
						}

						if (gItem.contains(items + ".lore")) {
							List<String> newLore = new ArrayList<String>();
							int amo = 0;
							for (World world : Bukkit.getWorlds()) {
								if (world.getName().contains(items)) {
									amo += world.getPlayers().size();
								}
							}
							for (String loreLine : gItem.getStringList(items + ".lore"))
								newLore.add(Main.color("&r" + Main.cST(loreLine, player)));
							if (name.matches("(?i)(gamemenu)")) {
								newLore.add("");
								newLore.add(Main.color("&rJoin &a" + amo + " &rother players!"));
							}
							meta.setLore(newLore);
						}
						meta.setDisplayName(Main.color(Main.cST(gItem.getString(items + ".name"), player)));
						tempItem.setItemMeta(meta);
						if (gItem.contains(items + ".rank")) {
							if (Main.getRankID(player.getUniqueId()).matches(gItem.getString(items + ".rank"))) {
								gui.setItem(gItem.getInt(items + ".slot"), tempItem);
							}
						} else {
							gui.setItem(gItem.getInt(items + ".slot"), tempItem);
						}
						if (gItem.contains(items + ".comRank")) {
							if (Main.getRank(player,
									Main.activeCom.get(player)) >= (gItem.getInt(items + ".comRank"))) {
								gui.setItem(gItem.getInt(items + ".slot"), tempItem);
							}
						} else {
							gui.setItem(gItem.getInt(items + ".slot"), tempItem);
						}
					}
				}
				ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 3);
				ItemMeta meta = glass.getItemMeta();
				meta.setDisplayName(" ");
				glass.setItemMeta(meta);
				if (Main.guis.getBoolean(res + ".GlassPanes")) {
					for (int i = 0; i < gui.getSize(); i++) {
						if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR)
							gui.setItem(i, glass);
					}
				}
				return gui;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void makePurchase(Player player, String name, int cost, int amo, String type, Material item) {
		if (cost == -1)
			return;
		Main.cost.put(player, cost);
		player.closeInventory();
		Main.purchasing.put(player, name);
		Main.costType.put(player, type);
		Main.costAmo.put(player, amo);
		Inventory inv = Bukkit.createInventory(null, 54,
				Main.camelCase(Main.color("Purchase " + name.replace("chest", " Chest"))));
		ItemStack accept = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 5);
		ItemStack deny = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 14);
		ItemStack itema = new ItemStack(item);
		ItemMeta ameta = accept.getItemMeta();
		ItemMeta dmeta = accept.getItemMeta();
		ItemMeta meta = itema.getItemMeta();
		meta.setDisplayName(Main.color("&a&lPurchase " + amo + " " + GadgetManager.gadgetName(name) + " for &e" + cost
				+ " &b&l" + Main.camelCase(type)));
		ameta.setDisplayName(Main.color("&a&lOk"));
		dmeta.setDisplayName(Main.color("&c&lCancel"));
		itema.setItemMeta(meta);
		accept.setItemMeta(ameta);
		deny.setItemMeta(dmeta);
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				inv.setItem(27 + x + (9 * y), accept);
				inv.setItem(33 + x + (9 * y), deny);
			}
		}
		inv.setItem(4, itema);
		player.openInventory(inv);
	}
}
