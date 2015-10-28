package fr.utopics.eikins.mutation.item.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.mutagen.MutagenType;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeAntidote implements IRecipe {
	
    private ItemStack antidote;

	@Override
	public boolean matches(InventoryCrafting inventory, World worldIn) {
		antidote = null;
		HashMap<Item, Float> map = Registry.getInstance().getPlantValues();
		ArrayList<Item> usedItems = new ArrayList<Item>();
		float value = 0;
		boolean syringe = false;
		boolean canCraft = true;
		int differentItems = 0;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
        	if(inventory.getStackInSlot(i) != null) {
            	if(map.containsKey(inventory.getStackInSlot(i).getItem())) {
            		Item item = inventory.getStackInSlot(i).getItem();
            		if(usedItems.contains(item)) {
            			value += map.get(item).floatValue() / Math.pow(2, Collections.frequency(usedItems, item));
            		}
            		else {
            			value += map.get(item).floatValue();
            			differentItems++;
            		}
        			usedItems.add(item);
            	}
            	else if(inventory.getStackInSlot(i).getItem().equals(Registry.syringe)) {
            		if(usedItems.contains(Registry.syringe)) {
            			syringe = false;
            		}
            		else {
            			syringe = true;
            		}
        			usedItems.add(Registry.syringe);
            	}
            	else {
            		canCraft = false;
            	}
        	}
        }
        if(differentItems > 3) {
        	value /= (differentItems - 2);
        }
        if(value > 0 && canCraft && syringe) {
        	antidote = new ItemStack(Registry.antidote);
            NBTTagCompound compound = new NBTTagCompound();
            compound.setFloat("Value", value);
            antidote.setTagCompound(compound);
            return true;
        }
        return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		return antidote.copy();
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return antidote;
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
