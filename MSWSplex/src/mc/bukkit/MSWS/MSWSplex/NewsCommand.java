package mc.bukkit.MSWS.MSWSplex;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class NewsCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9News> &7";
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		ConfigurationSection ranks = Main.ranks;
		FileConfiguration config = Main.config;
		if (ranks.getInt(rank + ".rank") >= 19) {
			if (args.length > 0) {
				List<String> messages = config.getStringList("Announcements");
				switch (args[0].toLowerCase()) {
				case "clear":
					messages.clear();
					sender.sendMessage(Main.color(prefix + "All announcements cleared."));
					break;
				case "add":
					if (args.length >= 2) {
						String tempMsg = "";
						for (String res : args) {
							if (res != args[0])
								tempMsg = tempMsg + res + " ";
						}
						messages.add(tempMsg);
						sender.sendMessage(Main.color(prefix + "Added the announcement: &r" + tempMsg));
					} else {
						sender.sendMessage(Main.color("&4/news add [message]"));
					}
					break;
				case "remove":
					if (args.length == 2) {
						if (Integer.valueOf(args[1]) < messages.size()) {
							sender.sendMessage(Main.color(prefix + "Succesfully removed the message: &r"
									+ messages.get(Integer.valueOf(args[1]))));
							messages.remove(messages.get(Integer.valueOf(args[1])));
						} else {
							sender.sendMessage(Main.color(prefix + "Invalid ID."));
						}
					} else {
						sender.sendMessage(Main.color("&4/news remove [id]"));
					}
					break;
				case "list":
					sender.sendMessage(Main.color(prefix + "Listing announcements"));
					int pos = 0;
					for (String res : config.getStringList("Announcements")) {
						sender.sendMessage(Main.color("&e" + pos + " &7: &r" + res));
						pos++;
					}
					break;
				}
				config.set("Announcements", messages);
			} else {
				sender.sendMessage(Main.color(prefix + "Listing commands"));
				sender.sendMessage(Main.color("&4/news clear"));
				sender.sendMessage(Main.color("&4/news add [message]"));
				sender.sendMessage(Main.color("&4/news remove [id]"));
				sender.sendMessage(Main.color("&4/news list"));
			}
		} else {
			Main.noPerm(sender);
		}

	}
}
