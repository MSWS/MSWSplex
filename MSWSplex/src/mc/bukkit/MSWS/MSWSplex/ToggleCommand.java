package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ToggleCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		ConfigurationSection ranks = Main.ranks;
		FileConfiguration config = Main.config;
		String prefix = "&9Toggle> &7";
		if (ranks.getInt(rank + ".rank") >= 19) {
			if (args.length != 0) {
				boolean enabled = false;
				if (config.contains("Features." + args[0])) {
					config.set("Features." + args[0], !config.getBoolean("Features." + args[0]));
					enabled = config.getBoolean("Features." + args[0]);
				} else if (config.contains("Features.commands." + args[0])) {
					config.set("Features.commands." + args[0], !config.getBoolean("Features.commands." + args[0]));
					enabled = config.getBoolean("Features.commands." + args[0]);
				} else if (args[0].equalsIgnoreCase("all")) {
					if (args.length == 2) {
						for (String res : config.getConfigurationSection("Features.commands").getKeys(false))
							config.set("Features.commands." + res, Boolean.valueOf(args[1]));
						for (String res : config.getConfigurationSection("Features").getKeys(false)) {
							if (!res.equals("commands")) {
								config.set("Features." + res, Boolean.valueOf(args[1]));
								enabled = Boolean.valueOf(args[1]);
							}
						}
					} else {
						sender.sendMessage(Main.color(prefix + "/toggle all [true/false]"));
						return;
					}
				} else {
					sender.sendMessage(Main.color(prefix + "Unknown feature."));
					return;
				}
				if (enabled) {
					sender.sendMessage(Main.color(prefix + "&r" + Main.camelCase(args[0]) + ": &aTrue"));
				} else {
					sender.sendMessage(Main.color(prefix + "&r" + Main.camelCase(args[0]) + ": &cFalse"));
				}
				Main.save.put("config", true);
			} else {
				sender.sendMessage(Main.color(prefix + "/toggle [Feature|all]"));
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
