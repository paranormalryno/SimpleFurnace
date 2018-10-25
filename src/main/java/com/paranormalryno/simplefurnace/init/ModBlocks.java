package com.paranormalryno.simplefurnace.init;

import java.util.ArrayList;
import java.util.List;

import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.BlockSimpleFurnace;

import net.minecraft.block.Block;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block SIMPLE_FURNACE = new BlockSimpleFurnace("simple_furnace");
}
