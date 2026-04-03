package uk.jacobempire.melonmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import uk.jacobempire.melonmod.client.render.entity.MelonGoblinRenderer;
import uk.jacobempire.melonmod.common.init.ModBlocks;
import uk.jacobempire.melonmod.common.init.ModEntities;
import uk.jacobempire.melonmod.common.init.ModItems;
import uk.jacobempire.melonmod.common.world.ModCarvers;

@Mod("melonmod")
public class MelonMod
{
    public static final String MODID = "melonmod";
    private static final Logger LOGGER = LogManager.getLogger();

    private void doClientStuff(final FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.MELON_GRASS.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MELON_GRASS_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MELON_FIRE.get(), RenderType.cutout());
        });

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.MELON_GOBLIN.get(), MelonGoblinRenderer::new);
    }

    public MelonMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModCarvers.WORLD_CARVERS.register(eventBus);
        ModEntities.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
        eventBus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }
}

