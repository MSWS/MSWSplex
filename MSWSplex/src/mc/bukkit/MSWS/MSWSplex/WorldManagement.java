package mc.bukkit.MSWS.MSWSplex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldManagement {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "loadlobby":
			String prefix = "&9Portal> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (args.length != 1) {
					sender.sendMessage(Main.color(prefix + "/loadlobby [World]"));
					return;
				}
				World source;
				if (sender instanceof Player) {
					source = ((Player) sender).getWorld();
				} else {
					source = Bukkit.getWorld("Backup-1");
					if (source == null)
						source = Bukkit.getWorld("Lobby-1");
				}
				sender.sendMessage(
						Main.color(prefix + "Generating &6" + args[0] + " &7based off of &6" + source.getName()));
				Bukkit.unloadWorld(args[0], false);
				Bukkit.createWorld(WorldCreator.name(args[0]).copy(source));
				World targetw = Bukkit.getWorld(args[0]);
				File targetFolder = targetw.getWorldFolder();
				Bukkit.unloadWorld(targetw, false);
				Main.copyWorld(source.getWorldFolder(), targetFolder);
				Bukkit.createWorld(WorldCreator.name(args[0]));
				sender.sendMessage(Main.color(prefix + "&6" + args[0] + " &7was succesfully created."));
				List<String> worlds = new ArrayList<String>();
				for (World world : Bukkit.getWorlds()) {
					if (!Main.mps.contains(world.getName()))
						worlds.add(world.getName());
				}
				Main.data.set("Worlds", worlds);
				Main.save.put("save", true);
			} else {
				Main.noPerm(sender);
			}
			break;
		case "unload":
			prefix = "&9Portal> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				World eventWorld = null;
				if (args.length == 0) {
					if (sender instanceof Player) {
						eventWorld = ((Player) sender).getWorld();
					} else {
						sender.sendMessage(Main.color(prefix + "You must be a player."));
					}
				} else {
					eventWorld = Bukkit.getWorld(args[0]);
				}
				if (eventWorld == null) {
					sender.sendMessage(Main.color(prefix + "[&6" + args[0] + "&7] does not exist."));
					return;
				}
				List<World> lobbies = new ArrayList<World>();
				for (World world : Bukkit.getWorlds()) {
					if (world.getName().contains("Lobby-") && world != eventWorld) {
						lobbies.add(world);
					}
				}
				sender.sendMessage(Main.color(prefix + "Unloading world..."));
				for (Player ePlayer : eventWorld.getPlayers()) {
					int lobbyNum = 0;
					if (lobbies.size() > 1) {
						lobbyNum = (int) Math.floor(Math.random() * lobbies.size());
					}
					World lobby = lobbies.get(lobbyNum);
					ePlayer.sendMessage(
							Main.color(prefix + "This server is restarting, sending you to &6" + lobby.getName()));
					ePlayer.teleport(lobby.getSpawnLocation());
				}
				Main.deleteWorld(Bukkit.getWorld(args[0]).getWorldFolder());
				Bukkit.unloadWorld(args[0], false);
				sender.sendMessage(Main.color(prefix + "&6" + args[0] + " &7succesfully unloaded."));
			} else {
				Main.noPerm(sender);
			}
			break;
		case "hostserver":
		case "mps":
			prefix = "&9Player Server Manager> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 3) {
				if (Bukkit.getWorld(sender.getName() + "-1") != null) {
					if (sender instanceof Player)
						((Player) sender).teleport(Bukkit.getWorld(sender.getName() + "-1").getSpawnLocation());
				} else {
					sender.sendMessage(Main.color(prefix + "Your server is being created..."));
					Bukkit.createWorld(
							WorldCreator.name(sender.getName() + "-1").type(WorldType.FLAT).generateStructures(false));
					World mps = Bukkit.getWorld(sender.getName() + "-1");
					Main.mps.add(mps.getName());
					mps.setGameRuleValue("doMobSpawning", "false");
					mps.setGameRuleValue("doMobLoot", "false");
					sender.sendMessage(Main.color(
							prefix + "&6" + sender.getName() + "-1 &7was succesfully created, teleporting you now..."));
					if (sender instanceof Player)
						((Player) sender).teleport(Bukkit.getWorld(sender.getName() + "-1").getSpawnLocation());
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "closemps":
			prefix = "&9Player Server Manager> &7";
			if (args.length == 1) {
				if (Main.ranks.getInt(rank + ".rank") >= 16) {
					for (Player mpsPlayer : Bukkit.getOnlinePlayers()) {
						if (args[0].equals(mpsPlayer.getName())) {
							if (Bukkit.getWorld(args[0] + "-1") == null) {
								sender.sendMessage(Main.color(prefix + "&6" + args[0] + "-1 &7does not exist."));
								return;
							}
							sender.sendMessage(Main.color(prefix + "Closing the MPS..."));
							World mps = Bukkit.getWorld(args[0] + "-1");
							List<World> lobbies = new ArrayList<World>();
							for (Player ePlayer : mps.getPlayers()) {
								int lobbyNum = 0;
								for (World world : Bukkit.getWorlds()) {
									if (world.getName().contains("Lobby-")) {
										lobbies.add(world);
									}
								}
								if (lobbies.size() > 1) {
									lobbyNum = (int) Math.floor(Math.random() * lobbies.size());
								}
								World lobby = lobbies.get(lobbyNum);
								ePlayer.sendMessage(Main.color(prefix + "&e" + sender.getName()
										+ " &7has closed the server, sending you to &6" + lobby.getName()));
								ePlayer.teleport(lobby.getSpawnLocation());
							}
							Main.deleteWorld(Bukkit.getWorld(args[0] + "-1").getWorldFolder());
							Bukkit.unloadWorld(args[0] + "-1", false);
							sender.sendMessage(Main.color(prefix + "Succesfully closed &6" + args[0] + "-1"));
							return;
						}
					}
				}
			}
			if (Bukkit.getWorld(sender.getName() + "-1") == null) {
				sender.sendMessage(Main.color(prefix + "You do not have an active MPS open."));
			} else {
				sender.sendMessage(Main.color(prefix + "Closing the MPS..."));
				World mps = Bukkit.getWorld(sender.getName() + "-1");
				List<World> lobbies = new ArrayList<World>();
				for (Player ePlayer : mps.getPlayers()) {
					int lobbyNum = 0;
					for (World world : Bukkit.getWorlds()) {
						if (world.getName().contains("Lobby-")) {
							lobbies.add(world);
						}
					}
					if (lobbies.size() > 1) {
						lobbyNum = (int) Math.floor(Math.random() * lobbies.size());
					}
					World lobby = lobbies.get(lobbyNum);
					ePlayer.sendMessage(Main.color(
							prefix + "The MPS host has closed their server, sending you to &6" + lobby.getName()));
					ePlayer.teleport(lobby.getSpawnLocation());
				}
				Main.deleteWorld(Bukkit.getWorld(sender.getName() + "-1").getWorldFolder());
				Bukkit.unloadWorld(sender.getName() + "-1", false);
				sender.sendMessage(Main.color(prefix + "Succesfully closed your MPS"));
			}
			break;
		case "lobby":
		case "leave":
		case "hub":
			prefix = "&9Portal> &7";
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.getWorld().getName().contains("Lobby-")) {
					sender.sendMessage(Main.color(prefix + "You are already in a lobby."));
					return;
				}
				int lobbyNum = 0;
				List<World> lobbies = new ArrayList<World>();
				for (World world : Bukkit.getWorlds()) {
					if (world.getName().contains("Lobby-")) {
						lobbies.add(world);
					}
				}
				if (lobbies.size() > 1)
					lobbyNum = (int) Math.floor(Math.random() * lobbies.size());

				World lobby = lobbies.get(lobbyNum);
				player.teleport(lobby.getSpawnLocation());
				break;
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
			}
		case "send":
			prefix = "&9Send> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (args.length != 2) {
					sender.sendMessage(Main.color(prefix + "/send [player] [server]"));
					return;
				}
				if (Bukkit.getWorld(args[1]) == null) {
					sender.sendMessage(Main.color(prefix + "Server &6" + args[1] + " &7does not exist!"));
					return;
				}
				if (args[0].equalsIgnoreCase("all")) {
					for (Player sTarget : Bukkit.getOnlinePlayers()) {
						sTarget.sendMessage(Main
								.color("&9Portal> &e" + sender.getName() + "&7 has sent you to &6" + args[1] + "&7."));
						sTarget.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
					}
					sender.sendMessage(Main.color(prefix + "Succesfully sent &eeveryone &7to &6" + args[1] + "&7."));
					return;
				}
				if (args[0].equalsIgnoreCase("here")) {
					if (sender instanceof Player) {
						for (Player sTarget : ((Player) sender).getWorld().getPlayers()) {
							sTarget.sendMessage(Main.color(
									"&9Portal> &e" + sender.getName() + "&7 has sent you to &6" + args[1] + "&7."));
							sTarget.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
						}
						sender.sendMessage(
								Main.color(prefix + "Succesfully sent &eeveryone &7to &6" + args[1] + "&7."));
						return;
					} else {
						sender.sendMessage(Main.color(prefix + "You must be a player to use &ehere&7."));
						return;
					}
				}
				Player player = Main.getPlayer(args[0], sender);
				if (player != null) {
					sender.sendMessage(Main
							.color(prefix + "Succesfully sent &e" + player.getName() + " &7to &6" + args[1] + "&7."));
					player.sendMessage(
							Main.color("&9Portal> &e" + sender.getName() + "&7 has sent you to &6" + args[1] + "&7."));
					player.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
				}
			} else {
				Main.noPerm(sender);
			}
			break;

		}
	}
}
