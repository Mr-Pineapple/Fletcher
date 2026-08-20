package com.mrpineapple.fletcher.compat.rei;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrpineapple.fletcher.Fletcher;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class REIFletchingDisplay extends BasicDisplay {
    public static final CategoryIdentifier<REIFletchingDisplay> CATEGORY = CategoryIdentifier.of(Fletcher.MOD_ID, "fletching");
    public static final DisplaySerializer<REIFletchingDisplay> SERIALIZER = DisplaySerializer.of(RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REIFletchingDisplay::getInputEntries),
            EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REIFletchingDisplay::getOutputEntries)).apply(instance, REIFletchingDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), REIFletchingDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), REIFletchingDisplay::getOutputEntries,
                    REIFletchingDisplay::new
                    )
            );

    public REIFletchingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs, Optional.empty());
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CATEGORY;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
