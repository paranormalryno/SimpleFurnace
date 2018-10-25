package com.paranormalryno.simplefurnace;

import org.apache.logging.log4j.Logger;

import com.paranormalryno.simplefurnace.proxy.CommonProxy;
import com.paranormalryno.simplefurnace.util.Reference;
import com.paranormalryno.simplefurnace.util.handlers.RegistryHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class SimpleFurnace {
	
	@Instance
	public static SimpleFurnace instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        RegistryHandler.preInitRegistries();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	RegistryHandler.initRegistries();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	RegistryHandler.postInitRegistries();
    }
}
