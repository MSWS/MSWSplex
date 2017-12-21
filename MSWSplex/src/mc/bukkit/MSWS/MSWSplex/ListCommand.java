package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		String prefix = "&9List> &7";
		if (Main.ranks.getInt(rank + ".rank") >= 16) {
			if (args.length == 0) {
				if (Bukkit.getOnlinePlayers().size() == 0) {
					sender.sendMessage(Main.color(prefix + "There is no one online."));
					return;
				}
				for (World world : Bukkit.getWorlds()) {
					String tempMsg = "&2World: &a" + world.getName() + " &6Players: &e";
					for (Player ltarget : world.getPlayers()) {
						if (Main.data.getBoolean("Users." + ltarget.getUniqueId() + ".vanished")) {
							if (sender instanceof Player) {
								if (Main.ranks.getInt(Main.getRankID(ltarget.getUniqueId()) + ".rank") <= Main.ranks
										.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
									tempMsg = tempMsg + ltarget.getName() + "&7, &e";
								}
							} else {
								tempMsg = tempMsg + ltarget.getName() + "&7, &e";
							}
						} else {
							tempMsg = tempMsg + ltarget.getName() + "&7, &e";
						}
					}
					if (world.getPlayers().size() > 0)
						sender.sendMessage(Main.color(prefix + tempMsg.substring(0, tempMsg.length() - 4)));
				}
			} else {
				if (Bukkit.getWorld(args[0]) == null) {
					sender.sendMessage(Main.color(prefix + "&6" + args[0] + " &7is not a valid world."));
					return;
				}
				if (Bukkit.getWorld(args[0]).getPlayers().size() == 0) {
					sender.sendMessage(Main.color(prefix + "There is no one on &6" + args[0] + "&7."));
				} else {
					String tempMsg = "&2World: &a" + args[0] + " &6Players: &e";
					for (Player ltarget : Bukkit.getWorld(args[0]).getPlayers()) {
						if (Main.data.getBoolean("Users." + ltarget.getUniqueId() + ".vanished")) {
							if (sender instanceof Player) {
								if (Main.ranks.getInt(Main.getRankID(ltarget.getUniqueId()) + ".rank") <= Main.ranks
										.getInt(Main.getRankID(((Player) sender).getUniqueId()) + ".rank")) {
									tempMsg = tempMsg + ltarget.getName() + "&7, &e";
								}
							} else {
								tempMsg = tempMsg + ltarget.getName() + "&7, &e";
							}
						} else {
							tempMsg = tempMsg + ltarget.getName() + "&7, &e";
						}
						sender.sendMessage(Main.color(prefix + tempMsg.substring(0, tempMsg.length() - 4)));
					}
				}
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
