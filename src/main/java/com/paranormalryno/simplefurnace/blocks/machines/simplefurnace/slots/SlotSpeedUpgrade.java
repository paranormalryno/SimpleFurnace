package com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.slots;

import com.paranormalryno.simplefurnace.init.ModItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotSpeedUpgrade extends Slot {

	public SlotSpeedUpgrade(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		Item item = stack.getItem();
		if(item == ModItems.SPEED_UPGRADE) return true;
		return false;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 4;
	}

}
