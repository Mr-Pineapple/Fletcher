package com.mrpineapple.fletcher.screen;

import com.mrpineapple.fletcher.core.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class FletchingTableMenu extends AbstractContainerMenu {
    private static final int SLOT_ARROW = 0;
    private static final int SLOT_MODIFIER = 1;
    private static final int SLOT_OUT = 2;
    private final BlockPos blockPos;
    private final Player interactionPlayer;

    public FletchingTableMenu(int syncId, Inventory inventory, BlockPos pos) {
        super(ModRegistry.FLETCHING_MENU, syncId);
        this.blockPos = pos;
        this.interactionPlayer = inventory.player;

        SimpleContainer inventory1 = new SimpleContainer(3) {};

        this.addSlot(new Slot(inventory1, SLOT_ARROW, 27, 35) {});
        this.addSlot(new Slot(inventory1, SLOT_MODIFIER, 76, 35) {});
        this.addSlot(new Slot(inventory1, SLOT_OUT, 134, 35) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return false;
            }
        });

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for(int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
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
