package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.packets.animationPackets.SyncAnimation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Class that allows enaria to attack
 */
public class EntityAIAttackEnaria extends EntityAIBase
{
    // The enaria entity that is attacking
    private final EntityEnaria enaria;
    // The target enaria is hitting
    private EntityLivingBase target;
    // The ticks until enarias next attack
    private int ticksUntilNextAttack;

    /**
     * Constructor sets the enaria entity
     *
     * @param enaria The enaria entity
     */
    public EntityAIAttackEnaria(EntityEnaria enaria)
    {
        this.enaria = enaria;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     *
     * @return True if the target is non-null, false otherwise
     */
    @Override
    public boolean shouldExecute()
    {
        // Get enaria's target
        EntityLivingBase target = this.enaria.getAttackTarget();

        // if there's no target don't execute, otherwise set the target
        if (target == null)
        {
            return false;
        }
        else
        {
            this.target = target;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     *
     * @return True if there is no target or there is no path to the entity
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute() || !this.enaria.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.target = null;
        this.ticksUntilNextAttack = 0;
    }

    /**
     * Updates the task and attacks the target
     */
    public void updateTask()
    {
        // Look at the target
        this.enaria.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        // Ddecrease the ticks until next attack
        this.ticksUntilNextAttack = this.ticksUntilNextAttack - 1;

        // If ticks until next attack is 0 then perform a spell attack
        if (this.ticksUntilNextAttack <= 0)
        {
            // The next spell will be in the next 7 and 13 seconds
            this.ticksUntilNextAttack = this.enaria.world.rand.nextInt(100) + 140;
            // Perform a random spell attack
            this.enaria.getEnariaAttacks().performRandomSpell();
            // Make sure you can't use potions on enaria, she clears them every spell cast
            this.enaria.clearActivePotions();
            // Play the spell cast animation
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(new SyncAnimation("spell", this.enaria, "spell"), new NetworkRegistry.TargetPoint(this.enaria.dimension, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 100));
        }
        // If ticks until next attack is a multiple of 40 then perform a basic attack
        else if (this.ticksUntilNextAttack % 40 == 0)
        {
            // If Enaria is not invisible basic attack
            if (!this.enaria.isPotionActive(Potion.getPotionById(14)))
            {
                // Perform basic attack
                this.enaria.getEnariaAttacks().performBasicAttack();
                // Show the auto attack animation
                AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(new SyncAnimation("autoattack", this.enaria, "spell", "autoattack"), new NetworkRegistry.TargetPoint(this.enaria.dimension, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 100));
            }
        }
    }
}
