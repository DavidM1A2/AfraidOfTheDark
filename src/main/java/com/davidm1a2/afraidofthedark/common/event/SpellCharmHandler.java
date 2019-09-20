package com.davidm1a2.afraidofthedark.common.event;

import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Handles events that let the player be charmed
 */
public class SpellCharmHandler
{
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == Side.SERVER)
        {
            EntityPlayer entityPlayer = event.player;
            IAOTDPlayerSpellCharmData playerCharmData = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA, null);
            // Ensure there's at least 1 charm tick remaining
            if (playerCharmData.getCharmTicks() > 0)
            {
                // Reduce the charm ticks by 1
                playerCharmData.setCharmTicks(playerCharmData.getCharmTicks() - 1);

                // Force the player to look at the entity
                UUID charmingEntityId = playerCharmData.getCharmingEntityId();
                Entity charmingEntity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(charmingEntityId);
                // If the player is non-null set the player's facing
                if (charmingEntity != null)
                {
                    // A player cant charm themselves
                    if (!entityPlayer.getPersistentID().equals(charmingEntityId))
                    {
                        // Compute the vector from the charming entity to the charmed entity
                        Vec3d direction = new Vec3d(charmingEntity.posX, charmingEntity.posY, charmingEntity.posZ).subtract(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ).normalize();

                        // Convert 3d direction vector to pitch and yaw
                        float yaw = (float) (-Math.atan2(direction.x, direction.z) * 180 / Math.PI);
                        float pitch = (float) (-Math.asin(direction.y) * 180 / Math.PI);

                        // Set the player's look to be at the charming entity
                        ((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(
                                0,
                                0,
                                0,
                                yaw,
                                pitch,
                                ImmutableSet.of(SPacketPlayerPosLook.EnumFlags.X, SPacketPlayerPosLook.EnumFlags.Y, SPacketPlayerPosLook.EnumFlags.Z));
                    }
                }
            }
        }
    }
}
