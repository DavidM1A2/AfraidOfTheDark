package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellConeEntity
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.ConeUtils
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.PacketDistributor
import java.awt.Color
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

class ConeSpellDeliveryMethod : AOTDSpellDeliveryMethod("cone", ModResearches.MAGIC_MASTERY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
                .withDefaultValue(2.0)
                .withMinValue(1.0)
                .withMaxValue(10.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("length"))
                .withSetter(this::setLength)
                .withGetter(this::getLength)
                .withDefaultValue(4.0)
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

    override fun executeDelivery(state: DeliveryTransitionState) {
        val world = state.world
        val deliveryMethod = state.getCurrentStage().deliveryInstance!!
        val radius = getRadius(deliveryMethod)
        val length = getLength(deliveryMethod)
        val shellOnly = getShellOnly(deliveryMethod)

        val tipPos = state.position
        val forwardDir = state.direction
        val upDir = state.normal
        val leftDir = forwardDir.cross(upDir).normalize()

        world.addFreshEntity(SpellConeEntity(world, tipPos, radius.toFloat(), length.toFloat(), getColor(deliveryMethod), forwardDir))

        // Find the smallest (x, y, z) and biggest (x, y, z) coordinates to loop through. Ceil/floor the values, so we don't cut off any blocks partially within the cone
        val boundingBox = ConeUtils.getBoundingBox(tipPos, forwardDir, radius, length, upDir)
        val minX = floor(boundingBox.minX).toInt()
        val minY = floor(boundingBox.minY).toInt()
        val minZ = floor(boundingBox.minZ).toInt()
        val maxX = ceil(boundingBox.maxX).toInt()
        val maxY = ceil(boundingBox.maxY).toInt()
        val maxZ = ceil(boundingBox.maxZ).toInt()

        // We'll need a triple for loop to go over every block in the rectangle surrounding the cone. We'll filter out any points that lie outside the cone.
        var oneEffectProcd = false
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val conePos = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
                    if (isWithinCone(tipPos, forwardDir, length, radius, conePos, shellOnly)) {
                        var newDirection = conePos.subtract(tipPos).normalize()
                        // Direction may be 0 if conePos = tipPos. In this case, move it up
                        if (newDirection == Vector3d.ZERO) {
                            newDirection = Vector3d(0.0, 1.0, 0.0)
                        }
                        var newNormal = newDirection.getNormal()
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (newNormal == Vector3d.ZERO) {
                            newNormal = Vector3d(1.0, 0.0, 0.0)
                        }

                        val newState = DeliveryTransitionState(
                            spell = state.spell,
                            stageIndex = state.stageIndex,
                            world = world,
                            position = conePos,
                            blockPosition = BlockPos(conePos),
                            direction = newDirection,
                            normal = newNormal,
                            casterEntity = state.casterEntity
                        )
                        val procResult = procEffects(newState, false)
                        oneEffectProcd = oneEffectProcd || procResult.isSuccess
                        transitionFrom(newState)
                    }
                }
            }
        }

        // Create fizzle particles in the cone if the effect failed to proc
        if (!oneEffectProcd) {
            // Pick random positions within the cone
            val numParticles = (5 + length + radius).toInt() * 2
            val positions = List(numParticles) {
                val distanceDownCone = Random.nextDouble() * length
                val radiusAtDistance = distanceDownCone * radius / length
                tipPos.add(forwardDir.scale(distanceDownCone))
                    .add(leftDir.scale((Random.nextDouble() - 0.5) * radiusAtDistance * 2))
                    .add(upDir.scale((Random.nextDouble() - 0.5) * radiusAtDistance * 2))
            }
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(ModParticles.FIZZLE)
                    .positions(positions)
                    .speed(Vector3d(0.0, 0.1, 0.0))
                    .build(),
                PacketDistributor.TargetPoint(tipPos.x, tipPos.y, tipPos.z, 100.0, world.dimension())
            )
        }
    }

    private fun isWithinCone(tip: Vector3d, direction: Vector3d, height: Double, radius: Double, point: Vector3d, shellOnly: Boolean): Boolean {
        // To detect if a point is within the cone or not, we can use: https://stackoverflow.com/questions/12826117/how-can-i-detect-if-a-point-is-inside-a-cone-or-not-in-3d-space
        val coneDistance = point.subtract(tip).dot(direction)
        if (coneDistance < 0 || coneDistance > height) {
            return false
        }

        val coneRadius = (coneDistance / height) * radius
        val orthographicDistance = point.subtract(tip).subtract(direction.scale(coneDistance)).length()
        return orthographicDistance < coneRadius && (!shellOnly || (coneRadius - orthographicDistance) <= SHELL_ONLY_MARGIN)
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return estimateBlocksHit(instance).coerceAtLeast(1.0)
    }

    private fun estimateBlocksHit(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val radius = getRadius(instance)
        val length = getLength(instance)
        val shellOnly = getShellOnly(instance)

        val volume = PI * radius * radius * length / 3
        return if (shellOnly) {
            val innerVolume = PI * (radius - 1) * (radius - 1) * length / 3
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

    fun setLength(instance: SpellComponentInstance<*>, length: Double) {
        instance.data.putDouble(NBT_LENGTH, length)
    }

    fun getLength(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_LENGTH)
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
        private const val SHELL_ONLY_MARGIN = 1.5

        private const val NBT_RADIUS = "radius"
        private const val NBT_LENGTH = "length"
        private const val NBT_SHELL_ONLY = "shell_only"
        private const val NBT_COLOR = "color"
    }
}