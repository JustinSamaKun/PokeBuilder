package net.eterniamc.pokebuilder.ui;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import lombok.RequiredArgsConstructor;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.component.PokemonComponent;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.LangUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.Set;

import static com.pixelmonmod.pixelmon.config.PixelmonItemsHeld.*;

@RequiredArgsConstructor
public class NatureModifierUI extends DynamicUI {

    private final Pokemon pokemon;

    private final Map<Integer, EnumNature> tableLayout = Maps.newHashMap();


    @Override
    public void generateInventory() {
        inventory = createInventory(LangUtils.get("modifier.nature.ui.name"), 6);

        StatsType[] types = { StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed };
        Set<EnumNature> used = Sets.newHashSet();

        for (int row = 0; row < types.length; row++) {
            for (int column = 0; column < types.length; column++) {
                StatsType raised = types[row];
                StatsType lowered = types[column];

                EnumNature combination = getNextCombination(raised, lowered, used);
                used.add(combination);

                tableLayout.put((row + 1) * 9 + (column + 2), combination);
            }
        }

        addComponent(new PokemonComponent(1, pokemon));
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

        for (Map.Entry<Integer, EnumNature> entry : tableLayout.entrySet()) {
            EnumNature nature = entry.getValue();
            if (!pokemon.getNature().equals(nature)) {
                addListener(entry.getKey(), (player1, event) ->
                    UserInterfaceUtils.createConfirmation(pokemon, ModifierType.NATURE, () -> pokemon.setNature(nature))
                );
            }
        }
    }

    @Override
    public void render() {
        super.render();

        for (int i = 0; i < 6; i++) {
            ItemStack glass = new ItemStack(Blocks.STAINED_GLASS_PANE);
            ItemUtils.setDisplayName(glass, " ");
            setItem(i * 9, glass);
            setItem(i * 9 + 8, glass);
            setItem(i * 9 + 7, glass);
        }

        StatsType[] types = { StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed };
        Item[] items = { powerBracer, powerBelt, powerLens, powerBand, powerAnklet };

        for (int i = 0; i < types.length; i++) {
            ItemStack stack = new ItemStack(items[i]);
            ItemUtils.setDisplayName(stack, LangUtils.format("modifier.nature.lowering", types[i].getLocalizedName()));
            setItem(i + 2, stack);

            stack = new ItemStack(items[i]);
            ItemUtils.setDisplayName(stack, LangUtils.format("modifier.nature.raising", types[i].getLocalizedName()));
            setItem((i + 1) * 9 + 1, stack);
        }

        for (Map.Entry<Integer, EnumNature> entry : tableLayout.entrySet()) {
            EnumNature nature = entry.getValue();
            StatsType raised = nature.increasedStat;
            StatsType lowered = nature.decreasedStat;

            ItemStack item = new ItemStack(getItemForRaisedStat(raised));
            ItemUtils.setItemLore(
                    item,
                    LangUtils.format("modifier.nature.increase", raised.getLocalizedName()),
                    LangUtils.format("modifier.nature.decrease", lowered.getLocalizedName())
                    );
            if (raised == lowered) {
                item = new ItemStack(PixelmonItemsBadges.balanceBadge);
                ItemUtils.setItemLore(item, "modifier.nature.neutral");
            }
            if (pokemon.getNature().equals(nature)) {
                item.addEnchantment(Enchantments.UNBREAKING, 1);
            }
            ItemUtils.setDisplayName(item, nature.getLocalizedName());

            setItem(entry.getKey(), item);
        }
    }

    private EnumNature getNextCombination(StatsType raising, StatsType lowering, Set<EnumNature> used) {
        if (raising == lowering) {
            for (EnumNature nature : EnumNature.values()) {
                if (nature.decreasedStat == nature.increasedStat && !used.contains(nature)) {
                    return nature;
                }
            }
        }

        for (EnumNature nature : EnumNature.values()) {
            if (nature.increasedStat == raising && nature.decreasedStat == lowering && !used.contains(nature)) {
                return nature;
            }
        }

        return EnumNature.Adamant;
    }

    private Item getItemForRaisedStat(StatsType type) {
        switch (type) {
            case Attack: return PixelmonItemsBadges.volcanoBadge;
            case Defence: return PixelmonItemsBadges.thunderBadge;
            case SpecialAttack: return PixelmonItemsBadges.mindBadge;
            case SpecialDefence: return PixelmonItemsBadges.marshBadge;
            default: return PixelmonItemsBadges.featherBadge;
        }
    }
}
