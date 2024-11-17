package net.legionoid.workingsincebirth.mixin;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity {
    private static final Random RANDOM = new Random();

    @Inject(method = "initialize", at = @At("RETURN"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag, CallbackInfoReturnable<EntityData> cir) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        if (world instanceof ServerWorld serverWorld) {
            Registry<VillagerProfession> professionRegistry = serverWorld.getRegistryManager().get(RegistryKeys.VILLAGER_PROFESSION);
            List<VillagerProfession> professions = professionRegistry.stream().toList();

            VillagerProfession randomProfession = professions.get(RANDOM.nextInt(professions.size()));

            villager.setVillagerData(villager.getVillagerData().withProfession(randomProfession));
            villager.setExperience(1);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        if (villager.getVillagerData().getProfession() == VillagerProfession.NONE) {
            if (villager.getWorld() instanceof ServerWorld serverWorld) {
                Registry<VillagerProfession> professionRegistry = serverWorld.getRegistryManager().get(RegistryKeys.VILLAGER_PROFESSION);

                List<VillagerProfession> professions = professionRegistry.stream().toList();
                VillagerProfession randomProfession = professions.get(RANDOM.nextInt(professions.size()));

                villager.setVillagerData((villager.getVillagerData().withProfession(randomProfession)));
                villager.setExperience(1);
            }
        }
    }
}
