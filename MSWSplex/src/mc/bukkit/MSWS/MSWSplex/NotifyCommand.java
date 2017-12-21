package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotifyCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9Notify> &7";
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String uuid = player.getUniqueId() + "";
			if (args.length != 1) {
				sender.sendMessage(Main.color(prefix + "/notify [chatNotify|joinNotify]"));
			} else {
				Main.data.set("Users." + uuid + ".notify." + args[0],
						!Main.data.getBoolean("Users." + uuid + ".notify." + args[0]));
				switch (args[0].toLowerCase()) {
				case "chat":
					if (Main.data.getBoolean("Users." + uuid + ".notify." + args[0])) {
						sender.sendMessage(Main.color(
								prefix + "You will receive notifications when someone says your username in chat."));
					} else {
						sender.sendMessage(Main.color(prefix
								+ "You will no longer receive notifications when someone says your username in chat."));
					}
					break;
				case "join":
					if (Main.data.getBoolean("Users." + uuid + ".notify." + args[0])) {
						sender.sendMessage(
								Main.color(prefix + "You will receive notifications when someone joins the server."));
					} else {
						sender.sendMessage(Main.color(
								prefix + "You will no longer receive notifications when someone joins the server."));
					}
					break;
				case "kb":
					if (Main.ranks.getInt(Main.getRankID(player.getUniqueId()) + ".rank") >= 11) {
						if (Main.data.getBoolean("Users." + uuid + ".notify.kb")) {
							sender.sendMessage(Main.color(prefix + "Lobby Knockback is disabled."));
						} else {
							sender.sendMessage(Main.color(prefix + "Lobby Knockback is enabled."));
						}
					} else {
						Main.noPerm(sender);
					}
					break;
				case "gwen":
					if (Main.ranks.getInt(Main.getRankID(player.getUniqueId()) + ".rank") >= 11) {
						if (Main.data.getBoolean("Users." + uuid + ".notify.gwen")) {
							sender.sendMessage(Main.color(prefix + "Global GWEN is disabled."));
						} else {
							sender.sendMessage(Main.color(prefix + "Global GWEN is enabled."));
						}
					} else {
						Main.noPerm(sender);
					}
					break;
				default:
					sender.sendMessage(Main.color(prefix + "/notify [chatNotify|joinNotify|kb]"));
					return;
				}
			}
		} else {
			sender.sendMessage(Main.color(prefix + "You must be a player."));
		}
	}
}
