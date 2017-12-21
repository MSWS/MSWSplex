package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnCommand {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		String prefix = "&9Spawn> &7";
		if (Main.ranks.getInt(rank + ".rank") >= 16) {
			if (sender instanceof Player) {
				if (!((Player) sender).getWorld().getName().contains("Backup-")) {
					int amo = 1;
					if (args.length == 0) {
						sender.sendMessage(Main.color(prefix + "/summon [entity] <amount>"));
						return;
					}
					if (args.length == 2) {
						if (Main.isNumber(args[1]))
							amo = Integer.valueOf(args[1]);
					}
					try {
						EntityType.valueOf(args[0].toUpperCase());
					} catch (IllegalArgumentException e) {
						sender.sendMessage(Main.color(prefix + "Unknown entity."));
						return;
					}
					/**
					 * if(EntityType.valueOf(args[0].toUpperCase())==null){
					 * sender.sendMessage(Main.color(prefix+"Unknown Entity.")); return; }
					 */
					if (amo >= 1000) {
						Main.addPunish(sender.getName(), "Server", "Compromised Account - [Summoned too many entities]",
								"other", 4);
						return;
					}
					for (int i = 0; i < amo; i++) {
						((Player) sender).getWorld().spawnCreature(((Player) sender).getLocation(),
								EntityType.valueOf(args[0].toUpperCase()));
					}
					sender.sendMessage(Main.color(prefix + "Succesfully summoned &e" + amo + " "
							+ Main.camelCase(args[0]).replace("_", " ") + "&7."));
				} else {
					Main.noPerm(sender);
				}
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
