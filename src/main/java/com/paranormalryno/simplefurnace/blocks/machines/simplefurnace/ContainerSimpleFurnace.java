package com.paranormalryno.simplefurnace.blocks.machines.simplefurnace;

import com.paranormalryno.simplefurnace.SimpleFurnace;
import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.slots.SlotSimpleFurnaceFuel;
import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.slots.SlotSimpleFurnaceOutput;
import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.slots.SlotSpeedUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerSimpleFurnace extends Container {
	private final TileEntitySimpleFurnace tileentity;
	private int cookTime, totalCookTime, cookTime2, totalCookTime2, burnTime, currentBurnTime;
	
	public ContainerSimpleFurnace(InventoryPlayer player, TileEntitySimpleFurnace tileentity) {
		this.tileentity = tileentity;
		
		this.addSlotToContainer(new Slot(tileentity, 0, 56, 20));
		this.addSlotToContainer(new Slot(tileentity, 1, 56, 53));
		this.addSlotToContainer(new SlotSimpleFurnaceFuel(tileentity, 2, 16, 53));
		this.addSlotToContainer(new SlotSimpleFurnaceOutput(player.player, tileentity, 3, 110, 20));
		this.addSlotToContainer(new SlotSimpleFurnaceOutput(player.player, tileentity, 4, 110, 53));
		this.addSlotToContainer(new SlotSpeedUpgrade(tileentity, 5, 144, 20));
		
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		
		for(int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
		}
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileentity);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for(int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener listener = (IContainerListener)this.listeners.get(i);
			
			if(this.cookTime != this.tileentity.getField(2)) listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
			if(this.cookTime2 != this.tileentity.getField(4)) listener.sendWindowProperty(this, 4, this.tileentity.getField(4));
			if(this.burnTime != this.tileentity.getField(0)) listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
			if(this.currentBurnTime != this.tileentity.getField(1)) listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
			if(this.totalCookTime != this.tileentity.getField(3)) listener.sendWindowProperty(this, 3, this.tileentity.getField(3));
			if(this.totalCookTime2 != this.tileentity.getField(5)) listener.sendWindowProperty(this, 5, this.tileentity.getField(5));
		}
		
		this.cookTime = this.tileentity.getField(2);
		this.cookTime2 = this.tileentity.getField(4);
		this.burnTime = this.tileentity.getField(0);
		this.currentBurnTime = this.tileentity.getField(1);
		this.totalCookTime = this.tileentity.getField(3);
		this.totalCookTime2 = this.tileentity.getField(5);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tileentity.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileentity.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();	
			if(index == 3 || index == 4) {
				if(!this.mergeItemStack(stack1, 5, 41, true)) return ItemStack.EMPTY;
				slot.onSlotChange(stack1, stack);
			} else if(index != 2 && index != 1 && index != 0) {				
				if(!FurnaceRecipes.instance().getSmeltingResult(stack1).isEmpty()) {
					if(!this.mergeItemStack(stack1, 0, 2, false)) {
						return ItemStack.EMPTY;
					} else if(TileEntitySimpleFurnace.isItemFuel(stack1)) {
						SimpleFurnace.logger.info("isItemFuel is true");
						if(!this.mergeItemStack(stack1, 2, 3, false)) return ItemStack.EMPTY;
					} else if(index >= 5 && index < 32) {
						if(!this.mergeItemStack(stack1, 32, 41, false)) return ItemStack.EMPTY;
					} else if(index >= 32 && index < 41 && !this.mergeItemStack(stack1, 5, 32, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if(!this.mergeItemStack(stack1, 5, 41, false)) {
				return ItemStack.EMPTY;
			}
			if(stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, stack1);
		}
		return stack;
	}
}
