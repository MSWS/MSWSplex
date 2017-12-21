package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		String prefix = "&9Ping> &7";
		if (Main.ranks.getInt(rank + ".rank") >= 16 && sender instanceof Player) {
			prefix = "&9LagMeter> &7";
			Player player = (Player) sender;
			sender.sendMessage(Main.color(prefix+"Live-------&e20"));
			sender.sendMessage(Main.color("&6Your ping: &7" + Main.getPing(player)));
			sender.sendMessage(Main.color("&6Loaded Chunks: " + player.getWorld().getLoadedChunks().length));
		} else {
			sender.sendMessage(Main.color(prefix + "PONG!"));
		}
	}
}
