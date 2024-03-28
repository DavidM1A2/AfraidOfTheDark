package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.world.entity.player.Player
import net.minecraftforge.fmllegacy.network.PacketDistributor

class ImbueSpellDeliveryMethod : AOTDSpellDeliveryMethod("imbue", ModResearches.SCROLL_FORMATION) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("uses"))
                .withSetter(this::setUses)
                .withGetter(this::getUses)
                .withDefaultValue(1)
                .withMinValue(1)
                .withMaxValue(16)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        val entity = state.entity
        val position = state.position
        val world = state.world
        if (entity !is Player) {
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(ModParticles.IMBUE_FIZZLE)
                    .position(position)
                    .build(),
                PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, world.dimension())
            )
            return
        }

        val inventory = entity.inventory
        for (itemStack in inventory.items + inventory.offhand) {
            if (itemStack.item == ModItems.SPELL_SCROLL) {
                if (ModItems.SPELL_SCROLL.isEmpty(itemStack)) {
                    val uses = getUses(state.getCurrentStage().deliveryInstance!!)
                    ModItems.SPELL_SCROLL.setUses(itemStack, uses)
                    ModItems.SPELL_SCROLL.setMaxUses(itemStack, uses)
                    val oldSpellStages = state.spell.spellStages
                    val newSpellStages = oldSpellStages.subList(state.stageIndex, oldSpellStages.size).map {
                        SpellStage(it.serializeNBT())
                    }
                    newSpellStages[0].deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.SELF).apply {
                        setDefaults()
                    }
                    val newSpell = Spell().apply {
                        name = state.spell.name
                        spellStages.addAll(newSpellStages)
                    }
                    ModItems.SPELL_SCROLL.setSpell(itemStack, newSpell)

                    AfraidOfTheDark.packetHandler.sendToAllAround(
                        ParticlePacket.builder()
                            .particle(ModParticles.IMBUE)
                            .position(entity.position().add(0.0, entity.bbHeight / 2.0, 0.0).add(state.direction.scale(0.5)))
                            .iterations(10)
                            .build(),
                        PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, world.dimension())
                    )
                    return
                }
            }
        }

        // If we get here the particle fizzles because the player had no spell scrolls
        AfraidOfTheDark.packetHandler.sendToAllAround(
            ParticlePacket.builder()
                .particle(ModParticles.IMBUE_FIZZLE)
                .position(entity.position().add(0.0, 0.2, 0.0).add(state.direction.scale(0.5)))
                .build(),
            PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, world.dimension())
        )
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 10.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return getUses(instance).toDouble()
    }

    fun setUses(instance: SpellComponentInstance<*>, uses: Int) {
        instance.data.putInt(NBT_USES, uses)
    }

    fun getUses(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_USES)
    }

    companion object {
        private const val NBT_USES = "uses"
    }
}