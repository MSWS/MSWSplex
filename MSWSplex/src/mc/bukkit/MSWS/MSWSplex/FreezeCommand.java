package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class FreezeCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		Player player;
		ConfigurationSection ranks = Main.ranks;
		String prefix = "&9Freeze> &7";
		if (ranks.getInt(rank + ".rank") >= 16) {
			if (args.length > 0) {
				Boolean frz;
				switch (args[0].toLowerCase()) {
				case "here":
					if (args.length == 2) {
						frz = Boolean.valueOf(args[1]);
						if (sender instanceof Player) {
							player = (Player) sender;
							for (Player ftarget : player.getWorld().getPlayers()) {
								if (ranks.getInt(Main.getRankID(ftarget.getUniqueId()) + ".rank") < ranks
										.getInt(rank + ".rank")) {
									if (frz) {
										ftarget.sendMessage(Main.color(prefix + "You have been freezed."));
										Main.frozen.put(ftarget, ftarget.getLocation());
									} else {
										Main.frozen.remove(ftarget);
										ftarget.sendMessage(Main.color(prefix + "You have been thawed."));
									}
								}
								if (frz) {
									sender.sendMessage(Main.color(prefix + "everyone in you world is now frozen."));
								} else {
									sender.sendMessage(
											Main.color(prefix + "everyone in your world is no longer forzen."));
								}
							}
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player to use &ehere&7."));
						}
					} else {
						sender.sendMessage(
								Main.color("&4/freeze here [true/false] &7Freezes everyone in the same world"));
					}
					break;
				case "all":
					if (args.length == 2) {
						frz = Boolean.valueOf(args[1]);
						for (Player ftarget : Bukkit.getOnlinePlayers()) {
							if (ranks.getInt(Main.getRankID(ftarget.getUniqueId()) + ".rank") < ranks
									.getInt(rank + ".rank")) {
								if (frz) {
									ftarget.sendMessage(Main.color(prefix + "You have been freezed."));
									Main.frozen.put(ftarget, ftarget.getLocation());
								} else {
									Main.frozen.remove(ftarget);
									ftarget.sendMessage(Main.color(prefix + "You have been thawed."));
								}
							}
						}
						if (frz) {
							sender.sendMessage(Main.color(prefix + "everyone is now frozen."));
						} else {
							sender.sendMessage(Main.color(prefix + "everyone is no longer frozen."));
						}
					} else {
						sender.sendMessage(Main.color("&4/freeze all [true/false] &7Freezes everyone"));
					}
					break;
				default:
					Player ftarget = Main.getPlayer(args[0], sender);
					if (Main.frozen.containsKey(ftarget)) {
						frz = !Main.frozen.containsKey(ftarget);
					} else {
						frz = true;
					}
					if (ftarget != null) {
						if (ranks.getInt(Main.getRankID(ftarget.getUniqueId()) + ".rank") <= ranks
								.getInt(rank + ".rank")) {
							if (frz) {
								ftarget.sendMessage(Main.color(prefix + "You have been freezed."));
								sender.sendMessage(Main.color(prefix + ftarget.getName() + " is now frozen."));
								Main.frozen.put(ftarget, ftarget.getLocation());
							} else {
								Main.frozen.remove(ftarget);
								ftarget.sendMessage(Main.color(prefix + "You have been thawed."));
								sender.sendMessage(Main.color(prefix + ftarget.getName() + " is no longer frozen."));
							}
						} else {
							sender.sendMessage(Main.color(prefix + "You cannot freeze that person."));
						}
					}
					break;
				}
			} else {
				sender.sendMessage(Main.color(prefix + "Available Commands"));
				sender.sendMessage(Main.color("&4/freeze [player] <true/false> &7Freezes a player"));
				sender.sendMessage(Main.color("&4/freeze all [true/false] &7Freezes everyone"));
				sender.sendMessage(Main.color("&4/freeze here [true/false] &7Freezes everyone in the same world"));
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
