package fr.utopics.eikins.mutation;

import java.util.HashMap;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.utopics.eikins.mutation.client.HUDRenderer;
import fr.utopics.eikins.mutation.command.CommandAmplify;
import fr.utopics.eikins.mutation.entity.MobActions;
import fr.utopics.eikins.mutation.entity.player.PlayerActions;
import fr.utopics.eikins.mutation.item.ItemAntidote;
import fr.utopics.eikins.mutation.item.ItemMutagen;
import fr.utopics.eikins.mutation.item.ItemVaccine;
import fr.utopics.eikins.mutation.item.crafting.RecipeAntidote;
import fr.utopics.eikins.mutation.item.crafting.RecipeMutagen;
import fr.utopics.eikins.mutation.mutagen.Mutagen;
import fr.utopics.eikins.mutation.mutagen.MutagenExp;
import fr.utopics.eikins.mutation.mutagen.MutagenGold;
import fr.utopics.eikins.mutation.mutagen.MutagenGreen;
import fr.utopics.eikins.mutation.mutagen.MutagenRed;
import fr.utopics.eikins.mutation.mutagen.MutagenType;
import fr.utopics.eikins.mutation.mutagen.MutagenWhite;
import fr.utopics.eikins.mutation.network.MessageMutagenSync;
import fr.utopics.forge.util.RegistryUtil;

public class Registry {
	
	private HashMap<MutagenType, Mutagen> mutagenMap;
	private HashMap<Item, Float> plantValues;
	public static ItemMutagen greenMutagen;
	public static ItemMutagen redMutagen;
	public static ItemMutagen whiteMutagen;
	public static ItemMutagen goldMutagen;
	public static ItemMutagen expMutagen;
	public static Item dna;
	public static Item syringe;
	public static Item antidote;
	public static Item vaccine;
	
    public final static SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(ModMutation.MODID.toLowerCase());
    
    private static Registry instance;
    
    public static Registry getInstance() {
    	if(instance == null) {
    		instance = new Registry();
    	}
    	return instance;
    }
    
    private Registry() {}
    
	public void registerMutagens() {
		mutagenMap = new HashMap<MutagenType, Mutagen>();
		mutagenMap.put(MutagenType.GREEN_MUTAGEN, new MutagenGreen("mutagen_green", MutagenType.GREEN_MUTAGEN));
		mutagenMap.put(MutagenType.RED_MUTAGEN, new MutagenRed("mutagen_red", MutagenType.RED_MUTAGEN));
		mutagenMap.put(MutagenType.WHITE_MUTAGEN, new MutagenWhite("mutagen_white", MutagenType.WHITE_MUTAGEN));
		mutagenMap.put(MutagenType.GOLD_MUTAGEN, new MutagenGold("mutagen_gold", MutagenType.GOLD_MUTAGEN));
		mutagenMap.put(MutagenType.EXP_MUTAGEN, new MutagenExp("mutagen_exp", MutagenType.EXP_MUTAGEN));
	}
	
	public void initItems() {
		greenMutagen = (ItemMutagen) new ItemMutagen(mutagenMap.get(MutagenType.GREEN_MUTAGEN)).setUnlocalizedName("mutagen_green").setCreativeTab(CreativeTabs.tabMisc);
		redMutagen = (ItemMutagen) new ItemMutagen(mutagenMap.get(MutagenType.RED_MUTAGEN)).setUnlocalizedName("mutagen_red").setCreativeTab(CreativeTabs.tabMisc);
		whiteMutagen = (ItemMutagen) new ItemMutagen(mutagenMap.get(MutagenType.WHITE_MUTAGEN)).setUnlocalizedName("mutagen_white").setCreativeTab(CreativeTabs.tabMisc);
		goldMutagen = (ItemMutagen) new ItemMutagen(mutagenMap.get(MutagenType.GOLD_MUTAGEN)).setUnlocalizedName("mutagen_gold").setCreativeTab(CreativeTabs.tabMisc);
		expMutagen = (ItemMutagen) new ItemMutagen(mutagenMap.get(MutagenType.EXP_MUTAGEN)).setUnlocalizedName("mutagen_exp").setCreativeTab(CreativeTabs.tabMisc);
		dna = new Item().setUnlocalizedName("dna").setCreativeTab(CreativeTabs.tabMisc);
		syringe = new Item().setUnlocalizedName("syringe").setCreativeTab(CreativeTabs.tabMisc);
		antidote = new ItemAntidote().setUnlocalizedName("antidote").setCreativeTab(CreativeTabs.tabMisc);
		vaccine = new ItemVaccine().setUnlocalizedName("vaccine").setCreativeTab(CreativeTabs.tabMisc);
		mutagenMap.get(MutagenType.GREEN_MUTAGEN).setItem(greenMutagen);
		mutagenMap.get(MutagenType.RED_MUTAGEN).setItem(redMutagen);
		mutagenMap.get(MutagenType.WHITE_MUTAGEN).setItem(whiteMutagen);
		mutagenMap.get(MutagenType.GOLD_MUTAGEN).setItem(goldMutagen);
		mutagenMap.get(MutagenType.EXP_MUTAGEN).setItem(expMutagen);
	}
	
	public void registerItems() {
		RegistryUtil.registerItem(greenMutagen);
		RegistryUtil.registerItem(redMutagen);
		RegistryUtil.registerItem(whiteMutagen);
		RegistryUtil.registerItem(goldMutagen);
		RegistryUtil.registerItem(expMutagen);
		RegistryUtil.registerItem(dna);
		RegistryUtil.registerItem(syringe);
		RegistryUtil.registerItem(antidote);
		RegistryUtil.registerItem(vaccine);
	}

	@SideOnly(Side.CLIENT)
	public void registerRenders(String modId) {
		RegistryUtil.registerItemModel(greenMutagen, modId);
		RegistryUtil.registerItemModel(redMutagen, modId);
		RegistryUtil.registerItemModel(whiteMutagen, modId);
		RegistryUtil.registerItemModel(goldMutagen, modId);
		RegistryUtil.registerItemModel(expMutagen, modId);
		RegistryUtil.registerItemModel(dna, modId);
		RegistryUtil.registerItemModel(syringe, modId);
		RegistryUtil.registerItemModel(antidote, modId);
		RegistryUtil.registerItemModel(vaccine, modId);
	}
	
	public void registerPlantValues() {
		plantValues = new HashMap<Item, Float>();
		plantValues.put(Item.getItemFromBlock(Blocks.cactus), 0.65f);
		plantValues.put(Item.getItemFromBlock(Blocks.vine), 0.35f);
		plantValues.put(Item.getItemFromBlock(Blocks.waterlily), 0.75f);
		plantValues.put(Item.getItemFromBlock(Blocks.red_flower), 0.30f);
		plantValues.put(Item.getItemFromBlock(Blocks.red_mushroom), 1.15f);
		plantValues.put(Item.getItemFromBlock(Blocks.brown_mushroom), 0.90f);
		plantValues.put(Item.getItemFromBlock(Blocks.tallgrass), 0.45f);
		plantValues.put(Item.getItemFromBlock(Blocks.deadbush), 0.60f);
		plantValues.put(Item.getItemFromBlock(Blocks.double_plant), 0.95f);
		plantValues.put(Items.nether_wart, 3.10f);
		plantValues.put(Items.reeds, 0.30f);
		plantValues.put(Items.speckled_melon, 4.25f);
	}
	
	public void registerRecipes() {
		GameRegistry.addRecipe(new RecipeAntidote());
		GameRegistry.addRecipe(new RecipeMutagen());
		GameRegistry.addRecipe(new ItemStack(Registry.syringe), "G", "G", "I", 'G', Blocks.glass_pane, 'I', Items.iron_ingot);
		GameRegistry.addShapelessRecipe(new ItemStack(Registry.vaccine), new ItemStack(Items.water_bucket), new ItemStack(Items.nether_wart), new ItemStack(Items.nether_wart), new ItemStack(Items.ender_pearl), new ItemStack(syringe), new ItemStack(Registry.goldMutagen), new ItemStack(Registry.redMutagen), new ItemStack(Registry.greenMutagen), new ItemStack(Registry.whiteMutagen));
	}
	
	public void registerEventHandlers(Side side) {
		FMLCommonHandler.instance().bus().register(new PlayerActions());
		MinecraftForge.EVENT_BUS.register(new PlayerActions());
		FMLCommonHandler.instance().bus().register(new MobActions());
		MinecraftForge.EVENT_BUS.register(new MobActions());
		if(side.equals(Side.CLIENT)) {
			FMLCommonHandler.instance().bus().register(new HUDRenderer());
			MinecraftForge.EVENT_BUS.register(new HUDRenderer());
		}
	}
	
	public HashMap getPlantValues() {
		return plantValues;
	}
	
	public Mutagen getMutagen(MutagenType type) {
		if(mutagenMap.containsKey(type)) {
			return mutagenMap.get(type);
		}
		return null;
	}
	
    public void initNetwork() {
    	NETWORK.registerMessage(MessageMutagenSync.class, MessageMutagenSync.class, 1, Side.CLIENT);
    }
    
	public static void registerCommands(ServerCommandManager manager) {
		manager.registerCommand(new CommandAmplify());
	}
	
}
