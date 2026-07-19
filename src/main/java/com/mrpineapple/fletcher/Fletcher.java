package com.mrpineapple.fletcher;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fletcher implements ModInitializer {
	public static final String MOD_ID = "fletcher";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockPos blockPosition = hitResult.getBlockPos();
			if(!world.getBlockState(blockPosition).is(Blocks.FLETCHING_TABLE)) {
				return InteractionResult.PASS;
			}

			if(world.isClientSide()) {
				return InteractionResult.SUCCESS;
			}

			if(player instanceof ServerPlayer serverPlayer) {
				System.out.println("FLETCHER");
				return InteractionResult.CONSUME;
			}
			return InteractionResult.PASS;
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
