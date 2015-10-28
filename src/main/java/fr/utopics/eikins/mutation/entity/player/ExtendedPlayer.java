package fr.utopics.eikins.mutation.entity.player;

import java.util.ArrayList;
import java.util.List;

import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.mutagen.ITickableMutagen;
import fr.utopics.eikins.mutation.mutagen.Mutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenGreen;
import fr.utopics.eikins.mutation.mutagen.MutagenType;
import fr.utopics.eikins.mutation.network.MessageMutagenSync;
import fr.utopics.eikins.mutation.proxy.Proxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

public class ExtendedPlayer implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = "Mutation";
	private final EntityPlayer player;
	private ArrayList<Mutagen> mutagens;
	private int mutationCount;
	private static final int MUTATION_WATCHABLE_INDEX = 31;
	private int mutationDamage;

	public ExtendedPlayer(EntityPlayer player) {
		this.player = player;
		mutagens = new ArrayList<Mutagen>();
		this.player.getDataWatcher().addObject(MUTATION_WATCHABLE_INDEX, 0);
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}

	public static final ExtendedPlayer get(EntityPlayer player) {
		return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		properties.setInteger("Mutation", player.getDataWatcher().getWatchableObjectInt(MUTATION_WATCHABLE_INDEX));
		properties.setInteger("MutationDamage", mutationDamage);
		properties.setInteger("MListSize", mutagens.size());
		NBTTagList mutagenList = new NBTTagList();
		for (int i = 0; i < mutagens.size(); i++) {
			Mutagen gene = mutagens.get(i);                                               
			if (gene != null) {
				NBTTagCompound tag = new NBTTagCompound(); 
				tag.setByte("Slot", (byte) i);                   
				gene.saveToNBT(tag); 
				mutagenList.appendTag(tag);
			}
		}
		properties.setTag("Mutagens", mutagenList);   
		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		player.getDataWatcher().updateObject(MUTATION_WATCHABLE_INDEX, properties.getInteger("Mutation"));
		mutationDamage = properties.getInteger("MutationDamage");
		int mSize = properties.getInteger("MListSize");
		Mutagen[] mArray = new Mutagen[mSize];
		NBTTagList tagList = properties.getTagList("Mutagens", 10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");                                                 
			if (slot >= 0 && slot < mSize) {
				mArray[slot] = Mutagen.loadFromNBT(tag);
			}                                                                                               
		}
		for(int i = 0; i < mSize; i++) {
			mutagens.add(mArray[i]);
		}
	}

	@Override
	public void init(Entity entity, World world){
	}

	public boolean isMutant() {
		return !mutagens.isEmpty();
	}

	public void addMutagen(Mutagen mutagen) {
		mutagens.add(mutagen);
		mutagen.onMutagenAdded(player);
	}

	public void removeMutagen(Mutagen mutagen) {
		mutagen.onMutagenRemoved(player);
		mutagens.remove(mutagen);
	}

	public void clearMutagens() {
		for(Mutagen m : mutagens) {
		m.onMutagenRemoved(player);
		}
		mutagens.clear();
	}

	public void callTickedMutagens() {
		if(isMutant()) {
			for(int i = 0; i < mutagens.size(); i++) {
				if(mutagens.get(i) instanceof ITickableMutagen) {
					((ITickableMutagen)mutagens.get(i)).onPlayerTick(player);
				}
			}
		}
	}

	public boolean hasMutagen(Mutagen mutagen) {
		for(int i = 0; i < mutagens.size(); i++) {
			if(mutagens.get(i).getName().equals(mutagen.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasMutagen(MutagenType type) {
		Mutagen mutagen = Registry.getInstance().getMutagen(type);
		for(int i = 0; i < mutagens.size(); i++) {
			if(mutagens.get(i).getName().equals(mutagen.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public Mutagen getMutagen(MutagenType type) {
		for(int i = 0; i < mutagens.size(); i++) {
			if(mutagens.get(i).getType().equals(type)) {
				return mutagens.get(i);
			}
		}
		return null;
	}

	public int getMaxMutationLevel() {
		return 1000000;
	}

	public int getMutationLevel() {
		return player.getDataWatcher().getWatchableObjectInt(MUTATION_WATCHABLE_INDEX);
	}

	public void update() {
		if(isMutant()) {
			if(mutationCount >= 80) {
				mutationCount = 0;
				raiseMutation();
			}
			mutationCount++;	
		}

	}

	private void raiseMutation() {
		if(getMutationLevel() >= getMaxMutationLevel()) {
			mutationDamage++;
			player.attackEntityFrom(DamageSource.generic, mutationDamage);
			return;
		}
		int mutationLevel = player.getDataWatcher().getWatchableObjectInt(MUTATION_WATCHABLE_INDEX);
		int ammount = 0;
		for(int i = 0; i < mutagens.size(); i++) {
			ammount += mutagens.get(i).getMutationRaise(mutagens.get(i).getAmplifier());
		}
		player.getDataWatcher().updateObject(MUTATION_WATCHABLE_INDEX, mutationLevel + ammount);
	}

	public void resetMutationLevel() {
		player.getDataWatcher().updateObject(MUTATION_WATCHABLE_INDEX, 0);
	}

	public void addMutation(int ammount) {
		if(player.capabilities.isCreativeMode) {
			return;
		}
		int mutationLevel = player.getDataWatcher().getWatchableObjectInt(MUTATION_WATCHABLE_INDEX) + ammount;
		if(getMutationLevel() > getMaxMutationLevel()) {
			mutationLevel = getMaxMutationLevel();
		}
		player.getDataWatcher().updateObject(MUTATION_WATCHABLE_INDEX, mutationLevel);	
	}

	public void removeMutation(int ammount) {
		int mutationLevel = player.getDataWatcher().getWatchableObjectInt(MUTATION_WATCHABLE_INDEX) - ammount;
		if(mutationLevel < 0) {
			mutationLevel = 0;
		}
		player.getDataWatcher().updateObject(MUTATION_WATCHABLE_INDEX, mutationLevel);	
	}

	public ArrayList getMutagens() {
		return mutagens;
	}

	public void syncData() {
		if(player instanceof EntityPlayerMP) {
			Registry.getInstance().NETWORK.sendTo(new MessageMutagenSync((byte)2, MutagenType.CLEAR), (EntityPlayerMP) player);   				
			for(int i = 0; i < mutagens.size(); i++) {
				Registry.getInstance().NETWORK.sendTo(new MessageMutagenSync((byte)0, mutagens.get(i).getType()), (EntityPlayerMP) player);   		
			}
		}
	}
}
