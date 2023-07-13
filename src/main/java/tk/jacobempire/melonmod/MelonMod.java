package tk.jacobempire.melonmod;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.jacobempire.melonmod.RegistryHandler;
import tk.jacobempire.melonmod.common.init.ModBlocks;
import tk.jacobempire.melonmod.common.init.ModItems;

import java.util.stream.Collectors;


@Mod("melonmod")
public class MelonMod
{
    public static final String MODID = "melonmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public MelonMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryHandler.init();
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }


}
