package com.mrpineapple.fletcher.screen;

import com.mrpineapple.fletcher.core.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class FletchingTableMenu extends AbstractContainerMenu {
    private final BlockPos blockPos;
    private final Player interactionPlayer;

    public FletchingTableMenu(int syncId, Inventory inventory, BlockPos pos) {
        super(ModRegistry.FLETCHING_MENU, syncId);
        this.blockPos = pos;
        this.interactionPlayer = inventory.player;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        if(interactionPlayer.level().isClientSide()) return true;
        return interactionPlayer.blockPosition().closerThan(this.blockPos, 8.0);
    }

}
