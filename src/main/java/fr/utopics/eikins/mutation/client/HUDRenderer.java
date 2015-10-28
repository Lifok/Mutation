package fr.utopics.eikins.mutation.client;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import fr.utopics.eikins.mutation.ModMutation;
import fr.utopics.eikins.mutation.entity.player.ExtendedPlayer;
import fr.utopics.eikins.mutation.mutagen.Mutagen;

public class HUDRenderer {
	
	private Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void renderHUDObjects(RenderGameOverlayEvent.Post event)
	{
	    if(event.isCancelable() || event.type != ElementType.EXPERIENCE)
	    {      
	      return;
	    }
	    if((ExtendedPlayer.get(mc.thePlayer).getMutationLevel() > 0 || ExtendedPlayer.get(mc.thePlayer).isMutant()) && !mc.thePlayer.capabilities.isCreativeMode) {
			renderMutationBar(event);
	    }
	    if(ExtendedPlayer.get(mc.thePlayer).isMutant()) {
	    	renderMutagenIcons(event);
	    }
	}

	private void renderMutationBar(RenderGameOverlayEvent event) {
		int x = event.resolution.getScaledWidth();
		int y = event.resolution.getScaledHeight();
	    GL11.glDisable(GL11.GL_LIGHTING);      
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		ExtendedPlayer player = ExtendedPlayer.get(mc.thePlayer);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(ModMutation.MODID, "textures/gui/mutation.png"));
		this.drawTexturedModalRect(1, y - 6, 0, 5, 82, 5);
		this.drawTexturedModalRect(2, y - 5, 1, 1, (int)((float)player.getMutationLevel() / (float)player.getMaxMutationLevel() * 80), 3);
	}
	
	private void renderMutagenIcons(RenderGameOverlayEvent event) {
		int x = event.resolution.getScaledWidth();
		int y = event.resolution.getScaledHeight();
		ExtendedPlayer player = ExtendedPlayer.get(mc.thePlayer);
		ArrayList<Mutagen> mutagens = player.getMutagens();
		int k = 23;
		if(mc.thePlayer.capabilities.isCreativeMode) {
			k -= 6;
		}
		for(int i = 0; i < mutagens.size(); i++) {
			this.mc.getTextureManager().bindTexture(new ResourceLocation(ModMutation.MODID, "textures/items/" + mutagens.get(i).getName() + ".png"));
			this.draw16pxTexture(1 + 16*i, y - k);
		}
	}
	
	public void drawTexturedModalRect(double par1, double par2, double par3, double par4, double par5, double par6) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV((double)par1, (double)(par2 + par6), (double)-90F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
		worldrenderer.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)-90F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
		worldrenderer.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)-90F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
		worldrenderer.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)-90F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
		tessellator.draw();
	}
	
	public void draw16pxTexture(double x, double y) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(x, y + 16, -90F, 0, 1);
		worldrenderer.addVertexWithUV(x + 16, y + 16, -90F, 1, 1);
		worldrenderer.addVertexWithUV(x + 16, y, -90F, 1, 0);
		worldrenderer.addVertexWithUV(x, y, -90F, 0, 0);
		tessellator.draw();
	}

}
