package u.mods.essentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.activation.CommandMap;

import u.mods.essentials.command.CommandBack;
import u.mods.essentials.command.CommandHome;
import u.mods.essentials.command.CommandSeen;
import u.mods.essentials.command.CommandSetspawn;
import u.mods.essentials.command.CommandSpawn;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="uEssentials", name="uEssentials", version="1.0-Beta")
public class UEssentials
{
	MinecraftServer			server = ModLoader.getMinecraftServerInstance();
	public NBTTagCompound	dataEssentials = new NBTTagCompound();
	public boolean			backTroughDimension;
	public boolean			homeTroughDimension;
	public int				maxHouses;
	
	@Instance("uEssentials")
	public static UEssentials	instance;
	
	@PreInit
	public void	preInit(FMLPreInitializationEvent event)
	{
		Configuration	config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		this.backTroughDimension = config.get("Config", "backTroughDimension", false).getBoolean(false);
		this.homeTroughDimension = config.get("Config", "homeTroughDimension", false).getBoolean(false);
		this.maxHouses = config.get("Config", "maxHouses", 5).getInt(5);
		config.save();
	}
	
	@ServerStarting
	public void	serverStarting(FMLServerStartingEvent event)
	{
		String	fname = this.server.getFolderName() + "/uEssentials";
		File	path = new File(fname);
		if (!path.exists() && !path.mkdir())
		{
			System.err.println("[uEssentials] Failed to create uEssentials data file. Aborting.");
			return;
		}
		fname += "/data.dat";
		File	data = new File(fname);
		if (!data.exists())
		{
			try {
				data.createNewFile();
				this.dataEssentials.setInteger("[MaxHouses]", this.maxHouses);
				CompressedStreamTools.writeCompressed(this.dataEssentials, new FileOutputStream(fname));
			} catch (Exception e) {
				System.err.println("[uEssentials] Failed to create uEssentials data file. Aborting.");
				return;
			}
		}

		try {
			this.dataEssentials = CompressedStreamTools.readCompressed(new FileInputStream(fname));
			this.dataEssentials.setInteger("[MaxHouses]", this.maxHouses);
			CompressedStreamTools.writeCompressed(this.dataEssentials, new FileOutputStream(fname));
		}
		catch (Exception e) {
			System.err.println("[uEssentials] Failed to load uEssentials data file. Aborting.");
			e.printStackTrace();
			return;
		}
		
		GameRegistry.registerPlayerTracker(new PlayerTracker());
		ServerCommandManager	manager = (ServerCommandManager)this.server.getCommandManager();
		manager.registerCommand(new CommandSeen());
		manager.registerCommand(new CommandSpawn());
		manager.registerCommand(new CommandSetspawn());
		manager.registerCommand(new CommandBack());
		manager.registerCommand(new CommandHome());
		
		MinecraftForge.EVENT_BUS.register(new DeathEventHandler());
	}
	
	public boolean	saveData()
	{
		MinecraftServer			server = ModLoader.getMinecraftServerInstance();
		String	fname = server.getFolderName() + "/uEssentials/data.dat";
		try {
			CompressedStreamTools.writeCompressed(this.dataEssentials, new FileOutputStream(fname));
		} catch (Exception e) {
			System.err.println("[uEssentials] Failed to save datas.");
			return false;
		}
		System.out.println("[uEssentials] Essentials data saved.");
		return true;
	}
	
	public void	setPlayerBack(EntityPlayer player)
	{
		NBTTagCompound	playerTag = this.dataEssentials.getCompoundTag(player.username);
		NBTTagCompound	back = playerTag.getCompoundTag("[Back]");
		back.setInteger("Dimension", player.dimension);
		back.setDouble("PosX", player.posX);
		back.setDouble("PosY", player.posY);
		back.setDouble("PosZ", player.posZ);
		back.setFloat("Yaw", player.rotationYaw);
		back.setFloat("Pitch", player.rotationPitch);
		playerTag.setCompoundTag("[Back]", back);
		this.dataEssentials.setCompoundTag(player.username, playerTag);
		this.saveData();
	}
	
	public void	teleportPlayerTroughDimension(EntityPlayerMP player, int dim, double x, double y, double z, float yaw, float pitch)
	{
		if (player.dimension != dim)
			this.server.getConfigurationManager().transferPlayerToDimension(player, dim);
		teleportPlayer(player, x, y, z, yaw, pitch);
	}
	
	public void	teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch)
	{
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
	}
	
	public void teleportPlayer(EntityPlayerMP player, double x, double y, double z)
	{
		player.setPositionAndUpdate(x, y, z);
	}
}
