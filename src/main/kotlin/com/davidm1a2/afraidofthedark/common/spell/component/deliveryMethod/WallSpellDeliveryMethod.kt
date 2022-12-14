package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellWallEntity
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.PacketDistributor
import java.awt.Color
import kotlin.random.Random

class WallSpellDeliveryMethod : AOTDSpellDeliveryMethod("wall", ModResearches.APPRENTICE_ASCENDED) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("width"))
                .withSetter(this::setWidth)
                .withGetter(this::getWidth)
                .withDefaultValue(2.0)
                .withMinValue(1.0)
                .withMaxValue(20.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("height"))
                .withSetter(this::setHeight)
                .withGetter(this::getHeight)
                .withDefaultValue(2.0)
                .withMinValue(1.0)
                .withMaxValue(20.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.colorProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("color"))
                .withSetter(this::setColor)
                .withGetter(this::getColor)
                .withDefaultValue(Color.LIGHT_GRAY)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        val world = state.world
        val deliveryMethod = state.getCurrentStage().deliveryInstance!!
        val width = getWidth(deliveryMethod)
        val height = getHeight(deliveryMethod)
        val position = state.position
        val forwardBackwardDir = state.direction
        val upDownDir = state.normal
        val leftRightDir = forwardBackwardDir.cross(upDownDir).normalize()

        world.addFreshEntity(SpellWallEntity(world, position, leftRightDir.scale(width), upDownDir.scale(height), getColor(deliveryMethod)))
        // Add 0.5 to center on a block
        var oneEffectProcd = false
        var xOffset = -width / 2
        while (xOffset < width / 2) {
            var zOffset = -height / 2
            while (zOffset < height / 2) {
                val wallPos = position
                    .add(leftRightDir.scale(xOffset))
                    .add(upDownDir.scale(zOffset))

                val newState = DeliveryTransitionState(
                    spell = state.spell,
                    stageIndex = state.stageIndex,
                    world = world,
                    position = wallPos,
                    blockPosition = BlockPos(wallPos),
                    direction = forwardBackwardDir,
                    normal = upDownDir,
                    casterEntity = state.casterEntity
                )
                val procResult = procEffects(newState, false)
                oneEffectProcd = oneEffectProcd || procResult.isSuccess
                transitionFrom(newState)

                zOffset = zOffset + 1.0
            }
            xOffset = xOffset + 1.0
        }

        // Create fizzle particles if no effect procd successfully
        if (!oneEffectProcd) {
            val numParticles = (1 + width + height).toInt() * 2
            val positions = List(numParticles) {
                position.add(upDownDir.scale((Random.nextDouble() - 0.5) * height))
                    .add(leftRightDir.scale((Random.nextDouble() - 0.5) * width))
            }
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(ModParticles.FIZZLE)
                    .positions(positions)
                    .speed(Vector3d(0.0, 0.1, 0.0))
                    .build(),
                PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, world.dimension())
            )
        }
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return getWidth(instance) * getHeight(instance)
    }

    fun setWidth(instance: SpellComponentInstance<*>, width: Double) {
        instance.data.putDouble(NBT_WIDTH, width)
    }

    fun getWidth(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_WIDTH)
    }

    fun setHeight(instance: SpellComponentInstance<*>, height: Double) {
        instance.data.putDouble(NBT_HEIGHT, height)
    }

    fun getHeight(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_HEIGHT)
    }

    fun setColor(instance: SpellComponentInstance<*>, color: Color) {
        instance.data.putString(NBT_COLOR, "${color.red} ${color.green} ${color.blue}")
    }

    fun getColor(instance: SpellComponentInstance<*>): Color {
        val rgb = instance.data.getString(NBT_COLOR).split(Regex("\\s+")).map { it.toInt() }
        return Color(rgb[0], rgb[1], rgb[2])
    }

    companion object {
        private const val NBT_WIDTH = "width"
        private const val NBT_HEIGHT = "height"
        private const val NBT_COLOR = "color"
    }
}