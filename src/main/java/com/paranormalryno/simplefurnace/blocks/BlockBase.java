package com.paranormalryno.simplefurnace.blocks;

import com.paranormalryno.simplefurnace.SimpleFurnace;
import com.paranormalryno.simplefurnace.init.ModBlocks;
import com.paranormalryno.simplefurnace.init.ModItems;
import com.paranormalryno.simplefurnace.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {
	
	public BlockBase(String name, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(SimpleFurnace.tabSimpleFurnace);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels() {
		SimpleFurnace.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

}
