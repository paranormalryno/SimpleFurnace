package com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSimpleFurnaceOutput extends Slot {
	
	@SuppressWarnings("unused")
	private final EntityPlayer player;
	@SuppressWarnings("unused")
	private int removeCount;

	public SlotSimpleFurnaceOutput(EntityPlayer player, IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.player = player;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}
	
	@Override
	public ItemStack decrStackSize(int amount) {
		if(this.getHasStack()) this.removeCount += Math.min(amount, this.getStack().getCount());
		return super.decrStackSize(amount);
	}
}
