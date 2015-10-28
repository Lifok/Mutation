package fr.utopics.eikins.mutation.mutagen;

import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.item.ItemMutagen;
import fr.utopics.eikins.mutation.network.MessageMutagenSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Mutagen {

	private int color, amplifier;
	protected boolean hasUpgrade;
	protected int[] upgrade;
	private String name;
	private MutagenType type;
	private ItemMutagen item;

	public Mutagen(String mutagenName, MutagenType mutagenType) {
		name = mutagenName;
		color = 0xFFFFFF;
		type = mutagenType;
	}
	
    public NBTTagCompound saveToNBT(NBTTagCompound nbt) {
    	if(hasUpgrade) {
    		nbt.setIntArray("Upgrade", upgrade);
    	}
    	nbt.setInteger("Type", type.ordinal());
        nbt.setString("Name", name);
        nbt.setByte("Amplifier", (byte)this.amplifier);
        nbt.setInteger("Color", color);
        return nbt;
    }
    
	public static Mutagen loadFromNBT(NBTTagCompound nbt) {
		Mutagen mutagen = Registry.getInstance().getMutagen(MutagenType.values()[nbt.getInteger("Type")]);
		if(nbt.hasKey("Upgrade", 11)) {
			mutagen.setUpgrade(nbt.getIntArray("Upgrade"));
		}
		mutagen.setAmplifier(nbt.getByte("Amplifier"));
		mutagen.setColor(nbt.getInteger("Color"));
		return mutagen;
	}
	
	/** 
	 * @return the quantity of mutation to increase each raises. (4secondes)
	 */
	public abstract int getMutationRaise(int amplifier);

	public void setUpgrade(int[] upgrade) {
		this.upgrade =  upgrade;
		this.hasUpgrade = true;
	}
	
	public boolean hasUpgrade() {
		return hasUpgrade;
	}

	public void onMutagenAdded(EntityPlayer player) {
		if(player instanceof EntityPlayerMP) {
			Registry.NETWORK.sendTo(new MessageMutagenSync((byte)0, this.type), (EntityPlayerMP)player);
		}
	}
	
	public void onMutagenRemoved(EntityPlayer player) {
		if(player instanceof EntityPlayerMP) {
			Registry.NETWORK.sendTo(new MessageMutagenSync((byte)1, this.type), (EntityPlayerMP)player);
		}
	}
	
	public Mutagen setColor(int mutagenColor) {
		color = mutagenColor;
		return this;
	}
	
	public Mutagen setAmplifier(int mutagenAmplifier) {
		amplifier = mutagenAmplifier;
		return this;
	}
	
	public void setItem(ItemMutagen mutagenItem) {
		item = mutagenItem;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getAmplifier() {
		return amplifier;
	}
	
	public ItemMutagen getItem() {
		return item;
	}
	
	@SideOnly(Side.CLIENT)
	public String getDescription() {
		return "mutagen.description." + name;
	}
	
	public abstract String getEffectString(int amplifier);

	public String getName() {
		return name;
	}

	public MutagenType getType() {
		return type;
	}
	
}
