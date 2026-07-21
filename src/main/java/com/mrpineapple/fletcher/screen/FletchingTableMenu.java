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
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

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

    public FletchingTableMenu(int i, Inventory inventory) {
        super(ModRegistry.FLETCHING_MENU, i);

        this.level = inventory.player.level();

        addSlot(new Slot(this.input, 0, 27, 35));
        addSlot(new Slot(this.input, 1, 76, 35));

        addSlot(new Slot(this.output, 0, 134, 35) {
            @Override
            public void onTake(Player player, ItemStack carried) {
                FletchingTableMenu.this.onTake(player, carried);
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

        ItemStack modifier = this.input.getItem(1).copy();

        this.input.removeItem(0, stack.getCount());
        this.input.removeItem(1, stack.getCount());

        if(modifier.is(Items.POTION)) {
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if(this.input.getItem(1).isEmpty()) {
                this.input.setItem(1, bottle);
            } else {
                player.getInventory().placeItemBackInInventory(bottle);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        clearContainer(player, this.input);
    }


}
