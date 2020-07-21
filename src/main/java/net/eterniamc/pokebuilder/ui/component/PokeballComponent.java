package net.eterniamc.pokebuilder.ui.component;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.eterniamc.dynamicui.Component;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.dynamicui.implementation.InventoryUI;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import static net.minecraft.item.EnumDyeColor.*;

public class PokeballComponent extends Component {

    private final PokemonComponent pokemon;

    private final EnumDyeColor[] lidColors;

    public PokeballComponent(int origin, Pokemon pokemon) {
        super(origin);

        this.pokemon = new PokemonComponent(origin + 10, pokemon);

        lidColors = getLidColors(pokemon.getCaughtBall());
    }

    @Override
    public void loadListeners(DynamicUI ui) {
        pokemon.loadListeners(ui);
    }

    @Override
    public void render(InventoryUI inventory) {
        // Render top
        for (int i = getOrigin(); i < getOrigin() + 3; i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, lidColors[i - getOrigin()].getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            inventory.setInventorySlotContents(i, stack);
        }

        // Render black middle
        for (int i = getOrigin() + 9; i < getOrigin() + 12; i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, BLACK.getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            inventory.setInventorySlotContents(i, stack);
        }

        // Render light grey bottom
        for (int i = getOrigin() + 18; i < getOrigin() + 21; i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, SILVER.getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            inventory.setInventorySlotContents(i, stack);
        }

        pokemon.render(inventory);
    }

    private EnumDyeColor[] getLidColors(EnumPokeballs ball) {
        switch (ball) {
            case GreatBall: return new EnumDyeColor[] { RED, BLUE, RED};
            case UltraBall: return new EnumDyeColor[] { YELLOW, BLACK, YELLOW };
            case GSBall: return new EnumDyeColor[] { YELLOW, ORANGE, YELLOW };
            case NetBall: return new EnumDyeColor[] { BLUE, BLACK, BLUE };
            case DiveBall: return new EnumDyeColor[] { SILVER, LIGHT_BLUE, SILVER };
            case DuskBall: return new EnumDyeColor[] { GRAY, GREEN, GRAY };
            case SportBall:
            case FastBall: return new EnumDyeColor[] { ORANGE, YELLOW, ORANGE };
            case HealBall: return new EnumDyeColor[] { WHITE, PINK, WHITE };
            case LoveBall: return new EnumDyeColor[] { PINK, PINK, PINK };
            case LureBall: return new EnumDyeColor[] { BLUE, RED, BLUE };
            case MoonBall: return new EnumDyeColor[] { BLUE, YELLOW, GRAY };
            case NestBall: return new EnumDyeColor[] { LIME, GREEN, LIME };
            case ParkBall: return new EnumDyeColor[] { YELLOW, YELLOW, YELLOW };
            case BeastBall: return new EnumDyeColor[] { YELLOW, BLUE, YELLOW };
            case DreamBall: return new EnumDyeColor[] { PINK, RED, PINK };
            case HeavyBall: return new EnumDyeColor[] { BLUE, GRAY, BLUE };
            case LevelBall: return new EnumDyeColor[] { GRAY, RED, GRAY };
            case QuickBall: return new EnumDyeColor[] { YELLOW, LIGHT_BLUE, YELLOW };
            case TimerBall: return new EnumDyeColor[] { WHITE, YELLOW, WHITE };
            case FriendBall: return new EnumDyeColor[] { LIME, LIME, LIME };
            case LuxuryBall: return new EnumDyeColor[] { GRAY, RED, GRAY };
            case MasterBall: return new EnumDyeColor[] { PINK, PURPLE, PINK };
            case RepeatBall: return new EnumDyeColor[] { ORANGE, GRAY, ORANGE };
            case SafariBall: return new EnumDyeColor[] { GREEN, GREEN, GREEN };
            case CherishBall: return new EnumDyeColor[] { RED, ORANGE, RED };
            case PremierBall: return new EnumDyeColor[] { WHITE, WHITE, WHITE };
            case PokeBall:
            default: return new EnumDyeColor[] { RED, RED, RED };
        }
    }
}
