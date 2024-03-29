package net.darktree.stylishoccult.block.entity;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.altar.AltarPlateBlockEntity;
import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronBlockEntity;
import net.darktree.stylishoccult.block.entity.demon.LavaDemonBlockEntity;
import net.darktree.stylishoccult.block.entity.rune.RuneBlockEntity;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BlockEntities {

	public static final BlockEntityType<LavaDemonBlockEntity> LAVA_DEMON = FabricBlockEntityTypeBuilder.create(
			LavaDemonBlockEntity::new, ModBlocks.LAVA_DEMON).build();

	public static final BlockEntityType<RuneBlockEntity> RUNESTONE = FabricBlockEntityTypeBuilder.create(
			RuneBlockEntity::new, ModBlocks.RUNESTONES.toArray(new Block[0]) ).build();

	public static final BlockEntityType<AltarPlateBlockEntity> ALTAR_PLATE = FabricBlockEntityTypeBuilder.create(
			AltarPlateBlockEntity::new, ModBlocks.ALTAR_PLATE).build();

	public static final BlockEntityType<OccultCauldronBlockEntity> OCCULT_CAULDRON = FabricBlockEntityTypeBuilder.create(
			OccultCauldronBlockEntity::new, ModBlocks.OCCULT_CAULDRON).build();

	public static void init() {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("lava_demon"), LAVA_DEMON);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("runestone"), RUNESTONE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("altar_plate"), ALTAR_PLATE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("occult_cauldron"), OCCULT_CAULDRON);
	}

}
