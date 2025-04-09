package uk.jacobempire.melonmod.common;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import uk.jacobempire.melonmod.common.entity.MelonGoblinEntity;
import uk.jacobempire.melonmod.common.init.ModEntities;

@EventBusSubscriber(bus = Bus.MOD)
public class ModEvents {
	@SubscribeEvent
	public static void onAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(ModEntities.MELON_GOBLIN.get(), MelonGoblinEntity.createAttributes().build());
	}
}
