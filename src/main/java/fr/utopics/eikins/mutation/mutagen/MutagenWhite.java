package fr.utopics.eikins.mutation.mutagen;

import fr.utopics.eikins.mutation.ModMutation;
import fr.utopics.eikins.mutation.Registry;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class MutagenWhite extends Mutagen {

	private int count;

	public MutagenWhite(String mutageneName, MutagenType type) {
		super(mutageneName, type);
	}

	@Override
	public void onMutagenAdded(EntityPlayer player) {
		super.onMutagenAdded(player);
		player.capabilities.setPlayerWalkSpeed(player.capabilities.getWalkSpeed() + 0.01f + 0.01f * this.getAmplifier());	
		if(!player.worldObj.isRemote)
			System.out.println(player.capabilities.getWalkSpeed());
	}

	@Override
	public void onMutagenRemoved(EntityPlayer player) {
		super.onMutagenRemoved(player);
		player.capabilities.setPlayerWalkSpeed(player.capabilities.getWalkSpeed() - 0.01f - 0.01f * this.getAmplifier());
		if(!player.worldObj.isRemote)
			System.out.println(player.capabilities.getWalkSpeed());
	}

	@Override
	public int getMutationRaise(int amplifier) {
		return 1000 + 100 * amplifier;
	}

	@Override
	public String getEffectString(int amplifier) {
		String s = StatCollector.translateToLocal(ModMutation.MODID + ".effect." + this.getName());
		return String.format(s, 10 + 10 * amplifier);
	}

}
