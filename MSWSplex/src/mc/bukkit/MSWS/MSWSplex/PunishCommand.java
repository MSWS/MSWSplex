package mc.bukkit.MSWS.MSWSplex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PunishCommand {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "p":
		case "punish":
			String prefix = "&9Punish> &7";
			String temprank = "owner";
			if (sender instanceof Player)
				temprank = Main.getRankID(((Player) sender).getUniqueId());
			if (Main.staff.contains(temprank)) {
				if (sender instanceof Player) {
					String msg = "";
					if (args.length == 0) {
						sender.sendMessage(Main.color(prefix + "Commands List:"));
						sender.sendMessage(Main.color("&6/punish &7<player> <reason>"));
						return;
					}
					if (args.length == 1)
						msg = "Checking";
					else {
						for (String res : args) {
							if (res != args[0])
								msg = msg + res + " ";
						}
					}
					if (Bukkit.getOfflinePlayer(args[0]) == null) {
						Main.notFound(args[0], sender, true);
						return;
					}
					Main.reason.put(((Player) sender), msg);
					Main.punishing.put(((Player) sender), args[0]);
					List<String> lore = new ArrayList<String>();
					lore.add(Main.color("&r" + msg));
					Player punisher = (Player) sender;
					Inventory punishGui = Bukkit.createInventory(null, Main.guis.getInt("PunishMenu.size"),
							Main.color(Main.guis.getString("PunishMenu.name").replace("%player%",
									Bukkit.getOfflinePlayer(args[0]).getName())));
					ItemStack skull = new ItemStack(Material.SKULL_ITEM);
					SkullMeta meta = (SkullMeta) skull.getItemMeta();
					skull.setDurability((short) 3);
					meta.setOwner(args[0]);
					meta.setDisplayName(Main.color("&a&l" + Bukkit.getOfflinePlayer(args[0]).getName()));
					meta.setLore(lore);
					skull.setItemMeta(meta);
					punishGui.setItem(4, skull);
					String uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId() + "";
					Integer pastClient1 = 0, pastClient2 = 0, pastClient3 = 0, pastChat1 = 0, pastChat2 = 0,
							pastChat3 = 0, pastChat4 = 0, pastGameplay1 = 0, pastGameplay4 = 0;
					if (Main.data.getConfigurationSection("Users." + uuid + ".history") != null) {
						ConfigurationSection history = Main.data.getConfigurationSection("Users." + uuid + ".history");
						int pos = 0;
						List<String> his = new ArrayList<String>();
						for (String res : history.getKeys(false))
							his.add(res);
						for (int i = his.size() - 1; i >= 0; i--) {
							String res = his.get(i);
							String type = history.getString(res + ".type");
							Integer sev = history.getInt(res + ".severity");
							String reason = history.getString(res + ".reason");
							String user = history.getString(res + ".user");
							String date = history.getString(res + ".date");
							String unbanreason = history.getString(res + ".unbanreason");
							String unbanner = history.getString(res + ".unbanner");
							Material icon = null;
							String itemName = null;
							switch (type) {
							case "other":
								icon = Material.REDSTONE_BLOCK;
								itemName = "Permanent Ban";
								break;
							case "ipban":
								icon = Material.BEDROCK;
								itemName = "Permanent IP Ban";
								break;
							case "warning":
								itemName = "Warning";
								icon = Material.PAPER;
								break;
							case "exploiting":
								switch (sev) {
								case 1:
									if (!history.contains(res + ".unbanner"))
										pastGameplay1++;
									break;
								case 4:
									if (!history.contains(res + ".unbanner"))
										pastGameplay4++;
									break;
								}
								itemName = "General";
								icon = Material.HOPPER;
								break;
							case "hacking":
								switch (sev) {
								case 1:
									if (!history.contains(res + ".unbanner"))
										pastClient1++;
									break;
								case 2:
									if (!history.contains(res + ".unbanner"))
										pastClient2++;
									break;
								case 3:
									if (!history.contains(res + ".unbanner"))
										pastClient3++;
									break;
								}
								itemName = "Client Mod";
								icon = Material.IRON_SWORD;
								break;
							case "chatoffense":
								switch (sev) {
								case 1:
									if (!history.contains(res + ".unbanner"))
										pastChat1++;
									break;
								case 2:
									if (!history.contains(res + ".unbanner"))
										pastChat2++;
									break;
								case 3:
									if (!history.contains(res + ".unbanner"))
										pastChat3++;
									break;
								case 4:
									if (!history.contains(res + ".unbanner"))
										pastChat4++;
									break;
								}
								itemName = "Chat Offense";
								icon = Material.BOOK_AND_QUILL;
								break;
							}
							List<String> punishLore = new ArrayList<String>();
							punishLore.add(Main.color("&ePunishment Type: &r" + Main.camelCase(type)));
							if (!type.equals("warning"))
								punishLore.add(Main.color("&eSeverity: &r" + sev));
							punishLore.add("");
							punishLore.add(Main.color("&eReason: &r"
									+ reason.replace("\n", "").replace("Token: ", "").replace(" Cheat Detection", "")));
							punishLore.add("");
							punishLore.add(Main.color("&eStaff: &r" + user));
							punishLore.add("");
							punishLore.add(Main.color("&eDate: &r" + TimeManagement.dateFromMillis(date)));
							if (unbanner != null) {
								punishLore.add("");
								punishLore.add(Main.color("&eRemoved by: &r" + unbanner));
								punishLore.add(Main.color("&eRemove Reason: &r" + unbanreason));
							}
							ItemStack item = new ItemStack(icon, 1);
							ItemMeta punishMeta = item.getItemMeta();
							if (System.currentTimeMillis() < (Main.data
									.getLong("Users." + uuid + ".history." + res + ".date")
									+ Main.data.getLong("Users." + uuid + ".history." + res + ".duration") * 60 * 1000)
									|| Main.data.getLong("Users." + uuid + ".history." + res + ".duration") == -1) {
								punishMeta.addEnchant(Enchantment.DURABILITY, 1, false);
							}
							punishMeta.setDisplayName(Main.color("&a&l" + itemName));
							punishMeta.setLore(punishLore);
							item.setItemMeta(punishMeta);
							punishGui.setItem(45 + pos, item);
							pos++;
							if (pos >= 9)
								break;
						}
					}
					ConfigurationSection his = Main.guis.getConfigurationSection("PunishMenu");
					for (String res : his.getKeys(false)) {
						if (Main.guis.contains("PunishMenu." + res + ".slot")) {
							ItemStack item = new ItemStack(Material.valueOf(his.getString(res + ".icon")), 1);
							if (his.contains(res + ".data"))
								item.setDurability((byte) his.getInt(res + ".data"));
							ItemMeta cMeta = item.getItemMeta();
							cMeta.setDisplayName(Main.color(his.getString(res + ".name")));
							List<String> tempLore = new ArrayList<String>();
							for (String loreLine : his.getStringList(res + ".lore")) {
								String tempMsg = loreLine.replace("%player%", args[0])
										.replace("%chat1%", pastChat1 + "").replace("%chat2%", pastChat2 + "")
										.replace("%chat3%", pastChat3 + "").replace("%chat4%", pastChat4 + "")
										.replace("%client1%", pastClient1 + "").replace("%client2%", pastClient2 + "")
										.replace("%client3%", pastClient3 + "")
										.replace("%gameplay1%", pastGameplay1 + "")
										.replace("%gameplay4%", pastGameplay4 + "")
										.replace("%chat1dur%", TimeManagement.getTime(((1 + pastChat1) * 2 * 60)))
										.replace("%chat2dur%", TimeManagement.getTime((1 + pastChat2) * 1 * 24 * 60))
										.replace("%chat3dur%", TimeManagement.getTime((1 + pastChat3) * 30 * 24 * 60))
										.replace("%chat4dur%", TimeManagement.getTime((1 + pastChat4) * 24 * 60))
										.replace("%client1dur%",
												TimeManagement.getTime((1 + pastClient1) * 5 * 24 * 60))
										.replace("%gameplay1dur%", TimeManagement.getTime((1 + pastGameplay1) * 4 * 60))
										.replace("%gameplay4dur%", "Permanent");
								if (pastClient2 > 0 || pastClient3 > 0) {
									tempMsg = tempMsg.replace("%client2dur%", "Permanent");
									tempMsg = tempMsg.replace("%client3dur%", "Permanent");
								} else {
									tempMsg = tempMsg.replace("%client3dur%",
											TimeManagement.getTime((1 + pastClient2) * 24 * 30 * 60));
									tempMsg = tempMsg.replace("%client2dur%",
											TimeManagement.getTime((1 + pastClient2) * 24 * 30 * 60));
								}
								tempLore.add(Main.color(tempMsg));
							}
							cMeta.setLore(tempLore);
							item.setItemMeta(cMeta);
							if (his.contains(res + ".rank")) {
								if (!rank.matches(his.getString(res + ".rank")))
									continue;
							}
							punishGui.setItem(his.getInt(res + ".slot"), item);
						}
					}
					punisher.openInventory(punishGui);
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "unpunish":
			prefix = "&9Punish> &7";
			if (!(sender instanceof Player)) {
				String reason = "";
				if (args.length >= 3) {
					for (String res : args)
						if (res != args[0] && res != args[1])
							reason = reason + res + " ";
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
					ConfigurationSection history = Main.data
							.getConfigurationSection("Users." + target.getUniqueId() + ".history");
					if (history == null) {
						sender.sendMessage(Main.color(prefix + "Unknown Player"));
					} else {
						if (history.contains(args[1])) {
							history.set(args[1] + ".unbanner", sender.getName());
							history.set(args[1] + ".unbanreason", reason);
							history.set(args[1] + ".duration", 0);
							if (history.getString(args[1] + ".type").equals("ipban")
									&& Main.data.getConfigurationSection("Users.ipban") != null) {
								for (String resip : Main.data.getConfigurationSection("Users.ipban").getKeys(false)) {
									if (Main.data.getString("Users.ipban." + resip + ".ip")
											.equals(Main.getIP(target))) {
										Main.data.set("Users.ipban." + resip, null);
									}
								}
							}
						} else if (args[1].equalsIgnoreCase("all")) {
							for (String res : history.getKeys(false)) {
								if (!history.contains(res + ".unbanner")) {
									history.set(res + ".unbanner", sender.getName());
									history.set(res + ".unbanreason", reason);
									history.set(res + ".duration", 0);
								}
								if (history.getString(res + ".type").equals("ipban")
										&& Main.data.getConfigurationSection("Users.ipban") != null) {
									for (String resip : Main.data.getConfigurationSection("Users.ipban")
											.getKeys(false)) {
										if (Main.data.getString("Users.ipban." + resip + ".ip")
												.equals(Main.getIP(target))) {
											Main.data.set("Users.ipban." + resip, null);
										}
									}
								}
							}
						} else {
							sender.sendMessage(Main.color(prefix + "Punishment not found."));
							return;
						}
						sender.sendMessage(Main.color(
								"Succesfully removed &e" + target.getName() + " &6" + args[1] + " &7punishment(s)."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/unpunish [player] [punishment|all] [reason]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}
}
