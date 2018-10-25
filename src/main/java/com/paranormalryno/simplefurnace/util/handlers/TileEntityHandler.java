package com.paranormalryno.simplefurnace.util.handlers;

import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.TileEntitySimpleFurnace;
import com.paranormalryno.simplefurnace.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
	public static void registerTileEntites() {
		GameRegistry.registerTileEntity(TileEntitySimpleFurnace.class, new ResourceLocation(Reference.MOD_ID + "simple_furnace"));
	}
}
