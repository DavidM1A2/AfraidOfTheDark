package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraftforge.common.MinecraftForge
import kotlin.math.roundToInt

class SpellScrollItem : AOTDItem("spell_scroll", Properties().stacksTo(1)) {
    private val preRequisiteResearch: Research? by lazy {
        ModResearches.SPELL_SCROLLS.preRequisite
    }

    override fun useOn(itemUseContext: UseOnContext): InteractionResult {
        val world = itemUseContext.level
        val blockState = world.getBlockState(itemUseContext.clickedPos)
        val player = itemUseContext.player
        val spell = getSpell(itemUseContext.itemInHand)
        if (player != null && spell != null && blockState.block == ModBlocks.SPELL_CRAFTING_TABLE) {
            if (!world.isClientSide) {
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(player, ModResearches.ACQUIRED_KNOWLEDGE))
            }
            // Check if preRequisite is researched, not the actual research. This ensures if our acquired knowledge packet doesn't arrive in time we'll still learn the spell
            val preRequisite = ModResearches.ACQUIRED_KNOWLEDGE.preRequisite
            if (preRequisite == null || player.getResearch().isResearched(preRequisite)) {
                if (knowsSpellComponents(player, spell)) {
                    learnSpell(player, spell)
                    if (!player.isCreative) {
                        itemUseContext.itemInHand.shrink(1)
                    }
                } else {
                    if (!world.isClientSide) {
                        player.sendMessage(TranslatableComponent("message.afraidofthedark.spell_scroll.unknown_component"))
                    }
                }
                return InteractionResult.CONSUME
            }
        }
        return super.useOn(itemUseContext)
    }

    override fun use(world: Level, playerEntity: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = playerEntity.getItemInHand(hand)

        if (preRequisiteResearch != null && !playerEntity.getResearch().isResearched(preRequisiteResearch!!)) {
            if (!world.isClientSide) {
                playerEntity.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            return InteractionResultHolder.fail(itemStack)
        }

        val spell = getSpell(itemStack)
        if (spell == null) {
            if (!world.isClientSide) {
                playerEntity.sendMessage(TranslatableComponent("message.afraidofthedark.spell_scroll.empty"))
            }
            return InteractionResultHolder.pass(itemStack)
        }

        val uses = getUses(itemStack)
        if (uses < 1) {
            if (!world.isClientSide) {
                playerEntity.sendMessage(TranslatableComponent("message.afraidofthedark.spell_scroll.no_uses_left"))
            }
            itemStack.shrink(1)
            return InteractionResultHolder.success(itemStack)
        }

        val newUses = if (playerEntity.isCreative) {
            uses
        } else {
            uses - 1
        }
        setUses(itemStack, newUses)
        if (!world.isClientSide) {
            if (newUses == 0) {
                itemStack.shrink(1)
            }
            spell.attemptToCast(playerEntity, isSpellScroll = true)
        }

        return InteractionResultHolder.success(itemStack)
    }

    override fun getBarWidth(stack: ItemStack): Int {
        return if (isEmpty(stack)) {
            0
        } else {
            val uses = getUses(stack)
            val maxUses = getMaxUses(stack)
            if (maxUses == 0 || uses > maxUses || uses < 0) {
                0
            } else {
                // Bar is 13px wide at max
                (13*(1 - (uses.toDouble() / maxUses.toDouble()))).roundToInt()
            }
        }
    }

    override fun isFoil(stack: ItemStack): Boolean {
        return !isEmpty(stack)
    }

    override fun isBarVisible(stack: ItemStack): Boolean {
        return !isEmpty(stack)
    }

    override fun getName(itemStack: ItemStack): Component {
        val spell = getSpell(itemStack)
        return if (spell == null) {
            TranslatableComponent("item.afraidofthedark.spell_scroll")
        } else {
            TranslatableComponent("item.afraidofthedark.spell_scroll_filled", spell.name)
        }
    }

    private fun knowsSpellComponents(playerEntity: Player, spell: Spell): Boolean {
        val research = playerEntity.getResearch()
        for (spellStage in spell.spellStages) {
            var prereqResearch = spellStage.deliveryInstance?.component?.prerequisiteResearch
            if (prereqResearch != null && !research.isResearched(prereqResearch)) {
                return false
            }
            for (effect in spellStage.effects) {
                prereqResearch = effect?.component?.prerequisiteResearch
                if (prereqResearch != null && !research.isResearched(prereqResearch)) {
                    return false
                }
            }
        }
        return true
    }

    private fun learnSpell(playerEntity: Player, spell: Spell) {
        val world = playerEntity.level
        if (!world.isClientSide) {
            playerEntity.sendMessage(TranslatableComponent("message.afraidofthedark.spell_scroll.learn_spell", spell.name))
        }
        if (world.isClientSide) {
            playerEntity.playSound(ModSounds.SCROLL_LEARNED, 2f, 1f)
        }

        playerEntity.getSpellManager().createSpell(spell)
    }

    fun setSpell(itemStack: ItemStack, spell: Spell) {
        NBTHelper.setCompound(itemStack, NBT_SPELL, spell.serializeNBT())
    }

    fun getSpell(itemStack: ItemStack): Spell? {
        return NBTHelper.getCompound(itemStack, NBT_SPELL)?.let { Spell(it) }
    }

    fun isEmpty(itemStack: ItemStack): Boolean {
        return !NBTHelper.hasTag(itemStack, NBT_SPELL) || !NBTHelper.hasTag(itemStack, NBT_USES) || !NBTHelper.hasTag(itemStack, NBT_MAX_USES)
    }

    fun setUses(itemStack: ItemStack, uses: Int) {
        NBTHelper.setInteger(itemStack, NBT_USES, uses)
    }

    fun getUses(itemStack: ItemStack): Int {
        return NBTHelper.getInteger(itemStack, NBT_USES) ?: 0
    }

    fun setMaxUses(itemStack: ItemStack, uses: Int) {
        NBTHelper.setInteger(itemStack, NBT_MAX_USES, uses)
    }

    fun getMaxUses(itemStack: ItemStack): Int {
        return NBTHelper.getInteger(itemStack, NBT_MAX_USES) ?: 1
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && (preRequisiteResearch == null || player.getResearch().isResearched(preRequisiteResearch!!))) {
            val spell = getSpell(itemStack)
            if (spell == null) {
                tooltip.add(TranslatableComponent("tooltip.afraidofthedark.spell_scroll.empty"))
            } else {
                tooltip.add(TranslatableComponent("tooltip.afraidofthedark.spell_scroll.spell_name", spell.name))
                tooltip.add(TranslatableComponent("tooltip.afraidofthedark.spell_scroll.uses_remaining", getUses(itemStack)))
                for ((index, spellStage) in spell.spellStages.withIndex()) {
                    tooltip.add(
                        TranslatableComponent(
                            "tooltip.afraidofthedark.spell_scroll.spell_stage",
                            index + 1,
                            spellStage.deliveryInstance?.component?.getName() ?: "-",
                            spellStage.effects[0]?.component?.getName() ?: "-",
                            spellStage.effects[1]?.component?.getName() ?: "-",
                            spellStage.effects[2]?.component?.getName() ?: "-",
                            spellStage.effects[3]?.component?.getName() ?: "-"
                        )
                    )
                }
            }
        } else {
            tooltip.add(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
    }

    companion object {
        private const val NBT_SPELL = "spell"
        private const val NBT_USES = "uses"
        private const val NBT_MAX_USES = "max_uses"
    }
}