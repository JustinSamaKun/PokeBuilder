package net.eterniamc.pokebuilder.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsValuables;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import lombok.RequiredArgsConstructor;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.controller.IVController;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.component.PokeballComponent;
import net.eterniamc.pokebuilder.util.CurrencyUtils;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.LangUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.pixelmonmod.pixelmon.config.PixelmonItemsHeld.*;

@RequiredArgsConstructor
public class IVModifierUI extends DynamicUI {
    private static final int[] LAYOUT = { 1, 10, 19, 28, 37, 46 };
    private static final Item[] DISPLAY = { powerWeight, powerBracer, powerBelt, powerLens, powerBand, powerAnklet };
    private static final String[] NAME = { "HP", "Attack", "Defence", "Special Attack", "Special Defence", "Speed" };
    private static final int MAX_IVS = 41;
    private static final int RANDOM_IVS = 43;

    private final Pokemon pokemon;

    @Override
    public void initialize() {
        super.initialize();

        addComponent(new PokeballComponent(14, pokemon));
    }

    @Override
    public void generateInventory() {
        inventory = createInventory(LangUtils.get("modifier.iv.ui.name"), 6);
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

        int[] ivs = pokemon.getIVs().getArray();
        for (int i = 0; i < LAYOUT.length; i++) {
            int slot = LAYOUT[i] + 1;
            int value = ivs[i];
            int k = i;

            addListener(slot, (player, action) -> {
                int target = Math.max(0, value - (action.getClickType() == ClickType.QUICK_MOVE ? 10 : 1));
                CurrencyUtils.wrapAction(
                        player,
                        action.getClickType() == ClickType.QUICK_MOVE ? ModifierType.DECREASE_IV_10 : ModifierType.DECREASE_IV,
                        pokemon,
                        () -> {
                            ivs[k] = target;
                            pokemon.getIVs().fillFromArray(ivs);
                        }
                );
                render();
            });
            slot = LAYOUT[i] + 2;

            addListener(slot, (player, action) -> {
                int target = Math.min(31, value + (action.getClickType() == ClickType.QUICK_MOVE ? 10 : 1));
                CurrencyUtils.wrapAction(
                        player,
                        action.getClickType() == ClickType.QUICK_MOVE ? ModifierType.INCREASE_IV_10 : ModifierType.INCREASE_IV,
                        pokemon,
                        () -> {
                            ivs[k] = target;
                            pokemon.getIVs().fillFromArray(ivs);
                        }
                );
                render();
            });
        }

        addListener(MAX_IVS, (player, action) ->
            UserInterfaceUtils.createConfirmation(pokemon, ModifierType.MAX_IV, pokemon.getIVs()::maximizeIVs)
        );

        addListener(RANDOM_IVS, (player, action) ->
            UserInterfaceUtils.createConfirmation(pokemon, ModifierType.MAX_IV, () -> {
                pokemon.getIVs().CopyIVs(IVStore.CreateNewIVs3Perfect());
                pokemon.markDirty(EnumUpdateType.IVs);
            })
        );
    }

    @Override
    public void render() {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE);
            ItemUtils.setDisplayName(stack, " ");
            setItem(i, stack);
        }

        int[] ivs = pokemon.getIVs().getArray();
        for (int i = 0; i < LAYOUT.length; i++) {
            int slot = LAYOUT[i];
            int value = ivs[i];

            ItemStack display = new ItemStack(DISPLAY[i]);
            ItemUtils.setDisplayName(display, NAME[i]);
            display.setCount(value);
            setItem(slot, display);

            ItemStack decrease = new ItemStack(Blocks.CONCRETE, 1, EnumDyeColor.RED.getMetadata());
            ItemUtils.setDisplayName(decrease, "modifier.iv.decrease.name");
            ItemUtils.setItemLore(decrease,
                    LangUtils.format("modifier.iv.decrease.one", ConfigController.INSTANCE.getPriceFor(ModifierType.DECREASE_IV, pokemon)),
                    LangUtils.format("modifier.iv.decrease.ten", ConfigController.INSTANCE.getPriceFor(ModifierType.DECREASE_IV_10, pokemon))
            );
            setItem(slot + 1, decrease);

            ItemStack increase = new ItemStack(Blocks.CONCRETE, 1, EnumDyeColor.GREEN.getMetadata());
            ItemUtils.setDisplayName(increase, "modifier.iv.increase.name");
            ItemUtils.setItemLore(
                    increase,
                    LangUtils.format("modifier.iv.increase.one", ConfigController.INSTANCE.getPriceFor(ModifierType.INCREASE_IV, pokemon)),
                    LangUtils.format("modifier.iv.increase.ten", ConfigController.INSTANCE.getPriceFor(ModifierType.INCREASE_IV_10, pokemon))
            );
            setItem(slot + 2, increase);
        }

        ItemStack maxIvs = new ItemStack(PixelmonItems.maxPotion);
        ItemUtils.setDisplayName(maxIvs, "modifier.iv.max-ivs.name");
        ItemUtils.setItemLore(maxIvs, IVController.INSTANCE.getPriceLine(pokemon));
        setItem(MAX_IVS, maxIvs);

        ItemStack randomIvs = new ItemStack(PixelmonItemsValuables.strangeSouvenir);
        ItemUtils.setDisplayName(randomIvs, "modifier.iv.random-ivs.name");
        ItemUtils.setItemLore(randomIvs, ConfigController.INSTANCE.createPriceLine(ConfigController.INSTANCE.getPriceFor(ModifierType.RANDOM_IV, pokemon)));
        setItem(RANDOM_IVS, randomIvs);

        super.render();
    }
}
