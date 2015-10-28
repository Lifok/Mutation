package fr.utopics.eikins.mutation.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.utopics.eikins.mutation.ModMutation;
import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.entity.player.ExtendedPlayer;
import fr.utopics.eikins.mutation.mutagen.Mutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenGreen;

public class ItemAntidote extends Item {

	public ItemAntidote() {
		this.setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(stack.getTagCompound() != null) {
			String s = String.format(StatCollector.translateToLocal(ModMutation.MODID + ".effect.antidote"), String.format("%.2f", stack.getTagCompound().getFloat("Value")));
			list.add(EnumChatFormatting.GREEN + s);
		}
	}
	
	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
		if(!par2World.isRemote) {
			if(ExtendedPlayer.get(player).getMutationLevel() > 0) {
				if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("Value")) {
					int value = (int)(stack.getTagCompound().getFloat("Value") * 10000);
					System.out.println(stack.getTagCompound().getFloat("Value") + "%");
					ExtendedPlayer.get(player).removeMutation(value);
					if (!player.capabilities.isCreativeMode) {
						stack = new ItemStack(Registry.syringe, 1);
						player.attackEntityFrom(DamageSource.cactus, 1f);
					}
				}
				else {
					ExtendedPlayer.get(player).removeMutation(125000);
					if (!player.capabilities.isCreativeMode) {
						stack = new ItemStack(Registry.syringe, 1);
						player.attackEntityFrom(DamageSource.cactus, 1f);
					}
				}
			}
		}
		return stack;
	}

}
