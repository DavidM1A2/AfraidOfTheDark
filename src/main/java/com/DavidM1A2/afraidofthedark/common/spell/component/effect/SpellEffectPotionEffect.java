package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.InvalidValueException;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Spell effect that applies potion effects
 */
public class SpellEffectPotionEffect extends AOTDSpellEffect
{
    // NBT constants spell potion stats
    private static final String NBT_POTION_TYPE = "potion_type";
    private static final String NBT_POTION_STRENGTH = "potion_strength";
    private static final String NBT_POTION_DURATION = "potion_duration";
    private static final String NBT_POTION_RADIUS = "potion_radius";

    // Spell potion fields
    private Potion potionType = Potion.getPotionFromResourceLocation("minecraft:speed");
    private int potionStrength = 0;
    private int potionDuration = 20;
    private float potionRadius = 2;

    /**
     * Constructor adds the editable props
     */
    public SpellEffectPotionEffect()
    {
        super();
        this.addEditableProperty(new SpellComponentProperty(
                "Potion Type",
                "The type of potion effect to apply. Must be using the minecraft naming convention, like 'minecraft:speed'.",
                newValue ->
                {
                    // Grab the potion associated with the text
                    Potion type = Potion.REGISTRY.getObject(new ResourceLocation(newValue));
                    // If type is not null it's a valid potion type so we store it, otherwise throw an exception
                    if (type != null)
                    {
                        this.potionType = type;
                    }
                    else
                    {
                        throw new InvalidValueException("Invalid potion type " + newValue + ", it was not found in the registry.");
                    }
                },
                () -> this.potionType.getRegistryName().toString()));
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Potion Strength")
                .withDescription("The level of the potion to apply, ex. 4 means apply 'Potion Type' at level 4.")
                .withSetter(newValue -> this.potionStrength = newValue - 1)
                .withGetter(() -> this.potionStrength + 1)
                .withDefaultValue(1)
                .withMinValue(1)
                .build());
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Potion Duration")
                .withDescription("The number of ticks the potion effect should run for.")
                .withSetter(newValue -> this.potionDuration = newValue)
                .withGetter(() -> this.potionDuration)
                .withDefaultValue(20)
                .withMinValue(1)
                .build());
        this.addEditableProperty(SpellComponentPropertyFactory.floatProperty()
                .withName("Potion Cloud Radius")
                .withDescription("The size of the potion cloud if the potion is delivered to a block and not an entity.")
                .withSetter(newValue -> this.potionRadius = newValue)
                .withGetter(() -> this.potionRadius)
                .withDefaultValue(2f)
                .withMinValue(0f)
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
        return 15 + this.potionDuration / 5.0 * this.potionStrength * Math.max(Math.sqrt(this.potionRadius), 1);
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        Vec3d exactPosition = state.getPosition();
        // If we hit an entity just apply the potion effect
        if (state.getEntity() != null)
        {
            Entity entityHit = state.getEntity();
            if (entityHit instanceof EntityLivingBase)
            {
                createParticlesAt(1, 3, exactPosition, entityHit.dimension);
                ((EntityLivingBase) entityHit).addPotionEffect(new PotionEffect(this.potionType, this.potionDuration, this.potionStrength));
            }
        }
        // If we hit the ground create an AOE potion effect cloud
        else
        {
            World world = state.getWorld();
            EntityAreaEffectCloud aoePotion = new EntityAreaEffectCloud(world, exactPosition.x, exactPosition.y, exactPosition.z);
            aoePotion.addEffect(new PotionEffect(this.potionType, this.potionDuration, this.potionStrength));
            aoePotion.setOwner(state.getSpell().getOwner());
            aoePotion.setRadius(this.potionRadius);
            aoePotion.setRadiusPerTick(0);
            aoePotion.setDuration(this.potionDuration);
            world.spawnEntity(aoePotion);
            createParticlesAt(4, 8, exactPosition, world.provider.getDimension());
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
        return ModSpellEffects.POTION_EFFECT;
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

        nbt.setString(NBT_POTION_TYPE, this.potionType.getRegistryName().toString());
        nbt.setInteger(NBT_POTION_DURATION, this.potionDuration);
        nbt.setInteger(NBT_POTION_STRENGTH, this.potionStrength);
        nbt.setFloat(NBT_POTION_RADIUS, this.potionRadius);

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

        this.potionType = Potion.getPotionFromResourceLocation(nbt.getString(NBT_POTION_TYPE));
        this.potionDuration = nbt.getInteger(NBT_POTION_DURATION);
        this.potionStrength = nbt.getInteger(NBT_POTION_STRENGTH);
        this.potionRadius = nbt.getFloat(NBT_POTION_RADIUS);
    }
}
