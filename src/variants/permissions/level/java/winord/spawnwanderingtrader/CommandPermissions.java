package winord.spawnwanderingtrader;

import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;

public final class CommandPermissions {

    private static final int GAMEMASTER_LEVEL = 2;

    private CommandPermissions() {
    }

    public static Predicate<CommandSourceStack> gamemaster() {
        return source -> source.hasPermission(GAMEMASTER_LEVEL);
    }
}
