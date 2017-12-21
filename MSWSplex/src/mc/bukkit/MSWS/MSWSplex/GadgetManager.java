package mc.bukkit.MSWS.MSWSplex;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class GadgetManager {

	@SuppressWarnings("deprecation")
	public static void useGadget(Player player, String gadget) {
		switch (gadget) {
		case "throwingtnt":
			if (Main.cosmAmo(player, gadget) > 0) {
				if (System.currentTimeMillis() - Main.gadget.getDouble(
						"Users." + player.getUniqueId() + ".timer." + gadget) > Main.gadget.getDouble(gadget) * 1000) {
					TNTPrimed tnt = (TNTPrimed) player.getWorld()
							.spawnEntity(player.getEyeLocation().subtract(0, .25, 0), EntityType.PRIMED_TNT);
					tnt.setFuseTicks(40);
					tnt.setVelocity(player.getLocation().getDirection().multiply(1.2));
					tnt.setCustomName("throwingtnt");
				} else {
					return;
				}
			} else {
				GUIManager.makePurchase(player, gadget, 10, 20, "shards", Material.TNT);
				return;
			}
			break;
		case "epearl":
			if (Main.cosmAmo(player, gadget) > 0) {
				if (System.currentTimeMillis() - Main.gadget.getDouble(
						"Users." + player.getUniqueId() + ".timer." + gadget) > Main.gadget.getDouble(gadget) * 1000) {
					EnderPearl pearl = (EnderPearl) player.launchProjectile(EnderPearl.class);
					pearl.setShooter(null);
					pearl.setVelocity(player.getLocation().getDirection().multiply(2.0));
					pearl.setCustomName(player.getName() + "pearl");
					pearl.setPassenger(player);
				} else {
					return;
				}
			} else {
				GUIManager.makePurchase(player, gadget, 50, 10, "shards", Material.ENDER_PEARL);
				return;
			}
			break;
		case "fireworkgadget":
			if (Main.cosmAmo(player, gadget) > 0) {
				if (System.currentTimeMillis() - Main.gadget.getDouble(
						"Users." + player.getUniqueId() + ".timer." + gadget) > Main.gadget.getDouble(gadget) * 1000) {
					Firework fire = (Firework) player.getWorld().spawnEntity(
							player.getTargetBlock((HashSet<Byte>) null, 5).getLocation().add(.5, 0, .5),
							EntityType.FIREWORK);
					FireworkMeta fwm = fire.getFireworkMeta();
					Random r = new Random();
					int rt = r.nextInt(5) + 1;
					Type type = Type.BALL;
					if (rt == 2)
						type = Type.BALL_LARGE;
					if (rt == 3)
						type = Type.BURST;
					if (rt == 4)
						type = Type.CREEPER;
					if (rt == 5)
						type = Type.STAR;
					int rc1 = (int) Math.floor(Math.random() * 255);
					int rg1 = (int) Math.floor(Math.random() * 255);
					int rb1 = (int) Math.floor(Math.random() * 255);
					int rc2 = (int) Math.floor(Math.random() * 255);
					int rg2 = (int) Math.floor(Math.random() * 255);
					int rb2 = (int) Math.floor(Math.random() * 255);
					FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean())
							.withColor(Color.fromRGB(rc1, rg1, rb1)).withFade(Color.fromRGB(rc2, rg2, rb2)).with(type)
							.trail(r.nextBoolean()).build();
					fwm.addEffect(effect);
					fwm.setPower(1);
					fire.setFireworkMeta(fwm);
				} else {
					return;
				}
			} else {
				GUIManager.makePurchase(player, gadget, 10, 20, "shards", Material.FIREWORK);
				return;
			}
			break;
		case "paintball":
			if (Main.cosmAmo(player, gadget) > 0) {
				if (System.currentTimeMillis() - Main.gadget.getDouble(
						"Users." + player.getUniqueId() + ".timer." + gadget) > Main.gadget.getDouble(gadget) * 1000) {
					player.getWorld().playSound(player.getLocation(), Sound.ITEM_PICKUP, 3, 3);
					EnderPearl pearl = (EnderPearl) player.launchProjectile(EnderPearl.class);
					pearl.setShooter(null);
					pearl.setVelocity(player.getLocation().getDirection().multiply(4.0));
					pearl.setCustomName("paintball");
				} else {
					return;
				}
			} else {
				GUIManager.makePurchase(player, gadget, 10, 50, "shards", Material.GOLD_BARDING);
				return;
			}
			break;
		}
		Main.addCosmetic(player, gadget, -1);
		ItemMeta tempMeta = player.getItemInHand().getItemMeta();
		tempMeta.setDisplayName(Main.color("&e" + Main.cosmAmo(player, gadget) + " " + gadgetName(gadget)));
		player.getItemInHand().setItemMeta(tempMeta);
		player.sendMessage(Main.color("&9Skill> &7You threw &a" + gadgetName(gadget) + "&7."));
		Main.gadget.set("Users." + player.getUniqueId() + ".timer." + gadget, System.currentTimeMillis());
	}

	public static void toggleGadget(Player player, String gadget) {
		String prefix = "&9Gadget> &7";
		Material mat = Material.AIR;
		if (Main.settings.containsKey(gadget)) {
			if (!Boolean.valueOf(Main.settings.get(gadget))) {
				player.sendMessage(Main.color(prefix + "&a" + gadgetName(gadget) + " &7is not enabled."));
				return;
			}
		}
		switch (gadget) {
		case "throwingtnt":
			mat = Material.TNT;
			break;
		case "epearl":
			mat = Material.ENDER_PEARL;
			break;
		case "fireworkgadget":
			mat = Material.FIREWORK;
			break;
		case "paintball":
			mat = Material.GOLD_BARDING;
			break;
		}
		if (activeGadget(player) == gadget) {
			Main.gadget.set("Users." + player.getUniqueId() + ".active.gadget", null);
			player.sendMessage(Main.color(prefix + "You unequipped &a" + gadgetName(gadget) + "&7."));
			mat = Material.AIR;
		} else {
			if (Main.gadget.contains("Users." + player.getUniqueId() + ".active.gadget")) {
				player.sendMessage(Main.color(prefix + "You unequipped &a"
						+ gadgetName(Main.gadget.getString("Users." + player.getUniqueId() + ".active.gadget"))
						+ "&7."));
			}
			Main.gadget.set("Users." + player.getUniqueId() + ".active.gadget", gadget);
			player.sendMessage(Main.color(prefix + "You equipped &a" + gadgetName(gadget) + "&7."));
		}
		ItemStack item = Main.newItem(mat, "&e" + Main.cosmAmo(player, gadget) + " " + gadgetName(gadget));
		player.getInventory().setItem(3, item);
	}

	public static void timer(Player player) {
		ConfigurationSection timer = Main.gadget.getConfigurationSection("Users." + player.getUniqueId() + ".timer");
		if (timer != null) {
			for (String res : timer.getKeys(false)) {
				String name = gadgetName(res);
				if (System.currentTimeMillis() - timer.getDouble(res) > Main.gadget.getDouble(res) * 1000) {
					Main.sendAction(player, Main.color("&a&l" + name + " Recharged"));
					player.playSound(player.getLocation(), Sound.WOOD_CLICK, 2, 2);
					Main.gadget.set("Users." + player.getUniqueId() + ".timer." + res, null);
				} else {
					String disp = "";
					double progress = (System.currentTimeMillis() - timer.getDouble(res))
							/ (Main.gadget.getDouble(res) * 1000);
					int len = 30;
					for (double i = 0; i < len; i++) {
						if (i / len > progress) {
							disp = disp + "&c▍";
						} else {
							disp = disp + "&a▍";
						}
					}
					Main.sendAction(player, Main.color(name + "&r " + disp + "&r " + (TimeManagement.getTime(
							Main.gadget.getDouble(res) * 1000 - (System.currentTimeMillis() - timer.getDouble(res))))));
				}
			}
		}
	}

	public static String activeGadget(Player player) {
		return (Main.gadget.getString("Users." + player.getUniqueId() + ".active.gadget"));
	}

	public static String gadgetName(String id) {
		switch (id.toLowerCase()) {
		case "throwingtnt":
			return "TNT";
		case "epearl":
			return "Ethereal Pearl";
		case "fireworkgadget":
			return "Firework";
		case "paintball":
			return "Paintball Gun";
		}
		return "";
	}
}
