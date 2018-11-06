package com.paranormalryno.simplefurnace.blocks.machines.simplefurnace;

import com.paranormalryno.simplefurnace.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySimpleFurnace extends TileEntity implements IInventory, ITickable, ISidedInventory {

	private static final int[] SLOTS_TOP = new int[] {0,1};
	private static final int[] SLOTS_BOTTOM = new int[] {3,4};
	private static final int[] SLOTS_SIDES = new int[] {2};
	
	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
	private String customName;
	
	private int burnTime;
	private int currentBurnTime;
	private int cookTime;
	private int cookTime2;
	private int totalCookTime;
	private int totalCookTime2;
	
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.simple_furnace";
	}
	
	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}
	
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}
	
	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.inventory) {
			if (!stack.isEmpty()) return false;
		}
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return (ItemStack)this.inventory.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack)this.inventory.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.inventory.set(index, stack);
		
		if(stack.getCount() > this.getInventoryStackLimit())
			stack.setCount(this.getInventoryStackLimit());
		if(index == 0 && !flag) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		} else if (index == 1 && !flag) {
			this.totalCookTime2 = this.getCookTime(stack);
			this.cookTime2 = 0;
			this.markDirty();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.cookTime2 = compound.getInteger("CookTime2");
		this.totalCookTime2 = compound.getInteger("TotalCookTime2");
		this.currentBurnTime = getItemBurnTime((ItemStack)this.inventory.get(2));
		
		if(compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short)this.burnTime);
		compound.setInteger("CookTime", (short)this.cookTime);
		compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
		compound.setInteger("CookTime2", (short)this.cookTime2);
		compound.setInteger("TotalCookTime2", (short)this.totalCookTime2);
		ItemStackHelper.saveAllItems(compound, this.inventory);
		
		if(this.hasCustomName()) compound.setString("CustomName", this.customName);
		return compound;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}
	
	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;
		
		if(this.isBurning()) {
			if(this.canSmelt(0, 3) && this.canSmelt(1, 4)) {
				this.burnTime = this.burnTime - 2;
			} else --this.burnTime;
		}
		if(!this.world.isRemote) {
			ItemStack fuel = (ItemStack)this.inventory.get(2);
			if(this.isBurning() || (!fuel.isEmpty() && !(((ItemStack)this.inventory.get(0)).isEmpty())) || (!fuel.isEmpty() && !(((ItemStack)this.inventory.get(1)).isEmpty()))) {
				if((!this.isBurning() && this.canSmelt(0, 3)) || (!this.isBurning() && this.canSmelt(1, 4)) || (!this.isBurning() && this.canSmelt(0, 3) && this.canSmelt(1, 4))) {
					this.burnTime = getItemBurnTime(fuel);
					this.currentBurnTime = this.burnTime;
					
					if(this.isBurning()) {
						flag1 = true;
						if(!fuel.isEmpty()) {
							Item item = fuel.getItem();
							fuel.shrink(1);
						
							if(fuel.isEmpty()) {
								ItemStack item1 = item.getContainerItem(fuel);
								this.inventory.set(2, item1);
							}
						}
					}
				}
			
				if(this.isBurning() && this.canSmelt(0, 3)) {
					++this.cookTime;
					
					if(this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime((ItemStack)this.inventory.get(0));
						this.smeltItem(0, 3);
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
				if (this.isBurning() && this.canSmelt(1, 4)) {
					++this.cookTime2;
					
					if(this.cookTime2 == this.totalCookTime2) {
						this.cookTime2 = 0;
						this.totalCookTime2 = this.getCookTime((ItemStack)this.inventory.get(1));
						this.smeltItem(1, 4);
						flag1 = true;
						
					}
				} else {
					this.cookTime2 = 0;
				}
			
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			} else if (!this.isBurning() && this.cookTime2 > 0) {
				this.cookTime2 = MathHelper.clamp(this.cookTime2 - 2, 0, this.totalCookTime2);
			}
			if(flag != this.isBurning()) {
				flag = true;
				
				BlockSimpleFurnace.setState(this.isBurning(), this.world, this.pos);
			}
		}
		if (flag1) this.markDirty();
	}
	
	public int getCookTime(ItemStack input1) {
		if(inventory.get(5).getItem() == ModItems.SPEED_UPGRADE) {
			int speed = this.inventory.get(5).getCount();
			return 200 - (speed * 40);
		}
		return 200;
	}
	
	private boolean canSmelt(int inputSlot, int outputSlot) {
		if (((ItemStack)this.inventory.get(inputSlot)).isEmpty()) return false;
		else {
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult((ItemStack)this.inventory.get(inputSlot));
			if(result.isEmpty()) return false;
			else {
				ItemStack output = (ItemStack)this.inventory.get(outputSlot);
				if(output.isEmpty()) return true;
				if(!output.isItemEqual(result)) return false;
				int res = output.getCount() + result.getCount();
				
				return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();
			}
		}
	}
	
	public void smeltItem(int inputSlot, int outputSlot) {
		if(this.canSmelt(inputSlot, outputSlot)) {
			ItemStack input = (ItemStack)this.inventory.get(inputSlot);
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
			ItemStack output = (ItemStack)this.inventory.get(outputSlot);
			
			if(output.isEmpty()) this.inventory.set(outputSlot, result.copy());
			else if(output.getItem() == result.getItem()) output.grow(result.getCount());
			input.shrink(1);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static int getItemBurnTime(ItemStack fuel) {
		if(fuel.isEmpty()) return 0;
		else {
			Item item = fuel.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.WOODEN_SLAB) return 150;
				if (block.getDefaultState().getMaterial() == Material.WOOD) return 300;
				if (block == Blocks.COAL_BLOCK) return 16000;
			}

			if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
			if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
			if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
			if (item == Items.STICK) return 100;
			if (item == Items.COAL) return 1600;
			if (item == Items.LAVA_BUCKET) return 20000;
			if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
			if (item == Items.BLAZE_ROD) return 2400;

			return GameRegistry.getFuelValue(fuel);
		}
	}
		
	public static boolean isItemFuel(ItemStack fuel) {
		return getItemBurnTime(fuel) > 0;
	}
	
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 3) return false;
		else if(index != 2) return true;
		else return isItemFuel(stack);
	}
	
	public String getGuiI() {
		return "tm:advanced_furnace";
	}

	public int getField(int id) {
		switch(id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		case 4:
			return this.cookTime2;
		case 5:
			return this.totalCookTime2;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch(id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
			break;
		case 4:
			this.cookTime2 = value;
			break;
		case 5:
			this.totalCookTime2 = value;
		}
	}
	
	@Override
	public int getFieldCount() {
		return 6;
	}
	
	@Override
	public void clear() {
		this.inventory.clear();
	}
	
	public boolean isItemValid(int index, ItemStack stack) {
		if(index == 3 || index == 4 || index == 5) {
			return false;
		} else if(index == 0 || index == 1) {
			return true;
		} else {
			ItemStack itemstack = this.inventory.get(1);
            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
	}
}
