package fr.utopics.eikins.mutation;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import fr.utopics.eikins.mutation.client.HUDRenderer;
import fr.utopics.eikins.mutation.entity.player.PlayerActions;
import fr.utopics.eikins.mutation.item.crafting.RecipeAntidote;

@Mod(modid = ModMutation.MODID, version = ModMutation.VERSION)
public class ModMutation
{
	public static final String MODID = "mutation";
	public static final String VERSION = "1.0";

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Registry.getInstance().registerEventHandlers(event.getSide());
		Registry.getInstance().registerMutagens();
		Registry.getInstance().initItems();
		Registry.getInstance().registerItems();
		Registry.getInstance().initNetwork();
		Registry.getInstance().registerPlantValues();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(event.getSide().equals(Side.CLIENT)) {
			Registry.getInstance().registerRenders(this.MODID);
		}
		Registry.getInstance().registerRecipes();
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager command = server.getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		Registry.registerCommands(manager);
	}
}
