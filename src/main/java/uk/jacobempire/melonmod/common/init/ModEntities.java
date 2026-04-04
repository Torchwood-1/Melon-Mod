package uk.jacobempire.melonmod.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.jacobempire.melonmod.MelonMod;
import uk.jacobempire.melonmod.common.entity.MelonGoblinEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITIES, MelonMod.MODID);

    public static RegistryObject<EntityType<MelonGoblinEntity>> MELON_GOBLIN = registerEntity("melon_goblin",
            MelonGoblinEntity::new, EntityClassification.MONSTER);

    @SuppressWarnings("unused")
    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name,
            EntityType.IFactory<T> entityClass,
            EntityClassification classification, float width, float height) {
        return ENTITIES.register(name,
                () -> EntityType.Builder.of(entityClass, classification).sized(width, height)
                        .clientTrackingRange(8)
                        .build(MelonMod.resource(name).toString()));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name,
            EntityType.IFactory<T> entityClass,
            EntityClassification classification) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityClass, classification)
                .clientTrackingRange(8).build(MelonMod.resource(name).toString()));
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
