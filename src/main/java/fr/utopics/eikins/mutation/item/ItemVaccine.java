package fr.utopics.eikins.mutation.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.entity.player.ExtendedPlayer;
import fr.utopics.eikins.mutation.mutagen.Mutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenGreen;
import fr.utopics.eikins.mutation.mutagen.MutagenType;

public class ItemVaccine extends Item {

	public ItemVaccine() {
		this.setMaxStackSize(1);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player)
	{
		if(!par2World.isRemote) {
			if(ExtendedPlayer.get(player).isMutant()) {
				ExtendedPlayer.get(player).clearMutagens();
			}
			if (!player.capabilities.isCreativeMode) {
				par1ItemStack = new ItemStack(Registry.syringe, 1);
				player.attackEntityFrom(DamageSource.cactus, 1f);
			}
		}
		return par1ItemStack;
	}

}
