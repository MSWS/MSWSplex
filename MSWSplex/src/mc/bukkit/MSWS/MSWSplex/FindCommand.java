package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FindCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9Find> &7";
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		if (Main.staff.contains(rank)) {
			if (args.length != 1) {
				sender.sendMessage(Main.color("&9Find> &7/find <player>"));
				return;
			}
			Player ftarget = Main.getPlayer(args[0], sender);
			if (ftarget != null) {
				if (Main.data.getBoolean("Users." + ftarget.getUniqueId() + ".vanished")) {
					if (sender instanceof Player) {
						if (Main.ranks.getInt(Main.getRankID(ftarget.getUniqueId()) + ".rank") > Main.ranks
								.getInt(Main.getRankID(((Player) sender).getUniqueId()))) {
							Main.notFound(args[0], sender, false);
							return;
						}
					}
				}
				Location loc = ftarget.getLocation();
				if (sender instanceof Player) {
					if (((Player) sender).getWorld() == loc.getWorld()) {
						sender.sendMessage(
								Main.color(prefix + "&7[&e" + ftarget.getName() + "&7] is in the same server"));
						return;
					}
				}
				Main.jsonMSG(sender,
						"['',{'text':'Find>','color':'blue'},{'text':' '},{'text':'Located [','color':'gray'},{'text':'"
								+ ftarget.getName() + "','color':'yellow'},{'text':'] at ','color':'gray'},{'text':'"
								+ loc.getWorld().getName()
								+ "','color':'blue','clickEvent':{'action':'run_command','value':'/server "
								+ loc.getWorld().getName()
								+ "'},'hoverEvent':{'action':'show_text','value':'Teleport to " + ftarget.getName()
								+ "'}}]");
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
