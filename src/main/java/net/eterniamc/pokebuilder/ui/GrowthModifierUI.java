package net.eterniamc.pokebuilder.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import lombok.RequiredArgsConstructor;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.pokebuilder.controller.GrowthController;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.component.PokeballComponent;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.LangUtils;
import net.eterniamc.pokebuilder.util.TextUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

@RequiredArgsConstructor
public class GrowthModifierUI extends DynamicUI {
    private static final int[] LAYOUT = { 10, 11, 12, 19, 20, 21, 28, 29, 30 };

    private final Pokemon pokemon;

    @Override
    public void initialize() {
        super.initialize();

        addComponent(new PokeballComponent(14, pokemon));
    }

    @Override
    public void generateInventory() {
        inventory = createInventory(LangUtils.get("modifier.growth.ui.name"), 5);
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

        for (int i = 0; i < LAYOUT.length; i++) {
            int slot = LAYOUT[(i + 1) % 9];
            EnumGrowth growth = EnumGrowth.getGrowthFromIndex(i);

            addListener(slot, (player1, event) -> UserInterfaceUtils.createConfirmation(
                    pokemon,
                    ModifierType.GROWTH,
                    () -> pokemon.setGrowth(growth)
            ));
        }
    }

    @Override
    public void render() {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE);
            ItemUtils.setDisplayName(stack, " ");
            setItem(i, stack);
        }

        for (int i = 0; i < LAYOUT.length; i++) {
            int slot = LAYOUT[(i + 1) % 9];
            EnumGrowth growth = EnumGrowth.values()[i];

            ItemStack stack = new ItemStack(Blocks.CONCRETE, 1, i);
            ItemUtils.setDisplayName(stack, TextUtils.text(TextUtils.getFormattingForDyeColor(EnumDyeColor.byMetadata(i)) + growth.getLocalizedName()));
            ItemUtils.setItemLore(stack, GrowthController.INSTANCE.getPriceLine(pokemon));
            setItem(slot, stack);
        }

        super.render();
    }
}
