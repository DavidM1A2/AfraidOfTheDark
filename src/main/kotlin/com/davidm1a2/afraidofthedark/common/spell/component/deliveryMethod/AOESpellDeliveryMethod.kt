package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellAOEEntity
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fmllegacy.network.PacketDistributor
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * AOE method delivers the spell to the target in a circle
 *
 * @constructor initializes the editable properties
 */
class AOESpellDeliveryMethod : AOTDSpellDeliveryMethod("aoe", ModResearches.ADVANCED_MAGIC) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
                .withDefaultValue(3.0)
                .withMinValue(1.0)
                .withMaxValue(20.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("shell_only"))
                .withSetter(this::setShellOnly)
                .withGetter(this::getShellOnly)
                .withDefaultValue(false)
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

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        val deliveryMethod = state.getCurrentStage().deliveryInstance!!
        val radius = getRadius(deliveryMethod)
        val shellOnly = getShellOnly(deliveryMethod)

        val centerPos = state.position
        val world = state.world
        val blockRadius = ceil(radius).toInt()

        world.addFreshEntity(SpellAOEEntity(world, centerPos, radius.toFloat(), getColor(deliveryMethod)))
        // Go over every block in the radius
        var oneEffectProcd = false
        for (x in -blockRadius..blockRadius) {
            for (y in -blockRadius..blockRadius) {
                for (z in -blockRadius..blockRadius) {
                    val aoePos = centerPos.add(x.toDouble(), y.toDouble(), z.toDouble())
                    val distance = aoePos.distanceTo(centerPos)

                    // Test to see if the block is within the radius. If we're in "shellOnly" mode, only take the outer ~1.5 blocks of shell
                    if (distance <= radius && (!shellOnly || (radius - distance) < 1.5)) {
                        var direction = aoePos.subtract(centerPos).normalize()
                        // Direction may be 0 if aoePos = centerPos. In this case, move it up
                        if (direction == Vec3.ZERO) {
                            direction = Vec3(0.0, 1.0, 0.0)
                        }
                        var normal = direction.getNormal()
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (normal == Vec3.ZERO) {
                            normal = Vec3(1.0, 0.0, 0.0)
                        }

                        val newState = DeliveryTransitionState(
                            spell = state.spell,
                            stageIndex = state.stageIndex,
                            world = world,
                            position = aoePos,
                            blockPosition = BlockPos(aoePos),
                            direction = direction,
                            normal = normal,
                            casterEntity = state.casterEntity
                        )
                        val procResult = procEffects(newState, false)
                        oneEffectProcd = oneEffectProcd || procResult.isSuccess
                        transitionFrom(newState)
                    }
                }
            }
        }

        // Create fizzle particles if no effect procd successfully
        if (!oneEffectProcd) {
            // Generate random fizzle particles within the sphere
            val numParticles = (radius * 4).toInt()
            val positions = List(numParticles) {
                var x = (Random.nextDouble() - 0.5) * radius * 2
                var y = (Random.nextDouble() - 0.5) * radius * 2
                var z = (Random.nextDouble() - 0.5) * radius * 2
                val length = sqrt(x * x + y * y + z * z)
                x = x / length
                y = y / length
                z = z / length
                val c = Random.nextDouble().pow(1.0 / 3.0) * radius
                centerPos.add(x * c, y * c, z * c)
            }
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(ModParticles.FIZZLE)
                    .positions(positions)
                    .speed(Vec3(0.0, 0.1, 0.0))
                    .build(),
                PacketDistributor.TargetPoint(state.position.x, state.position.y, state.position.z, 100.0, state.world.dimension())
            )
        }
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return estimateBlocksHit(instance).coerceAtLeast(1.0)
    }

    private fun estimateBlocksHit(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val shellOnly = getShellOnly(instance)
        val radius = getRadius(instance)
        val volume = 4.0 / 3.0 * Math.PI * radius * radius * radius
        return if (shellOnly) {
            val innerVolume = 4.0 / 3.0 * Math.PI * (radius - 1) * (radius - 1) * (radius - 1)
            // Subtract out the inner volume to just leave the "shell"
            volume - innerVolume
        } else {
            volume
        }
    }

    fun setRadius(instance: SpellComponentInstance<*>, radius: Double) {
        instance.data.putDouble(NBT_RADIUS, radius)
    }

    fun getRadius(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_RADIUS)
    }

    fun setShellOnly(instance: SpellComponentInstance<*>, shellOnly: Boolean) {
        instance.data.putBoolean(NBT_SHELL_ONLY, shellOnly)
    }

    fun getShellOnly(instance: SpellComponentInstance<*>): Boolean {
        return instance.data.getBoolean(NBT_SHELL_ONLY)
    }

    fun setColor(instance: SpellComponentInstance<*>, color: Color) {
        instance.data.putString(NBT_COLOR, "${color.red} ${color.green} ${color.blue}")
    }

    fun getColor(instance: SpellComponentInstance<*>): Color {
        val rgb = instance.data.getString(NBT_COLOR).split(Regex("\\s+")).map { it.toInt() }
        return Color(rgb[0], rgb[1], rgb[2])
    }

    companion object {
        // The NBT keys
        private const val NBT_RADIUS = "radius"
        private const val NBT_SHELL_ONLY = "shell_only"
        private const val NBT_COLOR = "color"
    }
}