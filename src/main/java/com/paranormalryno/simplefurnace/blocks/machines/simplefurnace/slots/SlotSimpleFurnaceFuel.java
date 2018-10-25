package com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.slots;

import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.TileEntitySimpleFurnace;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSimpleFurnaceFuel extends Slot {

	public SlotSimpleFurnaceFuel(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return TileEntitySimpleFurnace.isItemFuel(stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return super.getItemStackLimit(stack);
	}
}
