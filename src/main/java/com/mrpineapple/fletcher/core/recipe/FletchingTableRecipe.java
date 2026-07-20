package com.mrpineapple.fletcher.core.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrpineapple.fletcher.core.ModRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class FletchingTableRecipe implements Recipe<FletchingRecipeInput> {

    public static final StreamCodec<RegistryFriendlyByteBuf, FletchingTableRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, FletchingTableRecipe::getArrowInput,
            Ingredient.CONTENTS_STREAM_CODEC, FletchingTableRecipe::getPotionInput,
            ItemStackTemplate.STREAM_CODEC, FletchingTableRecipe::getOutput,
            FletchingTableRecipe::new
    );

    public static final MapCodec<FletchingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::getArrowInput),
            Ingredient.CODEC.fieldOf("potion").forGetter(FletchingTableRecipe::getPotionInput),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::getOutput)
    ).apply(inst, FletchingTableRecipe::new));

    public static final RecipeSerializer<FletchingTableRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    private PlacementInfo info;

    public Ingredient getArrowInput() { return arrowInput; }
    private final Ingredient arrowInput ;

    public Ingredient getPotionInput() { return potionInput; }
    private final Ingredient potionInput ;

    public ItemStackTemplate getOutput() { return output; }
    private final ItemStackTemplate output;

    public FletchingTableRecipe(Ingredient ingredient1, Ingredient ingredient2, ItemStackTemplate output) {
        this.arrowInput = ingredient1;
        this.potionInput = ingredient2;
        this.output = output;
    }

    @Override
    public boolean matches(FletchingRecipeInput input, Level level) {
        return arrowInput.test(input.getItem(0)) && potionInput.test(input.getItem(1));
    }

    @Override
    public @NonNull ItemStack assemble(FletchingRecipeInput input) {
        return output.create();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<FletchingRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<FletchingRecipeInput>> getType() {
        return ModRegistry.FLETCHING_RECIPE_TYPE;
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        if(this.info == null) {
            this.info = PlacementInfo.create(List.of(arrowInput, potionInput));
        }
        return this.info;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
