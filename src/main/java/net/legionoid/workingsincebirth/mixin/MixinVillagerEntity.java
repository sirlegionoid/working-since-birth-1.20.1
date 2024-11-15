package net.legionoid.workingsincebirth.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity {
    private static final Random RANDOM = new Random();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onSpawn(EntityType<? extends VillagerEntity> entityType, ServerWorld world, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        Registry<VillagerProfession> professionRegistry = world.getRegistryManager().get(RegistryKeys.VILLAGER_PROFESSION);

        List<VillagerProfession> professions = professionRegistry.stream().toList();

        VillagerProfession randomProfession = professions.get(RANDOM.nextInt(professions.size()));

        villager.setVillagerData(villager.getVillagerData().withProfession(randomProfession));
    }
}
