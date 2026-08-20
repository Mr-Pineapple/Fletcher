package com.mrpineapple.fletcher.screen;

import com.mrpineapple.fletcher.core.ModRegistry;
import com.mrpineapple.fletcher.recipe.FletchingRecipe;
import com.mrpineapple.fletcher.recipe.FletchingRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

public class FletchingTableMenu extends AbstractContainerMenu {
    private final Container input = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            FletchingTableMenu.this.slotsChanged(this);
        }
    };

    private final ResultContainer output = new ResultContainer();
    private final Level level;
    private final BlockPos pos;

    public FletchingTableMenu(int i, Inventory inventory, BlockPos position) {
        super(ModRegistry.FLETCHING_MENU, i);

        this.level = inventory.player.level();
        this.pos = position;


        addSlot(new Slot(this.input, 0, 27, 35) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(Items.ARROW) || itemStack.is(Items.BOW) || itemStack.is(Items.CROSSBOW);
            }

            @Override
            public boolean allowModification(Player player) {
                return true;
            }
        });
        addSlot(new Slot(this.input, 1, 76, 35) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(Items.STRING) || itemStack.is(Items.GLOWSTONE_DUST) || itemStack.is(Items.POTION);
            }

            @Override
            public boolean allowModification(Player player) {
                return true;
            }
        });

        addSlot(new Slot(this.output, 0, 134, 35) {
            @Override
            public void onTake(Player player, ItemStack carried) {
                FletchingTableMenu.this.onTake(player, carried);
            }

            @Override
            public boolean allowModification(Player player) {
                return false;
            }
        });

        addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        if(container == this.input) {
            if(this.level instanceof ServerLevel serverLevel) {
                FletchingRecipeInput recipeInput = new FletchingRecipeInput(this.input.getItem(0), this.input.getItem(1));
                Optional<RecipeHolder<FletchingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(
                        ModRegistry.FLETCHING_RECIPE_TYPE,
                        recipeInput,
                        serverLevel);

                if(recipe.isPresent()) {
                    this.output.setItem(0, recipe.get().value().assemble(recipeInput));
                    this.output.setRecipeUsed(recipe.get());
                } else {
                    this.output.clearContent();
                    this.output.setRecipeUsed(null);
                }
            }
        }
    }

    public void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player, stack.getCount());
        this.output.awardUsedRecipes(player, List.of(this.input.getItem(0), this.input.getItem(1)));

        ItemStack base = this.input.getItem(0).copy();
        ItemStack modifier = this.input.getItem(1).copy();

        if(base.is(Items.ARROW)) {
            //Tipped arrow recipes consume amount of arrows
            if(modifier.is(Items.POTION)) {
                this.input.removeItem(0, stack.getCount());
                this.input.removeItem(1, 1);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if(this.input.getItem(1).isEmpty()) {
                    this.input.setItem(1, bottle);
                } else {
                    player.getInventory().placeItemBackInInventory(bottle);
                }
            }
            if(modifier.is(Items.GLOWSTONE_DUST)) {
                this.input.removeItem(0, stack.getCount());
                int glowstoneUsed = (int) Math.ceil(stack.getCount() / 8.0);
                this.input.removeItem(1, glowstoneUsed);
            }
        }
        else if((base.is(Items.BOW) || base.is(Items.CROSSBOW)) && modifier.is(Items.STRING)) {
            //Bow repair consumes all string used
            this.input.removeItem(0, 1);
            this.input.removeItem(1, modifier.getCount());
        } else {
            //Default recipe
            this.input.removeItem(0, 1);
            this.input.removeItem(1, 1);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        original = stack.copy();

        // Output slot
        if (slotIndex == 2) {
            if (!this.moveItemStackTo(stack, 3, 39, true)) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, original);
            this.onTake(player, original);
        }

        // Input slots
        else if (slotIndex == 0 || slotIndex == 1) {
            if (!this.moveItemStackTo(stack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
        }

        // Player inventory / hotbar
        else {
            boolean moved = false;

            // Try merging into the base slot
            if (this.slots.get(0).mayPlace(stack)) {
                moved = this.moveItemStackTo(stack, 0, 1, false);
            }

            // Then try the modifier slot
            if (!stack.isEmpty() && this.slots.get(1).mayPlace(stack)) {
                moved |= this.moveItemStackTo(stack, 1, 2, false);
            }

            // Otherwise move between inventory and hotbar
            if (!moved) {
                if (slotIndex < 30) {
                    if (!this.moveItemStackTo(stack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(stack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, stack);

        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        if(player.level().isClientSide()) return true;
        return player.blockPosition().closerThan(this.pos, 8.0);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        clearContainer(player, this.input);
    }


}
