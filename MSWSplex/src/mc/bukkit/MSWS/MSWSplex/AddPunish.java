package mc.bukkit.MSWS.MSWSplex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class AddPunish {
	@SuppressWarnings("deprecation")
	public static void addPunish(String punished, String punisher, String reason, String type, Integer severity) {
		OfflinePlayer target = Bukkit.getOfflinePlayer(punished);
		Player player = Bukkit.getPlayer(punisher);
		String uuid = target.getUniqueId() + "", prefix = "&9Punish> &7";
		ConfigurationSection user = Main.data.getConfigurationSection("Users." + uuid + ".history");
		if (user == null) {
			Main.data.createSection("Users." + uuid + ".history");
			user = Main.data.getConfigurationSection("Users." + uuid + ".history");
		}
		int pos = 0;
		if (Main.data.contains("Users." + uuid + ".history")) {
			while (Main.data.contains("Users." + uuid + ".history." + pos))
				pos++;
		}
		Integer pastClient1 = 0, pastClient2 = 0, pastClient3 = 0, pastChat1 = 0, pastChat2 = 0, pastChat3 = 0,
				pastChat4 = 0, pastGameplay1 = 0, pastGameplay4 = 0;
		if (Main.data.getConfigurationSection("Users." + uuid + ".history") != null) {
			ConfigurationSection history = Main.data.getConfigurationSection("Users." + uuid + ".history");
			List<String> his = new ArrayList<String>();
			for (String res : history.getKeys(false))
				his.add(res);
			for (int i = his.size() - 1; i >= 0; i--) {
				String res = his.get(i);
				if (history.contains(res + ".unbanner"))
					continue;
				String tempType = history.getString(res + ".type");
				Integer sev = history.getInt(res + ".severity");
				switch (tempType) {
				case "exploiting":
					switch (sev) {
					case 1:
						pastGameplay1++;
						break;
					case 4:
						pastGameplay4++;
						break;
					}
					break;
				case "hacking":
					switch (sev) {
					case 1:
						pastClient1++;
						break;
					case 2:
						pastClient2++;
						break;
					case 3:
						pastClient3++;
						break;
					}
					break;
				case "chatoffense":
					switch (sev) {
					case 1:
						pastChat1++;
						break;
					case 2:
						pastChat2++;
						break;
					case 3:
						pastChat3++;
						break;
					case 4:
						pastChat4++;
						break;
					}
					break;
				}
			}
		}
		user.set(pos + ".type", type);
		user.set(pos + ".severity", severity);
		user.set(pos + ".reason", reason);
		user.set(pos + ".user", punisher);
		user.set(pos + ".date", System.currentTimeMillis());
		int dur = 0;
		String typeName = type;
		switch (type) {
		case "warning":
			typeName = "warned";
			if (target.isOnline()) {
				((Player) target).sendMessage(Main.color(prefix + punisher + " issued a friendly warning to " + target.getName()+"."));
				((Player) target).sendMessage(Main.color(prefix + "&lReason: &7" + reason));
				((Player) target).playSound(((Player) target).getLocation(), Sound.CAT_MEOW, 1, (float) .25);
			}
			break;
		case "exploiting":
			typeName = "banned";
			if (severity == 1) {
				dur = (1 + pastGameplay1) * 4 * 60;
				if (target.isOnline()) {
					((Player) target).kickPlayer(
							Main.color("&c&lYou have been banned for " + TimeManagement.getTime(dur) + " by " + punisher
									+ "\n&r" + reason + "\n&2Unfairly banned? Appeal at &awww.mineplex.com/appeals"));
				}
			}
			break;
		case "hacking":
			type = "banned";
			if (severity == 1)
				dur = (1 + pastClient1) * 24 * 60;
			if (severity == 2) {
				if (pastClient2 > 0 || pastClient3 > 0)
					dur = -1;
				dur = (1 + pastClient2) * 24 * 30 * 60;
			}
			if (severity == 3) {
				if (pastClient2 > 0 || pastClient3 > 0)
					dur = -1;
				dur = (1 + pastClient3) * 24 * 30 * 60;
			}
			if (target.isOnline())
				((Player) target).kickPlayer(
						Main.color("&c&lYou have been banned for " + TimeManagement.getTime(dur) + " by " + punisher
								+ "\n&r" + reason + "\n&2Unfairly banned? Appeal at &awww.mineplex.com/appeals"));
			break;
		case "chatoffense":
			typeName = "muted";
			if (severity == 1)
				dur = (1 + pastChat1) * 2 * 60;
			if (severity == 2)
				dur = (1 + pastChat2) * 1 * 24 * 60;
			if (severity == 3)
				dur = (1 + pastChat3) * 30 * 24 * 60;
			if (severity == 4)
				dur = -1;
			if (dur == -1) {
				if (target.isOnline()) {
					((Player) target).playSound(((Player) target).getLocation(), Sound.CAT_MEOW, 1, (float) .25);
					((Player) target).sendMessage(
							Main.color(prefix + punisher + " muted " + target.getName() + " for Permanent."));
					((Player) target).sendMessage(Main.color(prefix + "&lReason: &7" + reason));
				}
			} else {
				if (target.isOnline()) {
					((Player) target).sendMessage(Main.color(prefix + punisher + " muted " + target.getName() + " for "
							+ TimeManagement.getTime(dur) + "."));
					((Player) target).sendMessage(Main.color(prefix + "&lReason: &7" + reason));
					((Player) target).playSound(((Player) target).getLocation(), Sound.CAT_MEOW, 1, (float) .25);
				}
			}
			break;
		case "ipban":
			typeName = "ip banned";
			if (target.isOnline()) {
				((Player) target).kickPlayer(Main.color("&c&lYou have been IP Banned for Permanent by" + punisher
						+ "\n&r" + reason + "\n&2Unfairly banned? Appeal at &awww.mineplex.com/appeals"));
			}
			dur = -1;
			break;
		case "other":
			typeName = "banned";
			dur = -1;
			if (target.isOnline()) {
				((Player) target).kickPlayer(Main.color("&c&lYou have been banned for Permanent by " + punisher + "\n&r"
						+ reason + "\n&2Unfairly banned? Appeal at &awww.mineplex.com/appeals"));
			}
			break;
		}
		String time = TimeManagement.getTime(dur);
		if (dur == -1)
			time = "Permanent";
		if (player != null && punisher != "GWEN") {
			if (typeName.equals("warned")) {
				Main.tellStaff(prefix + player.getName() + " issued a friendly warning to " + target.getName() + ".",
						player.getWorld().getName());
			} else {
				Main.tellStaff(
						prefix + player.getName() + " " + typeName + " " + target.getName() + " for " + time + ".",
						player.getWorld().getName());
			}
		} else if (punisher != "GWEN") {
			Main.tellStaff(prefix + punisher + " " + typeName + " " + target.getName() + " for " + time + ".");
		}
		user.set(pos + ".duration", dur);
		Main.save.put("data", true);
	}
}
