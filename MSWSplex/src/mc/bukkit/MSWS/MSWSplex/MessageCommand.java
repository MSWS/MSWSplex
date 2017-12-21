package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageCommand {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner", temprank = "owner", prefix;
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		ConfigurationSection ranks = Main.ranks;
		FileConfiguration config = Main.config;
		switch (command.toLowerCase()) {
		case "admin":
		case "a":
			prefix = "&9Message> &7";
			if (sender instanceof Player) {
				String uuid = ((Player) sender).getUniqueId() + "";
				if (Main.data.contains("Users." + uuid + ".history")) {
					for (String res : Main.data.getConfigurationSection("Users." + uuid + ".history").getKeys(false)) {
						if (System
								.currentTimeMillis() < (Main.data.getLong("Users." + uuid + ".history." + res + ".date")
										+ Main.data.getLong("Users." + uuid + ".history." + res + ".duration") * 60
												* 1000)
								|| Main.data.getLong("Users." + uuid + ".history." + res + ".duration") == -1) {
							if (Main.data.getString("Users." + uuid + ".history." + res + ".type")
									.matches("chatoffense")) {
								String time = TimeManagement.getTime(
										(long) ((Main.data.getLong("Users." + uuid + ".history." + res + ".date")
												+ Main.data.getLong("Users." + uuid + ".history." + res + ".duration")
														* 60 * 1000)
												- System.currentTimeMillis()) / 60000);
								if (Main.data.getInt("Users." + uuid + ".history." + res + ".duration") == -1)
									time = "Permanent";
								sender.sendMessage(Main.color("&9Punish> &7Shh, you're muted because "
										+ Main.data.getString("Users." + uuid + ".history." + res + ".reason") + "by "
										+ Main.data.getString("Users." + uuid + ".history." + res + ".user") + " for &a"
										+ time + "&7."));
								return;
							}
						}
					}
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
				return;
			}
			if (args.length == 0) {
				sender.sendMessage(Main.color(prefix + "/a <message>"));
				return;
			}
			String msg = "";
			for (String res : args) {
				msg = msg + res + " ";
			}
			sendA((Player) sender, null, msg, ((Player) sender).getWorld());
			break;
		case "ma":
			prefix = "&9Message> &7";
			if (sender instanceof Player)
				temprank = Main.getRankID(((Player) sender).getUniqueId());
			if (Main.staff.contains(temprank)) {
				msg = "";
				if (args.length < 1) {
					sender.sendMessage(Main.color("&6Message &7/ma <player> [message]"));
					return;
				} else {
					if (args.length < 2) {
						msg = config.getStringList("DMessages")
								.get((int) Math.floor(Math.random() * config.getStringList("DMessages").size()));
					} else {
						for (String res : args)
							if (res != args[0])
								msg = msg + res + " ";
					}
				}
				msg = Main.filter(msg);
				Player ontarget = Main.getPlayer(args[0], sender);
				if (ontarget == null)
					return;
				if (sender instanceof Player) {
					if (Main.data.contains("Users." + ontarget.getUniqueId() + ".ignoring")) {
						if (Main.data.getStringList("Users." + ontarget.getUniqueId() + ".ignoring")
								.contains(((Player) sender).getUniqueId() + "")) {
							sender.sendMessage(Main.color("&9Message> &7You cannot message that player."));
							return;
						}
					}
				}
				sendA(((Player) sender), ontarget, msg, ((Player) sender).getWorld());
				Main.recent.put(sender, ontarget);
			} else {
				Main.noPerm(sender);
			}
			break;
		case "ra":
			prefix = "&9Message> &7";
			if (sender instanceof Player)
				temprank = Main.getRankID(((Player) sender).getUniqueId());
			if (Main.staff.contains(temprank)) {
				msg = "";
				if (args.length < 1)
					msg = config.getStringList("DMessages")
							.get((int) Math.floor(Math.random() * config.getStringList("DMessages").size()));
				CommandSender ontarget = Main.recent.get(sender);
				if (ontarget == null) {
					sender.sendMessage(Main.color(prefix + "That player is now offline!"));
					return;
				}
				if (sender instanceof Player) {
					if (Main.data.contains("Users." + ((Player) ontarget).getUniqueId() + ".ignoring")) {
						if (Main.data.getStringList("Users." + ((Player) ontarget).getUniqueId() + ".ignoring")
								.contains(((Player) sender).getUniqueId() + "")) {
							sender.sendMessage(Main.color("&9Message> &7You cannot message that player."));
							return;
						}
					}
				}
				for (String res : args)
					msg = msg + res + " ";
				msg = Main.filter(msg);
				sendA(((Player) sender), (Player) ontarget, msg, ((Player) sender).getWorld());
				Main.recent.put(sender, ontarget);
			} else {
				Main.noPerm(sender);
			}
			break;
		case "message":
		case "whisper":
		case "tell":
		case "w":
		case "m":
			prefix = "&9Message> &7";
			msg = "";
			if (sender instanceof Player) {
				String uuid = ((Player) sender).getUniqueId() + "";
				if (Main.data.contains("Users." + uuid + ".history")) {
					for (String res : Main.data.getConfigurationSection("Users." + uuid + ".history").getKeys(false)) {
						if (Main.isActive(((Player) sender), res)) {
							if (Main.data.getString("Users." + uuid + ".history." + res + ".type")
									.matches("chatoffense")) {
								String time = TimeManagement.getTime(
										(int) ((Main.data.getLong("Users." + uuid + ".history." + res + ".date")
												+ Main.data.getInt("Users." + uuid + ".history." + res + ".duration")
														* 60 * 1000)
												- System.currentTimeMillis()) / 60000);
								if (Main.data.getInt("Users." + uuid + ".history." + res + ".duration") == -1)
									time = "Permanent";
								sender.sendMessage(Main.color("&9Punish> &7Shh, you're muted because "
										+ Main.data.getString("Users." + uuid + ".history." + res + ".reason") + "by "
										+ Main.data.getString("Users." + uuid + ".history." + res + ".user") + " for &a"
										+ time + "&7."));
								return;
							}
						}
					}
				}
			}
			if (args.length == 0) {
				sender.sendMessage(Main.color("&9Message> &7/tell [player] [message]"));
				return;
			}
			if (Main.getPlayer(args[0], sender) == null
					|| Main.data.getBoolean("Users." + Main.getPlayer(args[0], sender).getUniqueId() + ".vanished")) {
				return;
			}
			Player ontarget = Main.getPlayer(args[0], sender);
			if (sender instanceof Player) {
				if (Main.data.contains("Users." + ontarget.getUniqueId() + ".ignoring")) {
					if (Main.data.getStringList("Users." + ontarget.getUniqueId() + ".ignoring")
							.contains(((Player) sender).getUniqueId() + "")) {
						sender.sendMessage(Main.color("&dThat player is ignoring you."));
						return;
					}
				}
			}

			if (sender instanceof Player) {
				if (!Main.data.getBoolean("Users." + ontarget.getUniqueId() + ".notify.pm")) {
					sender.sendMessage(Main.color("&d" + ontarget.getName() + " has private messaging disabled."));
					return;
				} else if (!Main.data.getBoolean("Users." + ((Player) sender).getUniqueId() + ".notify.pm")) {
					sender.sendMessage(Main
							.color("&9Message> &7You have Private Messaging disabled, enable it to Private Message."));
					return;
				}
			} else if (!Main.data.getBoolean("Users." + ontarget.getUniqueId() + ".notify.pm")) {
				sender.sendMessage(Main.color("&d" + ontarget.getName() + " has private messaging disabled."));
				return;
			}

			if (args.length == 1) {
				msg = config.getStringList("DMessages")
						.get((int) Math.floor(Math.random() * config.getStringList("DMessages").size()));
			} else {
				for (String res : args)
					if (res != args[0])
						msg = msg + res + " ";
			}
			msg = Main.filter(msg);
			ontarget.playSound(ontarget.getLocation(), Sound.NOTE_PLING, 2, 2);
			ontarget.sendMessage(Main.color("&6&l" + sender.getName() + " > " + ontarget.getName() + "&e&l ") + msg);
			sender.sendMessage(Main.color("&6&l" + sender.getName() + " > " + ontarget.getName() + "&e&l ") + msg);
			if (sender instanceof Player) {
				Player soundP = (Player) sender;
				soundP.playSound(soundP.getLocation(), Sound.NOTE_PLING, 2, 2);
			}
			Main.recentDM.put(sender, ontarget);
			break;
		case "reply":
		case "r":
			prefix = "&9Message> &7";
			msg = "";
			if (args.length == 0) {
				sender.sendMessage(Main.color("&9Message> &7/r [message]"));
				return;
			}
			if (!Main.recentDM.containsKey(sender)) {
				sender.sendMessage(Main.color("&9Message> &7You have not messaged any one recently."));
				return;
			}
			ontarget = (Player) Main.recentDM.get(sender);
			if (ontarget == null) {
				Main.notFound(Main.recentDM.get(sender).getName(), sender, true);
				return;
			}
			if (Main.data.getBoolean("Users." + ontarget.getUniqueId() + ".vanished")) {
				Main.notFound(Main.recentDM.get(sender).getName(), sender, true);
				return;
			}
			if (sender instanceof Player) {
				if (Main.data.contains("Users." + ontarget.getUniqueId() + ".ignoring")) {
					if (Main.data.getStringList("Users." + ontarget.getUniqueId() + ".ignoring")
							.contains(((Player) sender).getUniqueId() + "")) {
						sender.sendMessage(Main.color("&dThat player is ignoring you."));
						return;
					}
				}
			}
			for (String res : args)
				msg = msg + res + " ";
			msg = Main.filter(msg);
			ontarget.playSound(ontarget.getLocation(), Sound.NOTE_PLING, 2, 2);
			ontarget.sendMessage(Main.color("&6&l" + sender.getName() + " > " + ontarget.getName() + "&e&l ") + msg);
			sender.sendMessage(Main.color("&6&l" + sender.getName() + " > " + ontarget.getName() + "&e&l ") + msg);
			if (sender instanceof Player) {
				Player soundP = (Player) sender;
				soundP.playSound(soundP.getLocation(), Sound.NOTE_PLING, 2, 2);
			}
			Main.recentDM.put(sender, ontarget);
			break;
		case "s":
			prefix = "&9Broadcast> &7";
			if (ranks.getInt(rank + ".rank") > 11) {
				if (args.length == 0) {
					sender.sendMessage(Main.color(prefix + "/s [message]"));
					return;
				}
				msg = "";
				for (String res : args)
					msg = msg + res + " ";
				if (Main.filter(msg.replace("*", "")).contains("*")) {
					Main.addPunish(sender.getName(), "GWEN", "Compromised Account - [Broadcasted \"" + msg + "\"]",
							"other", 4);
					return;
				}
				for (Player bctarget : Bukkit.getOnlinePlayers()) {
					if (sender instanceof Player)
						if (bctarget.getWorld() == ((Player) sender).getWorld())
							bctarget.sendMessage(Main.color("&l" + sender.getName() + " &b") + msg);
				}
				return;
			} else {
				Main.noPerm(sender);
			}
			break;
		case "announce":
			prefix = "&9Announce> &7";
			if (ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 0) {
					sender.sendMessage(Main.color("&9Announcement> &7/announce [message]"));
					return;
				}
				msg = "";
				for (String res : args)
					msg = msg + res + " ";
				if (Main.filter(msg.replace("*", "")).contains("*")) {
					Main.addPunish(sender.getName(), "GWEN", "Compromised Account - [Broadcasted \"" + msg + "\"]",
							"other", 4);
					return;
				}
				for (Player bctarget : Bukkit.getOnlinePlayers()) {
					bctarget.sendMessage(Main.color("&9Announcement> &b" + msg));
					bctarget.sendTitle(Main.color("&eAnnouncement"), Main.color(msg));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}

	public static void sendA(Player sender, Player receiver, String msg, World world) {
		String sprefix, rprefix;
		String prefix = "&9Message> &7";
		Main.hasA.put(sender, sender.getWorld().getName());
		if (Main.ranks.getInt(Main.getRankID(sender.getUniqueId()) + ".rank") == 0) {
			sprefix = "Player ";
		} else {
			sprefix = Main.getRank(sender.getUniqueId()).substring(0, 2)
					+ Main.camelCase(Main.getRank(sender.getUniqueId()).substring(4));
		}
		if (receiver != null) {
			if (Main.ranks.getInt(Main.getRankID(receiver.getUniqueId()) + ".rank") == 0) {
				rprefix = "Player ";
			} else {
				rprefix = Main.getRank(receiver.getUniqueId()).substring(0, 2)
						+ Main.camelCase(Main.getRank(receiver.getUniqueId()).substring(4));
			}
			for (Player target : world.getPlayers()) {
				if (target == sender) {
					target.sendMessage(Main.color("&d-> &r" + rprefix + receiver.getName() + " &d") + msg);
					target.playSound(target.getLocation(), Sound.NOTE_PLING, 2, 2);
				}
				if (target == receiver) {
					target.sendMessage(Main.color("&d<- &r" + sprefix + sender.getName() + " &d") + msg);
					target.playSound(target.getLocation(), Sound.NOTE_PLING, 2, 2);
				} else if (Main.ranks.getInt(target.getUniqueId() + ".rank") >= 11) {
					target.sendMessage(Main
							.color(sprefix + sender.getName() + " &d-> " + rprefix + receiver.getName() + " &d" + msg));
					target.playSound(target.getLocation(), Sound.NOTE_PLING, 2, 2);
				}
			}
		} else {
			for (Player target : world.getPlayers()) {
				if (Main.ranks.getInt(Main.getRankID(target.getUniqueId()) + ".rank") >= 11 || target == sender) {
					target.sendMessage(Main.color(sprefix + sender.getName() + " &d" + msg));
					target.playSound(target.getLocation(), Sound.NOTE_PLING, 2, 2);
				}
				if (sender instanceof Player && Main.hasA.containsKey(((Player) sender))) {
					if (sender.getWorld().getName() != Main.hasA.get(((Player) sender))) {
						if (Main.ranks.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank") < 11)
							sender.sendMessage(Main.color(prefix
									+ "If there are any staff currently online, you will receive a reply shortly."));
					}
				} else {
					if (Main.ranks.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank") < 11)
						sender.sendMessage(Main.color(
								prefix + "If there are any staff currently online, you will receive a reply shortly."));
				}
			}
		}
	}
}
