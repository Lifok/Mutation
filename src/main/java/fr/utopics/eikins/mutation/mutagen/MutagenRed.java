package fr.utopics.eikins.mutation.mutagen;

import fr.utopics.eikins.mutation.ModMutation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class MutagenRed extends Mutagen{

	private int count;

	public MutagenRed(String mutageneName, MutagenType type) {
		super(mutageneName, type);
	}

	@Override
	public void onMutagenAdded(EntityPlayer player) {
		super.onMutagenAdded(player);
		double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		player.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(damage + 3 + 1 * (this.getAmplifier()));
	}

	@Override
	public void onMutagenRemoved(EntityPlayer player) {
		super.onMutagenRemoved(player);
		double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		player.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(damage - 3 - 1 * (this.getAmplifier()));
	}

	@Override
	public int getMutationRaise(int amplifier) {
		return 750 + 250 * amplifier;
	}

	@Override
	public String getEffectString(int amplifier) {
		String s = StatCollector.translateToLocal(ModMutation.MODID + ".effect." + this.getName());
		return String.format(s, 3 + amplifier);
	}

}
