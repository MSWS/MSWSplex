package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class FriendCommand {
	@SuppressWarnings("deprecation")
	public static void command(CommandSender sender, String command, String label, String[] args) {
		String uuid, prefix;
		Player player;
		OfflinePlayer target;
		switch (command.toLowerCase()) {
		case "friend":
		case "f":
			prefix = "&9Friends> &7";
			if (sender instanceof Player) {
				player = (Player) sender;
				uuid = player.getUniqueId() + "";
				if (args.length == 0) {
					sender.sendMessage(Main.color("&b&m======================[&r&lFriends&b&m]======================"));
					if (Main.friend.contains(uuid)) {
						ConfigurationSection userF = Main.friend.getConfigurationSection(uuid + ".friends");
						ConfigurationSection userR = Main.friend.getConfigurationSection(uuid + ".requests");
						if (userF != null && (userF.getKeys(false).size() > 0 || userR.getKeys(false).size() > 0)) {
							for (String res : userF.getKeys(false)) {
								OfflinePlayer frnd = Bukkit.getOfflinePlayer(userF.getString(res + ".username"));
								ConfigurationSection targetF = Main.friend
										.getConfigurationSection(frnd.getUniqueId() + ".friends");
								if (targetF == null
										|| (targetF != null && targetF.contains(player.getUniqueId() + ""))) {
									if (frnd.isOnline()) {
										Main.jsonMSG(player,
												"['',{'text':'Teleport','bold':true,'color':'green','clickEvent':{'action':'run_command','value':'/server "
														+ ((Player) frnd).getWorld().getName()
														+ "'}},{'text':' ','bold':true,'color':'green'},{'text':'- '},{'text':'Delete','bold':true,'color':'red','clickEvent':{'action':'run_command','value':'/unfriend "
														+ frnd.getName()
														+ "'}},{'text':' ','bold':true,'color':'red'},{'text':'- '},{'text':'"
														+ frnd.getName() + " " + ((Player) frnd).getWorld().getName()
														+ "','color':'gray'}]");
									} else {
										Main.jsonMSG(player,
												"['',{'text':'Delete','bold':true,'color':'red','clickEvent':{'action':'run_command','value':'/unfriend "
														+ frnd.getName() + "'}},{'text':' - '},{'text':'"
														+ frnd.getName()
														+ "','color':'gray'},{'text':' - '},{'text':'Offline for "
														+ TimeManagement.lastOn(frnd) + "','color':'gray'}]");
									}
								} else {
									Main.jsonMSG(player,
											"['',{'text':'Cancel','bold':true,'color':'red','clickEvent':{'action':'run_command','value':'/unfriend "
													+ frnd.getName() + "'}},{'text':' - '},{'text':'" + frnd.getName()
													+ " Request pending','color':'gray'}]");

								}
							}
							if (userR != null) {
								for (String res : userR.getKeys(false)) {
									String name = userR.getString(res + ".username");
									Main.jsonMSG(player,
											"['',{'text':'Accept','bold':true,'color':'green','clickEvent':{'action':'run_command','value':'/friend "
													+ name
													+ "'}},{'text':' - '},{'text':'Deny','bold':true,'color':'red','clickEvent':{'action':'run_command','value':'/unfriend "
													+ name + "'}},{'text':' '},{'text':'" + name
													+ " Friend request','color':'gray'}]");
								}
							}
						} else {
							sender.sendMessage("");
							sender.sendMessage("Welcome to your friends list!");
							sender.sendMessage("");
							sender.sendMessage("To add friends, type /friend <Player Name>");
							sender.sendMessage("");
							sender.sendMessage("Type /friend at any time to interact with your friends!");
							sender.sendMessage("");
						}
					} else {
						sender.sendMessage("");
						sender.sendMessage("Welcome to your friends list!");
						sender.sendMessage("");
						sender.sendMessage("To add friends, type /friend <Player Name>");
						sender.sendMessage("");
						sender.sendMessage("Type /friend at any time to interact with your friends!");
						sender.sendMessage("");

					}
					sender.sendMessage(Main.color("&b&m======================&3Toggle GUI&b&m======================"));
				} else if (args.length == 1) {
					target = Bukkit.getOfflinePlayer(args[0]);
					if (target == sender) {
						sender.sendMessage(Main.color(prefix + "You cannot friend yourself."));
						return;
					}
					if (!Main.offExists(target)) {
						Main.notFound(args[0], sender, true);
					} else {
						Main.friend.set(uuid + ".friends." + target.getUniqueId() + ".username", target.getName());
						Main.friend.set(uuid + ".requests." + target.getUniqueId(), null);
						if (Main.friend.contains(uuid + ".requests." + target.getUniqueId())
								|| Main.friend.contains(target.getUniqueId() + ".friends." + player.getUniqueId())) {
							Main.friend.set(target.getUniqueId() + ".requests." + player.getUniqueId(), null);
						} else {
							Main.friend.set(target.getUniqueId() + ".requests." + uuid + ".username", sender.getName());
						}

						sender.sendMessage(
								Main.color(prefix + "Added &e" + target.getName() + "&7 to you friends list."));

						Main.save.put("friend", true);
					}

				} else {
					sender.sendMessage(Main.color(prefix + "/f [username]"));
				}

			} else {
				sender.sendMessage(Main.color(prefix + "Sorry, machines aren't allowed to have friends."));
			}
			break;
		case "unfriend":
			prefix = "&9Friends> &7";
			if (sender instanceof Player) {
				player = (Player) sender;
				uuid = player.getUniqueId() + "";
				if (args.length != 1) {
					sender.sendMessage(Main.color(prefix + "/unfriend [Player]"));
					return;
				}
				target = Bukkit.getOfflinePlayer(args[0]);
				if (!Main.offExists(target)) {
					Main.notFound(args[0], sender, true);
					return;
				}
				Main.friend.set(uuid + ".friends." + target.getUniqueId(), null);
				Main.friend.set(target.getUniqueId() + ".friends." + uuid, null);
				Main.friend.set(uuid + ".requests." + target.getUniqueId(), null);
				Main.friend.set(target.getUniqueId() + ".requests." + uuid, null);
				sender.sendMessage(Main.color(prefix + "Removed &e" + target.getName() + " &7from your friends list."));
				Main.save.put("friend", true);
			} else {
				sender.sendMessage(Main.color(prefix + "Sorry, machines aren't allowed to have friends."));
			}
			break;
		}
	}
}
