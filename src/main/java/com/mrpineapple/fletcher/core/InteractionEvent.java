package com.mrpineapple.fletcher.core;

import com.mrpineapple.fletcher.screen.FletchingTableMenu;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class InteractionEvent {
    public static void blockInteraction() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos blockPos = hitResult.getBlockPos();
            if(!world.getBlockState(blockPos).is(Blocks.FLETCHING_TABLE)) {
                return InteractionResult.PASS;
            }

            if(player.isShiftKeyDown() && !player.getItemInHand(hand).isEmpty()) {
                return InteractionResult.PASS;
            }

            if(world.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            if(player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return Component.translatable("block.minecraft.fletching_table");
                    }

                    @Override
                    public FletchingTableMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
                        return new FletchingTableMenu(containerId, inventory, blockPos);
                    }
                });
                player.awardStat(ModRegistry.FLETCHING_STAT, 1);
                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        });
    }
}
