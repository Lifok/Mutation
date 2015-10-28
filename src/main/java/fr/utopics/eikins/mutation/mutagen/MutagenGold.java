package fr.utopics.eikins.mutation.mutagen;

import fr.utopics.eikins.mutation.ModMutation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class MutagenGold extends Mutagen {

	private int count;

	public MutagenGold(String mutageneName, MutagenType type) {
		super(mutageneName, type);
	}

	@Override
	public void onMutagenAdded(EntityPlayer player) {
		super.onMutagenAdded(player);
	}

	@Override
	public void onMutagenRemoved(EntityPlayer player) {
		super.onMutagenRemoved(player);
	}

	@Override
	public int getMutationRaise(int amplifier) {
		return 750 + 150 * amplifier;
	}

	@Override
	public String getEffectString(int amplifier) {
		String s = StatCollector.translateToLocal(ModMutation.MODID + ".effect." + this.getName());
		return String.format(s, 15 + 15 * amplifier);
	}

}
