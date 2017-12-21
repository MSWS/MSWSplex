package mc.bukkit.MSWS.MSWSplex;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class MiscCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		Player player;
		String prefix, uuid;
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "up":
			prefix = "&9Up> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (sender instanceof Player) {
					player = (Player) sender;
					double height = 1.0;
					if (args.length != 0)
						if (Main.isNumber(args[0]))
							height = Double.valueOf(args[0]);
					Block block = player.getWorld().getBlockAt(player.getLocation().add(0, height, 0));
					if (block.getType() == Material.AIR)
						block.setType(Material.GLASS);
					player.teleport(player.getLocation().add(0, height + 1, 0));
					sender.sendMessage(Main.color(prefix + "You teleported up &e" + height + " blocks&7."));
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "radius":
			prefix = "&9Radius> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (sender instanceof Player) {
					player = (Player) sender;
					uuid = player.getUniqueId() + "";
					if (args.length == 1) {
						if (args[0].matches("(?i)(0|off|disable)")) {
							Main.data.set("Users." + uuid + ".radius", null);
							sender.sendMessage(Main.color(prefix + "Your toggled off your knockback."));
						} else {
							if (Main.isNumber(args[0])) {
								if (Double.valueOf(args[0]) >= 100) {
									Main.addPunish(sender.getName(), "Server",
											"Compromised Account - [Set radius to " + args[0] + "]", "other", 4);
									return;
								}
								Main.data.set("Users." + uuid + ".radius", Double.valueOf(args[0]));
							}
							sender.sendMessage(
									Main.color(prefix + "Your knockback radius is now &e" + args[0] + "&7."));
						}
					} else {
						Main.data.set("Users." + uuid + ".radius", null);
						sender.sendMessage(Main.color(prefix + "Your toggled off your knockback."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));

				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "prefs":
			prefix = "&9Prefs> &7";
			if (sender instanceof Player) {
				GUIManager.makeInventory(((Player) sender), "PrefsMenu");
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
			}
			break;
		case "rename":
			prefix = "&9Rename> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (sender instanceof Player) {
					player = (Player) sender;
					if (player.getInventory().getItemInHand() != null
							&& player.getInventory().getItemInHand().getType() != Material.AIR) {
						if (args.length != 0) {
							ItemStack item = player.getInventory().getItemInHand();
							ItemMeta meta = item.getItemMeta();
							String name = "";
							for (String res : args)
								name = name + res + " ";
							meta.setDisplayName(Main.color("&r" + name));
							item.setItemMeta(meta);
							player.getInventory().setItemInHand(item);
							player.sendMessage(Main.color(prefix + "Succesfully renamed item."));
						} else {
							sender.sendMessage(Main.color(prefix + "/rename [name]"));
						}
					} else {
						sender.sendMessage(Main.color(prefix + "You must be holding an item!"));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "perms":
			for (PermissionAttachmentInfo res : sender.getEffectivePermissions()) {
				if (res.getPermission().contains("mswsplex.")) {
					sender.sendMessage(res.getPermission());
				}
			}
			sender.sendMessage(Main.color("&6You have the following groups: default"));
			break;
		case "rules":
			prefix = "&9Rules> &7";
			sender.sendMessage(Main.color(prefix + "The rules can be found here: &awww.mineplex.com/rules"));
			break;
		case "saveall":
			prefix = "&9Server> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				sender.sendMessage(Main.color(prefix + "Saving all files..."));
				Main.save.put("all", true);
				sender.sendMessage(Main.color(prefix + "Files succesfully saved."));
			} else {
				Main.noPerm(sender);
			}
			break;
		case "stats":
			prefix = "&9Stats> &7";
			if (sender instanceof Player) {
				player = (Player) sender;
				Player target = player;
				if (args.length > 0)
					target = Main.getPlayer(args[0], sender);
				if (target != null) {
					Inventory stats = Bukkit.createInventory(null, 45, target.getName() + "'s stats");
					player.openInventory(stats);
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player!"));
			}
			break;
		case "setfake":
			prefix = "&9Fake Players> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 19) {
				if (args.length == 1) {
					if (Main.isNumber(args[0])) {
						sender.sendMessage(Main.color(prefix + "Amount of fake players has been set to " + args[0]));
						Main.config.set("FakePlayers", Integer.valueOf(args[0]));
						Main.save.put("config", true);
					} else {
						sender.sendMessage(Main.color(prefix + "Invalid player count."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/setfake [amount]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "motd":
			List<String> motd = Main.config.getStringList("MOTDList");
			prefix = "&9MOTD> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 19) {
				if (args.length > 0) {
					switch (args[0].toLowerCase()) {
					case "set":
						if (args.length >= 3) {
							if (Main.isNumber(args[1])) {
								int pos = Integer.valueOf(args[1]);
								String msg = "";
								for (String res : args)
									if (res != args[0] && res != args[1])
										msg = msg + res + " ";
								if (motd.isEmpty()) {
									motd.add(msg);
								} else {
									if (pos >= 0 && pos <= motd.size()) {
										if (motd.size() > pos) {
											motd.set(pos, msg);
										} else {
											motd.add(pos, msg);
										}
									} else {
										sender.sendMessage(Main.color(prefix + "Invalid number."));
										return;
									}
								}
								sender.sendMessage(
										Main.color(prefix + "Succesfully set &e" + pos + "&7 to &r" + Main.color(msg)));
							} else {
								sender.sendMessage(Main.color(prefix + "Invalid number."));
							}
						} else {
							sender.sendMessage(Main.color(prefix + "&4/motd set [number] [message]"));
						}
						break;
					case "clear":
						motd.clear();
						sender.sendMessage(Main.color(prefix + "Succesfully cleared messages."));
						break;
					case "remove":
						if (args.length == 2) {
							if (Main.isNumber(args[1])) {
								if (motd.get(Integer.valueOf(args[1])) != null) {
									sender.sendMessage(Main.color("Succesfully removed line &e" + args[1] + "&7(&r"
											+ motd.get(Integer.valueOf(args[1])) + "&7)"));
									motd.remove((int) Integer.valueOf(args[1]));
								}
							}
						} else {
							sender.sendMessage(Main.color(prefix + "&4/motd remove [number]"));
						}
						break;
					case "list":
						int pos = 0;
						sender.sendMessage(Main.color(prefix + "Listing Messages."));
						for (String res : motd) {
							sender.sendMessage(Main.color("&e" + pos + "&7: &r" + res));
							pos++;
						}
						break;
					}
					Main.config.set("MOTDList", motd);
					Main.save.put("config", true);
					// Main.refreshMotd();
				} else {
					sender.sendMessage(Main.color(prefix + "Available Commands"));
					sender.sendMessage(Main.color(prefix + "&4/motd set [number] [message]"));
					sender.sendMessage(Main.color(prefix + "&4/motd remove [number]"));
					sender.sendMessage(Main.color(prefix + "&4/motd clear"));
					sender.sendMessage(Main.color(prefix + "&4/motd list"));

				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}
}
