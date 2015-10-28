package fr.utopics.eikins.mutation.network;

import fr.utopics.eikins.mutation.Registry;
import fr.utopics.eikins.mutation.entity.player.ExtendedPlayer;
import fr.utopics.eikins.mutation.mutagen.MutagenType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageMutagenSync implements IMessage, IMessageHandler<MessageMutagenSync, IMessage>{

	private byte action;
	private int type;

	public MessageMutagenSync() {		
	}

	/**
	 * @param action 0 = onMutagenAdded, 1 = onMutagenRemoved, 2 = clearList
	 * @param type The type of the mutagen.
	 */
	public MessageMutagenSync(byte action, MutagenType type) {
		this.action = action;
		this.type = type.ordinal();
	}

	@Override
	public void fromBytes(ByteBuf buf) {		
		type = buf.readInt();
		action = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
		buf.writeInt(type);
		buf.writeByte(action);
	}

	@Override
	public IMessage onMessage(final MessageMutagenSync message, final MessageContext ctx) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				processMessage(message, ctx);
			}
		};
		if(ctx.side == Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(task);
		}
		return null;
	}
	
	private void processMessage(MessageMutagenSync message, MessageContext ctx) {
		switch (message.action) {
		case 0: ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).addMutagen(Registry.getInstance().getMutagen(MutagenType.values()[message.type]));
		break;
		case 1:	ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).removeMutagen(Registry.getInstance().getMutagen(MutagenType.values()[message.type]));	
		break;
		case 2: ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).clearMutagens();
		break;
		}
	}

}
