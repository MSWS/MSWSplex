package mc.bukkit.MSWS.MSWSplex;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatControl {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String rank = "owner", prefix;
		Player player;
		if (sender instanceof Player)
			rank = Main.getRankID(((Player) sender).getUniqueId());
		switch (command.toLowerCase()) {
		case "silence":
			if (sender instanceof Player) {
				player = (Player) sender;
				prefix = "&9Chat> &7";
				if (Main.ranks.getInt(rank + ".rank") >= 16) {
					if (Main.chatSlow.containsKey(player.getWorld().getName())
							&& Main.chatSlow.get(player.getWorld().getName()) == -1) {
						for (Player bctarget : Bukkit.getOnlinePlayers()) {
							bctarget.sendMessage(Main.color(prefix + "Chat is no longer silenced."));
							Main.timer.put(bctarget, 0.0);
							Main.chatSlow.put(player.getWorld().getName(), (double) 0);
						}
					} else {
						for (Player bctarget : Bukkit.getOnlinePlayers()) {
							bctarget.sendMessage(Main.color(prefix + "Chat has been silenced."));
							Main.timer.put(bctarget, -1.0);
							Main.chatSlow.put(player.getWorld().getName(), (double) -1);
						}
					}
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "chatslow":
			prefix = "&9Chat> &7";
			if (Main.ranks.getInt(rank + ".rank") >= 13) {
				if (sender instanceof Player) {
					player = (Player) sender;
					if (args.length == 1) {
						if (args[0].matches("(0|off|disable|none)")) {
							for (Player bctarget : Bukkit.getOnlinePlayers()) {
								bctarget.sendMessage(Main.color(prefix + "Chat Slow has been disabled."));
								Main.timer.put(bctarget, 0.0);
								Main.chatSlow.put(player.getWorld().getName(), 0.0);
								return;
							}
						}
						if (Main.isNumber(args[0])) {
							/**
							 * for (Player bctarget : Bukkit.getOnlinePlayers()) bctarget.sendMessage(
							 * Main.color(prefix + "Chat Slow &7has been enabled for &a" + args[0] + "
							 * seconds&7."));
							 */
							Main.tellPlayers(prefix + "Chat Slow has been enabled for &a" + args[0] + " seconds&7.",
									"global");
							Main.chatSlow.put(player.getWorld().getName(), Double.valueOf(args[0]));
						} else {
							sender.sendMessage(Main.color(prefix + "Invalid chatslow amount."));
						}
					} else {
						sender.sendMessage(Main.color(prefix + "/chatslow [seconds]"));
					}
				} else {
					sender.sendMessage(Main.color(prefix + "You must be a player."));
				}
			} else {
				Main.noPerm(sender);
			}
			break;
		case "ignore":
			prefix = "&9Ignore> &7";
			if (sender instanceof Player) {
				String uuid = ((Player) sender).getUniqueId() + "";
				if (args.length == 1) {
					if (!Main.offExists(args[0])) {
						Main.notFound(args[0], sender, true);
						return;
					}
					List<String> ignoring = new ArrayList<String>();
					if (Main.data.contains("Users." + uuid + ".ignoring")) {
						ignoring = Main.data.getStringList("Users." + uuid + ".ignoring");
					}
					if (ignoring.contains(Bukkit.getOfflinePlayer(args[0]).getUniqueId() + "")) {
						sender.sendMessage(Main.color(
								prefix + "You are already ignoring &a" + Bukkit.getOfflinePlayer(args[0]).getName()));
						return;
					}
					ignoring.add(Bukkit.getOfflinePlayer(args[0]).getUniqueId() + "");
					Main.data.set("Users." + uuid + ".ignoring", ignoring);
					sender.sendMessage(
							Main.color(prefix + "Now ignoring &a" + Bukkit.getOfflinePlayer(args[0]).getName()));
				} else {
					List<String> ignores = Main.data.getStringList("Users." + uuid + ".ignoring");
					if (ignores != null && ignores.size() > 0) {
						sender.sendMessage(
								Main.color("&b&m=====================[&r&lIgnoring&b&m]======================"));
						for (String res : ignores) {
							String name = Bukkit.getOfflinePlayer(UUID.fromString(res)).getName();
							Main.jsonMSG(sender,
									"['',{'text':'Unignore','bold':true,'color':'red','clickEvent':{'action':'run_command','value':'/unignore "
											+ name + "'}},{'text':' - '},{'text':'" + name + "','color':'gray'}]");
						}
						sender.sendMessage(Main.color("&b&m====================================================="));
					} else {
						sender.sendMessage(
								Main.color("&b&m=====================[&r&lIgnoring&b&m]======================"));
						sender.sendMessage("");
						sender.sendMessage("Welcome to your Ignore List!");
						sender.sendMessage("");
						sender.sendMessage(Main.color("To ignore people, type &a/ignore <Player Name>"));
						sender.sendMessage("");
						sender.sendMessage(Main.color("Type &a/ignore &fat any time to view the ignored!"));
						sender.sendMessage("");
						sender.sendMessage(Main.color("&b&m====================================================="));
					}
				}
			} else {
				sender.sendMessage(Main.color(prefix + "It's not like you see chat messages..."));
			}
			break;
		case "unignore":
			prefix = "&9Ignore> &7";
			if (sender instanceof Player) {
				String uuid = ((Player) sender).getUniqueId() + "";
				if (args.length == 1) {
					if (Main.offExists(args[0])) {
						Main.data.set("Users." + uuid + ".ignoring." + Bukkit.getOfflinePlayer(args[0]).getUniqueId(),
								null);
						sender.sendMessage(Main.color(
								prefix + "No longer ignoring &a" + Bukkit.getOfflinePlayer(args[0]).getName() + "&7!"));
					} else {
						Main.notFound(args[0], sender, true);
					}
				} else {
					sender.sendMessage(Main.color(prefix + "/ignore [player]"));
				}
			} else {
				sender.sendMessage(Main.color(prefix + "It's not like you see chat messages..."));
			}
			break;
		}
	}
}
