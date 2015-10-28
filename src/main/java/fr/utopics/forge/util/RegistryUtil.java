package fr.utopics.forge.util;

import fr.utopics.eikins.mutation.ModMutation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegistryUtil {
	
	/**
	 * Used to register and item without his name in params. The name used is the unlocalizedName.
	 * @param item
	 */
	public static void registerItem(Item item) {
		String s = item.getUnlocalizedName().replace("item.", "");
		GameRegistry.registerItem(item , s);
	}
	
	/**
	 * Used to register an item on the client-side : register his model in the ItemModelMesher.
	 * @param item
	 * @param modId
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, String modId) {
		String s = item.getUnlocalizedName().replace("item.", "");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(modId + ":" + s, "inventory"));
	}
	
	/**
	 * Used to register an item on the client-side : register his model in the ItemModelMesher.
	 * @param item
	 * @param mod
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, Mod mod) {
		String s = item.getUnlocalizedName().replace("item.", "");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(mod.modid() + ":" + s, "inventory"));
	}
}
