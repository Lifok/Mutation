package fr.utopics.eikins.mutation.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import fr.utopics.eikins.mutation.item.ItemMutagen;

public class RecipeMutagen implements IRecipe {

	private ItemStack mutagen;
	
	@Override
	public boolean matches(InventoryCrafting inventory, World worldIn) {
		ItemStack currentMutagen;
		if(inventory.getStackInSlot(0) == null || !(inventory.getStackInSlot(0).getItem() instanceof ItemMutagen)) {
			mutagen = null;
			return false;
		} else {
			currentMutagen = inventory.getStackInSlot(0);
		}
		if(currentMutagen.hasTagCompound() && currentMutagen.getItem() instanceof ItemMutagen) {
			if(((ItemMutagen)currentMutagen.getItem()).getAmplifier(currentMutagen) >= 5) {
				return false;
			}
		}
		for(int i = 1; i < inventory.getSizeInventory(); i++) {
			if(inventory.getStackInSlot(i) == null || !inventory.getStackInSlot(i).getIsItemStackEqual(currentMutagen)) {
				mutagen = null;
				return false;
			}
		}
		mutagen = currentMutagen.copy();
		mutagen = ((ItemMutagen)mutagen.getItem()).setAmplifier(mutagen, ((ItemMutagen)mutagen.getItem()).getAmplifier(mutagen) + 1);
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		return mutagen.copy();
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return mutagen;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inventory) {
        ItemStack[] aitemstack = new ItemStack[inventory.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
	}

}
