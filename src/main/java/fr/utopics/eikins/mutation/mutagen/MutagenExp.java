package fr.utopics.eikins.mutation.mutagen;

import fr.utopics.eikins.mutation.ModMutation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;

public class MutagenExp extends Mutagen implements ITickableMutagen{

	private int count = 0;
	
	public MutagenExp(String mutageneName, MutagenType type) {
		super(mutageneName, type);
	}
	
	@Override
	public void onPlayerTick(EntityPlayer player) {
		if(count >= 200 - this.getAmplifier() * 10) {
			count = 0;
			player.addExperience(1);
		}
	    count++;
	}

	@Override
	public int getMutationRaise(int amplifier) {
		return 500;
	}

	@Override
	public String getEffectString(int amplifier) {
		String s = StatCollector.translateToLocal(ModMutation.MODID + ".effect." + this.getName());
		return String.format(s, 10f - 0.5f * amplifier);
	}

}
