package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class ClientManager {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		ConfigurationSection ranks = Main.ranks;
		FileConfiguration config = Main.config;
		String prefix = "&9Toggle> &7";
		switch (command.toLowerCase()) {
		case "updaterank":
			prefix = "&9Client Manager> &7";
			if (ranks.getInt(rank + ".rank") >= 15) {
				if (args.length != 2) {
					sender.sendMessage(Main.color(prefix + "/updaterank [player] [rank]"));
					return;
				}
				if (Bukkit.getOfflinePlayer(args[0]) == null) {
					Main.notFound(args[0], sender, true);
					return;
				}
				if (!ranks.contains(args[1].toLowerCase())) {
					sender.sendMessage(Main.color(prefix + "Rank not found."));
					return;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if (ranks.getInt(Main.getRankID(target.getUniqueId()) + ".rank") > ranks.getInt(rank + ".rank") || ranks
						.getInt(Main.data.getString("Users." + target.getUniqueId() + ".temprank") + ".rank") > ranks
								.getInt(rank + ".rank")) {
					sender.sendMessage(Main.color(prefix + "You cannot change that player's rank."));
					return;
				}
				if (ranks.getInt(args[1].toLowerCase() + ".rank") > ranks.getInt(rank + ".rank")) {
					sender.sendMessage(Main.color(
							prefix + "You can only promote up to " + Main.getRank(((Player) sender).getUniqueId())));
					return;
				}
				Main.data.set("Users." + target.getUniqueId() + ".rank", args[1].toLowerCase());
				Main.data.set("Users." + target.getUniqueId() + ".temprank", null);
				sender.sendMessage(Main.color(prefix + "Succesfully set &e" + target.getName() + "&7's rank to "
						+ Main.camelCase(Main.getRankID(target.getUniqueId()) + "&7.")));

				if (target.isOnline()) {
					((Player) target).sendMessage(Main.color(prefix + "Your rank has been updated to "
							+ Main.camelCase(Main.getRankID(((Player) target).getUniqueId()) + "&7!")));
					if (((Player) target).getOpenInventory().getTitle().contains("Punish"))
						((Player) target).closeInventory();
					Main.refreshTab();
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "createrank":
			prefix = "&9Client Manager> &7";
			if (ranks.getInt(rank + ".rank") >= 19) {
				if (args.length <= 1) {
					sender.sendMessage(Main.color(prefix + "/createrank [rank id] [prefix]"));
					return;
				}
				String sprefix = "";
				for (String res : args) {
					if (res != args[0]) {
						sprefix = sprefix + res + " ";
					}
				}
				ranks.set(args[0].toLowerCase() + ".prefix", sprefix);
				sender.sendMessage(Main.color(prefix + "Rank " + sprefix + " &7succesfully created."));
			} else {
				Main.noPerm(sender);
			}
			break;
		case "deleterank":
			prefix = "&9Client Manager> &7";
			if (ranks.getInt(rank + ".rank") >= 19) {
				if (args.length != 1) {
					sender.sendMessage(Main.color(prefix + "/deleterank [rank id]"));
					return;
				}
				if (!ranks.contains(args[0].toLowerCase())) {
					sender.sendMessage(Main.color(prefix + "Rank not found."));
					return;
				}
				if (sender instanceof Player)
					if (ranks.getInt(args[0].toLowerCase() + ".rank") >= ranks
							.getInt(Main.getRankID(((Player) sender).getUniqueId()))) {
						sender.sendMessage(Main.color(prefix + "You are not allowed to delete that rank."));
						return;
					}
				sender.sendMessage(Main.color(prefix + config.getString("Ranks." + args[0].toLowerCase() + ".prefix")
						+ " &7succesfully deleted"));
				Main.refreshTab();
				config.set("Ranks." + args[0].toLowerCase(), null);
			} else {
				Main.noPerm(sender);
			}
			break;
		case "setprefix":
			prefix = "&9Client Manager> &7";
			if (ranks.getInt(rank + ".rank") >= 19) {
				if (args.length < 2) {
					sender.sendMessage(Main.color(prefix + "/setprefix <rank id> <prefix>"));
					return;
				}
				if (!ranks.contains(args[0].toLowerCase())) {
					sender.sendMessage(Main.color(prefix + "Rank not found."));
					return;
				}
				String sprefix = "";
				for (String res : args) {
					if (res != args[0])
						sprefix = sprefix + res + " ";
				}
				ranks.set(args[0].toLowerCase() + ".prefix", sprefix);
				sender.sendMessage(Main.color(prefix + "Succesfully set &e" + args[0] + "&7's prefix to " + args[1]));
				for (Player tagtarget : Bukkit.getOnlinePlayers()) {
					if (Main.getRankID(tagtarget.getUniqueId()) != "member") {
						tagtarget.setDisplayName(
								Main.color(Main.getRank(tagtarget.getUniqueId()) + "&r") + tagtarget.getName());
						tagtarget.setPlayerListName(
								Main.color(Main.getRank(tagtarget.getUniqueId()) + "&r") + tagtarget.getName());
					}
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "listranks":
			prefix = "&9Client Manager> &7";
			sender.sendMessage("Current ranks:");
			for (String res : ranks.getKeys(false)) {
				sender.sendMessage(res + " - " + Main.color(config.getString("Ranks." + res + ".prefix")));
			}
			break;
		case "getlevel":
			prefix = "&9Level Manager> &7";
			if (!sender.hasPermission("survival.getlevel")) {
				Main.noPerm(sender);
				return;
			}
			if (args.length != 1) {
				sender.sendMessage(Main.color(prefix + "/getlevel <player>"));
				return;
			}
			if (Bukkit.getOfflinePlayer(args[0]) == null) {
				Main.notFound(args[0], sender, true);
				return;
			}
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			String uuid = target.getUniqueId().toString();
			sender.sendMessage(Main.color("&c" + target.getName() + "'s &7level progression"));
			sender.sendMessage(Main
					.color("&cCurrent Level: &7" + (int) Math.floor(Main.data.getDouble("Users." + uuid + ".level"))));
			sender.sendMessage(
					Main.color("&cLevel Percent: &7" + ((Main.data.getDouble("Users." + uuid + ".level") + ""))));
			break;
		case "testrank":
			prefix = "&9Client Manager> &7";
			if (sender instanceof Player) {
				Player player = (Player) sender;
				uuid = player.getUniqueId() + "";
				if (Main.data.contains("Users." + uuid + ".temprank")) {
					Main.data.set("Users." + uuid + ".rank", Main.data.getString("Users." + uuid + ".temprank"));
					player.sendMessage(Main.color(prefix + "Your rank has been set back to "
							+ Main.camelCase(Main.getRankID(player.getUniqueId())) + "!"));
					Main.data.set("Users." + uuid + ".temprank", null);
					Main.refreshTab();
					return;
				}
				if (ranks.getInt(rank + ".rank") >= 13) {
					if (args.length == 1) {
						if (!ranks.contains(args[0].toLowerCase())) {
							sender.sendMessage(Main.color(prefix + "Rank not found."));
							return;
						}
						if (ranks.getInt(rank + ".rank") < config.getInt("Ranks." + args[0].toLowerCase() + ".rank")) {
							sender.sendMessage(Main.color(prefix + "You cannot test out this rank."));
							return;
						}
						Main.data.set("Users." + uuid + ".temprank", rank);
						Main.data.set("Users." + uuid + ".rank", args[0].toLowerCase());
						sender.sendMessage(Main.color(
								prefix + "Your rank has been temporarily set to " + Main.camelCase(args[0]) + "&7!"));
						sender.sendMessage(Main.color(prefix + "To get your original rank back, run /testrank again."));
						Main.refreshTab();
					} else {
						sender.sendMessage(Main.color(prefix + "/testrank [rank]"));
					}
				} else {
					Main.noPerm(sender);
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player!"));
			}
			break;
		case "getrank":
			prefix = "&9Client Manager> &7";
			if (Main.staff.contains(rank)) {
				if (args.length != 1) {
					sender.sendMessage(Main.color(prefix + "/getrank <player>"));
					return;
				}
				target = Bukkit.getOfflinePlayer(args[0]);
				if (!Main.offExists(target) && !Main.data.contains("Users." + target.getUniqueId() + ".rank")) {
					Main.notFound(args[0], sender, true);
				} else {
					sender.sendMessage(Main.color("&9Client Manager> &e" + target.getName() + " &7is rank &e"
							+ Main.camelCase(Main.getRankID(target.getUniqueId())) + "&7."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "heal":
			prefix = "&9Client Manager> &7";
			if (Main.staff.contains(rank)) {
				Player player = null;
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("here")) {
						if (sender instanceof Player) {
							for (Player htarget : ((Player) sender).getWorld().getPlayers()) {
								htarget.setHealth(20);
								htarget.setSaturation(20);
								htarget.setFoodLevel(20);
								htarget.setFireTicks(0);
								for (PotionEffect potion : htarget.getActivePotionEffects())
									htarget.removePotionEffect(potion.getType());
								htarget.sendMessage(
										Main.color(prefix + "&e" + sender.getName() + " &7has healed you."));
							}
							sender.sendMessage(Main.color(prefix + "Succesfully healed &eeveryone (same world)&7."));
							return;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player to use &ehere&7."));
						}
					} else if (args[0].equalsIgnoreCase("all")) {
						for (Player htarget : Bukkit.getOnlinePlayers()) {
							htarget.setHealth(20);
							htarget.setSaturation(20);
							htarget.setFoodLevel(20);
							htarget.setFireTicks(0);
							for (PotionEffect potion : htarget.getActivePotionEffects())
								htarget.removePotionEffect(potion.getType());
							htarget.sendMessage(Main.color(prefix + "&e" + sender.getName() + " &7has healed you."));
						}
						sender.sendMessage(Main.color(prefix + "Succesfully healed &eeveryone&7."));
						return;
					} else {
						player = Main.getPlayer(args[0], sender);
						if (player == null) {
							return;
						}
					}
				} else {
					if (sender instanceof Player) {
						player = (Player) sender;
					} else {
						sender.sendMessage(Main.color(prefix + "You must be a player."));
					}
				}
				player.setHealth(20);
				player.setSaturation(20);
				player.setFoodLevel(20);
				player.setFireTicks(0);
				for (PotionEffect potion : player.getActivePotionEffects())
					player.removePotionEffect(potion.getType());
				if (player != sender) {
					player.sendMessage(Main.color(prefix + "&e" + sender.getName() + " &7has healed you."));
					sender.sendMessage(Main.color(prefix + "Succesfully healed &e" + player.getName() + "&7."));
				} else {
					sender.sendMessage(Main.color(prefix + "You healed yourself."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "gm":
		case "gamemode":
			prefix = "&9Gamemode> &7";
			rank = "owner";
			if (sender instanceof Player)
				rank = Main.getRankID(((Player) sender).getUniqueId());
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 0)
					return;
				if (args.length == 1) {
					if (sender instanceof Player) {
						Player gPlayer = (Player) sender;
						GameMode gm = null;
						if (args[0].matches("(c|1|creative)"))
							gm = GameMode.CREATIVE;
						if (args[0].matches("(s|0|survival)"))
							gm = GameMode.SURVIVAL;
						if (args[0].matches("(a|2|adventure)"))
							gm = GameMode.ADVENTURE;
						if (args[0].matches("(sp|3|spectator)"))
							gm = GameMode.SPECTATOR;
						if (gm == null) {
							sender.sendMessage(Main.color(prefix + "Unknown gamemode."));
							return;
						}
						gPlayer.setGameMode(gm);
						sender.sendMessage(Main.color("&9Gamemode> &7Succesfully set your gamemode to &e"
								+ Main.camelCase(gm.name()) + "&7."));
					} else {
						sender.sendMessage(Main.color(prefix + "You must be a player."));
					}
				}
				if (args.length == 2) {
					GameMode gm = null;
					if (args[0].matches("(c|1|creative)"))
						gm = GameMode.CREATIVE;
					if (args[0].matches("(s|0|survival)"))
						gm = GameMode.SURVIVAL;
					if (args[0].matches("(a|2|adventure)"))
						gm = GameMode.ADVENTURE;
					if (args[0].matches("(sp|3|spectator)"))
						gm = GameMode.SPECTATOR;
					if (gm == null) {
						sender.sendMessage(Main.color(prefix + "Unknown gamemode."));
						return;
					}
					if (args[1].equals("all")) {
						for (Player gTarget : Bukkit.getOnlinePlayers()) {
							gTarget.setGameMode(gm);
							gTarget.sendMessage(Main.color(prefix + "Your gamemode was set to &e"
									+ Main.camelCase(gm.name()) + "&7 by &e" + sender.getName() + "&7."));
						}
						sender.sendMessage(Main.color(prefix + "Succesfully set &eeveryone&7's gamemode to &e"
								+ Main.camelCase(gm.name()) + "&7."));
						return;
					}
					if (args[1].equals("here")) {
						if (sender instanceof Player) {
							for (Player gTarget : ((Player) sender).getWorld().getPlayers()) {
								gTarget.setGameMode(gm);
								gTarget.sendMessage(Main.color(prefix + "Your gamemode was set to &e"
										+ Main.camelCase(gm.name()) + "&7 by &e" + sender.getName() + "&7."));
							}
							sender.sendMessage(Main.color(prefix + "Succesfully set &eeveryone&7's gamemode to &e"
									+ Main.camelCase(gm.name()) + "&7."));
							return;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player!"));
						}
					}
					Player gTarget = Main.getPlayer(args[1], sender);
					if (gTarget == null)
						return;
					gTarget.setGameMode(gm);
					gTarget.sendMessage(Main.color(prefix + "Your gamemode was set to &e" + Main.camelCase(gm.name())
							+ "&7 by &e" + sender.getName() + "&7."));
					sender.sendMessage(Main.color(prefix + "Succesfully set &e" + gTarget.getName()
							+ "&7's gamemode to &e" + Main.camelCase(gm.name() + "&7.")));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "kill":
			prefix = "&9Kill> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 0) {
						player.setHealth(0);
						sender.sendMessage(Main.color("&9Kill> &7Succesfully killed yourself."));
					}
					if (args.length == 1) {
						Player killtarget = Main.getPlayer(args[0], sender);
						if (killtarget == null) {
							sender.sendMessage(Main.color("&9Kill> &7Player not found."));
						} else {
							killtarget.setHealth(0.0);
							killtarget.sendMessage(Main.color("&9Kill> &e" + sender.getName() + " &7has killed you."));
							sender.sendMessage(
									Main.color("&9Kill> &7Succesfully killed &e" + killtarget.getName() + "&7."));
						}
					}
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("all")) {
							try {
								EntityType.valueOf(args[1].toUpperCase());
							} catch (IllegalArgumentException e) {
								sender.sendMessage(Main.color("&9Kill> &7Unknown entity."));
								return;
							}
							for (Entity e : player.getWorld().getEntities()) {
								if (e.getType() == EntityType.valueOf(args[1].toUpperCase())) {
									if (e instanceof LivingEntity) {
										LivingEntity ea = (LivingEntity) e;
										ea.setHealth(0.0);
									} else {
										e.remove();
									}
								}
							}
							sender.sendMessage(Main.color("&9Kill> &7Succesfull killed all &e"
									+ Main.camelCase(EntityType.valueOf(args[1].toUpperCase()) + "").replace("_", " ")
									+ "s&7."));
							return;
						}
						if (args[0].equalsIgnoreCase("here")) {
							if (sender instanceof Player) {
								try {
									EntityType.valueOf(args[1].toUpperCase());
								} catch (IllegalArgumentException e) {
									sender.sendMessage(Main.color("&9Kill> &7Unknown entity."));
									return;
								}
								for (Entity e : player.getWorld().getEntities()) {
									if (e.getType() == EntityType.valueOf(args[1].toUpperCase())) {
										if (e instanceof LivingEntity) {
											LivingEntity ea = (LivingEntity) e;
											ea.setHealth(0.0);
										} else {
											e.remove();
										}
									}
								}
								sender.sendMessage(Main.color("&9Kill> &7Succesfull killed all &e" + Main
										.camelCase(EntityType.valueOf(args[1].toUpperCase()) + "").replace("_", " ")
										+ "s&7."));
								return;
							} else {
								sender.sendMessage(Main.color(prefix + "You must be a player."));
							}

						}
						if (Main.isNumber(args[1])) {
							try {
								EntityType.valueOf(args[0].toUpperCase());
							} catch (IllegalArgumentException e) {
								sender.sendMessage(Main.color("&9Kill> &7Unknown entity."));
								return;
							}
							Double val = Double.valueOf(args[1]);
							for (Entity e : player.getNearbyEntities(val, val, val)) {
								if (e.getType() == EntityType.valueOf(args[0].toUpperCase())) {
									if (e instanceof LivingEntity) {
										LivingEntity ea = (LivingEntity) e;
										ea.setHealth(0.0);
									} else {
										e.remove();
									}
								}
							}
							sender.sendMessage(Main.color("&9Kill> &7Succesfull killed all &e"
									+ Main.camelCase(EntityType.valueOf(args[0].toUpperCase()) + "") + "s &7within a &e"
									+ args[1] + " block &7radius."));
						}
					} /**
						 * else{ sender.sendMessage(Main.color("&9Kill> &7 /kill [entity|all|player]
						 * [radius|type]")); }
						 */
				} else {
					sender.sendMessage(Main.color("&9Kill> &7You must be a player."));
				}

			} else {
				Main.noPerm(sender);
			}
			break;
		case "hubitems":
			prefix = "&9Inventory> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 0) {
					if (sender instanceof Player) {
						Main.hubItems(((Player) sender));
						sender.sendMessage(Main.color(prefix + "You reset &eyour &7inventory."));
					} else {
						sender.sendMessage(Main.color(prefix + "Here hold this, oh wait..."));
					}
				} else {
					switch (args[0].toLowerCase()) {
					case "all":
						for (Player htarget : Bukkit.getOnlinePlayers())
							Main.hubItems(htarget);
						sender.sendMessage(Main.color(prefix + "You reset &eeveryone's &7inventory."));
						break;
					case "here":
						if (sender instanceof Player) {
							for (Player htarget : ((Player) sender).getWorld().getPlayers())
								Main.hubItems(htarget);
							sender.sendMessage(Main.color(prefix + "You reset &eeveryone (in world)'s &7inventory."));
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player to use &ehere&7."));
						}
						break;
					default:
						Player player = Main.getPlayer(args[0], sender);
						if (player != null) {
							Main.hubItems(player);
							sender.sendMessage(
									Main.color(prefix + "You reset &e" + player.getName() + "&7's inventory."));
						}
						break;
					}
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "clear":
			prefix = "&9Inventory> &7";
			if (Main.staff.contains(rank) && !rank.equals("trainee")) {
				if (args.length == 0) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						player.getInventory().clear();
						player.getInventory().setArmorContents(null);
						sender.sendMessage(Main.color(prefix + "Succesfully cleared your inventory."));
					} else {
						sender.sendMessage(Main.color(prefix + "/clear [player]"));
						return;
					}
				} else {
					if (args[0].equalsIgnoreCase("all")) {
						sender.sendMessage(Main.color(prefix + "Succesfully cleared &eeveryone&7's inventory."));
						for (Player targeted : Bukkit.getOnlinePlayers()) {
							targeted.getInventory().clear();
							targeted.getInventory().setArmorContents(null);
							targeted.sendMessage(
									Main.color(prefix + "&e" + sender.getName() + " &7has cleared your inventory."));
						}
						return;
					}
					if (args[0].equalsIgnoreCase("here")) {
						if (sender instanceof Player) {
							sender.sendMessage(Main.color(
									prefix + "Succesfully cleared all inventories of players in your same world."));
							for (Player targeted : ((Player) sender).getWorld().getPlayers()) {
								targeted.getInventory().clear();
								targeted.getInventory().setArmorContents(null);
								targeted.sendMessage(Main
										.color(prefix + "&e" + sender.getName() + " &7has cleared your inventory."));
							}
							return;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player!"));
						}
					}
					Player ontarget = Main.getPlayer(args[0], sender);
					if (ontarget != null) {
						ontarget.getInventory().clear();
						ontarget.getInventory().setArmorContents(null);
						sender.sendMessage(Main.color(
								"&9Inventory> &7Succesfully cleared &e" + ontarget.getName() + "&7's inventory."));
						ontarget.sendMessage(
								Main.color("&9Inventory> &e" + sender.getName() + " &7has cleared your inventory."));
					}
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "incognito":
		case "vanish":
			prefix = "&9Incognito> &7";
			if (Main.staff.contains(rank)) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					uuid = player.getUniqueId() + "";
					if (Main.data.contains("Users." + uuid + ".vanished")) {
						Main.data.set("Users." + uuid + ".vanished",
								!Main.data.getBoolean("Users." + uuid + ".vanished"));
					} else {
						Main.data.set("Users." + uuid + ".vanished", true);
					}
					if (Main.data.getBoolean("Users." + uuid + ".vanished")) {
						sender.sendMessage(Main.color(prefix
								+ "You are now incognito. Your status will only change when you run &e/vanish &7again."));
						if (!player.getWorld().getName().contains("Lobby-")) {
							Main.tellPlayers("&8Quit> &7" +Main.teamCol(Main.getTeam(player)) + player.getName(), player.getWorld().getName());
							player.setAllowFlight(true);
						}
						for (Player vtarget : player.getWorld().getPlayers()) {
							if (ranks.getInt(Main.getRankID(vtarget.getUniqueId()) + ".rank") < ranks
									.getInt(Main.getRankID(player.getUniqueId()) + ".rank")) {
								vtarget.hidePlayer(player);
							}
						}
					} else {
						sender.sendMessage(Main.color(prefix
								+ "You are no longer incognito. Your status will only change when you run &e/vanish &7again."));
						if (!player.getWorld().getName().contains("Lobby-")) {
							Main.tellPlayers("&8Join> &7" + player.getName(), player.getWorld().getName());
							if (player.isFlying())
								player.teleport(player.getWorld().getSpawnLocation());
							player.setFlying(false);
							player.setAllowFlight(false);
						}
						for (Player vtarget : player.getWorld().getPlayers())
							vtarget.showPlayer(player);
					}
				} else {
					sender.sendMessage(Main.color(prefix + "Poof! You're invisible."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "setlevel":
			prefix = "&9Client Manager> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 2) {
					target = Bukkit.getOfflinePlayer(args[0]);
					if (Main.isNumber(args[1])) {
						Main.data.set("Users." + target.getUniqueId() + ".level", Double.valueOf(args[1]));
						sender.sendMessage(
								Main.color(prefix + "Succesfully updated &e" + target.getName() + "'s&7 level to "
										+ Main.getCol(Main.getLevel(target.getUniqueId())) + args[1] + "&7."));
					} else {
						sender.sendMessage(Main.color(prefix + "Invalid number!"));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/setlevel [player] [level]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}

	public static void kbEntity(Entity entity, Location loc) {
		Vector vel = entity.getLocation().toVector().subtract(loc.toVector()).normalize();
		entity.setVelocity(vel.multiply(2).setY(1));
	}
}
