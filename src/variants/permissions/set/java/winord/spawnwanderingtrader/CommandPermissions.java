package winord.spawnwanderingtrader;

import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class CommandPermissions {

    private CommandPermissions() {
    }

    public static Predicate<CommandSourceStack> gamemaster() {
        return Commands.hasPermission(Commands.LEVEL_GAMEMASTERS);
    }
}
