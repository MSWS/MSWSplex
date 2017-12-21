package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommunityCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		int maxCom = Main.config.getInt("MaximumCommunities");
		String prefix = "&9Communities &7";
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String uuid = player.getUniqueId() + "";
			if (args.length == 0) {
				Main.openCom(player, "main");
			} else {
				switch (args[0].toLowerCase()) {
				case "help":
					sender.sendMessage(Main.color(prefix + "Available Commands"));
					sender.sendMessage(Main.color("&3/com help &7Shows community help"));
					sender.sendMessage(Main.color("&3/com mcs [community] &7Creates a community MCS"));
					sender.sendMessage(Main.color("&3/com create [Community Name] &7Creates a community"));
					sender.sendMessage(Main.color("&3/com leave [community] &7Leaves a community"));
					sender.sendMessage(Main.color("&3/com chat [community] &7Sets what community you're chatting to"));
					sender.sendMessage(Main.color("&3/com join [community] &7Joins a community"));
					sender.sendMessage(
							Main.color("&3/com desc [community] [description] &7Changes a community's description"));
					sender.sendMessage(Main.color(
							"&3/com kick [community] [player] &7Kicks a player in the community (&bCo-Leader+&7)"));
					sender.sendMessage(
							Main.color("&3/com rename [community] [New Name] &7Renames a community (&bCo-Leader+&7)"));
					break;
				case "create":
					if (Main.ranks.getInt(rank + ".rank") >= 5) {
						if (Main.getComAmount(player) < maxCom || Main.ranks.getInt(rank + ".rank") >= 16) {
							if (args.length == 2) {
								if (!args[1].matches(
										"(?i)(help|browse|mcs|create|leave|join|kick|rename|main|owner|leader|admin|srmod|mod|trainee|builder|mapper|maplead)")) {
									if (!Main.filter(args[1]).contains("*")) {
										if (!Main.comExists(args[1])) {
											Main.com.set(args[1] + ".player." + uuid + ".rank", 2);
											Main.com.set(args[1] + ".setting.c1", "&c");
											Main.com.set(args[1] + ".setting.c2", "&4");
											Main.com.set(args[1] + ".setting.c3", "&c");
											Main.com.set(args[1] + ".setting.joinStatus", "open");
											Main.com.set(args[1] + ".setting.icon", "STONE");
											Main.com.set(args[1] + ".setting.description", "None yet set");
											Main.com.set(args[1] + ".setting.faveGame", "None yet set");
											sender.sendMessage(
													Main.color(prefix + "Succesfully created community: " + args[1]));
										} else {
											sender.sendMessage(
													Main.color(prefix + "A community already exists with that name!"));
										}
									} else {
										sender.sendMessage(Main.color(prefix + "That name is unapproved."));
									}
								} else {
									sender.sendMessage(Main.color(prefix + "That name is unapproved."));
								}
							} else {
								sender.sendMessage(Main.color("&3/com create [Community Name] &7Creates a community"));
							}
						} else {
							sender.sendMessage(Main.color(prefix + "You can only own &e" + maxCom
									+ "&7 communities. (You own &e" + Main.getComAmount(player) + "&7)"));
						}
					} else {
						Main.noPerm(sender);
					}
					break;
				case "rename":
					if (args.length == 3) {
						if (Main.getRank(player, args[1]) == 2 || Main.ranks.getInt(rank + ".rank") >= 16) {
							if (Main.comExists(args[1])) {
								if (!args[2].matches(
										"(?i)(help|browse|mcs|create|leave|join|kick|rename|main|owner|leader|admin|srmod|mod|trainee|builder|mapper|maplead)")) {
									if (!Main.filter(args[2]).contains("*")) {
										Main.tellCom(
												prefix + sender.getName() + " has renamed to community to " + args[2],
												args[1]);
										Main.com.set(args[1], args[2]);
									} else {
										sender.sendMessage(Main.color(prefix + "That name is unapproved."));
									}
								} else {
									sender.sendMessage(Main.color(prefix + "That name is unapproved."));
								}
							} else {
								sender.sendMessage(Main.color(prefix + "Unable to find community."));
							}
						} else {
							Main.noPerm(sender);
						}
					} else {
						sender.sendMessage(Main
								.color("&3/com rename [community] [New Name] &7Renames a community (&bCo-Owner+&7)"));
					}
					break;
				case "mcs":
					break;
				case "leave":
					if (args.length == 2) {
						if (Main.comExists(args[1])) {
							if (Main.com.contains(args[1] + ".player." + player.getUniqueId())) {
								if (Main.getRank(player, args[1]) == 2) {
									Main.tellCom(prefix + player.getName() + " has disbanded the community.", args[1]);
									Main.com.set(args[1], null);
								} else {
									Main.tellCom(prefix + player.getName() + " has left the community.", args[1]);
									Main.com.set(args[1] + ".player." + player.getUniqueId(), null);
								}
							} else {
								sender.sendMessage(Main.color(prefix + "You are not a member of that community."));
							}
						} else {
							sender.sendMessage(Main.color(prefix + "Unable to find community."));
						}
					} else {
						sender.sendMessage(Main.color("&3/com leave [community] &7Leaves a community"));
					}
					break;
				case "join":
					if (args.length == 2) {
						if (Main.comExists(args[1])) {
							switch (Main.com.getString(args[1] + "setting.joinStatus")) {
							case "open":
								Main.com.set(args[1] + ".player." + uuid, sender.getName());
								Main.tellCom(prefix + sender.getName() + " has joined the community.", args[1]);
								break;
							case "request":
								Main.com.set(args[1] + ".requests." + uuid, sender.getName());
								Main.tellCom(prefix + sender.getName() + " has requested to join the community.",
										args[1]);
								break;
							case "closed":
								sender.sendMessage(Main.color(prefix + "Requests to join are not open."));
								break;
							}
						} else {
							sender.sendMessage(Main.color(prefix + "Unable to find community."));
						}
					} else {
						sender.sendMessage(Main.color("&3/com join [community] &7Joins a community"));
					}
					break;
				case "chat":
					if (args.length == 2) {
						if (Main.comExists(args[1])) {
							if (Main.inCom(player, args[1])) {
								sender.sendMessage(Main.color(
										prefix + "You are now talking to &e" + args[1] + " &7, use &e! &7to chat."));
								Main.comChat.put(player, args[1]);
							} else {
								sender.sendMessage(Main.color(prefix + "You are not in that community"));
							}
						} else {
							sender.sendMessage(Main.color(prefix + "Unable to find community."));
						}
					} else {
						sender.sendMessage(
								Main.color("&3/com chat [community] &7Sets what community you're chatting to"));
					}
					break;
				case "desc":
					if (args.length > 2) {
						if (Main.comExists(args[1])) {
							if (Main.getRank(player, args[1]) > 0 || Main.ranks.getInt(rank + ".rank") >= 16) {
								String desc = "";
								for (String res : args) {
									if (res != args[0] && res != args[1])
										desc = desc + res + " ";
								}
								if (desc.length() < 20) {
									if (!Main.filter(desc).contains("*")) {
										Main.com.set(args[1] + ".setting.description", desc);
										Main.tellCom(prefix + sender.getName() + " has set the description to " + desc,
												args[1]);
									} else {
										sender.sendMessage(Main.color(prefix + "That description cannot be applied."));
									}
								} else {
									sender.sendMessage(Main.color(prefix + "That description is too long."));
								}
							} else {
								Main.noPerm(sender);
							}
						} else {
							sender.sendMessage(Main.color(prefix + "Unable to find community."));
						}
					} else {
						sender.sendMessage(Main
								.color("&3/com desc [community] [description] &7Changes a community's description"));
					}
					break;
				default:
					if (Main.comExists(args[0])) {
						Main.openCom(player, args[0]);
					} else {
						sender.sendMessage(Main.color(prefix + "Unable to find community."));
					}
					break;
				}
			}
		} else {
			sender.sendMessage(Main.color(prefix + "You must be a player."));
		}
	}
}
