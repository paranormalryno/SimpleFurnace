package com.paranormalryno.simplefurnace.util.handlers;

import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.ContainerSimpleFurnace;
import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.GuiSimpleFurnace;
import com.paranormalryno.simplefurnace.blocks.machines.simplefurnace.TileEntitySimpleFurnace;
import com.paranormalryno.simplefurnace.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Reference.GUI_SIMPLE_FURNACE) return new ContainerSimpleFurnace(player.inventory, (TileEntitySimpleFurnace)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Reference.GUI_SIMPLE_FURNACE) return new GuiSimpleFurnace(player.inventory, (TileEntitySimpleFurnace)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}
}
