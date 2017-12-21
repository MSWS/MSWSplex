package mc.bukkit.MSWS.MSWSplex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardCommand {
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String prefix = "&9Scoreboard> &7", msg;
		String rank = "owner";
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		if (Main.ranks.getInt(rank + ".rank") >= 16) {
			if (args.length == 0) {
				sender.sendMessage(Main.color(prefix + "Available Commands"));
				sender.sendMessage(Main.color("&4/scoreboard create [title]"));
				sender.sendMessage(Main.color("&4/scoreboard setline [line] [text]"));
				sender.sendMessage(Main.color("&4/scoreboard rename [title]"));
				sender.sendMessage(Main.color("&4/scoreboard save [name]"));
				sender.sendMessage(Main.color("&4/scoreboard load [name]"));
				sender.sendMessage(Main.color("&4/scoreboard clear"));
				sender.sendMessage(Main.color("&4/scoreboard toggle"));
				return;
			}
			switch (args[0].toLowerCase()) {
			case "create":
				msg = "";
				if (args.length < 2) {
					sender.sendMessage(Main.color("&4/scoreboard create [title] <l:number of lines>"));
					return;
				}
				int amo = 15;
				for (String res : args) {
					if (res.startsWith("l:")) {
						amo = Integer.valueOf(res.substring(2));
					} else {
						if (res != args[0])
							msg = msg + res + " ";
					}
				}
				Main.makeScoreboard(msg, sender, amo);
				break;
			case "setline":
				msg = "";
				if (Main.boardLines.get(1) == null)
					Main.makeScoreboard("Untitled", sender);
				if (args.length < 3) {
					sender.sendMessage(Main.color("&4/scoreboard setline [line] [text]"));
					return;
				}
				for (String res : args) {
					if (res != args[0] && res != args[1])
						msg = msg + res + " ";
				}
				if (msg.length() > 40) {
					sender.sendMessage(Main.color(prefix + "Text is too long. (&e" + msg.length() + "/40&7)"));
					return;
				}
				int line;
				try {
					line = Integer.valueOf(args[1]);
				} catch (IllegalArgumentException e) {
					sender.sendMessage(Main.color(prefix + "Invalid line number."));
					return;
				}
				if (!Main.boardLines.containsKey(line)) {
					sender.sendMessage(Main.color(prefix + "Invalid line number."));
					return;
				}
				for (int i = 1; i <= 15; i++) {
					if (Main.boardLines.get(i) != null) {
						if (Main.boardLines.get(i).replace("&r", "").equals(msg.replace("&r", ""))) {
							msg = msg + "&r";
						}
					}
				}
				if (Main.boardLines.get(line) != null) {
					Main.board.resetScores(Main.color(Main.boardLines.get(line)));
				}

				Main.obj.getScore(Main.color(msg)).setScore(line);
				Main.boardLines.put(line, msg);
				sender.sendMessage(Main.color(prefix + "Line &e" + line + " &7succesfully set to " + msg + "&7."));
				break;
			case "clear":
				Main.makeScoreboard(Main.obj.getDisplayName(), sender);
				break;
			case "toggle":
				Main.enabled = !Main.enabled;
				if (Main.enabled) {
					for (Player starget : Bukkit.getOnlinePlayers()) {
						Main.oldBoard.put(starget, starget.getScoreboard());
						starget.setScoreboard(Main.board);
					}
					sender.sendMessage(Main.color(prefix + "Scoreboard: &aEnabled&7."));
				} else {
					for (Player starget : Bukkit.getOnlinePlayers()) {
						if (Main.oldBoard.containsKey(starget))
							starget.setScoreboard(Main.oldBoard.get(starget));
					}
					sender.sendMessage(Main.color(prefix + "Scoreboard: &cDisabled&7."));
				}
				break;
			case "rename":
				msg = "";
				for (String res : args)
					if (res != args[0])
						msg = msg + res + " ";
				if (msg.length() > 32) {
					sender.sendMessage(Main.color(prefix + "Title is too long. (&e" + msg.length() + "/32&7)"));
					return;
				}
				if (Main.boardLines.get(1) == null) {
					Main.makeScoreboard(Main.color(msg), sender);
				} else {
					Main.obj.setDisplayName(Main.color(msg));
				}
				sender.sendMessage(Main.color(prefix + "Succesfully renamed scoreboard to &e" + msg + "&7."));
				break;
			case "save":
				if (args.length != 2) {
					sender.sendMessage(Main.color(prefix + "/scoreboard save [name]"));
					return;
				}
				List<String> tempList = new ArrayList<String>();
				for (int i = 15; i >= 1; i--) {
					tempList.add(Main.boardLines.get(i));
				}
				Main.data.set("Scoreboards." + args[1] + ".lines", tempList);
				Main.data.set("Scoreboards." + args[1] + ".name", Main.obj.getDisplayName());
				sender.sendMessage(Main.color(prefix + "Current scoreboard succesfully saved."));
				break;
			case "load":
				if (Main.boardLines.get(1) == null)
					Main.makeScoreboard("Untitled", sender);
				if (args.length != 2) {
					sender.sendMessage(Main.color(prefix + "/scoreboard load [name]"));
					return;
				}
				if (Main.data.contains("Scoreboards." + args[1])) {
					List<String> list = Main.data.getStringList("Scoreboards." + args[1] + ".lines");
					for (int i = 1; i <= 15; i++) {
						if (Main.boardLines.get(i) != null) {
							Main.board.resetScores(Main.color(Main.boardLines.get(i)));
						}
					}
					for (int i = list.size(); i >= 1; i--) {
						Main.obj.getScore(Main.color(list.get(i - 1) + "")).setScore(list.size() - i + 1);
						Main.boardLines.put(list.size() - i + 1, list.get(i - 1) + "");
					}
					Main.obj.setDisplayName(Main.color(Main.data.getString("Scoreboards." + args[1] + ".name")));
					sender.sendMessage(Main.color(prefix + "Scoreboard succesfully loaded."));
				} else {
					sender.sendMessage(Main.color(prefix + "Unknown scoreboard."));
				}
				break;
			default:
				sender.sendMessage(Main.color(prefix + "Available Commands"));
				sender.sendMessage(Main.color("&4/scoreboard create [title]"));
				sender.sendMessage(Main.color("&4/scoreboard setline [line] [text]"));
				sender.sendMessage(Main.color("&4/scoreboard rename [title]"));
				sender.sendMessage(Main.color("&4/scoreboard clear"));
				sender.sendMessage(Main.color("&4/scoreboard toggle"));
				break;
			}
		} else {
			Main.noPerm(sender);
		}
	}
}
