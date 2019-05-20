package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Ghastly enaria task that lets her chase players
 */
public class GhastlyEnariaPlayerChase extends EntityAIBase
{
    // A reference to the enaria entity
    private EntityGhastlyEnaria enaria;
    // The target player
    private EntityPlayer targetPlayer;

    /**
     * Constructor just initializes the enaria reference
     *
     * @param enaria The enaria entity reference
     */
    public GhastlyEnariaPlayerChase(EntityGhastlyEnaria enaria)
    {
        this.enaria = enaria;
        // Sync movement, look, and etc state
        this.setMutexBits(1 | 2 | 4);
    }

    /**
     * This will only execute if enaria is not moving
     */
    @Override
    public boolean shouldExecute()
    {
        EntityMoveHelper entitymovehelper = this.enaria.getMoveHelper();
        return !entitymovehelper.isUpdating();
    }

    /**
     * False since the ai task only executes once
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return false;
    }

    /**
     * Executes the task. Finds a nearby player and follows them
     */
    @Override
    public void startExecuting()
    {
        int distanceBetweenIslands = AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
        // If the target player is null try and find a nearby player
        if (this.targetPlayer == null)
        {
            this.targetPlayer = this.enaria.world.getClosestPlayerToEntity(this.enaria, distanceBetweenIslands / 2);
        }

        // If there are no players nearby kill enaria
        if (this.targetPlayer == null || targetPlayer.isDead)
        {
            this.enaria.setDead();
        }
        else
        {
            // Otherwise follow the player if not benign
            if (!this.enaria.isBenign())
            {
                // Move to the player
                this.enaria.getMoveHelper().setMoveTo(this.targetPlayer.posX, this.targetPlayer.posY, this.targetPlayer.posZ, this.enaria.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            }
            // Face the player entity always
            this.enaria.faceEntity(this.targetPlayer, 360f, 360f);
            // If the player can see enaria, add slowness 4 to the player
            if (this.targetPlayer.canEntityBeSeen(this.enaria))
            {
                this.targetPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, 4, false, false));
            }
        }
    }
}
