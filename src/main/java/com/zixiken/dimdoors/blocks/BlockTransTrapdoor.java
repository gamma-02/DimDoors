package com.zixiken.dimdoors.blocks;

import java.util.Random;

import com.zixiken.dimdoors.DimDoors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import com.zixiken.dimdoors.tileentities.TileEntityTransTrapdoor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTransTrapdoor extends BlockTrapDoor implements IDimDoor, ITileEntityProvider {
	public static final String ID = "blockDimHatch";

	public BlockTransTrapdoor() {
		super(Material.wood);
		this.setCreativeTab(DimDoors.dimDoorsCreativeTab);
        setHardness(1.0F);
        setUnlocalizedName(ID);
	}

	//Teleports the player to the exit link of that dimension, assuming it is a pocket
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity) {enterDimDoor(world, pos, entity);}

	public boolean checkCanOpen(World world, BlockPos pos) {return this.checkCanOpen(world, pos, null);}
	
	public boolean checkCanOpen(World world, BlockPos pos, EntityPlayer player) {
		return true;
	}

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return checkCanOpen(worldIn, pos, playerIn) &&
                super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY,  hitZ);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if(checkCanOpen(worldIn, pos)) super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    @Override
	public void enterDimDoor(World world, BlockPos pos, Entity entity) {
        IBlockState state = world.getBlockState(pos);
		if (!world.isRemote && state.getValue(BlockTrapDoor.OPEN)) {
			if (entity instanceof EntityPlayer) {
                state.cycleProperty(BlockTrapDoor.OPEN);
                world.markBlockRangeForRenderUpdate(pos, pos);
                world.playAuxSFXAtEntity(null, 1006, pos, 0);
            }
		}
	}	

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		this.placeLink(world, pos);
		world.setTileEntity(pos, createNewTileEntity(world, getMetaFromState(state)));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {return new TileEntityTransTrapdoor();}

	@Override
	public void placeLink(World world, BlockPos pos) {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this.getItemDoor(), 1, 0);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortuneLevel) {
        return Item.getItemFromBlock(Blocks.trapdoor);
    }
	
	@Override
	public Item getItemDoor() {return Item.getItemFromBlock(ModBlocks.blockDimHatch);}
	
	public static boolean isTrapdoorSetLow(IBlockState state) {
        return state.getValue(BlockTrapDoor.HALF) == DoorHalf.BOTTOM;
    }
	
	@Override
	public TileEntity initDoorTE(World world, BlockPos pos) {
		TileEntity te = createNewTileEntity(world, getMetaFromState(world.getBlockState(pos)));
		world.setTileEntity(pos, te);
		return te;
	}

	@Override
	public boolean isDoorOnRift(World world, BlockPos pos) {return true;}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		// This function runs on the server side after a block is replaced
		// We MUST call super.breakBlock() since it involves removing tile entities
        super.breakBlock(world, pos, state);
    }
}