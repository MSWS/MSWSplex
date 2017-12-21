package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;

public class PartyCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9Party> &7";
		if (args.length == 0) {
			if (Main.party.contains(sender.getName())) {
				sender.sendMessage(Main.color(prefix + "Party Members:"));
				for (String res : Main.party.getConfigurationSection(sender.getName()).getKeys(false)) {
					sender.sendMessage(res);
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You are not in a party!"));
			}
		} else {
			if (args[0].equalsIgnoreCase("leave")) {
				Main.party.set(sender.getName(), null);
				sender.sendMessage(Main.color(prefix + "You have left your party."));
				return;
			}
			if (Main.getPlayer(args[0], sender) == null) {
				return;
			}
			Main.party.set(sender.getName() + "." + args[0], "");
		}
		return;
	}
}
