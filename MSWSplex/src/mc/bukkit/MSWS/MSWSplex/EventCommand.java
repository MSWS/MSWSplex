package mc.bukkit.MSWS.MSWSplex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EventCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "event":
		case "e":
			String prefix = "&9Event Settings> &7";
			if (sender instanceof Player && !((Player) sender).getWorld().getName().contains("EVENT-")
					&& Main.ranks.getInt(rank + ".rank") <= 13) {
				Main.noPerm(sender);
				return;
			}
			if (Main.ranks.getInt(rank + ".rank") >= 13) {
				if (args.length >= 1) {
					String res = "";
					switch (args[0].toLowerCase()) {
					case "damage":
						if (args.length != 3) {
							sender.sendMessage(Main.color(prefix + "/e damage [cause] [true/false]"));
							String causes = "";
							for (DamageCause cause : DamageCause.values()) {
								causes = causes + cause + " ";
							}
							sender.sendMessage(Main.color("&4Causes: &7" + Main.camelCase(causes)));
							return;
						}
						if (args[1].equalsIgnoreCase("all")) {
							for (DamageCause cause : DamageCause.values()) {
								Main.causes.put(cause, Boolean.valueOf(args[2]));
							}
						} else {
							Main.causes.put(DamageCause.valueOf(args[1].toUpperCase()), Boolean.valueOf(args[2]));
						}
						res = Main.camelCase(args[1].replace("_", " ")) + " Damage";
						break;
					case "explosions":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e explosions [true/false]"));
							return;
						}
						Main.settings.put("explosions", Boolean.valueOf(args[1]) + "");
						res = "Explosions";
						break;
					case "save":
						for (DamageCause tempC : DamageCause.values()) {
							if (Main.causes.containsKey(tempC)) {
								Main.data.set("Main.settings." + tempC, Main.causes.get(tempC));
							}
						}
						Main.data.set("Main.settings.explosions", Main.settings.get("explosions") + "");
						Main.data.set("Main.settings.time", Main.settings.get("time") + "");
						Main.data.set("Main.settings.doublejump", Main.settings.get("doubleJump") + "");
						Main.data.set("Main.settings.weather", Main.settings.get("weather") + "");
						Main.data.set("Main.settings.blockPlace", Main.settings.get("blockPlace") + "");
						Main.data.set("Main.settings.blockBreak", Main.settings.get("blockBreak") + "");
						Main.data.set("Main.settings.hunger", Main.settings.get("hunger") + "");
						Main.data.set("Main.settings.health", Main.settings.get("health") + "");
						Main.data.set("Main.settings.pvp", Main.settings.get("pvp") + "");
						Main.data.set("Main.settings.pve", Main.settings.get("pve") + "");
						Main.data.set("Main.settings.evp", Main.settings.get("evp") + "");
						sender.sendMessage(Main.color(prefix + "Settings succesfully saved."));
						break;
					case "pvp":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e pvp [true/false]"));
							return;
						}
						Main.settings.put("pvp", Boolean.valueOf(args[1]) + "");
						res = "PvP";
						break;
					case "pve":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e pve [true/false]"));
							return;
						}
						Main.settings.put("pve", Boolean.valueOf(args[1]) + "");
						res = "PvE";
						break;
					case "evp":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e evp [true/false]"));
							return;
						}
						Main.settings.put("evp", Boolean.valueOf(args[1]) + "");
						res = "EvP";
						break;
					case "time":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e time [day/night/number]"));
							return;
						}
						if (args[1].equalsIgnoreCase("day")) {
							Main.settings.put("time", "6000");
						} else if (args[1].equalsIgnoreCase("night")) {
							Main.settings.put("time", "18000");
						} else if (Main.isNumber(args[1])) {
							Main.settings.put("time", Integer.valueOf(args[1]) + "");
						}
						res = "Time Set";
						break;
					case "doublejump":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e doublejump [true/false]"));
							return;
						}
						Main.settings.put("doubleJump", Boolean.valueOf(args[1]) + "");
						for (Player bctarget : Bukkit.getOnlinePlayers()) {
							if (!Boolean.valueOf(args[1]))
								if (bctarget.getGameMode() == GameMode.SURVIVAL)
									bctarget.setAllowFlight(false);
						}
						res = "Double Jump";
						break;
					case "hunger":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e hunger [saturation/off]"));
							return;
						}
						if (args[1].equalsIgnoreCase("off")) {
							Main.settings.put("hunger", null);
						} else {
							Main.settings.put("hunger", Integer.valueOf(args[1]) + "");
						}
						res = "Hunger";
						break;
					case "health":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e health [health/off]"));
							return;
						}
						if (args[1].equalsIgnoreCase("off")) {
							Main.settings.put("health", null);
						} else {
							if (Integer.valueOf(args[1]) > 0 && Integer.valueOf(args[1]) <= 20)
								Main.settings.put("health", Integer.valueOf(args[1]) + "");
						}
						res = "Health";
						break;
					case "weather":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e weather [clear|rain]"));
							return;
						}
						if (args[1].equalsIgnoreCase("rain")) {
							Main.settings.put("weather", "DOWNFALL");
						} else
							Main.settings.put("weather", WeatherType.valueOf(args[1].toUpperCase()) + "");
						res = "Weather";
						break;
					case "blockbreak":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e blockBreak [true|false]"));
							return;
						}
						Main.settings.put("blockBreak", Boolean.valueOf(args[1]) + "");
						res = "Block Break";
						break;
					case "blockplace":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e blockPlace [true|false]"));
							return;
						}
						Main.settings.put("blockPlace", Boolean.valueOf(args[1]) + "");
						res = "Block Place";
						break;
					case "itemdrop":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e itemDrop [true|false]"));
							return;
						}
						Main.settings.put("itemDrop", Boolean.valueOf(args[1]) + "");
						res = "Item Drop";
						break;
					case "itempickup":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e itemPickup [true|false]"));
							return;
						}
						Main.settings.put("itemPickup", Boolean.valueOf(args[1]) + "");
						res = "Item Pickup";
						break;
					case "reset":
						Main.settings.put("time", "6000");
						Main.settings.put("weather", "CLEAR");
						Main.settings.put("explosions", "false");
						Main.settings.put("doubleJump", "true");
						Main.settings.put("health", "20");
						Main.settings.put("hunger", "20");
						Main.settings.put("blockBreak", "false");
						Main.settings.put("blockPlace", "false");
						Main.settings.put("itemDrop", "false");
						Main.settings.put("itemPickup", "false");
						Main.settings.put("pvp", "false");
						Main.settings.put("pve", "false");
						Main.settings.put("evp", "false");
						for (DamageCause cause : DamageCause.values()) {
							Main.causes.put(cause, false);
						}
						sender.sendMessage(Main.color(prefix + "Reset event settings."));
						break;
					case "gadget":
						if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("list")) {
								sender.sendMessage(Main.color(prefix + "Listing Gadgets"));
								for (String gad : Main.gadget.getKeys(false)) {
									if (!gad.equals("Users"))
										sender.sendMessage(gad);
								}
							} else if (args.length == 3) {
								if (args[1].equalsIgnoreCase("all")) {
									if (!Boolean.valueOf(args[2]))
										for (Player player : Bukkit.getOnlinePlayers()) {
											GadgetManager.toggleGadget(player, GadgetManager.activeGadget(player));
										}
									for (String tres : Main.gadget.getKeys(false))
										if (tres != "Users")
											Main.settings.put(tres, args[2]);
									res = "All Gadgets";
								} else if (Main.gadget.contains(args[1])) {
									if (!Boolean.valueOf(args[2]))
										for (Player player : Bukkit.getOnlinePlayers()) {
											if (GadgetManager.activeGadget(player) == args[1])
												GadgetManager.toggleGadget(player, args[1]);
										}
									Main.settings.put(args[1], args[2]);
									res = GadgetManager.gadgetName(args[1]) + " Gadget";
								} else {
									sender.sendMessage(Main.color(prefix + "Gadget not found."));
								}
							} else {
								sender.sendMessage(Main.color(prefix + "/e gadget [list/gadget/all] [true/false]"));
							}
						}
						break;
					case "gm":
						if (args.length != 2) {
							sender.sendMessage(Main.color(prefix + "/e gm [player] &eGives a player creative"));
							return;
						}
						Player gTarget = Main.getPlayer(args[1], sender);
						if (gTarget != null) {
							gTarget.setGameMode(GameMode.CREATIVE);
							sender.sendMessage(Main.color(prefix + "Gave creative to &e" + gTarget.getName() + "&7."));
							gTarget.sendMessage(Main.color(prefix + "Your gamemode was updated to Creative."));
						}
						break;
					}
					if (res != "") {
						String tempMsg = "";
						if (res.contains("Damage") || res.contains("Gadget")) {
							if (args[2].equalsIgnoreCase("true")) {
								tempMsg = "&aTrue";
							} else if (args[2].equalsIgnoreCase("false")) {
								tempMsg = "&cFalse";
							} else {
								tempMsg = "&e" + args[1];
							}
						} else {
							if (args[1].equalsIgnoreCase("true")) {
								tempMsg = "&aTrue";
							} else if (args[1].equalsIgnoreCase("false")) {
								tempMsg = "&cFalse";
							} else {
								tempMsg = "&e" + args[1];
							}
						}

						for (Player bctarget : Bukkit.getOnlinePlayers())
							bctarget.sendMessage(Main.color(prefix + "&r" + res + ": " + tempMsg + "&7."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "&eAvailable Commands:"));
					sender.sendMessage(Main.color("/e damage [cause] [true/false]"));
					sender.sendMessage(Main.color("/e explosions [true/false]"));
					sender.sendMessage(Main.color("/e time [day/night]"));
					sender.sendMessage(Main.color("/e weather [clear|rain|storm]"));
					sender.sendMessage(Main.color("/e doublejump [true/false]"));
					sender.sendMessage(Main.color("/e hunger [saturation/off]"));
					sender.sendMessage(Main.color("/e health [health/off]"));
					sender.sendMessage(Main.color("/e block(Break|Place) [true|false]"));
					sender.sendMessage(Main.color("/e item(Pickup|Drop) [true|false]"));
					sender.sendMessage(Main.color("/e save &eSaves settings"));
					sender.sendMessage(Main.color("/e reset &eResets settings"));
					sender.sendMessage(Main.color("/e pvp [true/false]"));
					sender.sendMessage(Main.color("/e pve [true/false]"));
					sender.sendMessage(Main.color("/e evp [true/false]"));
					sender.sendMessage(Main.color("/e gadget [list/gadget/all] [true/false]"));
					sender.sendMessage(Main.color("/e gm [player] &eGives a player creative"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "hostevent":
			prefix = "&9Event> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				sender.sendMessage(Main.color(prefix + "Creating event server..."));
				int eAmo = 1;
				while (Bukkit.getWorld("EVENT-" + eAmo) != null) {
					eAmo++;
				}
				Bukkit.createWorld(WorldCreator.name("EVENT-" + eAmo).type(WorldType.FLAT).generateStructures(false));
				Main.mps.add("EVENT-" + eAmo);
				Bukkit.getWorld("EVENT-" + eAmo).setGameRuleValue("doMobSpawning", "false");
				Bukkit.getWorld("EVENT-" + eAmo).setGameRuleValue("doMobLoot", "false");
				sender.sendMessage(Main.color(prefix + "&6EVENT-" + eAmo + "&7 succesfully started!"));
				if (sender instanceof Player)
					((Player) sender).teleport(Bukkit.getWorld("EVENT-" + eAmo).getSpawnLocation());
			} else {
				Main.noPerm(sender);
			}
			break;
		case "endevent":
			prefix = "&9Event> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				int eAmo = 1;
				if (args.length == 1)
					eAmo = Integer.valueOf(args[0]);
				World eventWorld = Bukkit.getWorld("EVENT-" + eAmo);
				if (eventWorld == null) {
					sender.sendMessage(Main.color(prefix + "&6EVENT-" + eAmo + " &7event is not currently going."));
					return;
				}
				List<World> lobbies = new ArrayList<World>();
				for (World world : Bukkit.getWorlds()) {
					if (world.getName().contains("Lobby-")) {
						lobbies.add(world);
					}
				}
				sender.sendMessage(Main.color(prefix + "Unloading event..."));
				for (Player ePlayer : eventWorld.getPlayers()) {
					int lobbyNum = 0;
					if (lobbies.size() > 1) {
						lobbyNum = (int) Math.floor(Math.random() * lobbies.size());
					}
					World lobby = lobbies.get(lobbyNum);
					ePlayer.sendMessage(
							Main.color(prefix + "The event has closed, sending you to &6" + lobby.getName()));
					ePlayer.teleport(lobby.getSpawnLocation());
				}
				Main.deleteWorld(Bukkit.getWorld("EVENT-" + eAmo).getWorldFolder());
				Bukkit.unloadWorld("EVENT-" + eAmo, false);
				sender.sendMessage(Main.color(prefix + "&6EVENT-" + eAmo + " &7succesfully unloaded."));

			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}
}
