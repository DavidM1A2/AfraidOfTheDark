package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.tileEntity.FeyLightTileEntity
import net.minecraft.core.BlockPos
import java.awt.Color

class FeyLightSpellEffect : AOTDSpellEffect("fey_light", ModResearches.THE_JOURNEY_BEGINS) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.colorProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("color"))
                .withSetter(this::setColor)
                .withGetter(this::getColor)
                .withDefaultValue(Color.MAGENTA)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        val world = state.world
        val direction = state.direction
        val position = BlockPos(state.position.add(direction.reverse().scale(0.001)))
        val blockState = world.getBlockState(position)
        if (blockState.material.isReplaceable) {
            world.setBlockAndUpdate(position, ModBlocks.FEY_LIGHT.defaultBlockState())
            val tileEntity = world.getBlockEntity(position) as? FeyLightTileEntity
            tileEntity?.color = getColor(instance)
            return ProcResult.success()
        }

        return ProcResult.failure()
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 20.0
    }

    fun setColor(instance: SpellComponentInstance<*>, color: Color) {
        instance.data.putString(NBT_COLOR, "${color.red} ${color.green} ${color.blue}")
    }

    fun getColor(instance: SpellComponentInstance<*>): Color {
        val rgb = instance.data.getString(NBT_COLOR).split(Regex("\\s+")).map { it.toInt() }
        return Color(rgb[0], rgb[1], rgb[2])
    }

    companion object {
        private const val NBT_COLOR = "color"
    }
}