package mc.bukkit.MSWS.MSWSplex;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;

public class DragonManager {

	public static void setBar(Player p, String text, float healthPercent) {
		Location loc = p.getLocation();
		WorldServer world = ((CraftWorld) p.getLocation().getWorld()).getHandle();

		EntityEnderDragon dragon = new EntityEnderDragon(world);
		dragon.setLocation(loc.getX(), loc.getY() - 200, loc.getZ(), 0, 0);

		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(dragon);

		DataWatcher watcher = new DataWatcher(null);
		watcher.a(0, (byte) 0x20);
		watcher.a(6, (healthPercent * 200) / 100);
		watcher.a(10, text);
		watcher.a(2, text);
		watcher.a(11, (byte) 1);
		watcher.a(3, (byte) 1);

		try {
			Field t = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("l");
			t.setAccessible(true);
			t.set(packet, watcher);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Main.dragons.put(p.getName(), dragon);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void removeBar(Player p) {
		if (Main.dragons.containsKey(p.getName())) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(Main.dragons.get(p.getName()).getId());
			Main.dragons.remove(p.getName());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void teleportBar(Player p) {
		if (Main.dragons.containsKey(p.getName())) {
			Location loc = p.getLocation();
			PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(Main.dragons.get(p.getName()).getId(),
					(int) loc.getX() * 32, (int) -100 * 32, (int) loc.getZ() * 32, (byte) 0, (byte) 0, false);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void updateText(Player p, String text) {
		updateBar(p, text, -1);
	}

	public static void updateHealth(Player p, float healthPercent) {
		updateBar(p, null, healthPercent);
	}

	public static void updateBar(Player p, String text, float healthPercent) {
		if (Main.dragons.containsKey(p.getName())) {
			DataWatcher watcher = new DataWatcher(null);
			watcher.a(0, (byte) 0x20);
			if (healthPercent != -1)
				watcher.a(6, (healthPercent * 200) / 100);
			if (text != null) {
				watcher.a(10, text);
				watcher.a(2, text);
			}
			watcher.a(11, (byte) 1);
			watcher.a(3, (byte) 1);

			PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(Main.dragons.get(p.getName()).getId(),
					watcher, true);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
