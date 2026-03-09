package uk.jacobempire.melonmod.common.world;

import static net.minecraft.entity.EntitySpawnPlacementRegistry.*;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap.Type;
import uk.jacobempire.melonmod.common.init.ModEntities;

public class ModEntitySpawnPlacement {
	public static void init() {
		register(ModEntities.MELON_GOBLIN.get(), PlacementType.ON_GROUND, Type.MOTION_BLOCKING_NO_LEAVES,
				MonsterEntity::checkAnyLightMonsterSpawnRules);
	}
}
