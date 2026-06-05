package com.hbm.blocks.generic;

import com.hbm.blocks.EnumMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BobbleBlock extends EnumMultiBlock implements EntityBlock {

    public BobbleBlock(Properties properties) {
        super(properties, BobbleType.class, true, false);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }


    // todo make strings translatable!!!
    public enum BobbleType {

        NONE(			"null",								"null",             null,														null,																								false,	ScrapType.BOARD_BLANK),
        STRENGTH(		"Strength",							"Strength",         null,														"It's essential to give your arguments impact.",													false,	ScrapType.BRIDGE_BIOS),
        PERCEPTION(		"Perception",						    "Perception",       null,														"Only through observation will you perceive weakness.",											false,	ScrapType.BRIDGE_NORTH),
        ENDURANCE(		"Endurance",						    "Endurance",        null,														"Always be ready to take one for the team.",														false,	ScrapType.BRIDGE_SOUTH),
        CHARISMA(		"Charisma",							"Charisma",         null,														"Nothing says pizzaz like a winning smile.",														false,	ScrapType.BRIDGE_IO),
        INTELLIGENCE(	"Intelligence",						"Intelligence",     null,														"It takes the smartest individuals to realize$there's always more to learn.",						false,	ScrapType.BRIDGE_BUS),
        AGILITY(		"Agility",							"Agility",          null,														"Never be afraid to dodge the sensitive issues.",													false,	ScrapType.BRIDGE_CHIPSET),
        LUCK(			"Luck",								"Luck",             null,														"There's only one way to give 110%.",																false,	ScrapType.BRIDGE_CMOS),
        BOB(			"Robert \"The Bobcat\" Katzinsky",	"HbMinecraft",      "Hbm's Nuclear Tech Mod",									"I know where you live, " + System.getProperty("user.name"),									    false,	ScrapType.CPU_SOCKET),
        FRIZZLE(		"Frooz",							    "Frooz",            "Weapon models",											"BLOOD IS FUEL",																					true,	    ScrapType.CPU_CLOCK),
        PU238(			"Pu-238",							    "Pu-238",           "Improved Tom impact mechanics",							null,																								false,	ScrapType.CPU_REGISTER),
        VT(				"VT-6/24",							"VT-6/24",          "Balefire warhead model and general texturework",			"You cannot unfuck a horse.",																		true,	    ScrapType.CPU_EXT),
        DOC(			"The Doctor",						    "Doctor17PH",       "Russian localization, lunar miner",						"Perhaps the moon rocks were too expensive",														true,	    ScrapType.CPU_CACHE),
        BLUEHAT(		"The Blue Hat",						"The Blue Hat",     "Textures",													"payday 2's deagle freeaim champ of the year 2022",												true,	    ScrapType.MEM_16K_A),
        PHEO(			"Pheo",								"Pheonix",          "Deuterium machines, tantalium textures, Reliant Rocket",	"RUN TO THE BEDROOM, ON THE SUITCASE ON THE LEFT,$YOU'LL FIND MY FAVORITE AXE",					true,   	ScrapType.MEM_16K_B),
        ADAM29(			"Adam29",							    "Adam29",           "Ethanol, liquid petroleum gas",							"You know, nukes are really quite beatiful.$It's like watching a star be born for a split second.",true,	ScrapType.MEM_16K_C),
        UFFR(			"UFFR",								"UFFR",             "All sorts of things from his PR",							"fried shrimp",																					false,	ScrapType.MEM_SOCKET),
        VAER(			"vaer",								"vaer",             "ZIRNOX",													"taken de family out to the weekend cigarette festival",											true,	    ScrapType.MEM_16K_D),
        NOS(			"Dr Nostalgia",						"Dr Nostalgia",     "SSG and Vortex models",									"Take a picture, I'ma pose, paparazzi$I've been drinking, moving like a zombie",					true,	    ScrapType.BOARD_TRANSISTOR),
        DRILLGON(		"Drillgon200",						"Drillgon200",      "1.12 Port",												null,																								false,	ScrapType.CPU_LOGIC),
        CIRNO(			"Cirno",							    "Cirno",            "the only multi layered skin i had",						"No brain. Head empty.",																			true,	    ScrapType.BOARD_BLANK),
        MICROWAVE(		"Microwave",						    "Microwave",        "OC Compatibility and massive RBMK/packet optimizations",	"they call me the food heater$john optimization",													true,	    ScrapType.BOARD_CONVERTER),
        PEEP(			"Peep",								"LePeeperSauvage",  "Coilgun, Leadburster and Congo Lake models, BDCL QC",		"Fluffy ears can't hide in ash, nor snow.",														true,	    ScrapType.CARD_BOARD),
        MELLOW(			"MELLOWARPEGGIATION",				    "Mellow",           "NBT Structures, industrial lighting, animation tools",		"Make something cool now, ask for permission later.",												true,	    ScrapType.CARD_PROCESSOR),
        ABEL(			"Abel1502", 						    "Abel1502",         "Abilities GUI, optimizations and many QoL improvements", 	"NANTO SUBARASHII",																				true,	    ScrapType.CPU_REGISTER);

        public final String name;			//the title of the tooltip
        public final String label;		    //the name engraved in the socket
        public final String contribution;	//what contributions this person has made, if applicable
        public final String inscription;	//the flavor text
        public final boolean skinLayers;
        public final ScrapType scrap;

        BobbleType(String name, String label, String contribution, String inscription, boolean layers, ScrapType scrap) {
            this.name = name;
            this.label = label;
            this.contribution = contribution;
            this.inscription = inscription;
            this.skinLayers = layers;
            this.scrap = scrap;
        }
    }

    // JUST BE HERE
    public enum ScrapType {
        //GENERAL BOARD
        BOARD_BLANK,
        BOARD_TRANSISTOR,
        BOARD_CONVERTER,

        //CHIPSET
        BRIDGE_NORTH,
        BRIDGE_SOUTH,
        BRIDGE_IO,
        BRIDGE_BUS,
        BRIDGE_CHIPSET,
        BRIDGE_CMOS,
        BRIDGE_BIOS,

        //CPU
        CPU_REGISTER,
        CPU_CLOCK,
        CPU_LOGIC,
        CPU_CACHE,
        CPU_EXT,
        CPU_SOCKET,

        //RAM
        MEM_SOCKET,
        MEM_16K_A,
        MEM_16K_B,
        MEM_16K_C,
        MEM_16K_D,

        //EXTENSION CARD
        CARD_BOARD,
        CARD_PROCESSOR
    }
}
