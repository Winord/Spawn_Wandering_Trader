package winord.spawnwanderingtrader;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import winord.spawnwanderingtrader.mixin.ServerLevelAccessor;
import winord.spawnwanderingtrader.mixin.WanderingTraderSpawnerInvoker;

public final class SpawnTraderCommand {

    private static final int MAX_ATTEMPTS = 1000;

    private static final SimpleCommandExceptionType ERROR_NO_SPAWNER =
            new SimpleCommandExceptionType(Component.literal("No spawner available"));

    private static final SimpleCommandExceptionType ERROR_NO_PLAYERS =
            new SimpleCommandExceptionType(Component.literal("No alive players in this dimension"));

    private static final SimpleCommandExceptionType ERROR_ALL_FAILED =
            new SimpleCommandExceptionType(Component.literal("All spawn attempts failed"));

    private SpawnTraderCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawntrader")
                .requires(CommandPermissions.gamemaster())
                .executes(SpawnTraderCommand::run));
    }

    private static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        WanderingTraderSpawnerInvoker spawner = findSpawner(level);
        if (spawner == null) {
            throw ERROR_NO_SPAWNER.create();
        }
        if (level.getRandomPlayer() == null) {
            throw ERROR_NO_PLAYERS.create();
        }
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            if (spawner.invokeSpawn(level)) {
                source.sendSuccess(() -> Component.literal("Success"), false);
                return 1;
            }
        }
        throw ERROR_ALL_FAILED.create();
    }

    private static WanderingTraderSpawnerInvoker findSpawner(ServerLevel level) {
        for (CustomSpawner spawner : ((ServerLevelAccessor) level).getCustomSpawners()) {
            if (spawner instanceof WanderingTraderSpawnerInvoker invoker) {
                return invoker;
            }
        }
        return null;
    }
}
