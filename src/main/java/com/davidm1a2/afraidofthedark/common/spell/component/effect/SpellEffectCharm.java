package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Effect that forces players to walk towards you or animals to mate
 */
public class SpellEffectCharm extends AOTDSpellEffect
{
    // NBT constants for charm duration
    private static final String NBT_CHARM_DURATION = "charm_duration";

    // The charm duration this effect gives
    private int charmDuration = 40;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectCharm()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Charm Duration")
                .withDescription("The number of ticks to charm to when hitting players.")
                .withSetter(newValue -> this.charmDuration = newValue)
                .withGetter(() -> this.charmDuration)
                .withDefaultValue(40)
                .withMinValue(1)
                .withMaxValue(1200)
                .build());
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 10 + this.charmDuration;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        Entity entity = state.getEntity();
        // If we hit an entity that is an animal set them in love
        if (entity instanceof EntityAnimal)
        {
            EntityAnimal entityAnimal = (EntityAnimal) entity;
            entityAnimal.setInLove(state.getSpell().getOwner());
        }
        // If we hit a player, force them to look at the casting player
        else if (entity instanceof EntityPlayer)
        {
            // Grab the player's charm data
            IAOTDPlayerSpellCharmData spellCharmData = entity.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA, null);
            // Charm them for the "charm duration"
            spellCharmData.setCharmTicks(this.charmDuration);
            // Set the charming entity
            spellCharmData.setCharmingEntityId(state.getSpell().getOwner().getPersistentID());

            ThreadLocalRandom random = ThreadLocalRandom.current();
            double width = entity.width;
            double height = entity.height;

            // Spawn 4 random heart particles
            for (int i = 0; i < 4; i++)
            {
                ((WorldServer) state.getWorld()).spawnParticle(
                        EnumParticleTypes.HEART,
                        // The position will be somewhere inside the player's hitbox
                        state.getPosition().x + (random.nextFloat() * width * 2.0F) - width,
                        state.getPosition().y + 0.5D + random.nextFloat() * height,
                        state.getPosition().z + random.nextFloat() * width * 2.0F - width,
                        // Spawn one particle
                        1,
                        // Randomize velocity
                        random.nextGaussian() * 0.02D,
                        random.nextGaussian() * 0.02D,
                        random.nextGaussian() * 0.02D,
                        0.02D
                );
            }
        }
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.CHARM;
    }

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setInteger(NBT_CHARM_DURATION, this.charmDuration);

        return nbt;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        this.charmDuration = nbt.getInteger(NBT_CHARM_DURATION);
    }
}
