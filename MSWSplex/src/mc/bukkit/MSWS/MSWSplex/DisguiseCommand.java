package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DisguiseCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "disguise":
		case "d":
			String prefix = "&9Disguise> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 1) {
						try {
							EntityType.valueOf(args[0].toUpperCase());
						} catch (Exception e) {
							player.sendMessage(Main.color(prefix + "Unknown entity."));
							return;
						}
						Main.disguise(player, EntityType.valueOf(args[0].toUpperCase()));
						sender.sendMessage(Main.color(prefix + "Succesfully disguised as a " + args[0]));
					} else {
						sender.sendMessage(Main.color(prefix + "/disguise [entity]"));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "undisguise":
			prefix = "&9Disguise> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (Main.disguise.containsKey(player)) {
						Main.disguise.remove(player);
						sender.sendMessage(Main.color(prefix + "Succesfully undisguised."));
					} else {
						sender.sendMessage(Main.color(prefix + "You are not disguised."));
					}
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
