package com.mrpineapple.fletcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrpineapple.fletcher.core.ModRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class FletchingRecipe implements Recipe<FletchingRecipeInput> {
    private final ItemStackTemplate result;
    private final Ingredient baseItem;
    private final Ingredient modifierItem;

    public static final MapCodec<FletchingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(FletchingRecipe::getResult),
            Ingredient.CODEC.fieldOf("baseItem").forGetter(FletchingRecipe::getBaseItem),
            Ingredient.CODEC.fieldOf("modifierItem").forGetter(FletchingRecipe::getModifierItem)
            ).apply(instance, FletchingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FletchingRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            FletchingRecipe::getResult,
            Ingredient.CONTENTS_STREAM_CODEC,
            FletchingRecipe::getBaseItem,
            Ingredient.CONTENTS_STREAM_CODEC,
            FletchingRecipe::getModifierItem,
            FletchingRecipe::new
    );

    public FletchingRecipe(ItemStackTemplate result, Ingredient baseItem, Ingredient modifierItem) {
        this.baseItem = baseItem;
        this.modifierItem = modifierItem;
        this.result = result;
    }

    public ItemStackTemplate getResult() {
        return this.result;
    }

    public Ingredient getBaseItem() {
        return this.baseItem;
    }

    public Ingredient getModifierItem() {
        return this.modifierItem;
    }

    @Override
    public boolean matches(FletchingRecipeInput input, Level level) {
        if(input.baseItem().is(Items.BOW) || input.baseItem().is(Items.CROSSBOW)) {
            return input.baseItem().getDamageValue() > 0;
        }

        return this.baseItem.test(input.baseItem()) && this.modifierItem.test(input.modifierItem());
    }

    @Override
    public ItemStack assemble(FletchingRecipeInput input) {
        ItemStack result = this.result.create();
        ItemStack modifier = input.modifierItem();
        ItemStack base = input.baseItem();

        if((base.is(Items.BOW) || base.is(Items.CROSSBOW)) && modifier.is(Items.STRING)) {
            return repairBowWithString(base, modifier.getCount());
        }

        if(base.is(Items.ARROW)) {
            result.setCount(Math.min(base.getCount(), 8));

            //Potion -> Tipped Arrow
            if(modifier.is(Items.POTION)) {
                PotionContents contents = modifier.get(DataComponents.POTION_CONTENTS);
                if(contents != null) {
                    result.set(DataComponents.POTION_CONTENTS, contents);
                }
            }
            if(modifier.is(Items.GLOWSTONE_DUST)) {
                result.setCount(Math.min(base.getCount(), modifier.getCount() * 8));
            }
        }

        return result;
    }

    private ItemStack repairBowWithString(ItemStack bow, int stringAmount) {
        ItemStack result = bow.copy();
        int maxDamage = result.getMaxDamage();
        int repairAmount = (maxDamage / 8) * stringAmount;
        int newDamage = Math.max(0, result.getDamageValue() - repairAmount);
        result.setDamageValue(newDamage);
        return result;
    }

    @Override
    public RecipeSerializer<? extends Recipe<FletchingRecipeInput>> getSerializer() {
        return ModRegistry.FLETCHING_RECIPE_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<FletchingRecipeInput>> getType() {
        return ModRegistry.FLETCHING_RECIPE_TYPE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "fletching";
    }
}
