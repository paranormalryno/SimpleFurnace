package com.paranormalryno.simplefurnace.items;

import com.paranormalryno.simplefurnace.SimpleFurnace;
import com.paranormalryno.simplefurnace.init.ModItems;
import com.paranormalryno.simplefurnace.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
	
	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		SimpleFurnace.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
