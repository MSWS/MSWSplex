package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class TeamCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9Team> &7";
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getWorld().getName().contains("Lobby-")) {
				if (args.length == 1) {
					Color col;
					switch (args[0].toLowerCase()) {
					case "red":
						col = Color.RED;
						break;
					case "green":
						col = Color.LIME;
						break;
					case "blue":
						col = Color.fromRGB(50, 50, 255);
						break;
					case "orange":
						col = Color.ORANGE;
						break;
					default:
						sender.sendMessage(Main.color(prefix + "/team [red|green|blue|orange]"));
						return;
					}
					ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
					ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
					ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
					ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
					LeatherArmorMeta helmMeta = (LeatherArmorMeta) helm.getItemMeta();
					LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
					LeatherArmorMeta legMeta = (LeatherArmorMeta) leg.getItemMeta();
					LeatherArmorMeta bootMeta = (LeatherArmorMeta) boot.getItemMeta();
					helmMeta.setColor(col);
					chestMeta.setColor(col);
					legMeta.setColor(col);
					bootMeta.setColor(col);
					helm.setItemMeta(helmMeta);
					chest.setItemMeta(chestMeta);
					leg.setItemMeta(legMeta);
					boot.setItemMeta(bootMeta);
					player.getInventory().setItem(39, helm);
					player.getInventory().setItem(38, chest);
					player.getInventory().setItem(37, leg);
					player.getInventory().setItem(36, boot);
					sender.sendMessage(Main.color(prefix + "You joined team " + Main.camelCase(args[0]) + "!"));
					Main.data.set("Users." + player.getUniqueId() + ".team", args[0].toLowerCase());
				} else if (Main.data.contains("Users." + player.getUniqueId() + ".team")) {
					sender.sendMessage(Main.color(prefix + "You left "
							+ Main.camelCase(Main.data.getString("Users." + player.getUniqueId() + ".team"))));
				} else {
					sender.sendMessage(Main.color(prefix + "/team [red|green|blue|orange]"));
				}
			}
		} else {
			sender.sendMessage(Main.color(prefix + "No one wants a computer on their team."));
		}
	}
}
