package com.paranormalryno.simplefurnace.tabs;

import com.paranormalryno.simplefurnace.init.ModBlocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SimpleFurnaceTab extends CreativeTabs {

	public SimpleFurnaceTab(int index, String label) {
		super(index, label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModBlocks.SIMPLE_FURNACE);
	}
	
	

}
