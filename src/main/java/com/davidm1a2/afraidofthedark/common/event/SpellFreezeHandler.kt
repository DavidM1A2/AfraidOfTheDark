package com.davidm1a2.afraidofthedark.common.event;

import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Handles the on server tick to update any existing spell freeze effects
 */
public class SpellFreezeHandler
{
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == Side.SERVER)
        {
            EntityPlayer entityPlayer = event.player;
            IAOTDPlayerSpellFreezeData playerFreezeData = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null);
            // Ensure there's at least 1 freeze tick remaining
            if (playerFreezeData.getFreezeTicks() > 0)
            {
                // Reduce the freeze ticks by 1
                int newFreezeTicks = playerFreezeData.getFreezeTicks() - 1;
                playerFreezeData.setFreezeTicks(newFreezeTicks);
                // If no freeze ticks are left tell the client
                if (newFreezeTicks == 0)
                {
                    playerFreezeData.sync(entityPlayer);
                }

                // Freeze the player's location
                Vec3d freezePosition = playerFreezeData.getFreezePosition();
                ((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(
                        freezePosition.x,
                        freezePosition.y,
                        freezePosition.z,
                        playerFreezeData.getFreezeYaw(),
                        playerFreezeData.getFreezePitch());
            }
        }
    }

    /**
     * Called on the client side to block all movement input
     *
     * @param event The event to modify
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onInputUpdateEvent(InputUpdateEvent event)
    {
        IAOTDPlayerSpellFreezeData playerFreezeData = event.getEntityPlayer().getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null);
        // If the player is frozen block all movement
        if (playerFreezeData.getFreezeTicks() > 0)
        {
            MovementInput input = event.getMovementInput();
            input.backKeyDown = false;
            input.forwardKeyDown = false;
            input.jump = false;
            input.leftKeyDown = false;
            input.rightKeyDown = false;
            input.moveStrafe = 0;
            input.moveForward = 0;
            input.sneak = false;
        }
    }
}
