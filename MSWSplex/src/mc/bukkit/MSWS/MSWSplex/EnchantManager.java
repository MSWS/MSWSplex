package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantManager {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		ConfigurationSection ranks = Main.ranks;
		String prefix = "&9Enchant> &7";
		if (ranks.getInt(rank + ".rank") >= 16) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					sender.sendMessage(Main.color(prefix + "/enchant [enchantment] <amplifier>"));
					return;
				}
				Player player = (Player) sender;
				ItemStack item = player.getItemInHand();
				/**
				 * try{ Enchantment.getByName(args[0].toUpperCase()); }catch
				 * (IllegalArgumentException e){ sender.sendMessage(Main.color(prefix+"Unknown
				 * enchantment.")); return true; }
				 */
				Enchantment ench = Enchantment.getByName(toEnchant(args[0]));
				if (ench == null) {
					sender.sendMessage(Main.color(prefix + "Unknown enchantment."));
					return;
				}
				if (args.length == 2) {
					try {
						Integer.valueOf(args[1]);
					} catch (IllegalArgumentException e) {
						sender.sendMessage(Main.color(prefix + "Invalid amplifier."));
						return;
					}
					item.addUnsafeEnchantment(ench, Integer.valueOf(args[1]));
				} else {
					item.addUnsafeEnchantment(ench, 1);
				}
				sender.sendMessage(Main.color(prefix + "Succesfully added &e" + Main.camelCase(args[0]) + "&7."));
			} else {
				sender.sendMessage(Main.color(prefix + "You must be a player."));
			}
		} else {
			Main.noPerm(sender);
		}
		return;
	}

	public static String toEnchant(String enchant) {
		switch (enchant.toLowerCase().replace("_", "")) {
		case "power":
			return "ARROW_DAMAGE";
		case "flame":
			return "ARROW_FIRE";
		case "infinity":
		case "infinite":
			return "ARROW_INFINITE";
		case "punch":
			return "ARROW_KNOCKBACK";
		case "sharpness":
			return "DAMAGE_ALL";
		case "arthropods":
		case "baneofarthropods":
			return "DAMAGE_ARTHORPODS";
		case "smite":
			return "DAMAGE_UNDEAD";
		case "depthstrider":
			return "DEPTH_STRIDER";
		case "efficiency":
			return "DIG_SPEED";
		case "unbreaking":
			return "DURABILITY";
		case "fireaspect":
			return "FIRE_ASPECT";
		case "knockback":
		case "kb":
			return "KNOCKBACK";
		case "fortune":
			return "LOOT_BONUS_BLOCKS";
		case "looting":
			return "LOOT_BONUS_MOBS";
		case "luck":
			return "LUCK";
		case "lure":
			return "LURE";
		case "waterbreathing":
		case "respiration":
			return "OXYGEN";
		case "prot":
		case "protection":
			return "PROTECTION_ENVIRONMENTAL";
		case "blastprot":
		case "blastprotection":
			return "PROTECTION_EXPLOSIONS";
		case "feather":
		case "featherfalling":
			return "PROTECTION_FALL";
		case "fireprot":
		case "fireprotection":
			return "PROTECTION_FIRE";
		case "projectileprot":
		case "projectileprotection":
		case "projprot":
			return "PROTECTION_PROJECTILE";
		case "silktouch":
		case "silk":
			return "SILK_TOUCH";
		case "thorns":
			return "THORNS";
		case "aquaaffinity":
		case "waterworker":
			return "WATER_WORKER";
		}
		return enchant.toUpperCase();
	}
}
