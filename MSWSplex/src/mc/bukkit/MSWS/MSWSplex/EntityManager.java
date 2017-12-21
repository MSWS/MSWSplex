package mc.bukkit.MSWS.MSWSplex;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityMushroomCow;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class EntityManager {
	public static PacketPlayOutSpawnEntityLiving getEntity(EntityType entity,
			net.minecraft.server.v1_8_R3.World world) {
		if (entity == EntityType.ARMOR_STAND)
			return new PacketPlayOutSpawnEntityLiving(new EntityArmorStand(world));
		if (entity == EntityType.BAT)
			return new PacketPlayOutSpawnEntityLiving(new EntityBat(world));
		if (entity == EntityType.BLAZE)
			return new PacketPlayOutSpawnEntityLiving(new EntityBlaze(world));
		if (entity == EntityType.CAVE_SPIDER)
			return new PacketPlayOutSpawnEntityLiving(new EntityCaveSpider(world));
		if (entity == EntityType.CHICKEN)
			return new PacketPlayOutSpawnEntityLiving(new EntityChicken(world));
		if (entity == EntityType.COW)
			return new PacketPlayOutSpawnEntityLiving(new EntityCow(world));
		if (entity == EntityType.CREEPER)
			return new PacketPlayOutSpawnEntityLiving(new EntityCreeper(world));
		if (entity == EntityType.ENDER_DRAGON)
			return new PacketPlayOutSpawnEntityLiving(new EntityEnderDragon(world));
		if (entity == EntityType.ENDERMAN)
			return new PacketPlayOutSpawnEntityLiving(new EntityEnderman(world));
		if (entity == EntityType.ENDERMITE)
			return new PacketPlayOutSpawnEntityLiving(new EntityEndermite(world));
		if (entity == EntityType.GHAST)
			return new PacketPlayOutSpawnEntityLiving(new EntityGhast(world));
		if (entity == EntityType.GIANT)
			return new PacketPlayOutSpawnEntityLiving(new EntityGiantZombie(world));
		if (entity == EntityType.GUARDIAN)
			return new PacketPlayOutSpawnEntityLiving(new EntityGuardian(world));
		if (entity == EntityType.HORSE)
			return new PacketPlayOutSpawnEntityLiving(new EntityHorse(world));
		if (entity == EntityType.IRON_GOLEM)
			return new PacketPlayOutSpawnEntityLiving(new EntityIronGolem(world));
		if (entity == EntityType.MAGMA_CUBE)
			return new PacketPlayOutSpawnEntityLiving(new EntityMagmaCube(world));
		if (entity == EntityType.MUSHROOM_COW)
			return new PacketPlayOutSpawnEntityLiving(new EntityMushroomCow(world));
		if (entity == EntityType.OCELOT)
			return new PacketPlayOutSpawnEntityLiving(new EntityOcelot(world));
		if (entity == EntityType.PIG)
			return new PacketPlayOutSpawnEntityLiving(new EntityPig(world));
		if (entity == EntityType.RABBIT)
			return new PacketPlayOutSpawnEntityLiving(new EntityRabbit(world));
		if (entity == EntityType.SHEEP)
			return new PacketPlayOutSpawnEntityLiving(new EntitySheep(world));
		if (entity == EntityType.SILVERFISH)
			return new PacketPlayOutSpawnEntityLiving(new EntitySilverfish(world));
		if (entity == EntityType.SKELETON)
			return new PacketPlayOutSpawnEntityLiving(new EntitySkeleton(world));
		if (entity == EntityType.SLIME)
			return new PacketPlayOutSpawnEntityLiving(new EntitySlime(world));
		if (entity == EntityType.SNOWMAN)
			return new PacketPlayOutSpawnEntityLiving(new EntitySnowman(world));
		if (entity == EntityType.SPIDER)
			return new PacketPlayOutSpawnEntityLiving(new EntitySpider(world));
		if (entity == EntityType.SQUID)
			return new PacketPlayOutSpawnEntityLiving(new EntitySquid(world));
		if (entity == EntityType.VILLAGER)
			return new PacketPlayOutSpawnEntityLiving(new EntityVillager(world));
		if (entity == EntityType.WITCH)
			return new PacketPlayOutSpawnEntityLiving(new EntityWitch(world));
		if (entity == EntityType.WITHER)
			return new PacketPlayOutSpawnEntityLiving(new EntityWither(world));
		if (entity == EntityType.WOLF)
			return new PacketPlayOutSpawnEntityLiving(new EntityWolf(world));
		if (entity == EntityType.ZOMBIE)
			return new PacketPlayOutSpawnEntityLiving(new EntityZombie(world));
		return null;
	}
}
