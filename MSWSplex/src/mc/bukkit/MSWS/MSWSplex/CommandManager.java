package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandManager implements Listener {
	@EventHandler
	public static void event(PlayerCommandPreprocessEvent event) {
		String prefix = "&9Chat> &7";
		Player player = event.getPlayer();
		String msg = event.getMessage();
		String cmd = "";
		int loc = 0, size = 0;
		for (int i = 1; i < msg.length(); i++) {
			if (msg.charAt(i) == ' ') {
				size++;
			}
		}

		String[] args = new String[size];
		for (int i = 1; i < msg.length(); i++) {
			if (msg.charAt(i) != ' ') {
				if (loc == 0) {
					cmd = cmd + msg.charAt(i);
				} else {
					if (args[loc - 1] == null)
						args[loc - 1] = "";
					args[loc - 1] = args[loc - 1] + msg.charAt(i);
				}
			} else {
				loc++;
			}
		}

		if (cmd.equalsIgnoreCase("restart")) {
			if (!Main.config.getBoolean("Features.commands.restart"))
				return;
			RestartCommand.restartEvent(event);
		}
		if (Main.config.getInt("Ranks." + Main.getRankID(player.getUniqueId()) + ".rank") < 16) {
			String[] unallowed = new String[] { "minecraft", "bukkit" };
			for (String res : unallowed) {
				if (cmd.contains(res)) {
					Main.tell(player, prefix + "Nope, not allowed!");
					event.setCancelled(true);
				}
			}
			if (cmd.matches(
					"(?i)(pl|plugins|\\?|help|ver|version|icanhasbukkit|me|say|reload|rl|kick|ban|pardon|whitelist|ban-ip)")) {
				Main.tell(player,
						"&cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
				event.setCancelled(true);
			}
		} else if (cmd.matches("(reload)")) {
			if (!Main.config.getBoolean("Features.commands.reload"))
				return;
			for (Player target : Bukkit.getOnlinePlayers())
				target.kickPlayer(Main.color(
						"&cYou have been kicked from the server: &lServer Restart\n&rThe server is currently restarting, please be patient whilst waiting.\n&2You can also check out &a&nstatus.mineplex.com"));
			Bukkit.getServer().shutdown();
			event.setCancelled(true);
		}
		if ((Main.config.contains("Features." + cmd.toLowerCase())
				&& !Main.config.getBoolean("Features." + cmd.toLowerCase()))
				|| (Main.config.contains("Features.commands." + cmd.toLowerCase())
						&& !Main.config.getBoolean("Features.commands." + cmd.toLowerCase())))
			return;
		switch (cmd.toLowerCase()) {
		case "team":
			TeamCommand.command(player, cmd, "", args);
			break;
		case "news":
			NewsCommand.command(player, cmd, "", args);
			break;
		case "toggle":
			ToggleCommand.command(player, cmd, "", args);
			break;
		case "freeze":
			FreezeCommand.command(player, cmd, "", args);
			break;
		case "gm":
		case "gamemode":
			if (!Main.config.getBoolean("Features.commands.gamemode"))
				return;
			ClientManager.command(player, cmd, "", args);
			break;
		case "updaterank":
		case "createrank":
		case "deleterank":
		case "setprefix":
		case "setlevel":
		case "listranks":
		case "getlevel":
		case "getrank":
		case "testrank":
			if (!Main.config.getBoolean("Features.ranks") || !Main.config.getBoolean("Features.levels"))
				return;
			ClientManager.command(player, cmd, "", args);
			break;
		case "kill":
		case "heal":
		case "hubitems":
		case "clear":
		case "incognito":
		case "vanish":
			ClientManager.command(player, cmd, "", args);
			break;
		case "admin":
		case "a":
		case "ra":
		case "ma":
			if (!Main.config.getBoolean("Features.commands.a"))
				return;
			MessageCommand.command(player, cmd, "", args);
			break;
		case "s":
		case "announce":
			if (!Main.config.getBoolean("Features.commands.announce"))
				return;
			MessageCommand.command(player, cmd, "", args);
			break;
		case "message":
		case "whisper":
		case "tell":
		case "w":
		case "m":
		case "reply":
		case "r":
			if (!Main.config.getBoolean("Features.messaging"))
				return;
			MessageCommand.command(player, cmd, "", args);
			break;
		case "punish":
		case "p":
		case "unpunish":
			if (!Main.config.getBoolean("Features.punish"))
				return;
			PunishCommand.command(player, cmd, "", args);
			break;
		case "ei":
		case "i":
		case "g":
		case "give":
		case "shard":
		case "gem":
			if (!Main.config.getBoolean("Features.commands.give"))
				return;
			GiveCommand.command(player, cmd, "", args);
			break;
		case "ping":
		case "lag":
			PingCommand.command(player, cmd, "", args);
			break;
		case "teleport":
		case "tp":
			if (!Main.config.getBoolean("Features.commands.teleport"))
				return;
			TeleportCommand.command(player, cmd, "", args);
			break;
		case "silence":
		case "chatslow":
		case "ignore":
		case "unignore":
			ChatControl.command(player, cmd, "", args);
			break;
		case "find":
		case "locate":
			FindCommand.command(player, cmd, "", args);
			break;
		case "spawn":
			SpawnCommand.command(player, cmd, "", args);
			break;
		case "enchant":
			EnchantManager.command(player, cmd, "", args);
			break;
		case "scoreboard":
		case "sb":
			ScoreboardCommand.command(player, cmd, "", args);
			break;
		case "event":
		case "e":
		case "hostevent":
		case "endevent":
			if (!Main.config.getBoolean("Features.eventcontrol"))
				return;
			EventCommand.command(player, cmd, "", args);
			break;
		case "report":
		case "rh":
		case "reporthandle":
		case "reportview":
		case "rv":
		case "vr":
		case "listreports":
		case "lr":
			ReportCommand.command(player, cmd, "", args);
			break;
		case "server":
			ServerCommand.command(player, cmd, "", args);
			break;
		case "f":
		case "friend":
		case "unfriend":
			FriendCommand.command(player, cmd, "", args);
			break;
		case "loadlobby":
		case "unload":
			WorldManagement.command(player, cmd, "", args);
			break;
		case "mps":
		case "hostserver":
		case "closemps":
			if (!Main.config.getBoolean("Features.commands.mps"))
				return;
			WorldManagement.command(player, cmd, "", args);
			break;
		case "lobby":
		case "leave":
		case "hub":
			if (!Main.config.getBoolean("Features.lobbies"))
				return;
			WorldManagement.command(player, cmd, "", args);
			break;
		case "send":
			WorldManagement.command(player, cmd, "", args);
			break;
		case "time":
		case "settime":
		case "seen":
			TimeManagement.command(player, cmd, "", args);
			break;
		case "party":
		case "z":
			if (!Main.config.getBoolean("Features.commands.party"))
				return;
			PartyCommand.command(player, cmd, "", args);
			Main.save.put("party", true);
			break;
		case "list":
			ListCommand.command(player, cmd, "", args);
			break;
		case "notify":
			NotifyCommand.command(player, cmd, "", args);
			break;
		case "motd":
			Main.save.put("config", true);
		case "up":
		case "radius":
		case "prefs":
		case "rename":
		case "perms":
		case "stats":
		case "saveall":
		case "setfake":
			MiscCommand.command(player, cmd, "", args);
			break;
		case "holo":
		case "npc":
			HoloManager.command(player, cmd, "", args);
			break;
		case "disguise":
		case "d":
		case "undisguise":
			DisguiseCommand.command(player, cmd, "", args);
			break;
		case "community":
		case "com":
			CommunityCommand.command(player, cmd, "", args);
			Main.save.put("community", true);
			break;
		case "invsee":
			InvSeeCommand.command(player, cmd, "", args);
			break;
		default:
			return;
		}
		Main.save.put("data", true);
		event.setCancelled(true);
	}
}
