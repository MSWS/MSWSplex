package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		ConfigurationSection ranks = Main.ranks;
		String prefix = "&9Toggle> &7";
		switch (command.toLowerCase()) {
		case "ei":
			prefix = "&9Give> &7";
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (ranks.getInt(rank + ".rank") >= 16) {
					if (args.length < 1) {
						sender.sendMessage(Main.color(prefix + "/ei <item> [amount]"));
						return;
					}
					if (Material.getMaterial(args[0].toUpperCase()) == null) {
						sender.sendMessage(Main.color(prefix + "Unknown item."));
					} else {
						ItemStack item = new ItemStack(Material.getMaterial(args[0].toUpperCase()));
						String name = Main.camelCase(item.getType().name().replace("_", " "));
						if (args.length == 1) {
							player.getInventory().addItem(Main.newItem(Material.getMaterial(args[0].toUpperCase()), "",
									"&7Given by &e" + player.getName(), 1));
							player.sendMessage(Main.color(prefix + "Succesfully gave yourself &e1 " + name + "&7."));
						} else {
							player.getInventory().addItem(Main.newItem(Material.getMaterial(args[0].toUpperCase()), "",
									"&7Given by &e" + player.getName(), Integer.valueOf(args[1])));
							player.sendMessage(
									Main.color(prefix + "Succesfully gave yourself &e" + args[1] + " " + name + "&7."));
						}
					}
				} else {
					Main.noPerm(sender);
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
			}
			break;
		case "i":
			prefix = "&9Give> &7";
			if (sender instanceof Player) {
				if (ranks.getInt(rank + ".rank") >= 16) {
					if (args.length < 1) {
						sender.sendMessage(Main.color(prefix + "/i <item> [amount]"));
						return;
					}
					if (Material.getMaterial(args[0].toUpperCase()) == null) {
						sender.sendMessage(Main.color(prefix + "Unknown item."));
					} else {
						ItemStack item = new ItemStack(Material.getMaterial(args[0].toUpperCase()));
						String name = Main.camelCase(item.getType().name().replace("_", " "));
						Player player = (Player) sender;
						if (args.length == 1) {
							player.getInventory().addItem(Main.newItem(Material.getMaterial(args[0].toUpperCase()), "",
									"&7Given by &e" + player.getName(), 64));
							player.sendMessage(Main.color(prefix + "Succesfully gave yourself &e64 " + name + "&7."));
						} else {
							player.getInventory().addItem(Main.newItem(Material.getMaterial(args[0].toUpperCase()), "",
									"&7Given by &e" + player.getName(), Integer.valueOf(args[1])));
							player.sendMessage(
									Main.color(prefix + "Succesfully gave yourself &e" + args[1] + " " + name + "&7."));
						}
					}
				} else {
					Main.noPerm(sender);
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
			}

			break;
		case "give":
		case "g":
			prefix = "&9Give> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (args.length < 2) {
					sender.sendMessage(Main.color(prefix + "/give <player|all> <item> [amount]"));
					return;
				}
				int amo = 1;
				if (args.length == 3 && Main.isNumber(args[2])) {
					amo = Integer.valueOf(args[2].toUpperCase());
				} else if (args.length == 3) {
					sender.sendMessage(Main.color(prefix + "Invalid item amount."));
				}
				ItemStack defItem = null;
				try {
					defItem = new ItemStack(Material.valueOf(args[1].toUpperCase()), amo);
				} catch (Exception e) {

				}
				if (defItem == null) {
					if (args[1].equalsIgnoreCase("hand")) {
						if (sender instanceof Player) {
							defItem = ((Player) sender).getItemInHand();
							String name = Main.camelCase(defItem.getType().toString().replace("_", " "));
							if (args[0].equalsIgnoreCase("all")) {
								for (Player gTarget : Bukkit.getOnlinePlayers()) {
									gTarget.getInventory().addItem(defItem);
									gTarget.sendMessage(Main.color(
											prefix + "You received &e" + name + " &7from &e" + sender.getName()));
								}
								sender.sendMessage(Main.color(prefix + "Gave &eeveryone " + name + "&7."));
							} else if (args[0].equalsIgnoreCase("here")) {
								for (Player gTarget : ((Player) sender).getWorld().getPlayers()) {
									gTarget.getInventory().addItem(defItem);
									gTarget.sendMessage(Main.color(
											prefix + "You received &e" + name + " &7from &e" + sender.getName()));
								}
								sender.sendMessage(Main.color(prefix + "Gave &eeveryone (World)" + name + "&7."));
							} else {
								Player gTarget = Main.getPlayer(args[0], sender);
								if (gTarget == null)
									return;
								gTarget.getInventory().addItem(defItem);
								gTarget.sendMessage(Main
										.color(prefix + "You received &e" + name + " &7from &e" + sender.getName()));
								sender.sendMessage(
										Main.color(prefix + "Gave &e" + gTarget.getName() + " " + name + "&7."));
							}
							break;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player."));
						}
					} else if (args[1].equalsIgnoreCase("inventory")) {
						if (sender instanceof Player) {
							Inventory inv = ((Player) sender).getInventory();
							ItemStack invCont[] = new ItemStack[54];
							int size = 0;
							for (int i = 0; i < ((Player) sender).getInventory().getSize(); i++) {
								if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
									invCont[size] = inv.getItem(i);
									size++;
								}
							}
							if (args[0].equalsIgnoreCase("all")) {
								for (Player gTarget : Bukkit.getOnlinePlayers()) {
									for (int i = 0; i < size; i++) {
										gTarget.getInventory().addItem(invCont[i]);
									}
								}
							} else if (args[0].equalsIgnoreCase("here")) {
								for (Player gTarget : ((Player) sender).getWorld().getPlayers()) {
									for (int i = 0; i < size; i++) {
										gTarget.getInventory().addItem(invCont[i]);
									}
								}
							} else {
								Player gTarget = Main.getPlayer(args[0], sender);
								if (gTarget == null)
									return;
								for (int i = 0; i < size; i++) {
									gTarget.getInventory().addItem(invCont[i]);
								}
							}
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player."));
						}
					} else {
						String itemN = args[1].replace("chest", " Chest");
						if (args[0].equalsIgnoreCase("all")) {
							for (Player gTarget : Bukkit.getOnlinePlayers()) {
								gTarget.sendMessage(Main.color(prefix + "You received &e" + amo + " "
										+ Main.camelCase(itemN) + "&7 from &e" + sender.getName() + "&7."));
								Main.addCosmetic((gTarget), args[1], Main.cosmAmo(gTarget, args[1]) + amo);
							}
							sender.sendMessage(Main.color(prefix + "Gave &eeveryone " + amo + " "
									+ Main.camelCase(itemN) + " &4(Cosmetic)&7."));
						} else if (args[0].equalsIgnoreCase("here")) {
							if (sender instanceof Player) {
								for (Player gTarget : ((Player) sender).getWorld().getPlayers()) {
									gTarget.sendMessage(Main.color(prefix + "You received &e" + amo + " "
											+ Main.camelCase(itemN) + "&7 from &e" + sender.getName() + "&7."));
									Main.addCosmetic((gTarget), args[1], amo);
								}
								sender.sendMessage(Main.color(prefix + "Gave &eeveryone (world) " + amo + " "
										+ Main.camelCase(itemN) + " &4(Cosmetic)&7."));
							} else {
								sender.sendMessage(Main.color(prefix + "You must be a player!"));
							}
						} else {
							if (Main.offExists(args[0])) {
								OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
								sender.sendMessage(Main.color(prefix + "Gave &e" + target.getName() + " " + amo + " "
										+ Main.camelCase(itemN) + " &4(Cosmetic)&7."));
								if (target.isOnline())
									((Player) target).sendMessage(Main.color(prefix + "You received &e" + amo + " "
											+ Main.camelCase(itemN) + " &7from &e" + sender.getName() + "&7."));
								Main.addCosmetic(target, args[1].toLowerCase(), amo);
							} else {
								Main.notFound(args[0], sender, true);
							}
						}
					}
					return;
				} else {
					String name = Main.camelCase(args[1].replace("_", " "));
					if (args[0].equalsIgnoreCase("all")) {
						for (Player newtarget : Bukkit.getOnlinePlayers()) {
							newtarget.getInventory().addItem(defItem);
							newtarget.sendMessage(Main.color(prefix + "You received &e" + amo + " " + name
									+ " &7from &e" + sender.getName() + "&7."));
						}
						if (args.length == 2) {
							sender.sendMessage(
									Main.color(prefix + "Succesfully gave &eeveryone &e" + amo + " " + name + "&7."));
						}
						return;
					}
					if (args[0].equalsIgnoreCase("here")) {
						if (sender instanceof Player) {
							for (Player newtarget : ((Player) sender).getWorld().getPlayers()) {
								newtarget.getInventory().addItem(defItem);
								newtarget.sendMessage(Main.color(prefix + "You received &e" + amo + " " + name
										+ " &7from &e" + sender.getName() + "&7."));
							}
							sender.sendMessage(Main.color(
									prefix + "Succesfully gave everyone in your world &e" + amo + " " + name + "&7."));
							return;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player to use &ehere&7."));
						}
					}
					Player player = Main.getPlayer(args[0], sender);
					if (player == null)
						return;
					if (args.length == 2) {
						player.getInventory().addItem(defItem);
						player.sendMessage(Main
								.color(prefix + "You received &e1 " + name + " &7from &e" + sender.getName() + "&7."));
						sender.sendMessage(Main.color(
								"&9Give> &7Succesfully gave &e1 " + name + " &7to &e" + player.getName() + "&7."));
					} else {
						player.getInventory().addItem(defItem);
						player.sendMessage(Main.color(prefix + "You received &e" + args[2] + " " + name + " &7from &e"
								+ sender.getName() + "&7."));
						sender.sendMessage(Main.color(prefix + "Succesfully gave &e" + args[2] + " " + name + " &7to &e"
								+ player.getName() + "&7."));
					}
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "shard":
			prefix = "&9Shard> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 2) {
					if (Main.isNumber(args[1])) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
						if (target.isOnline())
							if (Integer.valueOf(args[1]) == 1) {
								((Player) target).sendMessage(Main.color(prefix + "You received &e" + args[1]
										+ " Shard &7from &e" + sender.getName() + "&7."));
							} else {
								((Player) target).sendMessage(Main.color(prefix + "You received &e" + args[1]
										+ " Shards &7from &e" + sender.getName() + "&7."));
							}
						sender.sendMessage(Main.color(
								"&9Shard> &7Succesfully gave &e" + target.getName() + " " + args[1] + " Shard&7."));
						Main.addCosmetic(target, "shards", Integer.valueOf(args[1]));
					} else {
						sender.sendMessage(Main.color(prefix + "Invalid shard amount."));
					}
				} else if (args.length == 3) {
					if (Main.isNumber(args[2])) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						if (target.isOnline())
							((Player) target).sendMessage(Main.color("&9Shard> &e" + sender.getName()
									+ " &7has set your Shards to &e" + Integer.valueOf(args[2]) + "&7."));
						sender.sendMessage(Main.color(prefix + "Succesfully set &e" + target.getName()
								+ "'s &7Shards to &e" + args[2] + "&7."));
						Main.addCosmetic(target, "shards", Integer.valueOf(args[2]) - Main.cosmAmo(target, "shards"));
					} else {
						sender.sendMessage(Main.color(prefix + "Invalid shard amount."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/shard <set> [player] [amount]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "gem":
			prefix = "&9Gem> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 2) {
					if (Main.isNumber(args[1])) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
						if (target.isOnline())
							if (Integer.valueOf(args[1]) == 1) {
								((Player) target).sendMessage(Main.color(prefix + "You received &e" + args[1]
										+ " Gem &7from &e" + sender.getName() + "&7."));
							} else {
								((Player) target).sendMessage(Main.color(prefix + "You received &e" + args[1]
										+ " Gems &7from &e" + sender.getName() + "&7."));
							}
						sender.sendMessage(Main
								.color(prefix + "Succesfully gave &e" + target.getName() + " " + args[1] + " Gem&7."));
						Main.addCosmetic(target, "gems", Integer.valueOf(args[1]));
					} else {
						sender.sendMessage(Main.color(prefix + "Invalid gem amount."));
					}
				} else if (args.length == 3) {
					if (Main.isNumber(args[2])) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						if (target.isOnline())
							((Player) target).sendMessage(Main.color("&9Gem> &e" + sender.getName()
									+ " &7has set your Gems to &e" + Integer.valueOf(args[2]) + "&7."));
						sender.sendMessage(Main.color(prefix + "Succesfully set &e" + target.getName()
								+ "'s &7Gems to &e" + args[2] + "&7."));
						Main.addCosmetic(target, "gems", Integer.valueOf(args[2]) - Main.cosmAmo(target, "gems"));
					} else {
						sender.sendMessage(Main.color(prefix + "Invalid gem amount."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/gem <set> [player] [amount]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}
}
