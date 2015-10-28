package fr.utopics.eikins.mutation.entity;

import java.util.Random;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.item.ItemMutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenType;

public class MobActions {
	
	private Random rand = new Random();

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if(event.entityLiving.worldObj.isRemote) {
			return;
		}
		if(event.entityLiving instanceof EntityZombie) {
			int rng = 1 + this.rand.nextInt(100); //Random number in [1;100]
			if(rng <= 5) {
				ItemMutagen mutagen = Registry.getInstance().getMutagen(MutagenType.values()[rand.nextInt(MutagenType.values().length - 2) + 2]).getItem();
				ItemStack stack = new ItemStack(mutagen);
				int rng2 = 1 + this.rand.nextInt(100);
				if(rng2 >= 98) {
					mutagen.setAmplifier(stack, 5);
				}
				else if(rng2 >= 95) {
					stack = mutagen.setAmplifier(stack, 4);
				}
				else if(rng2 >= 90) {
					stack = mutagen.setAmplifier(stack, 3);
				}
				else if(rng2 >= 80) {
					stack = mutagen.setAmplifier(stack, 2);
				}
				else if(rng2 >= 65) {
					stack = mutagen.setAmplifier(stack, 1);
				}
				event.entityLiving.entityDropItem(stack, 0f);
			}
		}
	}
}
