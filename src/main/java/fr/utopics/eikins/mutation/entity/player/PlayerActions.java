package fr.utopics.eikins.mutation.entity.player;

import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import fr.utopics.eikins.mutation.mutagen.Mutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenType;

public class PlayerActions {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(!event.player.worldObj.isRemote && event.phase.equals(Phase.END)) {
			ExtendedPlayer.get(event.player).callTickedMutagens();	
			ExtendedPlayer.get(event.player).update();
		}
	}
	
	@SubscribeEvent
	public void breakSpeed(PlayerEvent.BreakSpeed event) {
		if(ExtendedPlayer.get(event.entityPlayer).hasMutagen(MutagenType.GOLD_MUTAGEN)) {
			Mutagen mutagen = ExtendedPlayer.get(event.entityPlayer).getMutagen(MutagenType.GOLD_MUTAGEN);
			event.newSpeed = event.originalSpeed * (1f + 0.15f + 0.15f * mutagen.getAmplifier());
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) event.entity) == null) {
			ExtendedPlayer.register((EntityPlayer) event.entity);
		}
		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(ExtendedPlayer.EXT_PROP_NAME) == null) {
			event.entity.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer((EntityPlayer) event.entity));
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		if(event.entityLiving instanceof EntityPlayer && event.source.getSourceOfDamage() != null) {
			if(event.source.getSourceOfDamage() instanceof EntityZombie) {
				ExtendedPlayer.get((EntityPlayer)event.entityLiving).addMutation((int)event.ammount * 5000);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent e) {
		if (e.entity instanceof EntityPlayerMP) {	
			ExtendedPlayer.get((EntityPlayerMP) e.entity).syncData();
		}
	}


}
