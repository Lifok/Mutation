package fr.utopics.eikins.mutation.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.utopics.eikins.mutation.ModMutation;
import fr.utopics.eikins.mutation.entity.player.ExtendedPlayer;
import fr.utopics.eikins.mutation.mutagen.Mutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenGreen;

public class ItemMutagen extends Item {

	private Mutagen mutagen;

	public ItemMutagen(Mutagen itemMutagen) {
		mutagen = itemMutagen;
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal(ModMutation.MODID + ".effect") + " : " + mutagen.getEffectString(getAmplifier(itemstack)));
		list.add(EnumChatFormatting.RED + "Mutation : " + ((float)mutagen.getMutationRaise(getAmplifier(itemstack)) * 15 / 10000) + "% " +  StatCollector.translateToLocal(ModMutation.MODID + ".effect.minute"));
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
		if(!par2World.isRemote)
		{
			if (!player.capabilities.isCreativeMode)
			{
				--par1ItemStack.stackSize;
			}
			if(!ExtendedPlayer.get(player).hasMutagen(mutagen)) {
				ExtendedPlayer.get(player).addMutagen(mutagen.setAmplifier(getAmplifier(par1ItemStack)));
			}
		}
		return par1ItemStack;
	}

	public String getItemStackDisplayName(ItemStack stack) {
		String name = StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name");
		if(getAmplifier(stack) >= 5) {
			name += " V";
		}
		else if(getAmplifier(stack) == 4) {
			name += " IV";
		}
		else if(getAmplifier(stack) > 0) {
			name += " ";
			for(int i = 0; i < getAmplifier(stack); i++) {
				name+= "I";
			}
		}
		return name;
	}
	
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
    	if(par1ItemStack.getTagCompound() == null)
    	{
    		NBTTagCompound compound = new NBTTagCompound();
        	compound.setInteger("Amplifier", mutagen.getAmplifier());
    	}     
    }
    
    public ItemStack setAmplifier(ItemStack stack, int amplifier) {
    	if(amplifier > 5) {
    		amplifier = 5;
    	}
    	else if(amplifier < 0) {
    		amplifier = 0;
    	}
		NBTTagCompound compound = new NBTTagCompound();
    	compound.setInteger("Amplifier", amplifier);
    	stack.setTagCompound(compound);
    	return stack;
    }
    
    public int getAmplifier(ItemStack stack) {
    	if(stack.hasTagCompound()) {
    		return stack.getTagCompound().getInteger("Amplifier");
    	}
    	return 0;
    }

}
