package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvSeeCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		String prefix = "&9InvSee> &7";
		if (Main.ranks.getInt(rank + ".rank") >= 16 && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				Player target = Main.getPlayer(args[0], sender);
				if (target != null) {
					Inventory tInv = target.getInventory();
					player.openInventory(tInv);
					/**
					 * Inventory inv = Bukkit.createInventory(null, 45, target.getName()+"'s
					 * Inventory"); for(int i=0;i<tInv.getSize();i++) { inv.setItem(i,
					 * tInv.getItem(i)); } player.openInventory(inv);
					 */
				}
			} else {
				sender.sendMessage(Main.color(prefix + "/invsee [player]"));
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
