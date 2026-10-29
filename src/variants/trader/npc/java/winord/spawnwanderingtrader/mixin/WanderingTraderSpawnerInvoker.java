package winord.spawnwanderingtrader.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WanderingTraderSpawner.class)
public interface WanderingTraderSpawnerInvoker {

    @Invoker("spawn")
    boolean invokeSpawn(ServerLevel level);
}
