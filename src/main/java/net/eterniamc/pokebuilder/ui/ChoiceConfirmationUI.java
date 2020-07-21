package net.eterniamc.pokebuilder.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import lombok.RequiredArgsConstructor;
import net.eterniamc.dynamicui.ConfirmationUI;
import net.minecraft.entity.player.EntityPlayerMP;

@RequiredArgsConstructor
public class ChoiceConfirmationUI extends ConfirmationUI {
    private final Runnable onAccept;
    private final Pokemon pokemon;

    @Override
    public void onAccept(EntityPlayerMP player) {
        onAccept.run();
        new ModifierSelectUI(pokemon).open(player);
    }
}
