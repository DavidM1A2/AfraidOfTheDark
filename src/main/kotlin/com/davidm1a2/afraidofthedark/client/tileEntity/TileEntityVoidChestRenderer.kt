package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import net.minecraft.client.renderer.Atlases
import net.minecraft.client.renderer.model.Material
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.state.properties.ChestType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TileEntityVoidChestRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) :
    ChestTileEntityRenderer<VoidChestTileEntity>(tileEntityRendererDispatcher) {
    private val voidChestTexture = ResourceLocation("afraidofthedark:textures/block/void_chest/void_chest.png")

    override fun getMaterial(voidChestTileEntity: VoidChestTileEntity, chestType: ChestType): Material {
        return Material(Atlases.CHEST_ATLAS, voidChestTexture)
    }
}