package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "report":
			String prefix = "&9Report> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 4) {
				if (args.length < 2) {
					sender.sendMessage(Main.color(prefix + "/report [player] [reason]"));
					return;
				}
				if (sender instanceof Player) {
					Player player = (Player) sender;
					Player reportTarget = Main.getPlayer(args[0], sender);
					if (reportTarget == null) {
						return;
					}
					String msg = "";
					String type = "Unknown";
					for (String res : args) {
						if (res != args[0]) {
							msg = msg + res + " ";
							if (res.matches("(?i)(hacking|fly|speed|aimbot|killaura|flight|hacks|hacker|water)")) {
								type = "Hacking";
							} else if (res.matches("(?i)(spam|flood|chat|filter|bypass|rude)")) {
								type = "Chat";
							} else if (res.matches("(?i)(team|draw|build||skin|pet)")) {
								type = "Gameplay";
							}
						}
					}
					int reportAmo = 0;
					int totAmo = 0;
					for (String res : Main.reports.getKeys(false)) {
						if (Main.reports.getString(res + ".reporter").equals(player.getUniqueId() + "")
								&& !Main.reports.getBoolean(res + ".handled")) {
							reportAmo++;
						}
						totAmo++;
					}
					if (reportAmo >= 5) {
						sender.sendMessage(Main.color(prefix + "You have reached the maximum number of Main.reports."));
						return;
					}
					Main.reports.set(totAmo + ".reporter", player.getUniqueId() + "");
					Main.reports.set(totAmo + ".username", sender.getName());
					Main.reports.set(totAmo + ".reported", reportTarget.getName());
					Main.reports.set(totAmo + ".reason", msg);
					Main.reports.set(totAmo + ".type", type);
					Main.reports.set(totAmo + ".handled", false);
					sender.sendMessage(Main.color("&9Report #" + (totAmo) + "> &7Succesfully submitted report."));
					for (Player reportNotify : Bukkit.getOnlinePlayers()) {
						if (Main.staff.contains(Main.getRankID(reportNotify.getUniqueId())))
							reportNotify.sendMessage(Main.color(
									"&9Report> &e" + sender.getName() + " &7submitted a &e" + type + " &7report for &a"
											+ reportTarget.getName() + " &7(ID: &6" + totAmo + "&7)."));
					}
					Main.save.put("report", true);
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "reporthandle":
		case "rh":
			prefix = "&9Report> &7";
			if (Main.staff.contains(rank) && rank != "trainee") {
				if (args.length < 2) {
					sender.sendMessage(Main.color(prefix + "/reporthandle [report id] [result]"));
					return;
				}
				String msg = "";
				for (String res : args)
					if (res != args[0])
						msg = msg + res + " ";
				if (Main.reports.contains(args[0])) {
					if (Main.reports.getBoolean(args[0] + ".handled")) {
						sender.sendMessage(Main.color(prefix + "Report was already handled."));
						return;
					}
					Main.reports.set(args[0] + ".handled", true);
					Main.reports.set(args[0] + ".result", msg);
					Main.reports.set(args[0] + ".handler", sender.getName());
					sender.sendMessage(Main.color(prefix + "Report #" + args[0] + " succesfully handled."));
					Main.save.put("report", true);
					if (Bukkit.getOfflinePlayer(Main.reports.getString(args[0] + ".username")).isOnline()) {
						Player notifyR = Bukkit.getPlayer(Main.reports.getString(args[0] + ".username"));
						notifyR.sendMessage(
								Main.color("&9Report> &e" + sender.getName() + " &7closed your report against &e"
										+ Main.reports.getString(args[0] + ".reported") + "&7."));
						notifyR.sendMessage(Main.color(prefix + "Reason: &e" + msg));
						Main.reports.set(args[0] + ".notified", true);
					} else {
						Main.reports.set(args[0] + ".notified", false);
					}
				} else {
					sender.sendMessage(Main.color(prefix + "Report not found!"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "reportview":
		case "rv":
		case "vr":
			prefix = "&9Report> &7";
			if (Main.staff.contains(rank) && rank != "trainee") {
				if (args.length != 1) {
					sender.sendMessage(Main.color(prefix + "/viewreport [report id]"));
					return;
				}
				if (Main.reports.contains(args[0])) {
					sender.sendMessage(Main.color("&9Report #" + args[0] + " information:"));
					sender.sendMessage(Main.color("&eReporter: &7" + Main.reports.getString(args[0] + ".username")));
					sender.sendMessage(Main
							.color("&eReported: &7" + Main.reports.getString(args[0] + ".reported") + "&7 (Online: &a"
									+ Bukkit.getOfflinePlayer(Main.reports.getString(args[0] + ".reported")).isOnline()
									+ "&7)."));
					sender.sendMessage(Main.color("&eType: &7" + Main.reports.getString(args[0] + ".type")));
					sender.sendMessage(Main.color("&eReason: &7" + Main.reports.getString(args[0] + ".reason")));
					sender.sendMessage(Main.color("&eHandled: &7" + Main.reports.getBoolean(args[0] + ".handled")));
				} else {
					sender.sendMessage(Main.color(prefix + "Report not found."));
				}
			} else {
				Main.noPerm(sender);
			}

			break;
		case "listreports":
		case "lr":
			prefix = "&9Report> &7";
			if (Main.staff.contains(rank) && rank != "trainee") {
				int amo = 0;
				int max = 10;
				if (args.length == 1)
					if (Main.isNumber(args[0]))
						max = Integer.valueOf(args[0]);
				for (String res : Main.reports.getKeys(false)) {
					if (!Main.reports.getBoolean(res + ".handled")) {
						sender.sendMessage(Main.color("&9Report #" + res + "> &7Type: &e"
								+ Main.reports.getString(res + ".type") + "&7 Reported: &e"
								+ Main.reports.getString(res + ".reported") + "&7 (Online: &a"
								+ Bukkit.getOfflinePlayer(Main.reports.getString(res + ".reported")).isOnline()
								+ "&7)."));
						amo++;
						if (amo >= max)
							break;
					}
				}
				if (amo == 0) {
					sender.sendMessage(Main.color(prefix + "All reports have been handled!"));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		}
	}
}
