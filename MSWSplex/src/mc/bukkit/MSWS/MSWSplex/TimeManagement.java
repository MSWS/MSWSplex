package mc.bukkit.MSWS.MSWSplex;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeManagement {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "time":
			String prefix = "&9Time> &7";
			if (Main.staff.contains(rank)) {
				if (args.length != 1) {
					sender.sendMessage(Main.color(prefix + "time [player]"));
					return;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if (!Main.offExists(target)) {
					Main.notFound(args[0], sender, true);
					return;
				}
				sender.sendMessage(Main
						.color(prefix + "&e" + target.getName() + "&7 has &e" + getTime(target) + " &7in-game time."));
			} else {
				Main.noPerm(sender);
			}
			break;
		case "settime":
			prefix = "&9Time> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 16) {
				if (args.length == 2) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
					if (!Main.offExists(target)) {
						Main.notFound(args[0], sender, true);
						return;
					}
					Main.data.set("Users." + target.getUniqueId() + ".time", Integer.valueOf(args[1]));
					Main.save.put("data", true);
					sender.sendMessage(Main.color(prefix + "Succesfully set &e" + target.getName()
							+ "'s &7in-game time to &e" + getTime(target)));
				} else {
					sender.sendMessage(Main.color(prefix + "/settime [player] [minutes]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "seen":
			prefix = "&9Seen> &7";
			if (Main.staff.contains(rank)) {
				if (args.length == 1) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
					if (Main.offExists(target)) {
						sender.sendMessage(Main.color(
								prefix + "[&e" + target.getName() + "&7] was on &e" + lastOn(target) + " &7ago."));
					} else {
						sender.sendMessage(
								Main.color(prefix + "[&e" + target.getName() + "&7] has never joined the server."));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/seen [player]"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}

	public static String dateFromMillis(String milliSeconds) {
		String dateFormat = "MM-dd-yyyy hh:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(milliSeconds));
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String getTime(OfflinePlayer player) {
		String uuid = player.getUniqueId() + "";
		double time;
		double dec;
		if (Main.data.contains("Users." + uuid + ".time")) {
			time = Main.data.getInt("Users." + uuid + ".time");
		} else {
			time = 0;
		}
		double res = time;
		dec = 0;
		String type = "Minutes";
		if (res >= 60) {
			res = TimeUnit.HOURS.convert((long) time, TimeUnit.MINUTES);
			type = "Hours";
			dec = (time % 60) / 60;
			if (res >= 24) {
				res = TimeUnit.DAYS.convert((long) time, TimeUnit.MINUTES);
				type = "Days";
				dec = (time % 1440) / 1440;
			}
		}
		return (Math.round(res) + ((dec + "").substring(1, 3))) + " " + type;
	}

	public static String lastOn(OfflinePlayer target) {
		return getTime((long) (System.currentTimeMillis() - target.getLastPlayed()) / 1000 / 60);
	}

	public static String getTime(Integer minutes) {
		double time = minutes;
		double dec;
		double res = time;
		dec = 0;
		String type = "Minutes";
		if (res >= 60) {
			res = TimeUnit.HOURS.convert((long) time, TimeUnit.MINUTES);
			type = "Hours";
			dec = (time % 60) / 60;
			if (res >= 24) {
				res = TimeUnit.DAYS.convert((long) time, TimeUnit.MINUTES);
				type = "Days";
				dec = (time % 1440) / 1440;
			}
		}
		return (Math.round(res) + ((dec + "").substring(1, 3))) + " " + type;
	}

	public static String getTime(Double mils) {
		double time = mils;
		double dec;
		double res = time;
		dec = 0;
		res = TimeUnit.SECONDS.convert((long) time, TimeUnit.MILLISECONDS);
		String type = "Seconds";
		dec = (time % 1000) / 1000;
		if (res >= 60000) {
			res = TimeUnit.MINUTES.convert((long) time, TimeUnit.MILLISECONDS);
			type = "Minutes";
			dec = (time % 60000) / 60000;
			if (res >= 60000 / 60) {
				res = TimeUnit.HOURS.convert((long) time, TimeUnit.MILLISECONDS);
				type = "Hours";
				dec = (time % 60000 / 60) / 60000 / 60;
			}
		}
		return (Math.round(res) + ((dec + "").substring(1, 3))) + " " + type;
	}

	public static String getTime(Long minutes) {
		double time = minutes;
		double dec;
		double res = time;
		dec = 0;
		String type = "Minutes";
		if (res >= 60) {
			res = TimeUnit.HOURS.convert((long) time, TimeUnit.MINUTES);
			type = "Hours";
			dec = (time % 60) / 60;
			if (res >= 24) {
				res = TimeUnit.DAYS.convert((long) time, TimeUnit.MINUTES);
				type = "Days";
				dec = (time % 1440) / 1440;
			}
		}
		return (Math.round(res) + ((dec + "").substring(1, 3))) + " " + type;
	}
}
