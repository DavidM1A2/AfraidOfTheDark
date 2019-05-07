package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiClickAndDragable;
import com.DavidM1A2.afraidofthedark.client.gui.base.SpriteSheetController;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.specialControls.AOTDGuiResearchNodeButton;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiSpriteSheetImage;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * The research GUI used by the blood stained journal to show what has been unlocked and what has not been unlocked
 */
public class BloodStainedJournalResearchGUI extends AOTDGuiClickAndDragable
{
    // Static fields that will keep track of where the offset was the last time the UI was open
    private static int lastGuiOffsetX = 0;
    private static int lastGuiOffsetY = 0;
    // The GUI scroll background
    private AOTDGuiImage scrollBackground;
    // The border around the research
    private AOTDGuiImage backgroundBorder;
    // The base panel that contains all researches
    private AOTDGuiPanel researchTreeBase;
    // The panel that contains all research nodes
    private AOTDGuiPanel researchTree;
    // Two sprite sheet controllers, one for vertical arrows and one for horizontal arrows
    private SpriteSheetController nodeConnectorControllerVertical = new SpriteSheetController(500, 20, 60, 180, true, true);
    private SpriteSheetController nodeConnectorControllerHorizontal = new SpriteSheetController(500, 20, 180, 60, true, false);

    /**
     * Constructor initializes the entire GUI
     */
    public BloodStainedJournalResearchGUI()
    {
        // Background will always be 256x256
        int backgroundHeight = 256;
        int backgroundWidth = 256;

        // Calculate the various positions of GUI elements on the screen
        int xPosScroll = (Constants.GUI_WIDTH - backgroundWidth) / 2;
        int yPosScroll = (Constants.GUI_HEIGHT - backgroundHeight) / 2;

        // Recall our previous GUI offsets from the last time we had the GUI open, this helps remember where we left off in the UI
        this.guiOffsetX = lastGuiOffsetX;
        this.guiOffsetY = lastGuiOffsetY;

        // Create the research tree panel that will hold all the research nodes
        this.researchTreeBase = new AOTDGuiPanel(xPosScroll, yPosScroll, backgroundWidth, backgroundHeight, true);
        this.researchTree = new AOTDGuiPanel(-this.guiOffsetX, -this.guiOffsetY, backgroundWidth, backgroundHeight, false);
        this.getContentPane().add(researchTreeBase);

        this.scrollBackground = new AOTDGuiImage(0, 0, backgroundWidth, backgroundHeight, 1024, 1024, "afraidofthedark:textures/gui/blood_stained_journal_research_backdrop.png");
        this.backgroundBorder = new AOTDGuiImage(0, 0, backgroundWidth, backgroundHeight, "afraidofthedark:textures/gui/blood_stained_journal_research_background.png");
        this.scrollBackground.setU(this.guiOffsetX + 384);
        this.scrollBackground.setV(this.guiOffsetY + 768);
        this.researchTreeBase.add(scrollBackground);
        this.researchTreeBase.add(researchTree);
        this.researchTreeBase.add(backgroundBorder);

        // Grab the player's research to be used later...
        IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);

        // Create a node listener that controls the behavior of selected research nodes
        AOTDMouseListener nodeListener = new AOTDMouseListener()
        {
            /**
             * Called when we enter the node with our mouse, it just plays a click sound
             *
             * @param event The event containing information about the mouse entering event
             */
            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // If the node is visible then play the hover sound since we moved our mouse over it
                if (event.getSource().isVisible())
                {
                    entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.7f, 1.9f);
                }
            }

            /**
             * Called when we click a node, opens the page UI
             *
             * @param event The event containing information about the mouse click
             */
            @Override
            public void mouseClicked(AOTDMouseEvent event)
            {
                // Ensure the clicked button is a ResearchNodeButton and the button used clicked is left
                if (event.getSource().isHovered() && event.getSource() instanceof AOTDGuiResearchNodeButton && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    AOTDGuiResearchNodeButton current = (AOTDGuiResearchNodeButton) event.getSource();
                    // Only allow clicking the button if it's within the container
                    if (current.getParent().getParent().intersects(current))
                    {
                        // Store the selected research
                        ClientData.getInstance().setLastSelectedResearch(current.getResearch());
                        // If the research is researched show the page UI, otherwise show the pre-page UI
                        if (playerResearch.isResearched(current.getResearch()))
                        {
                            entityPlayer.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
                        }
                        else if (playerResearch.isResearched(current.getResearch().getPreRequisite()))
                        {
                            entityPlayer.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.BLOOD_STAINED_JOURNAL_PAGE_PRE_ID, entityPlayer.world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
                        }
                    }
                }
            }
        };

        // Distance between research icon nodes is 75px
        int distanceBetweenNodes = 75;

        // Iterate over all researches, first add each of the connector arrows
        for (Research research : ModRegistries.RESEARCH.getValuesCollection())
        {
            // Compute the button's X and Y position
            int xPos = backgroundWidth / 2 - 16 + distanceBetweenNodes * research.getXPosition();
            int yPos = backgroundHeight - 50 - distanceBetweenNodes * research.getZPosition();

            // If the research has a prerequisite add an arrow from the prerequisite to the current node first
            if (research.getPreRequisite() != null)
            {
                // Only add the arrows if we know if the
                if (playerResearch.isResearched(research) || playerResearch.canResearch(research))
                {
                    // Grab the prerequisite research
                    Research previous = research.getPreRequisite();

                    // Depending on where the research is in relation to its previous research create an arrow
                    if (research.getXPosition() < previous.getXPosition())
                    {
                        this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 26, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/research_horizontal.png"), this.nodeConnectorControllerHorizontal));
                    }
                    else if (research.getXPosition() > previous.getXPosition())
                    {
                        this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos - 50, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/research_horizontal.png"), this.nodeConnectorControllerHorizontal));
                    }
                    else if (research.getZPosition() > previous.getZPosition())
                    {
                        this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos + 30, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/research_vertical.png"), this.nodeConnectorControllerVertical));
                    }
                    else if (research.getZPosition() < previous.getZPosition())
                    {
                        this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos - 46, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/research_vertical.png"), this.nodeConnectorControllerVertical));
                    }
                }
            }

        }

        // Now that we have all arrows added add each research node on top to ensure correct z-layer order
        for (Research research : ModRegistries.RESEARCH.getValuesCollection())
        {
            // Compute the button's X and Y position
            int xPos = backgroundWidth / 2 - 16 + distanceBetweenNodes * research.getXPosition();
            int yPos = backgroundHeight - 50 - distanceBetweenNodes * research.getZPosition();

            // Create the research button
            AOTDGuiResearchNodeButton researchNode = new AOTDGuiResearchNodeButton(xPos, yPos, research);
            // Add our pre-build listener to this node
            researchNode.addMouseListener(nodeListener);
            // Add the node to our tree
            this.researchTree.add(researchNode);
        }

        // Add our sprite sheet controllers
        this.addSpriteSheetController(this.nodeConnectorControllerHorizontal);
        this.addSpriteSheetController(this.nodeConnectorControllerVertical);
    }

    /**
     * Called when we drag the mouse
     *
     * @param mouseX            The mouse X position
     * @param mouseY            The mouse Y position
     * @param lastButtonClicked The last button clicked
     * @param timeBetweenClicks The time between the last click
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeBetweenClicks)
    {
        // Call the super method
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);

        // Update the research tree's X and Y coordinates
        this.researchTree.setX(-this.guiOffsetX + researchTree.getParent().getX());
        this.researchTree.setY(-this.guiOffsetY + researchTree.getParent().getY());

        // Set the scroll background U and V
        scrollBackground.setU(this.guiOffsetX + 384);
        scrollBackground.setV(this.guiOffsetY + 768);
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    @Override
    protected void checkOutOfBounds()
    {
        this.guiOffsetX = MathHelper.clamp(this.guiOffsetX, -400, 400);
        this.guiOffsetY = MathHelper.clamp(this.guiOffsetY, -768, 0);
    }

    /**
     * Called when the screen is unloaded. Used set the last known gui offsets
     */
    @Override
    public void onGuiClosed()
    {
        lastGuiOffsetX = guiOffsetX;
        lastGuiOffsetY = guiOffsetY;
        super.onGuiClosed();
    }

    /**
     * @return True since we can use the inventory screen to close the GUI
     */
    @Override
    public boolean inventoryToCloseGuiScreen()
    {
        return true;
    }

    /**
     * @return True since we want to draw the gradient background
     */
    @Override
    public boolean drawGradientBackground()
    {
        return true;
    }
}
