package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class HoloManager {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "holo":
			String prefix = "&9Holograms> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 17) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 0) {
						sender.sendMessage(Main.color(prefix + "Available Commands:"));
						sender.sendMessage(
								Main.color("&4/holo create [id] [String] &7Creates a hologram at your location"));
						sender.sendMessage(Main.color("&4/holo delete [id] &7Deletes a hologram"));
						sender.sendMessage(Main.color("&4/holo list <range> &7Lists nearby holograms"));
						sender.sendMessage(Main.color("&4/holo reload &7Reloads all holograms"));
					} else {
						switch (args[0].toLowerCase()) {
						case "create":
							if (args.length < 3) {
								sender.sendMessage(Main.color(prefix + "/holo create [id] [String]"));
								return;
							}
							String name = "";
							for (String res : args)
								if (res != args[0] && res != args[1])
									name = name + res + " ";
							name = name.trim();
							if (player.getWorld().getName().contains("Backup-")) {
								Main.holos.set("Holos." + args[1] + ".location.world", "global");
							} else {
								Main.holos.set("Holos." + args[1] + ".location.world", player.getWorld().getName());
							}
							Main.holos.set("Holos." + args[1] + ".location.X", player.getLocation().getX());
							Main.holos.set("Holos." + args[1] + ".location.Y", player.getLocation().getY() - 1.5);
							Main.holos.set("Holos." + args[1] + ".location.Z", player.getLocation().getZ());
							Main.holos.set("Holos." + args[1] + ".name", name + "&r");
							Main.reloadHolograms();
							sender.sendMessage(
									Main.color(prefix + "Succesfully created a Hologram. [ID: &6" + args[1] + "&7]"));
							break;
						case "delete":
							if (args.length != 2) {
								sender.sendMessage(Main.color("&4/holo delete [id] &7Deletes a hologram"));
								return;
							} else {
								if (Main.holos.contains("Holos." + args[1])) {
									for (Entity e : player.getWorld().getEntities()) {
										if (e.getName().equals(
												Main.color(Main.holos.getString("Holos." + args[1] + ".name")))) {
											e.remove();
											sender.sendMessage(Main
													.color(prefix + "Succesfully removed &e" + e.getName() + "&7."));
										}
									}
									Main.holos.set("Holos." + args[1], null);
								} else {
									sender.sendMessage(Main.color(prefix + "Unknown hologram ID."));
								}
							}
							break;
						case "list":
							boolean has = false;
							if (args.length == 2) {
								double range = Double.valueOf(args[1]);
								for (Entity e : player.getNearbyEntities(range, range, range)) {
									if (e.getType() == EntityType.ARMOR_STAND) {
										if (!Main.getHoloID(e.getName()).equals("")) {
											sender.sendMessage(Main.color(prefix + "ID: &e"
													+ Main.getHoloID(e.getName()) + " &7Name: &e" + e.getName()));
											has = true;
										}
									}
								}
								if (!has)
									sender.sendMessage(Main.color(
											prefix + "There are no holograms within &e" + range + " &7blocks of you."));
							} else {
								for (Entity e : player.getWorld().getEntities()) {
									if (e.getType() == EntityType.ARMOR_STAND) {
										if (!Main.getHoloID(e.getName()).equals("")) {
											sender.sendMessage(Main.color(prefix + "ID: &e"
													+ Main.getHoloID(e.getName()) + " &7Name: &e" + e.getName()));
											has = true;
										}
									}
								}
								if (!has)
									sender.sendMessage(Main.color(prefix + "There are no nearby Holograms."));
							}

							break;
						case "reload":
							Main.reloadHolograms();
							sender.sendMessage(Main.color(prefix + "Succesfully reloaded all holograms."));
							break;
						}
					}
					Main.save.put("holo", true);
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "npc":
			prefix = "&9NPC> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 17) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 0) {
						sender.sendMessage(Main.color(prefix + "Available Commands:"));
						sender.sendMessage(
								Main.color("&4/npc create [id] [Entity] [Name] &7Creates a NPC at your location"));
						sender.sendMessage(Main.color("&4/npc delete [id] &7Deletes a NPC"));
						sender.sendMessage(Main.color("&4/npc list <range> &7Lists nearby NPCs"));
						sender.sendMessage(Main.color("&4/npc reload &7Reloads all NPCs"));
					} else {
						switch (args[0].toLowerCase()) {
						case "create":
							if (args.length < 4) {
								sender.sendMessage(Main
										.color("&4/npc create [id] [Entity] [Name] &7Creates a NPC at your location"));
							} else {
								String name = "";
								for (String res : args)
									if (res != args[0] && res != args[1] && res != args[2])
										name = name + res + " ";
								name = name.trim();
								try {
									EntityType.valueOf(args[2].toUpperCase());
								} catch (Exception unused) {
									sender.sendMessage(Main.color(prefix + "Invalid Entity!"));
									return;
								}

								if (player.getWorld().getName().contains("Backup-")) {
									Main.holos.set("NPC." + args[1] + ".location.world", "global");
									Main.holos.set("Holos." + args[1] + "npc.location.world", "global");
								} else {
									Main.holos.set("NPC." + args[1] + ".location.world", player.getWorld().getName());
									Main.holos.set("Holos." + args[1] + "npc.location.world",
											player.getWorld().getName());
								}
								Main.holos.set("NPC." + args[1] + ".location.X", player.getLocation().getX());
								Main.holos.set("NPC." + args[1] + ".location.Y", player.getLocation().getY());
								Main.holos.set("NPC." + args[1] + ".location.Z", player.getLocation().getZ());
								Main.holos.set("NPC." + args[1] + ".location.yaw", player.getLocation().getYaw());
								Main.holos.set("NPC." + args[1] + ".location.pitch", player.getLocation().getPitch());
								Main.holos.set("NPC." + args[1] + ".type", args[2].toUpperCase());
								Main.holos.set("NPC." + args[1] + ".name", name + "&r");

								Main.holos.set("Holos." + args[1] + "npc.location.X", player.getLocation().getX());
								Main.holos.set("Holos." + args[1] + "npc.location.Y", player.getLocation().getY());
								Main.holos.set("Holos." + args[1] + "npc.location.Z", player.getLocation().getZ());
								Main.holos.set("Holos." + args[1] + "npc.name", name + "&r");
								Main.reloadNPCs();
								sender.sendMessage(
										Main.color(prefix + "Succesfully created an NPC. [ID: &6" + args[1] + "&7]"));
							}
							break;
						case "delete":
							if (args.length != 2) {
								sender.sendMessage(Main.color("&4/npc delete [id] &7Deletes a NPC"));
								return;
							} else {
								if (Main.holos.contains("NPC." + args[1])) {
									for (Entity e : player.getWorld().getEntities()) {
										if (e.getName()
												.equals(Main.color(Main.holos.getString("NPC." + args[1] + ".name")))) {
											e.remove();
											sender.sendMessage(Main
													.color(prefix + "Succesfully removed &e" + e.getName() + "&7."));
										}
									}
									Main.holos.set("NPC." + args[1], null);
								} else {
									sender.sendMessage(Main.color(prefix + "Unknown NPC ID."));
								}
							}
							break;
						case "list":
							boolean has = false;
							if (args.length == 2) {
								double range = Double.valueOf(args[1]);
								for (Entity e : player.getWorld().getEntities()) {
									if (!Main.getNPCID(e.getName()).equals("")) {
										sender.sendMessage(Main.color(prefix + "ID: &e" + Main.getNPCID(e.getName())
												+ " &7Name: &e" + e.getName()));
										has = true;
									}
								}
								if (!has)
									sender.sendMessage(Main.color(
											prefix + "There are no NPCs within &e" + range + " &7blocks of you."));
							} else {
								for (Entity e : player.getWorld().getEntities()) {
									if (!Main.getNPCID(e.getName()).equals("")) {
										sender.sendMessage(Main.color(prefix + "ID: &e" + Main.getNPCID(e.getName())
												+ " &7Name: &e" + e.getName()));
										has = true;
									}
								}
								if (!has)
									sender.sendMessage(Main.color(prefix + "There are no nearby NPCs."));
							}
							break;
						case "reload":
							sender.sendMessage(Main.color(prefix + "NPCs succesfully reloaded."));
							Main.reloadNPCs();
							break;
						}
					}
					Main.save.put("holo", true);
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}
}
