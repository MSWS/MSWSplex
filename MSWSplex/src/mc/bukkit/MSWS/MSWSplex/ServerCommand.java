package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9Portal> &7";
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				if (args[0].matches("(Staff-1|Test-1|CUST-1|Backup-1)")
						&& !Main.staff.contains(Main.getRankID(player.getUniqueId()))) {
					sender.sendMessage(
							Main.color(prefix + "&cYou do not have permission to access &e" + args[0] + "&7."));
					return;
				}
				if (args[0].contains("Backup-") && !Main.getRankID(player.getUniqueId()).equals("owner")) {
					sender.sendMessage(
							Main.color(prefix + "&cYou do not have permission to access &e" + args[0] + "&7."));
					return;
				}
				if (Bukkit.getWorld(args[0]) == null || args[0].contains("world")) {
					sender.sendMessage(Main.color(prefix + "Server &6" + args[0] + " &7does not exist!"));
				} else {
					if (Bukkit.getWorld(args[0]).getName() == player.getWorld().getName()) {
						player.sendMessage(
								Main.color(prefix + "You are already on &6" + player.getWorld().getName() + "&7!"));
						return;
					}
					player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
				}
			} else {
				sender.sendMessage(
						Main.color(prefix + "You are currently on server: &6" + player.getWorld().getName()));
			}
		} else {
			sender.sendMessage(Main.color(prefix + "You must be a player."));
			return;
		}
		return;
	}
}
