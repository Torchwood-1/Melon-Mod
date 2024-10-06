package tk.jacobempire.melonmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.jacobempire.melonmod.common.init.ModBlocks;
import tk.jacobempire.melonmod.common.init.ModItems;
import tk.jacobempire.melonmod.common.world.ModCarvers;

@Mod("melonmod")
public class MelonMod
{
    public static final String MODID = "melonmod";
    private static final Logger LOGGER = LogManager.getLogger();

    private void doClientStuff(final FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.MELON_GRASS.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MELON_GRASS_BLOCK.get(), RenderType.cutout());
        });
    }

    public MelonMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModCarvers.WORLD_CARVERS.register(eventBus);


        MinecraftForge.EVENT_BUS.register(this);
        eventBus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);

    }
}

