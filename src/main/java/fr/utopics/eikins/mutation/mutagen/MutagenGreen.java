package fr.utopics.eikins.mutation.mutagen;

import fr.utopics.eikins.mutation.ModMutation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class MutagenGreen extends Mutagen implements ITickableMutagen{

	private int count;

	public MutagenGreen(String mutageneName, MutagenType type) {
		super(mutageneName, type);
	}

	@Override
	public void onMutagenAdded(EntityPlayer player) {
		super.onMutagenAdded(player);
		double maxHealth = player.getMaxHealth();
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth + 6 + 2 * (this.getAmplifier()));
	}

	@Override
	public void onMutagenRemoved(EntityPlayer player) {
		super.onMutagenRemoved(player);
		double maxHealth = player.getMaxHealth();
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth - 6 - 2 * (this.getAmplifier()));
	}

	@Override
	public void onPlayerTick(EntityPlayer player) {
		if(this.hasUpgrade()) {
			if(count >= 200) {
				count = 0;
				player.heal(1f);
			}
		    count++;
		}
	}

	@Override
	public int getMutationRaise(int amplifier) {
		return 500 + 100 * amplifier;
	}

	@Override
	public String getEffectString(int amplifier) {
		String s = StatCollector.translateToLocal(ModMutation.MODID + ".effect." + this.getName());
		return String.format(s, 3 + amplifier);
	}

}
