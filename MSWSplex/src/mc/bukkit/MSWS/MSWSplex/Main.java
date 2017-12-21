/*
 * Copyright (c) 2017 MSWS (Isaac K. Boaz)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *   * Neither the name of the author nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package mc.bukkit.MSWS.MSWSplex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import mc.bukkit.MSWS.GameManager.Manager;
/*
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
*/
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Main extends JavaPlugin implements Listener {
	private final JSONParser jsonParser = new JSONParser();
	private final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	public static Map<String, EntityEnderDragon> dragons = new ConcurrentHashMap<>();
	// final static List<WrappedGameProfile> names = new
	// ArrayList<WrappedGameProfile>();
	static FileConfiguration config;
	static FileConfiguration data;
	static FileConfiguration reports;
	static FileConfiguration guis;
	static FileConfiguration friend;
	static FileConfiguration party;
	static FileConfiguration filter;
	static FileConfiguration holos;
	static FileConfiguration com;
	static FileConfiguration gadget;
	static ConfigurationSection ranks;
	static HashMap<CommandSender, CommandSender> recent = new HashMap<>();
	static HashMap<CommandSender, CommandSender> recentDM = new HashMap<>();
	static HashMap<DamageCause, Boolean> causes = new HashMap<>();
	static HashMap<Integer, String> boardLines = new HashMap<>();
	static HashMap<Player, Double> timer = new HashMap<>();
	static HashMap<Player, Boolean> ignoreJoin = new HashMap<>();
	static HashMap<String, String> settings = new HashMap<>();
	static HashMap<Player, String> reason = new HashMap<>();
	static HashMap<String, Boolean> save = new HashMap<>();
	static HashMap<Player, String> punishing = new HashMap<>();
	static HashMap<Player, Scoreboard> oldBoard = new HashMap<>();
	HashMap<Player, String> prevRank = new HashMap<>();
	HashMap<Player, String> prevChat = new HashMap<>();
	static HashMap<Player, String> hasA = new HashMap<>();
	HashMap<Player, Location> chestOpening = new HashMap<>();
	HashMap<Player, Double> chestProg = new HashMap<>();
	HashMap<Player, Material> chestType = new HashMap<>();
	HashMap<Player, EnumParticle> particleType = new HashMap<>();
	HashMap<Player, String> holoID = new HashMap<>();
	static HashMap<Player, String> purchasing = new HashMap<>();
	static HashMap<Player, Integer> cost = new HashMap<>();
	static HashMap<Player, String> costType = new HashMap<>();
	static HashMap<Player, Integer> costAmo = new HashMap<>();
	static HashMap<Player, EntityType> disguise = new HashMap<>();
	static HashMap<Player, String> comChat = new HashMap<>();
	static HashMap<Player, Integer> comPage = new HashMap<>();
	static HashMap<String, Double> chatSlow = new HashMap<>();
	static HashMap<Player, String> activeCom = new HashMap<>();
	static HashMap<Player, String> lastDamage = new HashMap<>();
	static HashMap<Player, Location> frozen = new HashMap<>();
	static List<String> mps = new ArrayList<String>();
	World lobby;
	// ProtocolManager protocolManager;
	File configYml = new File(getDataFolder() + "/config.yml");
	File gadgetYml = new File(getDataFolder() + "/gadget.yml");
	File dataYml = new File(getDataFolder() + "/data.yml");
	File reportYml = new File(getDataFolder() + "/reports.yml");
	File guiYml = new File(getDataFolder() + "/guis.yml");
	File friendYml = new File(getDataFolder() + "/friends.yml");
	File partyYml = new File(getDataFolder() + "/parties.yml");
	File filterYml = new File(getDataFolder() + "/filter.yml");
	File holoYml = new File(getDataFolder() + "/holo.yml");
	File comYml = new File(getDataFolder() + "/communities.yml");
	File logYml = new File(getDataFolder() + "/punishLogs.yml");
	String header = color("&lMineplex Network    &a");
	static List<String> staff = new ArrayList<String>();
	static long startTime;
	ScoreboardManager manager;
	static Scoreboard board;
	Scoreboard defBoard;
	Scoreboard gameBoard;
	static Objective obj;
	int onlinePlayers = 0;
	int maxPlayers = 0;
	Objective defObj;
	Objective gameObj;
	static boolean enabled = false;
	boolean refresh = false;
	static CommandSender console = Bukkit.getConsoleSender();
	public void onEnable() {
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		defBoard = manager.getNewScoreboard();
		gameBoard = manager.getNewScoreboard();
		obj = board.registerNewObjective("MSWSplex", "dummy");
		defObj = defBoard.registerNewObjective("DefaultBoard", "dummy");
		gameObj = gameBoard.registerNewObjective("GameBoard", "dummy");
		startTime = System.currentTimeMillis();
		staff.add("trainee");
		staff.add("mod");
		staff.add("srmod");
		staff.add("admin");
		staff.add("leader");
		staff.add("dev");
		staff.add("owner");
		staff.add("support");
		staff.add("media");
		config = YamlConfiguration.loadConfiguration(configYml);
		data = YamlConfiguration.loadConfiguration(dataYml);
		reports = YamlConfiguration.loadConfiguration(reportYml);
		guis = YamlConfiguration.loadConfiguration(guiYml);
		friend = YamlConfiguration.loadConfiguration(friendYml);
		party = YamlConfiguration.loadConfiguration(partyYml);
		filter = YamlConfiguration.loadConfiguration(filterYml);
		holos = YamlConfiguration.loadConfiguration(holoYml);
		com = YamlConfiguration.loadConfiguration(comYml);
		gadget = YamlConfiguration.loadConfiguration(gadgetYml);
		if (!dataYml.exists())
			saveResource("data.yml", true);
		if (!configYml.exists())
			saveResource("config.yml", true);
		if (!new File(getDataFolder() + "/mplogo.png").exists())
			saveResource("mplogo.png", true);
		if (!new File(getDataFolder() + "/COPYRIGHT.txt").exists())
			saveResource("COPYRIGHT.txt", true);
		if (!guis.contains("GameMenu")) {
			saveResource("guis.yml", true);
		}
		if (!guis.contains("PunishMenu")) {
			saveResource("guis.yml", true);
		}
		if (!filter.contains("Words")) {
			saveResource("filter.yml", true);
		}
		if (holos.getConfigurationSection("Holos") == null)
			holos.createSection("Holos");
		if (holos.getConfigurationSection("NPC") == null)
			holos.createSection("NPC");
		if (!gadgetYml.exists())
			saveResource("gadget.yml", true);
		configYml = new File(getDataFolder() + "/config.yml");
		dataYml = new File(getDataFolder() + "/data.yml");
		reportYml = new File(getDataFolder() + "/reports.yml");
		guiYml = new File(getDataFolder() + "/guis.yml");
		friendYml = new File(getDataFolder() + "/friends.yml");
		partyYml = new File(getDataFolder() + "/parties.yml");
		filterYml = new File(getDataFolder() + "/filter.yml");
		holoYml = new File(getDataFolder() + "/holo.yml");
		comYml = new File(getDataFolder() + "/communities.yml");
		config = YamlConfiguration.loadConfiguration(configYml);
		data = YamlConfiguration.loadConfiguration(dataYml);
		reports = YamlConfiguration.loadConfiguration(reportYml);
		guis = YamlConfiguration.loadConfiguration(guiYml);
		friend = YamlConfiguration.loadConfiguration(friendYml);
		party = YamlConfiguration.loadConfiguration(partyYml);
		filter = YamlConfiguration.loadConfiguration(filterYml);
		holos = YamlConfiguration.loadConfiguration(holoYml);
		com = YamlConfiguration.loadConfiguration(comYml);
		gadget = YamlConfiguration.loadConfiguration(gadgetYml);
		if (config.getBoolean("Features.lobbies")) {
			String[] worlds = { "Lobby-1", "Lobby-2", "Staff-1", "Backup-1" };
			for (String res : worlds) {
				World tempWorld = Bukkit
						.createWorld(WorldCreator.name(res).type(WorldType.FLAT).generateStructures(false));
				tempWorld.setGameRuleValue("doMobSpawning", "false");
				tempWorld.setGameRuleValue("doMobLoot", "false");
				tempWorld.setSpawnLocation(0, 3, 0);
			}
			for (String res : data.getStringList("Worlds")) {
				Bukkit.createWorld(WorldCreator.name(res));
				World world = Bukkit.getWorld(res);
				world.setGameRuleValue("doMobSpawning", "false");
				world.setGameRuleValue("mobGreifing", "false");
				if (world.getSpawnLocation().getBlockX() == 0 && world.getSpawnLocation().getBlockY() == 0
						&& world.getSpawnLocation().getBlockZ() == 0) {
					world.setSpawnLocation(0, 3, 0);
					tell(console, "World " + world.getName() + " has bad spawn location, setting to 0 3 0.");
				}
			}
		}
		if (!configYml.exists()) {
			saveResource("config.yml", true);
			tell(console, "&a----------&lMSWSplex &afirst startup guide----------");
			tell(console, "&a1. Owner is given to the first player that joins");
			tell(console, "&a2. Console automatically has owner rank");
			tell(console, "&aPlease enjoy MSWSplex :D!");
		}
		if (config.getBoolean("Features.eventcontrol")) {
			Main.settings.put("time", "6000");
			Main.settings.put("weather", "CLEAR");
			Main.settings.put("explosions", "false");
			Main.settings.put("doubleJump", "true");
			Main.settings.put("health", "20");
			Main.settings.put("hunger", "20");
			Main.settings.put("blockBreak", "false");
			Main.settings.put("blockPlace", "false");
			Main.settings.put("itemDrop", "false");
			Main.settings.put("itemPickup", "false");
			Main.settings.put("pvp", "false");
			Main.settings.put("pve", "false");
			Main.settings.put("evp", "false");
			for (DamageCause cause : DamageCause.values()) {
				Main.causes.put(cause, false);
			}
		} else {
			settings.put("time", null);
			settings.put("weather", null);
			settings.put("explosions", "true");
			settings.put("doubleJump", "false");
			settings.put("hunger", null);
			settings.put("health", null);
			settings.put("blockBreak", "true");
			settings.put("blockPlace", "true");
			settings.put("itemDrop", "true");
			settings.put("itemPickup", "true");
			settings.put("pvp", "true");
			settings.put("pve", "true");
			settings.put("evp", "true");
		}
		// refreshMotd();
		/*
		 * ProtocolLibrary.getProtocolManager().addPacketListener(new
		 * PacketAdapter(this, ListenerPriority.NORMAL,
		 * Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO),
		 * ListenerOptions.ASYNC) {
		 * 
		 * @Override public void onPacketSending(PacketEvent event) { try { Socket sock
		 * = new Socket("us.mineplex.com", 25565); DataOutputStream out = new
		 * DataOutputStream(sock.getOutputStream()); DataInputStream in = new
		 * DataInputStream(sock.getInputStream()); out.write(0xFE); int b; StringBuffer
		 * str = new StringBuffer(); while ((b = in.read()) != -1) { if (b != 0 && b >
		 * 16 && b != 255 && b != 23 && b != 24) { str.append((char) b); } } String[]
		 * data = str.toString().split("§"); onlinePlayers = Integer.parseInt(data[1]);
		 * maxPlayers = Integer.parseInt(data[2]); sock.close(); } catch (Exception e) {
		 * 
		 * } int amo = 0; for (Player target : Bukkit.getOnlinePlayers()) { if
		 * (!data.getBoolean("Users." + target.getUniqueId() + ".vanished")) amo++; } if
		 * (config.getBoolean("Features.customtab")) {
		 * event.getPacket().getServerPings().read(0).setPlayersVisible(false);
		 * event.getPacket().getServerPings().read(0)
		 * .setVersionName(color("&a&lWhy aren't you using &2&l1.8?"));
		 * event.getPacket().getServerPings().read(0).setVersionProtocol(47);
		 * event.getPacket().getServerPings().read(0).setPlayers(names); if
		 * (config.getInt("FakePlayers") == -1) {
		 * event.getPacket().getServerPings().read(0).setPlayersOnline(onlinePlayers);
		 * event.getPacket().getServerPings().read(0).setPlayersMaximum(maxPlayers); }
		 * else { event.getPacket().getServerPings().read(0).setPlayersOnline(amo +
		 * config.getInt("FakePlayers")); event.getPacket().getServerPings().read(0)
		 * .setPlayersMaximum(amo + 1 + config.getInt("FakePlayers")); } } } });
		 */
		for (DamageCause cause : DamageCause.values())
			causes.put(cause, false);
		if (config.getConfigurationSection("Ranks") == null)
			config.createSection("Ranks");
		saveFriend();
		ranks = config.getConfigurationSection("Ranks");
		if (data.getConfigurationSection("Settings") != null) {
			ConfigurationSection causeSection = data.getConfigurationSection("Settings");
			for (String res : causeSection.getKeys(false)) {
				try {
					causes.put(DamageCause.valueOf(res), data.getBoolean("Settings." + res));
				} catch (Exception e) {
				}
			}
			if (data.contains("Settings.explosions"))
				settings.put("explosions", data.getString("Settings.explosions"));
			if (data.contains("Settings.time"))
				settings.put("time", data.getInt("Settings.time") + "");
			if (data.contains("Settings.doublejump"))
				settings.put("doubleJump", data.getString("Settings.doublejump"));
			if (data.contains("Settings.hunger"))
				settings.put("hunger", data.getString("Settings.hunger"));
			if (data.contains("Settings.health"))
				settings.put("health", data.getString("Settings.health"));
			if (data.contains("Settings.weather"))
				settings.put("weather", data.getString("Settings.weather"));
			if (data.contains("Settings.blockPlace"))
				settings.put("blockPlace", data.getString("Settings.blockPlace"));
			if (data.contains("Settings.blockBreak"))
				settings.put("blockBreak", data.getString("Settings.blockBreak"));
			System.out.println("Event Settings Loaded!");
		}
		String prefix = "&9Client Manager> &7";
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (data.getConfigurationSection("Users") == null
					|| data.getConfigurationSection("Users").getKeys(false).size() == 0) {
				tell(target, prefix + "Your rank has been updated to Owner!");
				data.set("Users." + target.getUniqueId() + ".rank", "owner");
			}
			refreshPerms(target);
			String uuid = target.getUniqueId() + "";
			data.set("Users." + uuid + ".username", target.getName());
			if (!data.contains("Users." + uuid + ".rank"))
				data.set("Users." + uuid + ".rank", "default");
			if (!data.contains("Users." + uuid + ".level"))
				data.set("Users." + uuid + ".level", 0);
			target.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			if(target.getWorld().getName().contains("Lobby-")||target.getWorld().getName().contains("EVENT-"))
			setHeaderFooter(target.getPlayer(), header + target.getWorld().getName(),
					"Visit &awww.mineplex.com &rfor News, Forums and Shop");
			if (Boolean.valueOf(settings.get("doubleJump"))&&target.getWorld().getName().contains("Lobby-")) {
				target.setAllowFlight(true);
			} else {
				if (target.getGameMode() == GameMode.SURVIVAL) {
					target.setFlying(false);
					target.setAllowFlight(false);
				}
			}
			hubItems(target);
		}
		refreshTab();
		// refreshMotd();
		reloadHolograms();
		reloadNPCs();
		new BukkitRunnable() {
			int count = 0;
			HashMap<Player, Integer> oldGem = new HashMap<Player, Integer>();
			HashMap<Player, Integer> oldShard = new HashMap<Player, Integer>();
			HashMap<Player, String> oldRank = new HashMap<Player, String>();
			HashMap<Player, String> oldWorld = new HashMap<Player, String>();

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					GadgetManager.timer(player);
					if (getPlayers().contains(player.getName())) {
						DragonManager.teleportBar(player);
					}
					List<String> announce = config.getStringList("Announcements");
					if (announce.size() > 0) {
						String name = cST(
								color(announce.get(((int) count / config.getInt("AnnounceRate")) % announce.size())),
								player);
						DragonManager.updateBar(player, name, 100);
					}
					if (data.contains("Users." + player.getUniqueId() + ".radius")) {
						for (Entity kplayer : player.getNearbyEntities(
								data.getDouble("Users." + player.getUniqueId() + ".radius"),
								data.getDouble("Users." + player.getUniqueId() + ".radius"),
								data.getDouble("Users." + player.getUniqueId() + ".radius"))) {
							if (!data.getBoolean("Users." + kplayer.getUniqueId() + ".notify.kb"))
								ClientManager.kbEntity(kplayer, player.getLocation());
							kplayer.getWorld().playSound(kplayer.getLocation(), Sound.CHICKEN_EGG_POP, 2, (float) 0.5);
						}
					}
					if (player.getWorld().getName().contains("EVENT-")
							|| player.getWorld().getName().contains("Lobby-")) {
						if (settings.get("time") != null)
							player.getWorld().setTime(Integer.valueOf(settings.get("time")));
						if (settings.get("weather") != null)
							player.setPlayerWeather(WeatherType.valueOf(settings.get("weather")));
						if (settings.get("hunger") != null)
							player.setFoodLevel(Integer.valueOf(settings.get("hunger")));
						if (settings.get("health") != null)
							player.setHealth(Double.valueOf(settings.get("health")));
					}
					if (!enabled && config.getBoolean("Features.scoreboard")) {
						if (player.getWorld().getName().contains("Lobby-")) {
							Scoreboard tempBoard = player.getScoreboard();
							if (tempBoard == null) {
								tempBoard = Bukkit.getScoreboardManager().getNewScoreboard();
								player.setScoreboard(tempBoard);
							}
							Objective tempObj = tempBoard.getObjective(player.getName());
							if (tempObj == null) {
								tempObj = tempBoard.registerNewObjective(player.getName(), "dummy");
								tempObj.setDisplaySlot(DisplaySlot.SIDEBAR);
							}
							String name = color("Welcome " + player.getName() + ", to the Mineplex Network!       ");
							name = color(cST(data.getString("Scoreboards.default.name"),player));
							String disp = "";
							disp = name.substring(count % name.length()) + name.substring(0, count % name.length());
							disp = disp.substring(0, Math.min(17,name.length()));
							tempObj.setDisplayName(color("&l" + disp));
							if (oldGem.containsKey(player) && oldGem.get(player) != cosmAmo(player, "gems"))
								tempBoard.resetScores(oldGem.get(player) + "");
							if (oldShard.containsKey(player) && oldShard.get(player) != cosmAmo(player, "shards"))
								tempBoard.resetScores(oldShard.get(player) + color("&r"));
							if (oldRank.containsKey(player) && oldRank.get(player) != getRankID(player.getUniqueId()))
								tempBoard.resetScores(camelCase(oldRank.get(player)).replace("Default", "No Rank"));
							if (oldWorld.containsKey(player) && oldWorld.get(player) != player.getWorld().getName()) {
								tempBoard.resetScores(oldWorld.get(player));
							}
							List<String> list = data.getStringList("Scoreboards.default.lines");
							for (int i = list.size(); i >= 1; i--) {
								tempObj.getScore(color(list.get(i - 1).replace("%gems%", cosmAmo(player, "gems") + "")
										.replace("%shards%", cosmAmo(player, "shards") + "")
										.replace("%server%", player.getWorld().getName())
										.replace("%rank%", camelCase(getRankID(player.getUniqueId())))
										.replace("Default", "No Rank") + "")).setScore(list.size() - i + 1);
							}
							oldGem.put(player, cosmAmo(player, "gems"));
							oldShard.put(player, cosmAmo(player, "shards"));
							oldRank.put(player, getRankID(player.getUniqueId()));
							oldWorld.put(player, player.getWorld().getName());
						}
					}
					if (frozen.containsKey(player)) {
						sendParticle(EnumParticle.SMOKE_LARGE, player.getLocation().add(0, 2, 0), 1, new Vector(), 0);
					}
				}
				if (getEntity("slimeball-Arena") != null) {
					for (Entity ent : getEntity("slimeball-Arena").getNearbyEntities(20, 10, 20)) {
						if (ent instanceof Player) {
							if (!data.getBoolean("Users." + ent.getUniqueId() + ".notify.kb")
									&& !data.contains("Users." + ent.getUniqueId() + ".team")) {
								ClientManager.kbEntity(ent, getEntity("slimeball-Arena").getLocation());
								tell((Player) ent, "&9Teams> &7Use /team [color] to play!");
							}
						}
					}
				}
				if (refresh) {
					refreshDisguise();
					refresh = false;
				}
				count++;
			}
		}.runTaskTimer(this, 0, 3);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (chestProg.containsKey(player)) {
						int i = 0;
						Double locX[] = { 1.0, 3.0, 3.0, 1.0, -1.0, -3.0, -3.0, -1.0 };
						Double locZ[] = { -3.0, -1.0, 1.0, 3.0, 3.0, 1.0, -1.0, -3.0 };
						if (chestProg.get(player) == 0) {
							Material type = chestType.get(player);
							player.getWorld().getBlockAt(chestOpening.get(player)).setType(Material.AIR);
							for (int ii = 0; ii < locX.length; ii++) {
								Location loc = chestOpening.get(player);
								player.getWorld().getBlockAt(loc.add(locX[ii], 0, locZ[ii])).setType(type);
								loc.subtract(locX[ii], 0, locZ[ii]);
							}
							toggleHolo(player.getLocation(), holoID.get(player), false);
						} else if (chestProg.get(player) < 320) {
							i = 0;
							double num = chestProg.get(player);
							for (int ii = 0; ii < 320; ii += 40) {
								if (num < ii) {
									i = ii / 40;
									break;
								}
							}
							Location loc = chestOpening.get(player);
							double rate = 5;
							double progX = Math.sin(chestProg.get(player) / rate);
							double progZ = Math.cos(chestProg.get(player) / rate);
							sendParticle(particleType.get(player),
									loc.add(progX, 2 - ((num % 40) / 20), progZ).add(locX[i], 0, locZ[i]), 1,
									new Vector(), 0);
							loc.subtract(progX, 2 - ((num % 40) / 20), progZ).subtract(locX[i], 0, locZ[i]);
							for (int ii = 40; ii < 320; ii += 40) {
								if (num == ii + 1 || num == 319) {
									loc = chestOpening.get(player);
									int pos = i - 1;
									if (pos == -1)
										pos = 7;
									if (num == 319)
										pos = 0;
									player.getWorld().getBlockAt(loc.add(locX[pos], 0, locZ[pos]))
											.setType(Material.CHEST);
									sendParticle(EnumParticle.EXPLOSION_LARGE, loc, 1, new Vector(), 0);
									loc.subtract(locX[pos], 0, locZ[pos]);
									if (num == 319)
										break;
								}
							}
						} else if (chestProg.get(player) > 2400) {
							tell(player, "&9Treasure> &7You took too long to open a chest!");
							player.getWorld().getBlockAt(chestOpening.get(player)).setType(Material.CHEST);
							for (int ii = 0; ii < locX.length; ii++) {
								Location loc = chestOpening.get(player);
								player.getWorld().getBlockAt(loc.add(locX[ii], 0, locZ[ii])).setType(Material.AIR);
								loc.subtract(locX[ii], 0, locZ[ii]);
							}
							chestProg.remove(player);
							chestType.remove(player);
							chestOpening.remove(player);
							toggleHolo(player.getLocation(), holoID.get(player), true);
							break;
						}
						chestProg.put(player, chestProg.get(player) + 1);
					}
				}
				for (String res : save.keySet()) {
					if (!save.get(res))
						continue;
					switch (res) {
					case "data":
						saveData();
						break;
					case "report":
						saveReport();
						break;
					case "config":
						saveConfig();
						break;
					case "friend":
						saveFriend();
						break;
					case "holo":
						saveHolo();
						break;
					case "party":
						saveParty();
						break;
					case "gui":
						saveGui();
						break;
					case "community":
						saveCom();
						break;
					case "all":
						saveAll();
						break;
					}
				}
				save.clear();
			}
		}.runTaskTimer(this, 0, 1);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					addTime(player, 1);
				}
			}
		}.runTaskTimer(this, 0, 1200);
		new BukkitRunnable() {
			@Override
			public void run() {
				saveAll();
			}
		}.runTaskTimer(this, 360000, 360000);
		if (config.getBoolean("Features.announcements")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (player.getWorld().getName().contains("Lobby-")) {
							tell(player, "&2&lCarl the Creeper> &aHey " + player.getName()
									+ "! I have some amazing rewards for you! Come see me!".replace("s", "sss"));
						}
					}
				}
			}.runTaskTimer(this, 0, 5000);
		}
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new CommandManager(), this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "unpunish":
			PunishCommand.command(sender, command.getName(), label, args);
			break;
		case "e":
			EventCommand.command(sender, command.getName(), label, args);
			break;
		case "updaterank":
			ClientManager.command(sender, command.getName(), label, args);
			break;
		default:
			return false;
		}
		saveConfig();
		saveData();
		saveParty();
		saveCom();
		return true;
	}

	public void refreshDisguise() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (disguise.containsKey(player)) {
				disguise(player, disguise.get(player));
			}
		}
	}

	public static Boolean comExists(String com) {
		return Main.com.contains(com);
	}

	public static Integer getComAmount(OfflinePlayer player) {
		int amo = 0;
		for (String res : com.getKeys(false)) {
			if (com.getInt(res + ".player." + player.getUniqueId() + ".rank") == 2)
				amo++;
		}
		return amo;
	}

	public static boolean inCom(OfflinePlayer player, String comName) {
		return (com.contains(comName + ".player." + player.getUniqueId()));
	}

	public static void tellCom(String message, String comName) {
		for (String res : com.getConfigurationSection(comName + ".player").getKeys(false)) {
			Player target = Bukkit.getPlayer(UUID.fromString(res));
			if (target != null) {
				tell(target, message);
			}
		}
	}

	public void chatCom(String message, String sender, String comName) {
		String c1 = com.getString(comName + ".setting.c1"), c2 = com.getString(comName + ".setting.c2"),
				c3 = com.getString(comName + ".setting.c3");
		for (String res : com.getConfigurationSection(comName + ".player").getKeys(false)) {
			Player target = Bukkit.getPlayer(UUID.fromString(res));
			if (target != null) {
				if (data.getBoolean("Users." + target.getUniqueId() + ".notify.com." + comName)
						|| !data.contains("Users." + target.getUniqueId() + ".notify.com." + comName)) {
					tell(target, c1 + "&l" + comName + " " + c2 + "&l" + sender + " " + c3
							+ ChatColor.stripColor(color(message)));
				}
			}
		}
	}

	public static int getRank(OfflinePlayer player, String comName) {
		if (comExists(comName))
			return com.getInt(comName + ".player." + player.getUniqueId() + ".rank");
		return 0;
	}

	public static void disguise(Player player, EntityType entity) {
		PacketPlayOutSpawnEntityLiving spawn = null;
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[] { player.getEntityId() });
		net.minecraft.server.v1_8_R3.World world = ((CraftPlayer) player).getHandle().getWorld();
		spawn = EntityManager.getEntity(entity, world);
		try {
			Field idField = spawn.getClass().getDeclaredField("a");
			idField.setAccessible(true);
			idField.set(spawn, player.getEntityId());
		} catch (Exception e) {
		}
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target != player) {
				((CraftPlayer) target).getHandle().playerConnection.sendPacket(destroy);
				((CraftPlayer) target).getHandle().playerConnection.sendPacket(spawn);
			}
		}
		disguise.put(player, entity);
	}

	public static void sendAction(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static String getHoloID(String name) {
		if (holos.getConfigurationSection("Holos") != null) {
			for (String res : holos.getConfigurationSection("Holos").getKeys(false)) {
				if (color(holos.getString("Holos." + res + ".name").trim()).equals(name.trim()))
					return res;
			}
		}
		return "";
	}

	public static String getNPCID(String name) {
		for (String res : holos.getConfigurationSection("NPC").getKeys(false)) {
			if (color(holos.getString("NPC." + res + ".name").trim()).equals(name.trim()))
				return res;
		}
		return "";
	}

	public static void reloadHolograms() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity e : world.getEntities()) {
				if (e.getType() == EntityType.ARMOR_STAND) {
					if (e.getName().endsWith(color("&r"))) {
						e.remove();
					}
				}
			}
		}
		if (holos.getConfigurationSection("Holos") != null) {
			for (String res : holos.getConfigurationSection("Holos").getKeys(false)) {
				if (holos.getString("Holos." + res + ".location.world").equals("global")) {
					for (World w : Bukkit.getWorlds()) {
						if (w.getName().contains("Lobby-") || w.getName().contains("Backup-")) {
							LivingEntity temparmor = (LivingEntity) w
									.spawnEntity(
											new Location(w, holos.getDouble("Holos." + res + ".location.X"),
													holos.getDouble("Holos." + res + ".location.Y"),
													holos.getDouble("Holos." + res + ".location.Z")),
											EntityType.ARMOR_STAND);
							ArmorStand tempstand = (ArmorStand) temparmor;
							temparmor.setCustomName(color(holos.getString("Holos." + res + ".name")));
							temparmor.setCustomNameVisible(true);
							tempstand.setVisible(false);
							tempstand.setGravity(false);
						}
					}
				} else {
					LivingEntity temparmor = (LivingEntity) Bukkit
							.getWorld(holos.getString("Holos." + res + ".location.world")).spawnEntity(
									new Location(Bukkit.getWorld(holos.getString("Holos." + res + ".location.world")),
											holos.getDouble("Holos." + res + ".location.X"),
											holos.getDouble("Holos." + res + ".location.Y"),
											holos.getDouble("Holos." + res + ".location.Z")),
									EntityType.ARMOR_STAND);
					ArmorStand tempstand = (ArmorStand) temparmor;
					temparmor.setCustomName(color(holos.getString("Holos." + res + ".name")));
					temparmor.setCustomNameVisible(true);
					tempstand.setVisible(false);
					tempstand.setGravity(false);
				}
			}
		}
	}

	public static void reloadNPCs() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity e : world.getEntities()) {
				if (e.getName().endsWith(color("&r"))) {
					e.remove();
				}
			}
		}
		if (holos.getConfigurationSection("NPC") != null) {
			for (String res : holos.getConfigurationSection("NPC").getKeys(false)) {
				if (holos.getString("NPC." + res + ".location.world").equals("global")) {
					for (World w : Bukkit.getWorlds()) {
						if (w.getName().contains("Lobby-") || w.getName().contains("Backup-")) {
							Entity temparmor = w.spawnEntity(
									new Location(w, holos.getDouble("NPC." + res + ".location.X"),
											holos.getDouble("NPC." + res + ".location.Y"),
											holos.getDouble("NPC." + res + ".location.Z")),
									EntityType.valueOf(holos.getString("NPC." + res + ".type")));
							temparmor.setCustomName(color(holos.getString("NPC." + res + ".name")));
							temparmor.setCustomNameVisible(false);
							net.minecraft.server.v1_8_R3.Entity NMSe = ((CraftEntity) temparmor).getHandle();
							NBTTagCompound tag = NMSe.getNBTTag();
							if (tag == null)
								tag = new NBTTagCompound();
							NMSe.c(tag);
							tag.setInt("NoAI", 1);
							tag.setInt("Invulnerable", 1);
							tag.setInt("CustomNameVisible", 0);
							NMSe.f(tag);
						}
					}
				} else {
					Entity temparmor = Bukkit.getWorld(holos.getString("NPC." + res + ".location.world")).spawnEntity(
							new Location(Bukkit.getWorld(holos.getString("NPC." + res + ".location.world")),
									holos.getDouble("NPC." + res + ".location.X"),
									holos.getDouble("NPC." + res + ".location.Y"),
									holos.getDouble("NPC." + res + ".location.Z")),
							EntityType.valueOf(holos.getString("NPC." + res + ".type")));
					temparmor.setCustomName(color(holos.getString("NPC." + res + ".name")));
					temparmor.setCustomNameVisible(false);
					net.minecraft.server.v1_8_R3.Entity NMSe = ((CraftEntity) temparmor).getHandle();
					NBTTagCompound tag = NMSe.getNBTTag();
					if (tag == null)
						tag = new NBTTagCompound();
					NMSe.c(tag);
					tag.setInt("NoAI", 1);
					tag.setInt("Invulnerable", 1);
					NMSe.f(tag);
				}
			}
		}
		reloadHolograms();
	}

	@SuppressWarnings("deprecation")
	public static void hubItems(Player player) {
		if (!config.getBoolean("Features.lobbies"))
			return;
		Inventory inv = player.getInventory();
		inv.clear();
		player.getInventory().setArmorContents(null);
		if (player.getWorld().getName().contains("Lobby-")) {
			inv.setItem(0, newItem(Material.COMPASS, "&aServer Selector"));
			inv.setItem(1, newItem(Material.WATCH, "&aLobby Selector"));
			inv.setItem(4, newItem(Material.CHEST, "&aCosmetic Menu"));
			inv.setItem(6, newItem(Material.NAME_TAG, "&aParties"));
			inv.setItem(7, newItem(Material.ENCHANTED_BOOK, "&aTitles"));
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 3);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(player.getName());
			meta.setDisplayName(color("&aMy Profile"));
			skull.setItemMeta(meta);
			inv.setItem(8, skull);
		} else {
			inv.setItem(5, newItem(Material.NAME_TAG, "&aParties"));
			if (!mps.contains(player.getWorld().getName()))
				inv.setItem(6, newItem(Material.CHEST, "&aCosmetic Menu"));
			if (!mps.contains(player.getWorld().getName()))
				inv.setItem(7, newItem(Material.EMERALD, "&aGame Amplifiers"));
			inv.setItem(8, newItem(Material.WATCH, "&aReturn to Hub"));
		}
	}

	public void saveAll() {
		saveData();
		saveConfig();
		saveGui();
		saveFriend();
		saveReport();
		saveFilter();
		saveHolo();
		saveCom();
	}

	public static String filter(String msg) {
		if (config.getBoolean("Features.filter")) {
			String result = msg;
			List<String> words = filter.getStringList("Words");
			for (String res : words)
				result = result.replaceAll(res, "****");
			return result;
		} else {
			return msg;
		}
	}

	public void addTime(OfflinePlayer player, int seconds) {
		String uuid = player.getUniqueId() + "";
		if (data.contains("Users." + uuid + ".time")) {
			data.set("Users." + uuid + ".time", data.getDouble("Users." + uuid + ".time") + seconds);
		} else {
			data.set("Users." + uuid + ".time", seconds);
		}
	}

	public static void deleteWorld(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
	}

	public static PacketPlayOutChat createPacketPlayOutChat(String s) {
		return new PacketPlayOutChat(ChatSerializer.a(s));
	}

	public static void jsonMSG(Player p, String s) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(createPacketPlayOutChat(s));
	}

	public static void jsonMSG(CommandSender p, String s) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(createPacketPlayOutChat(s));
	}

	static void makeScoreboard(String msg, CommandSender sender) {
		enabled = true;
		String prefix = "&9Scoreboard> &7";
		if (msg.length() > 32) {
			tell(sender, prefix + "Title is too long. (&e" + msg.length() + "/32&7)");
			return;
		}
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(color(msg));
		for (int i = 1; i <= 15; i++) {
			if (boardLines.get(i) != null) {
				board.resetScores(color(boardLines.get(i)));
			}
		}
		for (int i = 1; i <= 15; i++) {
			String line = " ";
			for (int ii = 0; ii < i; ii++) {
				line = line + " ";
			}
			for (int iii = i; iii < 15; iii++) {
				line = line + "&r";
			}
			obj.getScore(color(line)).setScore(i);
			boardLines.put(i, line);
		}
		for (Player starget : Bukkit.getOnlinePlayers())
			starget.setScoreboard(board);
		tell(sender, prefix + "Succesfully created scoreboard.");
	}

	static void makeScoreboard(String msg, CommandSender sender, int lines) {
		enabled = true;
		String prefix = "&9Scoreboard> &7";
		if (msg.length() > 32) {
			tell(sender, prefix + "Title is too long. (&e" + msg.length() + "/32&7)");
			return;
		}
		if (lines > 15) {
			tell(sender, prefix + "Number of lines is too high (&e" + lines + "/15&7)");
			return;
		}
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(color(msg));
		for (Integer res : boardLines.keySet()) {
			board.resetScores(color(boardLines.get(res)));
		}
		for (int i = 1; i <= lines; i++) {
			String line = " ";
			for (int ii = 0; ii < i; ii++) {
				line = line + " ";
			}
			for (int iii = i; iii < lines; iii++) {
				line = line + "&r";
			}
			obj.getScore(color(line)).setScore(i);
			boardLines.put(i, line);
		}
		for (Player starget : Bukkit.getOnlinePlayers())
			starget.setScoreboard(board);
		tell(sender, prefix + "Succesfully created scoreboard.");
	}

	public void toggleHolo(Location loc, String id, Boolean visible) {
		try {
			for (Entity e : loc.getWorld().getEntities()) {
				if (e.getCustomName() != null && e != null && e.getType() != null) {
					if (e.getType() == EntityType.ARMOR_STAND
							&& e.getCustomName().equals(color(holos.getString("Holos." + id + ".name")))) {
						e.setCustomNameVisible(visible);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String initiate(String uuid) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(PROFILE_URL + uuid.toString().replace("-", ""))
				.openConnection();
		JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
		String name = (String) response.get("name");
		String cause = (String) response.get("cause");
		String errorMessage = (String) response.get("errorMessage");
		if (cause != null && cause.length() > 0) {
			throw new IllegalStateException(errorMessage);
		}
		return name;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String name = player.getDisplayName(), prefix = "&9Chat> &7";
		String message = "";
		if (config.getBoolean("Features.chatformat")) {
			if (config.getBoolean("Features.levels"))
				message = message+ color(getCol(getLevel(player.getUniqueId())) + getLevel(player.getUniqueId()) + " ");
			message = color(message + name + "&r ") + filter(event.getMessage());
			event.setFormat(message);
		} else {
			event.setMessage(filter(event.getMessage()));
		}
		//event.setCancelled(true);
		String uuid = player.getUniqueId() + "";
		if (data.contains("Users." + uuid + ".history")) {
			for (String res : data.getConfigurationSection("Users." + uuid + ".history").getKeys(false)) {
				if (isActive(player, res)) {
					if (data.getString("Users." + uuid + ".history." + res + ".type").matches("chatoffense")) {
						String time = TimeManagement
								.getTime((long) ((data.getLong("Users." + uuid + ".history." + res + ".date")
										+ data.getLong("Users." + uuid + ".history." + res + ".duration") * 60 * 1000)
										- System.currentTimeMillis()) / 60000);
						if (data.getInt("Users." + uuid + ".history." + res + ".duration") == -1)
							time = "Permanent";
						tell(player,
								"&9Punish> &7Shh, you're muted because "
										+ data.getString("Users." + uuid + ".history." + res + ".reason") + "by "
										+ data.getString("Users." + uuid + ".history." + res + ".user") + " for &a"
										+ time + "&7.");
						event.setCancelled(true);
					}
				}
			}
		}
		if (chatSlow.containsKey(player.getWorld().getName())) {
			Double slow = chatSlow.get(player.getWorld().getName());
			if (ranks.getInt(getRankID(player.getUniqueId()) + ".rank") < 12) {
				if (slow == -1) {
					tell(player, prefix + "Chat is silenced.");
					event.setCancelled(true);
				}
				if (slow != 0 && timer.containsKey(player)
						&& System.currentTimeMillis() - timer.get(player) < slow * 1000) {
					tell(player,
							prefix + "Chat slow is enabled, please wait &a" + TimeManagement.getTime(
									(double) (((slow * 1000) - (System.currentTimeMillis() - timer.get(player)))))
									+ "&7.");
					event.setCancelled(true);
				}
				timer.put(player, (double) System.currentTimeMillis());
			}

		}
		if (data.getBoolean("Users." + player.getUniqueId() + ".vanished")) {
			tell(player, "&eYou cannot chat while incognito.");
			event.setCancelled(true);
			return;
		}
		if (prevChat.containsKey(player) && ranks.getInt(getRankID(player.getUniqueId()) + ".rank") < 12) {
			if (event.getMessage().length() > 5 && prevChat.get(player).length() > 5) {
				if (event.getMessage().substring(0, 5).equalsIgnoreCase(prevChat.get(player).substring(0, 5))) {
					tell(player, prefix + "This message is too similar to your previous message.");
				event.setCancelled(true);
				return;
				}
			} else if (event.getMessage().equalsIgnoreCase(prevChat.get(player))) {
				tell(player, prefix + "This message is too similar to your previous message.");
				event.setCancelled(true);
				return;
			}
		}
		prevChat.put(player, event.getMessage());
		if (event.getMessage().charAt(0) == '@') {
			if (party.contains(name)) {
				for (String res : party.getConfigurationSection(name).getKeys(false)) {
					tell(Bukkit.getPlayer(res), getCol(getLevel(player.getUniqueId())) + getLevel(player.getUniqueId())
							+ " &5&l" + name + " &d" + event.getMessage().substring(1));
				}
				event.setCancelled(true);
				return;
			} else {
				tell(player, "&9Party> &7You are not in a party!");
				event.setCancelled(true);
				return;
			}
		}
		if (event.getMessage().charAt(0) == '!') {
			if (comChat.containsKey(player)) {
				chatCom(event.getMessage().substring(1), player.getName(), comChat.get(player));
			} else {
				tell(player,
						"&9Communities> &7You must specify what community you are talking to. (Use /com chat [community])");
			}
			event.setCancelled(true);
			return;
		}
		for(Player target:Bukkit.getOnlinePlayers()) {
			if(!target.getWorld().equals(player.getWorld()))
				event.getRecipients().remove(target);
		}
		for (Player cTarget : player.getWorld().getPlayers()) {
			if (data.contains("Users." + cTarget.getUniqueId() + ".ignoring")) {
				List<String> ignores = data.getStringList("Users." + cTarget.getUniqueId() + ".ignoring");
				if (ignores != null && !ignores.contains(player.getUniqueId() + "")) {
					//tell(cTarget, message);
					if (event.getMessage().contains(cTarget.getName()) && getPref(cTarget.getUniqueId(), "chat")
							&& cTarget != player) {
						cTarget.playSound(cTarget.getLocation(), Sound.NOTE_PLING, 2, 2);
						cTarget.sendMessage(
								color("&9Notify> &e" + player.getName() + " &7has said your name in chat."));
					}
				}else {
					event.getRecipients().remove(cTarget);
				}
			} else {
				if (event.getMessage().contains(cTarget.getName()) && getPref(cTarget.getUniqueId(), "chat")
						&& cTarget != player) {
					cTarget.playSound(cTarget.getLocation(), Sound.NOTE_PLING, 2, 2);
					tell(cTarget, "&9Notify> &e" + player.getName() + " &7has said your name in chat.");
				}
			}
		}
		addXp(player, 0.0001f);
	}

	public static boolean isActive(OfflinePlayer player, String res) {
		String uuid = player.getUniqueId() + "";
		return (System
				.currentTimeMillis() < (long) (data.getLong("Users." + uuid + ".history." + res + ".date")
						+ data.getLong("Users." + uuid + ".history." + res + ".duration") * 60 * 1000)
				|| data.getLong("Users." + uuid + ".history." + res + ".duration") == -1);
	}

	public boolean isActive(String uuid, String res) {
		return (System
				.currentTimeMillis() < (long) (data.getLong("Users." + uuid + ".history." + res + ".date")
						+ data.getLong("Users." + uuid + ".history." + res + ".duration") * 60 * 1000)
				|| data.getLong("Users." + uuid + ".history." + res + ".duration") == -1);
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player player = (Player) event.getPlayer();
		String uuid = player.getUniqueId() + "";
		int lobbyNum = 0;
		List<World> lobbies = new ArrayList<World>();
		if (config.getBoolean("Features.lobbies")) {
			for (World world : Bukkit.getWorlds()) {
				if (world.getName().contains("Lobby-")) {
					lobbies.add(world);
				}
			}
			if (lobbies.size() > 1) {
				lobby = lobbies.get(lobbyNum);
				lobbyNum = (int) Math.floor(Math.random() * lobbies.size());
			}
		} else {
			lobby = player.getWorld();
		}

		ConfigurationSection ipban = data.getConfigurationSection("Users.ipban");
		if (ipban != null) {
			for (String res : ipban.getKeys(false)) {
				if (getIP(player) != null) {
					if (getIP(player).equals(ipban.getString(res + ".ip"))) {
						event.disallow(Result.KICK_BANNED, (color("&cKicked whilst connecting to " + lobby.getName()
								+ ": &lYou are IP Banned for Permanent" + " \n&r" + ipban.getString(res + ".reason")
								+ "\n&2Unfairly banned? Appeal at &awww.mineplex.com/appeals")));
						return;
					}
				}
			}
		}

		if (data.contains("Users." + uuid + ".history")) {
			for (String res : data.getConfigurationSection("Users." + uuid + ".history").getKeys(false)) {
				if (isActive(player, res)) {
					if (data.getString("Users." + uuid + ".history." + res + ".type")
							.matches("(permban|exploiting|hacking|other|ipban)")) {
						String time = TimeManagement
								.getTime((Long) ((data.getLong("Users." + uuid + ".history." + res + ".date")
										+ data.getLong("Users." + uuid + ".history." + res + ".duration") * 60 * 1000)
										- System.currentTimeMillis()) / 60000);
						if (data.getInt("Users." + uuid + ".history." + res + ".duration") == -1)
							time = "Permanent";
						event.disallow(Result.KICK_BANNED,
								(color("&cKicked whilst connecting to " + lobby.getName() + ": &lYou are banned for "
										+ time + "\n&r"
										+ data.getString("Users." + uuid + ".history." + res + ".reason")
										+ "\n&2Unfairly banned? Appeal at &awww.mineplex.com/appeals")));
						return;
					}
				}
			}
		}

		if (getIP(player) != null) {
			if (data.contains("Users." + uuid + ".history")) {
				for (String res : data.getConfigurationSection("Users").getKeys(false)) {
					if (getIP(player).equals(data.getString("Users." + res + ".ip"))) {
						for (String resres : data.getConfigurationSection("Users." + uuid + ".history")
								.getKeys(false)) {
							if (isActive(res, resres)) {
								if (data.getString("Users." + uuid + ".history." + resres + ".type")
										.matches("(permban|exploiting|hacking|other|ipban)")) {
									tellStaff(color("&9IP Check> &e" + player.getName()
											+ "'s &7IP has an active punishment on &e"
											+ data.getString("Users." + res + ".username")));
								}
							}
						}
					}
				}
			}
		}
		event.allow();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		String prefix = "&9Client Manager> &7";
		Player player = event.getPlayer();
		String uuid = player.getUniqueId() + "";
		if (frozen.containsKey(player))
			player.teleport(frozen.get(player));
		if (Boolean.valueOf(settings.get("doubleJump"))) {
			player.setAllowFlight(true);
		} else {
			if (player.getGameMode() == GameMode.SURVIVAL) {
				player.setFlying(false);
				player.setAllowFlight(false);
			}
		}
		ignoreJoin.put(player, true);
		if (config.getBoolean("Features.lobbies"))
			player.teleport(lobby.getSpawnLocation());
		if (data.getConfigurationSection("Users") == null
				|| data.getConfigurationSection("Users").getKeys(false).size() == 0) {
			tell(player, prefix + "Your rank has been updated to Owner!");
			data.set("Users." + uuid + ".rank", "owner");
		}
		data.set("Users." + uuid + ".username", player.getName());
		if (!data.contains("Users." + uuid + ".rank"))
			data.set("Users." + uuid + ".rank", "default");
		if (!data.contains("Users." + uuid + ".level"))
			data.set("Users." + uuid + ".level", 0);
		if (!data.contains("Users." + uuid + ".notify.pm"))
			data.set("Users." + uuid + ".notify.pm", true);
		data.set("Users." + uuid + ".ip", player.getAddress().getHostName());
		saveConfig();
		saveData();
		for (String res : reports.getKeys(false)) {
			if (reports.getString(res + ".reporter") == uuid) {
				if (reports.contains(res + ".notified") && reports.getBoolean(res + ".notified")) {
					tell(player, "&9Report> &e" + reports.getString(res + ".handler")
							+ " &7closed your report against &e" + reports.getString(res + ".reported") + "&7.");
					tell(player, prefix + "Reason: &e" + reports.getString(res + ".result"));
				}
			}
		}
		List<String> announce = config.getStringList("Announcements");
		int num = 0;
		if (announce.size() > 0 && config.getBoolean("Features.announcements")) {
			num = (int) Math.floor(Math.random() * announce.size());
			player.sendTitle(color("&6&lMINEPLEX"), cST(color(announce.get(num)), player));
		}
		if (config.getBoolean("Features.announcements"))
			DragonManager.setBar(player, cST(color(announce.get(num)), player), 100);
		if (config.getBoolean("Features.announcements")) {
			tell(player, "&2&lCarl the Creeper> &aHey " + player.getName()
					+ "! I have some amazing rewards for you! Come see me!".replace("s", "sss"));
		}
		if (enabled && config.getBoolean("Features.scoreboard")) {
			oldBoard.put(player, board);
			player.setScoreboard(board);
		} else {
			oldBoard.put(player, defBoard);
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		//player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		if (data.getBoolean("Users." + uuid + ".vanished")) {
			tell(player, "");
			tell(player, "&6&lYou are currently incognito.");
			tell(player, "");
		}
		setHeaderFooter(player, header + player.getWorld().getName(),
				"Visit &awww.mineplex.com &rfor News, Forums and Shop");
		refreshPerms(player);
		hubItems(player);
		player.setGameMode(GameMode.SURVIVAL);
		refreshTab();
		refreshDisguise();
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (getPref(target.getUniqueId(), "join")) {
				if (!data.getBoolean("Users." + uuid + ".vanished") && target != player) {
					tell(target, "&9Notify> &e" + player.getName() + " &7has joined the server at &9"
							+ player.getWorld().getName());
					target.playSound(target.getLocation(), Sound.NOTE_PLING, 1, 1);
				}
			}
			if (target.getWorld().equals(player.getWorld())) {
				target.showPlayer(player);
				player.showPlayer(target);
				if (data.getBoolean("Users." + player.getUniqueId() + ".vanished")
						&& ranks.getInt(getRankID(target.getUniqueId()) + ".rank") < ranks
								.getInt(getRankID(player.getUniqueId()) + ".rank")) {
					target.hidePlayer(player);
				}
				if (data.getBoolean("Users." + target.getUniqueId() + ".vanished")
						&& ranks.getInt(getRankID(player.getUniqueId()) + ".rank") < ranks
								.getInt(getRankID(target.getUniqueId()) + ".rank")) {
					player.hidePlayer(target);
				}
			} else {
				player.hidePlayer(target);
				target.hidePlayer(player);
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL
				&& (event.getPlayer().getWorld().getName().contains("EVENT-")
						|| event.getPlayer().getWorld().getName().contains("Lobby-"))) {
			if (!Boolean.valueOf(settings.get("itemDrop")))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL
				&& (event.getPlayer().getWorld().getName().contains("EVENT-")
						|| event.getPlayer().getWorld().getName().contains("Lobby-"))) {
			if (!Boolean.valueOf(settings.get("itemPickup")))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (frozen.containsKey(player))
			player.teleport(frozen.get(player));
		setHeaderFooter(event.getPlayer(), header + event.getPlayer().getWorld().getName(),
				"Visit &awww.mineplex.com &rfor News, Forums and Shop");
		if (party.contains(player.getName())) {
			for (String res : party.getConfigurationSection(player.getName()).getKeys(false)) {
				for (Player target : Bukkit.getOnlinePlayers()) {
					if (target.getName() == res) {
						target.teleport(player.getLocation());
					}
				}
			}
		}
		refresh = true;
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.getWorld().equals(player.getWorld())) {
				if (data.getBoolean("Users." + player.getUniqueId() + ".vanished")
						&& ranks.getInt(getRankID(target.getUniqueId()) + ".rank") < ranks
								.getInt(getRankID(player.getUniqueId()) + ".rank")) {
					target.hidePlayer(player);
				} else {
					target.showPlayer(player);
				}
				if (data.getBoolean("Users." + target.getUniqueId() + ".vanished")
						&& ranks.getInt(getRankID(player.getUniqueId()) + ".rank") < ranks
								.getInt(getRankID(target.getUniqueId()) + ".rank")) {
					player.hidePlayer(target);
				} else {
					player.showPlayer(target);
				}
			} else {
				player.hidePlayer(target);
				target.hidePlayer(player);
			}
		}
		if (!event.getFrom().getName().equals("world") && !ignoreJoin.containsKey(player))
			tell(player, "&9Portal> &7You have been sent from &6" + event.getFrom().getName() + " &7to &6"
					+ player.getWorld().getName());
		if (data.getBoolean("Users." + player.getUniqueId() + ".vanished")) {
			tell(player, "");
			tell(player, "&6&lYou are currently incognito");
			tell(player, "");
		}
		if (player.getWorld().getName().contains("Lobby-")
				|| data.getBoolean("Users." + player.getUniqueId() + ".vanished")) {
			player.setAllowFlight(true);
		} else {
			player.setFlying(false);
			player.setAllowFlight(false);
		}
		if (!event.getFrom().getName().contains("Lobby-")
				&& !data.getBoolean("Users." + player.getUniqueId() + ".vanished") && !ignoreJoin.containsKey(player)
				&& config.getBoolean("Features.lobbies")) {
			tellPlayers("&8Quit> &7" +teamCol(getTeam(player)) + player.getName(), event.getFrom().getName());
		}
		if (!player.getWorld().getName().contains("Lobby-")
				&& !data.getBoolean("Users." + player.getUniqueId() + ".vanished") && !ignoreJoin.containsKey(player)
				&& config.getBoolean("Features.lobbies"))
			tellPlayers("&8Join> &7" + player.getName(), player.getWorld().getName());
		if(!player.getWorld().getName().contains("Lobby-")) {
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
		if (player.getWorld().getName().contains("Backup-") && !getRankID(player.getUniqueId()).equals("owner")) {
			tell(player, "&9Portal> &7&cYou do not have permission to access &e" + player.getWorld().getName() + "&7.");
			player.teleport(event.getFrom().getSpawnLocation());
		}
		if (player.getWorld().getName().matches("(Staff-1|Test-1|CUST-1|Backup-1|world|world_end|world_the_nether)")
				&& !staff.contains(getRankID(player.getUniqueId()))) {
			tell(player, "&9Portal> &7&cYou do not have permission to access &e" + player.getWorld().getName() + "&7.");
			player.teleport(event.getFrom().getSpawnLocation());
		}
		Double locX[] = { 1.0, 3.0, 3.0, 1.0, -1.0, -3.0, -3.0, -1.0 };
		Double locZ[] = { -3.0, -1.0, 1.0, 3.0, 3.0, 1.0, -1.0, -3.0 };
		if (chestProg.containsKey(player)) {
			event.getFrom().getBlockAt(chestOpening.get(player)).setType(Material.CHEST);
			for (int ii = 0; ii < locX.length; ii++) {
				Location loc = chestOpening.get(player);
				event.getFrom().getBlockAt(loc.add(locX[ii], 0, locZ[ii])).setType(Material.AIR);
				loc.subtract(locX[ii], 0, locZ[ii]);
			}
			chestProg.remove(player);
			chestType.remove(player);
			chestOpening.remove(player);
			toggleHolo(event.getFrom().getSpawnLocation(), holoID.get(player), true);
		}
		for (String tres : Main.gadget.getKeys(false)) {
			if (tres != "Users") {
				if (GadgetManager.activeGadget(player) == tres)
					GadgetManager.toggleGadget(player, tres);
			}
		}
		player.setGameMode(GameMode.SURVIVAL);
		hubItems(player);
		ignoreJoin.remove(player);
		refreshTab();
	}

	public void setHeaderFooter(Player player, String head, String foot) {
		if (config.getBoolean("Features.customtab")) {
			IChatBaseComponent bottom = ChatSerializer.a("{text: '" + color(foot) + "'}");
			IChatBaseComponent top = ChatSerializer.a("{text: '" + color(head) + "'}");
			PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
			try {
				Field header = packet.getClass().getDeclaredField("a");
				Field footer = packet.getClass().getDeclaredField("b");
				header.setAccessible(true);
				footer.setAccessible(true);
				header.set(packet, top);
				footer.set(packet, bottom);
				header.setAccessible(false);
				footer.setAccessible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(null);
		defBoard.resetScores(event.getPlayer().getWorld().getName());
		DragonManager.removeBar(event.getPlayer());
		data.set("Users." + player.getUniqueId() + ".team", null);
		Double locX[] = { 1.0, 3.0, 3.0, 1.0, -1.0, -3.0, -3.0, -1.0 };
		Double locZ[] = { -3.0, -1.0, 1.0, 3.0, 3.0, 1.0, -1.0, -3.0 };
		if (chestProg.containsKey(player)) {
			player.getWorld().getBlockAt(chestOpening.get(player)).setType(Material.CHEST);
			for (int ii = 0; ii < locX.length; ii++) {
				Location loc = chestOpening.get(player);
				player.getWorld().getBlockAt(loc.add(locX[ii], 0, locZ[ii])).setType(Material.AIR);
				loc.subtract(locX[ii], 0, locZ[ii]);
			}
			chestProg.remove(player);
			chestType.remove(player);
			chestOpening.remove(player);
			toggleHolo(player.getLocation(), holoID.get(player), true);
		}
		if (!player.getWorld().getName().contains("Lobby-")
				&& !data.getBoolean("Users." + player.getUniqueId() + ".vanished")) {
			tellPlayers("&8Quit> &7"+teamCol(getTeam(player)) + player.getName(), player.getWorld().getName());
		}
		for (String tres : Main.gadget.getKeys(false)) {
			if (tres != "Users") {
				if (GadgetManager.activeGadget(player) == tres)
					GadgetManager.toggleGadget(player, tres);
			}
		}
	}

	public static String getRank(UUID uuid) {
		if (data.contains("Users." + uuid + ".rank")
				&& ranks.getString(data.getString("Users." + uuid + ".rank") + ".prefix") != null) {
			return ranks.getString(data.getString("Users." + uuid + ".rank") + ".prefix");
		}
		return "default";
	}

	public static int getLevel(UUID uuid) {
		return (int) Math.floor(data.getDouble("Users." + uuid + ".level"));
	}

	public static int getRankLevel(UUID uuid) {
		if (data.contains("Users." + uuid + ".rank")) {
			return ranks.getInt(data.getString("Users." + uuid + ".rank") + ".rank");
		}
		return 0;
	}

	public static String getRankID(UUID uuid) {
		if (data.contains("Users." + uuid + ".rank")) {
			return data.getString("Users." + uuid + ".rank");
		}
		return "default";
	}

	public static Boolean getPref(UUID uuid, String pref) {
		return data.getBoolean("Users." + uuid + ".notify." + pref);
	}

	public static String getCol(int level) {
		if (level < 0)
			return "&8";
		if (level < 20)
			return "&7";
		if (level < 40)
			return "&9";
		if (level < 60)
			return "&2";
		if (level < 80)
			return "&6";
		if (level <= 100)
			return "&c";
		return "&4";
	}

	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static boolean isNumber(String string) {
		return string != null && string.matches("[-+]?\\d*\\.?\\d+");
	}

	public static String camelCase(String string) {
		String prevChar = " ";
		String res = "";
		for (int i = 0; i < string.length(); i++) {
			if (i > 0)
				prevChar = string.charAt(i - 1) + "";
			if (!prevChar.matches("[a-zA-Z]")) {
				res = res + ((string.charAt(i) + "").toUpperCase());
			} else {
				res = res + ((string.charAt(i) + "").toLowerCase());
			}
		}
		return res;
	}

	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (item != null && item.getType() != Material.AIR) {
			if (item.getType() == Material.INK_SACK) {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 2));
			}
		}
		addXp(event.getPlayer(), 0.0001f);
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL
				&& event.getPlayer().getWorld().getName().contains("EVENT-"))
			if (!Boolean.valueOf(settings.get("blockBreak")))
				event.setCancelled(true);
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL
				&& event.getPlayer().getWorld().getName().contains("EVENT-"))
			if (!Boolean.valueOf(settings.get("blockPlace")))
				event.setCancelled(true);
	}

	@EventHandler
	public void entityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (causes.containsKey(event.getCause()) && !causes.get(event.getCause())
					&& event.getEntity().getWorld().getName().contains("EVENT-"))
				event.setCancelled(true);
			if (!lastDamage.containsKey(player)) {
				lastDamage.put(player, camelCase(event.getCause().name().replace("_", "")));
				if (event.getCause() == DamageCause.BLOCK_EXPLOSION)
					lastDamage.put(player, "Explosion");
				if (event.getCause() == DamageCause.ENTITY_EXPLOSION)
					lastDamage.put(player, "Explosion");
				if (event.getCause() == DamageCause.FALLING_BLOCK)
					lastDamage.put(player, "Falling Block");
			}
		}
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			addXp((Player) event.getWhoClicked(), 0.005f);
		}
	}

	@EventHandler
	public void onAttackEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (!Boolean.valueOf(settings.get("pve")) && event.getEntity().getType() != EntityType.PLAYER
					&& (event.getEntity().getWorld().getName().contains("EVENT-")
							|| event.getEntity().getWorld().getName().contains("Lobby-")))
				event.setCancelled(true);
			if (!Boolean.valueOf(settings.get("pvp")) && event.getEntity().getType() == EntityType.PLAYER
					&& (event.getEntity().getWorld().getName().contains("EVENT-")
							|| event.getEntity().getWorld().getName().contains("Lobby-")))
				event.setCancelled(true);
			addXp((Player) event.getDamager(), 0.00025f);
		}
		if (event.getEntity() instanceof Player) {
			if (!Boolean.valueOf(settings.get("evp")) && event.getDamager().getType() != EntityType.PLAYER
					&& (event.getEntity().getWorld().getName().contains("EVENT-")
							|| event.getEntity().getWorld().getName().contains("Lobby-")))
				event.setCancelled(true);
			if (event.getDamager().getType() == EntityType.PLAYER) {
				Player killer = (Player) event.getDamager();
				ItemStack hand = killer.getItemInHand();
				if (hand.getType() != Material.AIR) {
					if (hand.getItemMeta().hasDisplayName()) {
						lastDamage.put((Player) event.getEntity(), teamCol(getTeam((Player) event.getDamager()))+event.getDamager().getName() + " &7with &6"
								+ ChatColor.stripColor(color(hand.getItemMeta().getDisplayName().trim())));
					} else {
						lastDamage.put((Player) event.getEntity(), teamCol(getTeam((Player) event.getDamager()))+event.getDamager().getName() + " &7with &e"
								+ camelCase((hand.getType() + "").replace("_", " ")));
					}
				} else {
					lastDamage.put((Player) event.getEntity(), teamCol(getTeam((Player) event.getDamager()))+event.getDamager().getName());
				}
			} else {
				if (event.getDamager().getType() == EntityType.ARROW) {
					Arrow arrow = (Arrow) event.getDamager();
					ProjectileSource source = arrow.getShooter();
					if (source instanceof Player) {
						Player shooter = (Player) arrow.getShooter();
						lastDamage.put((Player) event.getEntity(), teamCol(getTeam(shooter))+shooter.getName() + " &7with &eArchery");
					} else {
						lastDamage.put((Player) event.getEntity(),
								source.toString().replace("Craft", "") + " &7with &eArchery");
					}
				} else {
					lastDamage.put((Player) event.getEntity(), camelCase(event.getDamager().getType() + ""));
				}
			}
		}
	}
	
	public static void sendDeath(Player player) {
		String msg = "";
		if (lastDamage.containsKey(player)) {
			msg = (color("&9Death> &e" + teamCol(getTeam(player))+player.getName() + " &7killed by &e"
					+ lastDamage.get(player) + "&7."));
		} else {
			msg = (color("&9Death> &e" +teamCol(getTeam(player))+ player.getName() + " &7killed by &eUnknown&7."));
		}
		for (Player target : player.getWorld().getPlayers()) {
			tell(target, msg);
		}
		lastDamage.remove(player);
	}

	@EventHandler
	public void onCollectXP(PlayerExpChangeEvent event) {
		addXp(event.getPlayer(), 0.0005f);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (event.getEntity().getWorld().getName().contains("Lobby-"))
			event.setKeepInventory(true);
		event.setDeathMessage(null);
		String msg = "";
		if (lastDamage.containsKey(event.getEntity())) {
			msg = (color("&9Death> &e" + teamCol(getTeam(event.getEntity()))+event.getEntity().getName() + " &7killed by &e"
					+ lastDamage.get(event.getEntity()) + "&7."));
		} else {
			msg = (color("&9Death> &e" +teamCol(getTeam(event.getEntity()))+ event.getEntity().getName() + " &7killed by &eUnknown&7."));
		}
		for (Player target : event.getEntity().getWorld().getPlayers()) {
			tell(target, msg);
		}
		lastDamage.remove(event.getEntity());
	}

	@EventHandler
	public void onLand(ProjectileHitEvent event) {
		String name = event.getEntity().getCustomName();
		Location loc = event.getEntity().getLocation();
		if (name != null) {
			if (name.contains("pearl")) {
				Firework fire = (Firework) loc.getWorld().spawnEntity(loc.subtract(0, .5, 0), EntityType.FIREWORK);
				FireworkMeta fwm = fire.getFireworkMeta();
				Random r = new Random();
				Type type = Type.BALL;
				int rc1 = 128;
				int rg1 = 0;
				int rb1 = 128;
				FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean())
						.withColor(Color.fromRGB(rc1, rg1, rb1)).withFade(Color.fromRGB(rc1, rg1, rb1)).with(type)
						.trail(r.nextBoolean()).build();
				fwm.addEffect(effect);
				fwm.setPower(0);
				fire.setFireworkMeta(fwm);
			}

			if (name.contains("paintball")) {
				event.getEntity().getWorld().playSound(loc, Sound.ITEM_PICKUP, 1, 1);
				for (int i = 0; i < 50; i++)
					sendParticle(EnumParticle.FIREWORKS_SPARK, loc.add(rnd(-1.0, 1.0), rnd(-1.0, 1.0), rnd(-1.0, 1.0)),
							1, new Vector(), (float) 0);
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		String name = event.getEntity().getCustomName();
		if (name != null && name.contains("throwingtnt")) {
			for (Entity ent : event.getEntity().getNearbyEntities(5, 5, 5)) {
				if (ent.getType() == EntityType.PLAYER) {
					if (!data.getBoolean("Users." + ((Player) ent).getUniqueId() + ".notify.kb"))
						ClientManager.kbEntity(ent, event.getLocation());
				}
			}
			Location loc = event.getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 1, false, false);
			event.setCancelled(true);
		}
		if ((!Boolean.valueOf(settings.get("explosions"))&&(event.getEntity().getWorld().getName().contains("Lobby-")||event.getEntity().getWorld().getName().contains("EVENT-")))
				|| event.getLocation().getWorld().getName().contains("Backup-"))
			event.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClickItem(InventoryClickEvent event) {
		String prefix = "&9Portal> &7";
		Player player = (Player) event.getWhoClicked();
		if (player.getGameMode() != GameMode.CREATIVE && player.getWorld().getName().contains("Lobby-"))
			event.setCancelled(true);
		if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
			ItemStack item = event.getCurrentItem();
			String name = item.getItemMeta().getDisplayName();
			if (name != null && name.contains("Server") && item.getType() == Material.EMERALD_BLOCK) {
				event.setCancelled(true);
				Integer worldNum = Integer.valueOf(name.substring(9));
				if (player.getWorld().getName() == Bukkit.getWorld("Lobby-" + worldNum).getName()) {
					tell(player, prefix + "You are already on &6" + player.getWorld().getName() + "&7!");
					return;
				}
				player.teleport(Bukkit.getWorld("Lobby-" + worldNum).getSpawnLocation());
			}
			if (event.getInventory().getName().contains("Game Menu")) {
				for (String res : guis.getConfigurationSection("GameMenu").getKeys(false)) {
					if (guis.contains("GameMenu."+res+".icon")) {
						HashMap<World, Integer> worldList = new HashMap<World, Integer>();
						if (name.equals(color(guis.getString("GameMenu."+res + ".name")))) {
							for (World world : Bukkit.getWorlds()) {
								if (world.getName().toLowerCase().contains(res.toLowerCase())) {
									if(world!=player.getWorld())
									worldList.put(world, worldList.size()+1);
								}
							}
							World tempWorld = null;
							int max = -1;
							for (World world : worldList.keySet()) {
								if (world.getPlayers().size() > max) {
									max = world.getPlayers().size();
									tempWorld = world;
								}
							}
							if(tempWorld!=null)
							player.teleport(tempWorld.getSpawnLocation());
						}
					}
				}
			}
			if (event.getInventory().getName().contains("Purchase ")) {
				ItemStack tempItem = null;
				ItemMeta tempMeta = null;
				if (name.contains("Ok")) {
					if (cost.containsKey(player)) {
						if (cosmAmo(player, costType.get(player)) >= cost.get(player)) {
							addCosmetic(player, costType.get(player), -cost.get(player));
							addCosmetic(player, purchasing.get(player), costAmo.get(player));
							tempItem = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 5);
							tempMeta = tempItem.getItemMeta();
							tempMeta.setDisplayName(color("&a&lPurchase Succesful"));
						} else {
							tempItem = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 14);
							tempMeta = tempItem.getItemMeta();
							tempMeta.setDisplayName(color("&c&lInsufficient Funds"));
						}
					} else {
						tempItem = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 14);
						tempMeta = tempItem.getItemMeta();
						tempMeta.setDisplayName(color("&c&lPurchase Failed"));
					}
				}
				if (name.contains("Cancel")) {
					tempItem = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 14);
					tempMeta = tempItem.getItemMeta();
					tempMeta.setDisplayName(color("&c&lPurchase Canceled"));
				}
				if (tempItem != null) {
					tempItem.setItemMeta(tempMeta);
					event.getInventory().clear();
					for (int i = 0; i < event.getInventory().getSize(); i++) {
						event.getInventory().setItem(i, tempItem);
					}
				}
				event.setCancelled(true);
			}
			if (event.getInventory().getName().contains("Cosmetics")) {
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case (49):
					cosmeticInventory(player, "gadget");
					break;
				}
			}
			prefix = "&9Treasure> &7";
			if (event.getInventory().getName().contains("Open Treasure - Page 1")) {
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case (53):
					GUIManager.makeInventory(player, "ChestMenu2");
					break;
				case (19):
					openChest(player, "old");
					break;
				case (21):
					openChest(player, "ancient");
					break;
				case (23):
					openChest(player, "mythical");
					break;
				case (25):
					openChest(player, "illuminated");
					break;
				case (38):
					openChest(player, "omega");
					break;
				case (40):
					openChest(player, "minestrike");
					break;
				case (42):
					openChest(player, "hog");
					break;
				}
			}
			if (event.getInventory().getName().contains("Open Treasure - Page 2")) {
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case (45):
					GUIManager.makeInventory(player, "ChestMenu1");
					break;
				}
			}
			if (event.getInventory().getName().contains("Your Communities")) {
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case (1):
					break;
				case (4):
					openCom(player, "browse");
					break;
				case (7):
					openCom(player, "invites");
					break;
				default:
					openCom(player, ChatColor.stripColor(name));
					break;
				}
			}
			if (event.getInventory().getName().contains("My Preferences")) {
				prefix = "&9Preferences> &7";
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case (10):
					if (getPref(player.getUniqueId(), "chatNotify")) {
						tell(player, prefix + "You will no longer receive notifications about chat.");
					} else {
						tell(player, prefix + "You will now receive notifications about chat.");
					}
					data.set("Users." + player.getUniqueId() + ".notify.chatNotify",
							!data.getBoolean("Users." + player.getUniqueId() + ".notify.chatNotify"));
					player.closeInventory();
					break;
				case (13):
					if (getPref(player.getUniqueId(), "pm")) {
						tell(player, prefix + "Private messaging is now disabled.");
					} else {
						tell(player, color(prefix + "Private messaging is now enabled."));
					}
					data.set("Users." + player.getUniqueId() + ".notify.pm",
							!data.getBoolean("Users." + player.getUniqueId() + ".notify.pm"));
					player.closeInventory();
					break;
				case (16):
					if (getPref(player.getUniqueId(), "joinNotify")) {
						tell(player, color(prefix + "You will no longer receive notifications about others joining."));
					} else {
						tell(player, color(prefix + "You will now receive notifications about others joining."));
					}
					data.set("Users." + player.getUniqueId() + ".notify.joinNotify",
							!data.getBoolean("Users." + player.getUniqueId() + ".notify.joinNotify"));
					player.closeInventory();
					break;
				case (24):
					if (getPref(player.getUniqueId(), "kb")) {
						tell(player, color(prefix + "Lobby Knockback is enabled."));
					} else {
						tell(player, color(prefix + "Lobby Knockback is disabled."));
					}
					data.set("Users." + player.getUniqueId() + ".notify.kb",
							!data.getBoolean("Users." + player.getUniqueId() + ".notify.kb"));
					player.closeInventory();
					break;
				case (18):
					if (getPref(player.getUniqueId(), "globalGWEN")) {
						tell(player, color(prefix + "Global GWEN is enabled."));
					} else {
						tell(player, color(prefix + "Global GWEN is disabled."));
					}
					data.set("Users." + player.getUniqueId() + ".notify.globalGWEN",
							!getPref(player.getUniqueId(), "globalGWEN"));
					player.closeInventory();
					break;
				}
			}
			if (event.getInventory().getName().contains("Gadgets")) {
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case (4):
					GUIManager.makeInventory(player, "CosmeticMenu");
					return;
				case (12):
					GadgetManager.toggleGadget(player, "throwingtnt");
					break;
				case (10):
					GadgetManager.toggleGadget(player, "epearl");
					break;
				case (11):
					GadgetManager.toggleGadget(player, "fireworkgadget");
					break;
				case (15):
					GadgetManager.toggleGadget(player, "paintball");
					break;
				}
				cosmeticInventory(player, "gadget");
			}
			prefix = "&9Punish> &7";
			if (event.getInventory().getName().contains("Punish")) {
				OfflinePlayer target = Bukkit.getOfflinePlayer(punishing.get(event.getWhoClicked()));
				event.setCancelled(true);
				if (!reason.containsKey(player) || reason.get(player).trim().equalsIgnoreCase("checking")) {
					player.closeInventory();
					tell(player, prefix + "Please supply a reason.");
					return;
				}
				if (event.getClick() == ClickType.LEFT) {
					switch (event.getRawSlot()) {
					case (19):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"chatoffense", 1);
						player.closeInventory();
						break;
					case (28):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"chatoffense", 2);
						player.closeInventory();
						break;
					case (37):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"chatoffense", 3);
						player.closeInventory();
						break;
					case (21):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"exploiting", 1);
						player.closeInventory();
						break;
					case (23):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"hacking", 1);
						player.closeInventory();
						break;
					case (32):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"hacking", 2);
						player.closeInventory();
						break;
					case (41):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"hacking", 3);
						player.closeInventory();
						break;
					case (25):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"warning", 1);
						player.closeInventory();
						break;
					case (34):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"other", 4);
						player.closeInventory();
						break;
					case (43):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"chatoffense", 4);
						player.closeInventory();
						break;
					case (0):
						data.set("Users." + target.getUniqueId() + ".history", null);
						player.closeInventory();
						break;
					case (8):
						addPunish(target.getName(), event.getWhoClicked().getName(), reason.get(event.getWhoClicked()),
								"ipban", 4);
						int pos = 0;
						while (data.contains("Users.ipban." + pos)) {
							pos++;
						}
						data.set("Users.ipban." + pos + ".ip", getIP(target));
						data.set("Users.ipban." + pos + ".reason", reason.get(event.getWhoClicked()));
						player.closeInventory();
						break;
					default:
						break;
					}
				}
				if (event.getClick() == ClickType.RIGHT) {
					if (event.getSlot() >= 45) {
						int num = event.getSlot() - 45;
						HashMap<Integer, String> his = new HashMap<Integer, String>();
						ConfigurationSection history = data
								.getConfigurationSection("Users." + target.getUniqueId() + ".history");
						if (history == null)
							data.createSection("Users." + target.getUniqueId() + ".history");
						int pos = 1;
						for (String res : history.getKeys(false)) {
							his.put(history.getKeys(false).size() - pos, res);
							pos++;
						}
						String res = his.get(num);
						if (history.contains(res + ".unbanner") || history.getString(res + ".type").equals("warning")
								|| reason.get(player).equalsIgnoreCase("checking"))
							return;
						if (history.getString(res + ".type").equals("ipban")
								&& data.getConfigurationSection("Users.ipban") != null) {
							for (String resip : data.getConfigurationSection("Users.ipban").getKeys(false)) {
								if (data.getString("Users.ipban." + resip + ".ip")
										.equals(getIP(Bukkit.getOfflinePlayer(punishing.get(player))))) {
									data.set("Users.ipban." + resip, null);
								}
							}
						}
						history.set(res + ".unbanner", player.getName());
						history.set(res + ".unbanreason", reason.get(player));
						history.set(res + ".duration", 0);
						player.closeInventory();
					}
				}
				if (event.getClick() == ClickType.SHIFT_RIGHT) {
					if (ranks.getInt(getRankID(event.getWhoClicked().getUniqueId()) + ".rank") < 17)
						return;
					if (event.getSlot() >= 45) {
						int num = event.getSlot() - 45;
						HashMap<Integer, String> his = new HashMap<Integer, String>();
						ConfigurationSection history = data
								.getConfigurationSection("Users." + target.getUniqueId() + ".history");
						if (history == null)
							data.createSection("Users." + target.getUniqueId() + ".history");
						int pos = 1;
						for (String res : history.getKeys(false)) {
							his.put(history.getKeys(false).size() - pos, res);
							pos++;
						}
						String res = his.get(num);
						if (history.getString(res + ".type").equals("ipban")
								&& data.getConfigurationSection("Users.ipban") != null) {
							for (String resip : data.getConfigurationSection("Users.ipban").getKeys(false)) {
								if (data.getString("Users.ipban." + resip + ".ip")
										.equals(getIP(Bukkit.getOfflinePlayer(punishing.get(player))))) {
									data.set("Users.ipban." + resip, null);
								}
							}
						}
						history.set(res, null);
						player.closeInventory();
					}
				}
				saveData();
			}
		}
	}

	public void openChest(Player player, String type) {
		Material chest = Material.AIR;
		EnumParticle effect = EnumParticle.SMOKE_NORMAL;
		String name = null;
		int cost = 0;
		switch (type) {
		case "old":
			cost = 1000;
			break;
		case "ancient":
			name = "an &6Ancient Chest";
			effect = EnumParticle.BARRIER;
			cost = 5000;
			break;
		case "mythical":
			chest = Material.ENDER_CHEST;
			effect = EnumParticle.FIREWORKS_SPARK;
			name = "a &cMythical Chest";
			cost = 20000;
			break;
		case "illuminated":
			effect = EnumParticle.ENCHANTMENT_TABLE;
			chest = Material.SEA_LANTERN;
			name = "an &bIlluminated Chest";
			cost = -1;
			break;
		case "minestrike":
			chest = Material.TNT;
			effect = EnumParticle.PORTAL;
			name = "a &6Minestrike Chest";
			cost = 10000;
			break;
		case "omega":
			effect = EnumParticle.FLAME;
			chest = Material.DIAMOND_BLOCK;
			name = "an &bOmega Chest";
			cost = -1;
			break;
		case "hog":
			effect = EnumParticle.NOTE;
			chest = Material.AIR;
			name = "a &bHeroes of GWEN Chest";
			cost = -1;
			break;
		}
		if (cosmAmo(player, type + "chests") < 1) {
			GUIManager.makePurchase(player, type + "chests", cost, 1, "shards", Material.CHEST);
			return;
		}
		addCosmetic(player, type + "chests", -1);
		Location loc = chestOpening.get(player);
		player.teleport(loc);
		chestOpening.put(player, loc);
		chestProg.put(player, 0.0);
		chestType.put(player, chest);
		particleType.put(player, effect);

		if (name != null)
			tellPlayers("&9Treasure> &e" + player.getName() + " &7is opening " + name + "&7.",
					loc.getWorld().getName());
	}

	public void sendParticle(EnumParticle particle, Location loc, int amo, Vector dir, float spd) {
		for (Player player : loc.getWorld().getPlayers()) {
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(),
					(float) loc.getY(), (float) loc.getZ(), spd, (float) dir.getX(), (float) dir.getY(),
					(float) dir.getZ(), amo);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void refreshTab() {
		for (Player rtarget : Bukkit.getOnlinePlayers()) {
			if (config.getBoolean("Features.ranks")) {
				if(rtarget.getWorld().getName().contains("Lobby-")) {
					rtarget.setDisplayName(color(getRank(rtarget.getUniqueId()) + "&e") + rtarget.getName());
				}else {
					rtarget.setDisplayName(color(getRank(rtarget.getUniqueId()) + "&7") + rtarget.getName());
				}
				rtarget.setPlayerListName(color(getRank(rtarget.getUniqueId()) + "&r") + rtarget.getName());
			}
		}
	}

	public void refreshPerms(Player target) {
		for (String res : ranks.getKeys(false)) {
			if (getRankLevel(target.getUniqueId()) >= ranks.getInt(res + ".rank")) {
				target.addAttachment(this, "MSWSplex.rank." + res, true);
			} else {
				target.addAttachment(this, "MSWSplex.rank." + res, false);
			}
		}
	}

	public static void tellPlayers(String msg, String world) {
		if (world == "global") {
			for (Player target : Bukkit.getOnlinePlayers())
				target.sendMessage(color(msg));
		} else {
			for (Player target : Bukkit.getWorld(world).getPlayers())
				target.sendMessage(color(msg));
		}
	}

	public static void tellStaff(String msg) {
		for (Player target : Bukkit.getOnlinePlayers())
			if (staff.contains(getRankID(target.getUniqueId())))
				target.sendMessage(color(msg));
	}

	public static void tellStaff(String msg, String world) {
		for (Player target : Bukkit.getWorld(world).getPlayers())
			if (staff.contains(getRankID(target.getUniqueId())))
				target.sendMessage(color(msg));
	}

	public static void copyWorld(File source, File target) {
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if (!ignore.contains(source.getName())) {
				if (source.isDirectory()) {
					if (!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyWorld(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {

		}
	}

	@EventHandler
	public void onEntityInteract(PlayerInteractAtEntityEvent event) {
		Player player = (Player) event.getPlayer();
		if (config.getBoolean("Features.guis")) {
			if (event.getRightClicked().getName().contains("Open Treasure")
					&& event.getRightClicked().isCustomNameVisible()) {
				GUIManager.makeInventory(player, "ChestMenu1");
				chestOpening.put(player, event.getRightClicked().getLocation().add(0, 1, 0));
				holoID.put(player, getHoloID(event.getRightClicked().getName()));
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (config.getBoolean("Features.guis")) {
			if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
				String name = "";
				ItemStack item = event.getItem();
				if (item.getItemMeta().hasDisplayName())
					name = item.getItemMeta().getDisplayName();
				Inventory gui;
				if (name.contains("Server Selector")) {
					GUIManager.makeInventory(player, "GameMenu");
					event.setCancelled(true);
				}
				if (GadgetManager.activeGadget(player) != null
						&& name.contains(GadgetManager.gadgetName(GadgetManager.activeGadget(player)))) {
					GadgetManager.useGadget(player, GadgetManager.activeGadget(player));
					event.setCancelled(true);
				}
				if (name.contains("Lobby Selector")) {
					gui = Bukkit.createInventory(null, 54, color("          &nLobby Selector"));
					for (int i = 0; i < gui.getSize(); i++) {
						List<String> lore = new ArrayList<String>();
						if (Bukkit.getWorld("Lobby-" + (i + 1)) != null) {
							int amo = 0;
							for (Player target : Bukkit.getWorld("Lobby-" + (i + 1)).getPlayers()) {
								if (!data.getBoolean("Users." + target.getUniqueId() + ".vanished"))
									amo++;
							}
							lore.add("");
							lore.add(color("&r&ePlayers: &r" + amo) + "/80");
							lore.add("");
							lore.add(color("&r&nClick to join!"));
						} else {
							continue;
						}
						ItemStack lobby = new ItemStack(Material.EMERALD_BLOCK, i + 1);
						ItemMeta meta = lobby.getItemMeta();
						meta.setDisplayName(color("&rServer " + (i + 1)));
						meta.setLore(lore);
						lobby.setItemMeta(meta);
						gui.setItem(i, lobby);
					}
					player.openInventory(gui);
					event.setCancelled(true);
				}
				if (name.contains("Return to Hub")) {
					int lobbyNum = 0;
					List<World> lobbies = new ArrayList<World>();
					for (World world : Bukkit.getWorlds()) {
						if (world.getName().contains("Lobby-")) {
							lobbies.add(world);
						}
					}
					if (lobbies.size() > 1)
						lobbyNum = (int) Math.floor(Math.random() * lobbies.size());

					World lobby = lobbies.get(lobbyNum);
					player.teleport(lobby.getSpawnLocation());
				}
				if (name.contains("Cosmetic Menu")) {
					GUIManager.makeInventory(player, "CosmeticMenu");
					event.setCancelled(true);
				}
				if (name.contains("Parties")) {
					GUIManager.makeInventory(player, "PartyMenu");
					event.setCancelled(true);
				}
				if (name.contains("My Profile")) {
					GUIManager.makeInventory(player, "PrefMenu");
					event.setCancelled(true);
				}
				
				if(name.contains("Join another game!")) {
					HashMap<World, Integer> worldList = new HashMap<World, Integer>();
						for (World world : Bukkit.getWorlds()) {
							if (world.getName().toLowerCase().contains(player.getWorld().getName().toLowerCase())) {
								if(world!=player.getWorld())
								worldList.put(world, worldList.size()+1);
							}
						}
						World tempWorld = null;
						int max = -1;
						for (World world : worldList.keySet()) {
							if (world.getPlayers().size() > max) {
								max = world.getPlayers().size();
								tempWorld = world;
							}
						}
						if(tempWorld!=null) {
							player.teleport(tempWorld.getSpawnLocation());	
						}else {
							player.sendMessage(color("&9Portal> &7There are no good servers to send you to!"));
						}
						player.setItemInHand(new ItemStack(Material.AIR));
				}
			}
		}
	}

	public static void addPunish(String punished, String punisher, String reason, String type, Integer severity) {
		AddPunish.addPunish(punished, punisher, reason, type, severity);
	}

	public static void openCom(Player player, String comName) {
		Inventory inv;
		ConfigurationSection cCom = com.getConfigurationSection(comName);
		if (!comPage.containsKey(player))
			comPage.put(player, 1);
		activeCom.put(player, comName);
		if (comName == "main") {
			inv = GUIManager.getInventory(player, "CommunityMain");
			int pos = 0;
			for (String res : com.getKeys(false)) {
				if (com.contains(res + ".player." + player.getUniqueId())) {
					ItemStack comIcon = new ItemStack(Material.valueOf(com.getString(res + ".setting.icon")));
					ItemMeta comMeta = comIcon.getItemMeta();
					comMeta.setDisplayName(color("&r&a&l" + res));
					List<String> tempLore = new ArrayList<String>();
					tempLore.add("");
					tempLore.add(color(
							"&r&eMembers &r" + com.getConfigurationSection(res + ".player").getKeys(false).size()));
					tempLore.add(color("&r&eFavorite Game &r" + com.getString(res + ".setting.faveGame")));
					tempLore.add(color("&r&eDescription &r" + com.getString(res + ".setting.description")));
					tempLore.add("");
					tempLore.add(color("&r&aClick to view community"));
					comMeta.setLore(tempLore);
					comIcon.setItemMeta(comMeta);
					if (pos <= comPage.get(player) * 54) {
						inv.setItem((18 + pos) % 54, comIcon);
					}
					pos++;
				}
			}
		} else if (comName == "browse") {
			inv = GUIManager.getInventory(player, "CommunityBrowse");
			int pos = 0;
			for (String res : com.getKeys(false)) {
				ItemStack comIcon = new ItemStack(Material.valueOf(com.getString(res + ".setting.icon")));
				ItemMeta comMeta = comIcon.getItemMeta();
				comMeta.setDisplayName(color("&r&a&l" + res));
				List<String> tempLore = new ArrayList<String>();
				tempLore.add("");
				tempLore.add(
						color("&r&eMembers &r" + com.getConfigurationSection(res + ".player").getKeys(false).size()));
				tempLore.add(color("&r&eFavorite Game &r" + com.getString(res + ".setting.faveGame")));
				tempLore.add(color("&r&eDescription &r" + com.getString(res + ".setting.description")));
				tempLore.add("");
				tempLore.add(color("&r&aClick to view community"));
				comMeta.setLore(tempLore);
				comIcon.setItemMeta(comMeta);
				if (pos <= comPage.get(player) * 54) {
					inv.setItem((18 + pos) % 54, comIcon);
				}
				pos++;
			}

		} else if (comName == "invites") {
			inv = GUIManager.getInventory(player, "CommunityMenu");
		} else {
			inv = GUIManager.getInventory(player, "CommunityMenu");
			int pos = 0;
			for (String res : cCom.getConfigurationSection("player").getKeys(false)) {
				ItemStack skull = new ItemStack(Material.SKULL_ITEM);
				OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(res));
				if (target.isOnline())
					skull.setDurability((short) 3);
				SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
				skullMeta.setOwner(target.getName());
				List<String> tempLore = new ArrayList<String>();
				String rank = "Member";
				switch (getRank(target, comName)) {
				case 1:
					rank = "Co-Leader";
					break;
				case 2:
					rank = "Leader";
					break;
				}
				tempLore.add("");
				tempLore.add(color("&r&eRole &r" + rank));
				tempLore.add("");
				if (target.isOnline()) {
					skullMeta.setDisplayName(color("&r&a&l" + target.getName()));
					tempLore.add(color("&r&eServer &r" + ((Player) target).getWorld().getName()));
				} else {
					skullMeta.setDisplayName(color("&r&c&l" + target.getName()));
					tempLore.add(color("&r&7Last Seen " + TimeManagement.lastOn(target) + " Ago"));
				}
				if (getRank(player, comName) > getRank(target, comName)) {
					tempLore.add("");
					if (getRank(target, comName) < 1)
						tempLore.add(color("&r&eLeft Click &rPromote"));
					if (getRank(player, comName) == 2)
						tempLore.add(color("&r&eRight Click &rDemote"));
					tempLore.add(color("&r&eShift-Right Click &rKick"));
				}
				skullMeta.setLore(tempLore);
				skull.setItemMeta(skullMeta);
				if (pos <= comPage.get(player) * 54) {
					inv.setItem((18 + pos) % 54, skull);
				}
				pos++;
			}
		}
		player.openInventory(inv);
	}

	@SuppressWarnings("deprecation")
	public void cosmeticInventory(Player player, String inventory) {
		if (inventory.charAt(inventory.length() - 1) == 's')
			inventory = inventory.substring(0, inventory.length() - 1);
		ConfigurationSection parts = guis.getConfigurationSection(camelCase(inventory) + "Menu");
		Inventory inv = Bukkit.createInventory(null, parts.getInt(".size"), color(parts.getString(".name")));
		for (String res : parts.getKeys(false)) {
			if (parts.contains(res + ".icon")) {
				ItemStack item = new ItemStack(Material.valueOf(parts.getString(res + ".icon")));
				ItemMeta meta = item.getItemMeta();
				if (parts.contains(res + ".name"))
					meta.setDisplayName(cST(color(parts.getString(res + ".name")), player));
				List<String> tempLore = new ArrayList<String>();
				if (parts.contains(res + ".lore")) {
					for (String loreline : parts.getStringList(res + ".lore")) {
						tempLore.add(cST(color("&r" + loreline), player).replace("%amo%", cosmAmo(player, res) + ""));
					}
				}
				if (gadget.contains(res)) {
					if (GadgetManager.activeGadget(player) != null && GadgetManager.activeGadget(player).equals(res)) {
						tempLore.add(color("&aLeft-Click to Disable"));
					} else {
						tempLore.add(color("&aLeft-Click to Enable"));
					}
				}
				meta.setLore(tempLore);
				item.setItemMeta(meta);
				inv.setItem(parts.getInt(res + ".slot"), item);
			}
			ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 3);
			ItemMeta meta = glass.getItemMeta();
			meta.setDisplayName("");
			glass.setItemMeta(meta);
			if (parts.getBoolean("GlassPanes")) {
				for (int i = 0; i < inv.getSize(); i++) {
					if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)
						inv.setItem(i, glass);
				}
			}
		}
		player.openInventory(inv);
	}

	public static String cST(String string, Player player) {
		String[] reps = { "gems", "shards", "oldchests", "ancientchests", "mythicalchests", "illuminatedchests",
				"omegachests", "minestrikechests", "hogchests" };
		String res = string;
		for (String pos : reps) {
			res = res.replace("%" + pos + "%", cosmAmo(player, pos) + "");
		}
		int gadgown = 0;
		for (String gadget : Main.gadget.getKeys(false)) {
			if (cosmAmo(player, gadget) > 0) {
				gadgown++;
			}
		}
		res = res.replace("%player%", player.getName()).replace("%ping%", getPing(player) + "")
				.replace("%rank%", getRankID(player.getUniqueId()))
				.replace("%prefix%", color(getRank(player.getUniqueId())))
				.replace("%level%", getLevel(player.getUniqueId()) + "")
				.replace("%gadgetamo%", gadget.getKeys(false).size() - 1 + "").replace("%gadgetown%", gadgown + "");
		if (activeCom.containsKey(player) && !activeCom.get(player).matches("(main|invites|browse)")) {
			res = res.replace("%comName%", activeCom.get(player))
					.replace("%members%",
							com.getConfigurationSection(activeCom.get(player) + ".player").getKeys(false).size() + "")
					.replace("%desc%", com.getString(activeCom.get(player) + ".setting.description"))
					.replace("%faveGame%", com.getString(activeCom.get(player) + ".setting.faveGame"));
		}
		return res;
	}

	public static int cosmAmo(OfflinePlayer player, String cosmetic) {
		return data.getInt("Users." + player.getUniqueId() + ".treasure." + cosmetic);
	}

	public static void addCosmetic(OfflinePlayer player, String cosmetic, int amo) {
		data.set("Users." + player.getUniqueId() + ".treasure." + cosmetic.toLowerCase(),
				data.getInt("Users." + player.getUniqueId() + ".treasure." + cosmetic) + amo);
		save.put("data", true);
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		// " &6&lMineplex &f&lGames &8[&7&lUS&8] » &a&lNEW &C&LMOBA &A&LUPDATE\n
		// &2&l╭╯&8&o'Watch for the thorns...' » &2&lIVY ╰╮"
		// " &6&lMineplex &f&lGames &8[&7&lUS&8] » &b&lTURF WARS &A&LMAPS\n &d✦&5✧
		// &c&lCLANS SEASON 4 &D&L★ &6&LLIVE NOW &5✧&d✦"));
		// " &6&lMineplex &f&lGames &8[&7&lUS&8]» &a&l2x XP &b&lWEEKEND!!!\n &d✦&5✧
		// &c&lCLANS SEASON 4 &D&L★ &6&LLIVE NOW &5✧&d✦"
		// " &6&lMineplex &f&lGames &8[&7&lUS&8]» &B&lOCT &6&LPPC &b&lREWARD!\n &d✦&5✧
		// &c&lCLANS SEASON 4 &D&L★ &6&LLIVE NOW &5✧&d✦"));
		// " &6&lMineplex &f&lGames &8[&7&lUS&8]» &B&lNOV &6&LPPC &b&lREWARD!\n &d✦&5✧
		// &b&lCLANS &d&l4th &b&lSEASON! &5✧&d✦"));
		// " &9&m---&8&l&m[-&r &6&lMineplex &f&lGames&r &8&l&m-]&9&m---&r\n &c&lCake
		// &f&lWars &7Full Release"));
		// " &9&m---&8&l&m[-&r &6&lMineplex &f&lGames&r &8&l&m-]&9&m---&r\n &c&lCake
		// &f&lWars &7Full Release"
		// " &9&m---&8&l&m[-&r &6&lMineplex &f&lGames&r &8&l&m-]&9&m---&r\n
		// &a&lBLACK FRIDAY &8- &2&LBLOWOUT SALE"));
		int amo = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!data.getBoolean("Users." + player.getUniqueId() + ".vanished"))
				amo++;
		}
		if (config.getBoolean("Features.customtab")) {
			event.setMotd(color(
					"            &9&m---&8&l&m[-&r  &6&lMineplex &f&lGames&r &8&l&m-]&9&m---&r\n       &f&l❄ &c&lH&f&lO&c&lL&f&lI&c&lD&f&lA&c&lY&r &f&lL&c&lO&f&lB&c&lB&f&lY&f&l ❄ &lSNOW &b&lFIGHT &f&l❄"));
			try {
				event.setServerIcon(Bukkit.loadServerIcon(new File(getDataFolder() + "/mplogo.png")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		event.setMaxPlayers(amo + 1);
	}

	@EventHandler
	public void onToggleFlight(PlayerToggleFlightEvent event) {
		Player player = (Player) event.getPlayer();
		if (Boolean.valueOf(settings.get("doubleJump"))) {
			if (player.getWorld().getName().contains("Lobby-") && player.getGameMode() != GameMode.CREATIVE) {
				if (!player.isFlying()) {
					player.setFlying(false);
					player.setAllowFlight(false);
					Vector dir = player.getLocation().getDirection();
					dir.multiply(1.5);
					dir.setY(Math.abs(dir.getY()) + .3);
					player.setVelocity(dir);
					player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1, 1);
				}
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (Boolean.valueOf(settings.get("doubleJump"))) {
			if (player.getWorld().getName().contains("Lobby-") && player.getGameMode() != GameMode.CREATIVE) {
				if (player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0)).getType() != Material.AIR) {
					player.setAllowFlight(true);
					player.setFlying(false);
				}
			}
		}
		if (player.getLocation().getY() <= -100 || player.getLocation().getY() >= 230) {
			player.teleport(player.getWorld().getSpawnLocation());
		}
		if (chestOpening.containsKey(player) && chestProg.containsKey(player)) {
			if (chestOpening.get(player).distance(player.getLocation()) > 4) {
				player.teleport(chestOpening.get(player));
			}
		}
		if (event.getFrom().distance(event.getTo()) > 0) {
			if (frozen.containsKey(player)) {
				player.teleport(frozen.get(player));
			}
		}
		for (Entity e : player.getNearbyEntities(5, 5, 5)) {
			if (!getHoloID(e.getName()).equals("")) {
				if (!e.isCustomNameVisible()) {
					if (holoID.containsKey(player)) {
						if (holoID.get(player).equals(e.getCustomName())) {
							if (!staff.contains(getRankID(player.getUniqueId()))) {
								if (!data.getBoolean("Users." + player.getUniqueId() + ".notify.kb"))
									ClientManager.kbEntity(e, player.getLocation());
							}
						}
					} else {
						if (!staff.contains(getRankID(player.getUniqueId()))) {
							player.setVelocity(player.getLocation().getDirection().multiply(2).setY(1));
						}
					}
				}
			}
		}
	}

	public static boolean offExists(OfflinePlayer player) {
		return !(!player.hasPlayedBefore() && !player.isOnline());
	}

	public void applyScoreboard(Player player) {
		player.setScoreboard(gameBoard);
	}

	public void renameScoreboard(Player player, String name) {
		player.getScoreboard().getObjective("GameBoard").setDisplayName(color(name));
	}

	public void setLine(Player player, int line, String msg) {
		for (int i = 1; i <= 15; i++) {
			if (boardLines.get(i) != null) {
				if (boardLines.get(i).replace("&r", "").equals(msg.replace("&r", ""))) {
					msg = msg + "&r";
				}
			}
		}
		if (boardLines.get(line) != null) {
			gameBoard.resetScores(color(boardLines.get(line)));
		}
		defObj.getScore(color(msg)).setScore(line);
		boardLines.put(line, msg);
	}

	@SuppressWarnings("deprecation")
	public static boolean offExists(String name) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		return !(!player.hasPlayedBefore() && !player.isOnline());
	}

	public static void noPerm(CommandSender sender) {
		tell(sender, "&9Permissions> &7You do not have permission to do that.");
	}

	public static void notFound(String name, CommandSender sender, Boolean offline) {
		if (offline) {
			tell(sender, "&9Offline Player Search> &e0 &7matches for [&e" + name + "&7].");
		} else {
			tell(sender, "&9Online Player Search> &e0 &7matches for [&e" + name + "&7].");
		}
	}

	public static Player getPlayer(String name, CommandSender sender) {
		List<Player> players = new ArrayList<Player>();
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.getName().toLowerCase().contains(name.toLowerCase()))
				players.add(target);
		}
		if (players.size() == 0) {
			notFound(name, sender, false);
		} else if (players.size() == 1) {
			return players.get(0);
		} else {
			tell(sender, color("&9Online Player Search> &e" + players.size() + " &7matches for [&e" + name + "&7]."));
			String msg = "";
			for (Player res : players)
				msg = msg + ", &e" + res.getName() + "&7";
			msg = msg.substring(2);
			tell(sender, "&9Online Player Search> &7Matches [" + msg + "&7].");
		}
		return null;
	}

	public static void addXp(Player player, double level) {
		String uuid = player.getUniqueId().toString();
		int oldLvl = getLevel(player.getUniqueId()), newLvl = getLevel(player.getUniqueId());
		if (!data.contains("Users." + uuid + ".level")) {
			data.set("Users." + uuid + ".level", 0.1f);
		}
		level = level/10;
		if ((int) Math.floor(data.getDouble("Users." + uuid + ".level")) >= 100)
			return;
		data.set("Users." + uuid + ".level", data.getDouble("Users." + uuid + ".level")
				+ ((level) * (1 - (data.getDouble("Users." + uuid + ".level") / 50))));
		newLvl = getLevel(player.getUniqueId());
		if(newLvl!=oldLvl) {
			Firework fire = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
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
			player.sendMessage(color("&9Client Manager> &7You have ranked up to level &e"+newLvl+"&7."));
		}
		if (Math.floor(data.getDouble("Users." + uuid + ".level")) != oldLvl) {
			save.put("config", true);
			save.put("data", true);
		}
	}

	public static ItemStack newItem(Material material, String name) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();
		if (name.length() > 0 && material != Material.AIR)
			meta.setDisplayName(color(name));
		item.setItemMeta(meta);
		return item;
	}

	static ItemStack newItem(Material material, String name, String lore, int amount) {
		List<String> tempLore = new ArrayList<String>();
		tempLore.add(color(lore));
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		if (name.length() > 0)
			meta.setDisplayName(color(name));
		meta.setLore(tempLore);
		item.setItemMeta(meta);
		return item;
	}

	public static Entity getEntity(String name) {
		for (World world : Bukkit.getWorlds()) {
			for (Entity e : world.getEntities()) {
				if (e.getCustomName() != null) {
					if (e.getCustomName().equals(name))
						return e;
				}
			}
		}
		return null;
	}

	public void saveData() {
		try {
			data.save(dataYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveConfig() {
		try {
			config.save(configYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveReport() {
		try {
			reports.save(reportYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveGui() {
		try {
			guis.save(guiYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveFriend() {
		try {
			friend.save(friendYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveParty() {
		try {
			party.save(partyYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveFilter() {
		try {
			filter.save(filterYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveHolo() {
		try {
			holos.save(holoYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveCom() {
		try {
			com.save(comYml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<String> getPlayers() {
		Set<String> set = new HashSet<>();
		for (Map.Entry<String, EntityEnderDragon> entry : dragons.entrySet()) {
			set.add(entry.getKey());
		}
		return set;
	}

	public static int getPing(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		return ep.ping;
	}

	public static String getIP(OfflinePlayer target) {
		if (data.contains("Users." + target.getUniqueId() + ".ip")) {
			return data.getString("Users." + target.getUniqueId() + ".ip");
		}
		return "";
	}
	
	public static Boolean vanished(Player player) {
		return data.getBoolean("Users."+player.getUniqueId()+".vanished");
	}
	
	public static String getTeam(Player player) {
		if(Bukkit.getPluginManager().getPlugin("GameManager")==null) {
			return "";
		}else {
			return Manager.getTeam(player);
		}
	}
	
	public static String teamCol(String team) {
		if(Bukkit.getPluginManager().getPlugin("GameManager")==null) {
			return "";
		}else {
			return Manager.teamCol(team);
		}
	}

	/*
	 * @SuppressWarnings("deprecation") public static void refreshMotd() { int pos =
	 * 0; names.clear(); for (String res : config.getStringList("MOTDList")) {
	 * names.add(new WrappedGameProfile(pos + "", color(res))); pos++; } }
	 */
	public static double rnd(Double min, Double max) {
		return Math.random() * (max - min) + min;
	}

	public static void tell(CommandSender sender, String msg) {
		sender.sendMessage(color(msg));
	}

	public static String TorF(Boolean bool) {
		if (bool) {
			return "&aTrue&r";
		} else {
			return "&cFalse&r";
		}
	}
}
