package net.eterniamc.pokebuilder.ui.component;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import net.eterniamc.dynamicui.Component;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.dynamicui.implementation.InventoryUI;
import net.eterniamc.pokebuilder.ui.ModifierSelectUI;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.PokemonDisplayUtils;
import net.minecraft.item.ItemStack;

public class PokemonComponent extends Component {

    private final Pokemon pokemon;

    public PokemonComponent(int origin, Pokemon pokemon) {
        super(origin);

        this.pokemon = pokemon;
    }

    @Override
    public void loadListeners(DynamicUI ui) {
        ui.addListener(getOrigin(), (player, event) -> new ModifierSelectUI(pokemon).open(player));
    }

    @Override
    public void render(InventoryUI inventory) {
        ItemStack stack = ItemPixelmonSprite.getPhoto(pokemon);
        ItemUtils.setItemLore(stack, ItemUtils.splitToLore(PokemonDisplayUtils.getPokemonStats(pokemon)));
        ItemUtils.setDisplayName(stack, PokemonDisplayUtils.getPokemonName(pokemon));

        inventory.setInventorySlotContents(getOrigin(), stack);
    }
}
