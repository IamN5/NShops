package iamn5.shops.datagen.base.loot.table;

import iamn5.shops.NShops;
import iamn5.shops.blocks.interfaces.IHasTile;
import iamn5.shops.blocks.shop.ShopTileEntity;
import iamn5.shops.tile.interfaces.IHasOwner;
import iamn5.shops.tile.interfaces.IOwnable;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.inventory.IInventory;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BaseBlockLootTables extends BlockLootTables {
    private final Map<Block, LootTable.Builder> tables = new HashMap<>();


    @Override
    protected void registerLootTable(Block blockIn, LootTable.Builder table) {
        tables.put(blockIn, table);
    }

    public Map<Block, LootTable.Builder> getTables() { return tables; }

    protected LootTable.Builder dropSelfWithContentsAndData(Block block) {
        CopyNbt.Builder nbtBuilder = CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY);

        TileEntity blockTile = null;
        if (block instanceof IHasTile) {
            blockTile = ((IHasTile<?>) block).getTileType().create();
        }

        if (blockTile instanceof IHasOwner) {
            nbtBuilder.replaceOperation("ownerData", "BlockEntityTag.ownerData");
        }

        if (blockTile instanceof IInventory) {
            nbtBuilder.replaceOperation("Items", "BlockEntityTag.Items");
        }

        return LootTable.builder().addLootPool(withSurvivesExplosion(block,
                LootPool.builder()
                        .name(block.getRegistryName().getPath())
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(block)
                                .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
                                .acceptFunction(nbtBuilder)))
                                .acceptFunction(SetContents.builderIn()
                                    .addLootEntry(DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents"))))
        );
    }
}
