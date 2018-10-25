package com.paranormalryno.simplefurnace.blocks.machines.simplefurnace;

import com.paranormalryno.simplefurnace.util.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSimpleFurnace extends GuiContainer {
	
	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/simple_furnace.png");
	private final InventoryPlayer player;
	private final TileEntitySimpleFurnace tileentity;
	
	public GuiSimpleFurnace(InventoryPlayer player, TileEntitySimpleFurnace tileentity) {
		super(new ContainerSimpleFurnace(player, tileentity));
		this.player = player;
		this.tileentity = tileentity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tileName = this.tileentity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2), 7, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		if(TileEntitySimpleFurnace.isBurning(tileentity)) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(this.guiLeft + 17, this.guiTop + 37 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		int l = this.getCookProgressScaled(24, 2, 3);
		this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 21, 176, 14, l + 1, 16);
		
		int m = this.getCookProgressScaled(24, 4, 5);
		this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 54, 176, 14, m + 1, 16);
	}
	
	private int getBurnLeftScaled(int pixels) {
		int i = this.tileentity.getField(1);
		if(i == 0) i = 200;
		return this.tileentity.getField(0) * pixels / i;
	}
	
	private int getCookProgressScaled(int pixels, int slot, int slot2) {
		int i = this.tileentity.getField(slot);
		int j = this.tileentity.getField(slot2);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
