package com.davidm1a2.afraidofthedark.client.gui.guiScreens;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiClickAndDragable;
import com.davidm1a2.afraidofthedark.client.gui.base.SpriteSheetController;
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment;
import com.davidm1a2.afraidofthedark.client.gui.eventListeners.IAOTDMouseListener;
import com.davidm1a2.afraidofthedark.client.gui.eventListeners.IAOTDMouseMoveListener;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiResearchNodeButton;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiLabel;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiSpriteSheetImage;
import com.davidm1a2.afraidofthedark.client.settings.ClientData;
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.Color;

/**
 * The research GUI used by the blood stained journal to show what has been unlocked and what has not been unlocked
 */
public class BloodStainedJournalResearchGUI extends AOTDGuiClickAndDragable
{
    // Set the size of the UI to always be 256x256
    private static final int BACKGROUND_HEIGHT = 256;
    private static final int BACKGROUND_WIDTH = 256;
    // Distance between research icon nodes is 75px
    private static final int DISTANCE_BETWEEN_RESEARCHES = 75;
    // Static fields that will keep track of where the offset was the last time the UI was open
    private static int lastGuiOffsetX = 0;
    private static int lastGuiOffsetY = 0;
    // The GUI scroll background
    private final AOTDGuiImage scrollBackground;
    // The panel that contains all research nodes
    private final AOTDGuiPanel researchTree;
    // Two sprite sheet controllers, one for vertical arrows and one for horizontal arrows
    private final SpriteSheetController nodeConnectorControllerVertical = new SpriteSheetController(500, 20, 60, 180, true, true);
    private final SpriteSheetController nodeConnectorControllerHorizontal = new SpriteSheetController(500, 20, 180, 60, true, false);
    // Two node listeners to be used by all research tree nodes
    private final IAOTDMouseMoveListener researchNodeMouseMoveListener;
    private final IAOTDMouseListener researchNodeMouseListener;

    /**
     * Constructor initializes the entire GUI
     *
     * @param isCheatSheet True if the research GUI is a cheat sheet, false otherwise
     */
    public BloodStainedJournalResearchGUI(boolean isCheatSheet)
    {
        // Calculate the various positions of GUI elements on the screen
        int xPosScroll = (Constants.GUI_WIDTH - BACKGROUND_WIDTH) / 2;
        int yPosScroll = (Constants.GUI_HEIGHT - BACKGROUND_HEIGHT) / 2;

        // Recall our previous GUI offsets from the last time we had the GUI open, this helps remember where we left off in the UI
        this.guiOffsetX = lastGuiOffsetX;
        this.guiOffsetY = lastGuiOffsetY;

        // Create the research tree panel that will hold all the research nodes
        // The base panel that contains all researches
        AOTDGuiPanel researchTreeBase = new AOTDGuiPanel(xPosScroll, yPosScroll, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, true);
        this.researchTree = new AOTDGuiPanel(-this.guiOffsetX, -this.guiOffsetY, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, false);
        this.getContentPane().add(researchTreeBase);

        this.scrollBackground = new AOTDGuiImage(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 1024, 1024, "afraidofthedark:textures/gui/journal_tech_tree/background.png");
        this.scrollBackground.setU(this.guiOffsetX + (this.scrollBackground.getMaxTextureWidth() - this.scrollBackground.getWidth()) / 2);
        this.scrollBackground.setV(this.guiOffsetY + (this.scrollBackground.getMaxTextureHeight() - this.scrollBackground.getHeight()));
        // The border around the research
        AOTDGuiImage backgroundBorder = new AOTDGuiImage(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, "afraidofthedark:textures/gui/journal_tech_tree/frame.png");
        researchTreeBase.add(scrollBackground);
        researchTreeBase.add(researchTree);
        researchTreeBase.add(backgroundBorder);

        // If this is a cheat sheet add a label on top to make that clear
        if (isCheatSheet)
        {
            // Put the label on top and set the color to white
            AOTDGuiLabel lblCheatSheet = new AOTDGuiLabel(15, 20, BACKGROUND_WIDTH - 30, 20, ClientData.getInstance().getTargaMSHandFontSized(32f));
            lblCheatSheet.setTextAlignment(TextAlignment.ALIGN_CENTER);
            lblCheatSheet.setTextColor(new Color(255, 255, 255));
            lblCheatSheet.setText("Cheat sheet - select researches to unlock them");
            researchTreeBase.add(lblCheatSheet);
        }

        // Grab the player's research to be used later...
        IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);

        // Create two node listeners that controls the behavior of selected research nodes
        this.researchNodeMouseMoveListener = event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
            {
                // If the node is visible then play the hover sound since we moved our mouse over it
                if (event.getSource().isVisible() && researchTreeBase.intersects(event.getSource()))
                {
                    entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.7f, 1.9f);
                }
            }
        };
        this.researchNodeMouseListener = event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                // Ensure the clicked button is a ResearchNodeButton and the button used clicked is left
                if (event.getSource().isHovered() && event.getSource() instanceof AOTDGuiResearchNodeButton && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    AOTDGuiResearchNodeButton current = (AOTDGuiResearchNodeButton) event.getSource();
                    // Only allow clicking the button if it's within the container
                    if (researchTreeBase.intersects(current))
                    {
                        // If this isn't a cheat sheet open the research page
                        if (!isCheatSheet)
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
                        // If this is a cheat sheet unlock the clicked research
                        else
                        {
                            // If this research can be researched unlock it
                            if (playerResearch.canResearch(current.getResearch()))
                            {
                                playerResearch.setResearchAndAlert(current.getResearch(), true, entityPlayer);
                                playerResearch.sync(entityPlayer, false);
                            }
                        }
                    }
                }
            }
        };

        // Go over all known researches and add a connector for each that has a known pre-requisite
        ModRegistries.RESEARCH.getValuesCollection().stream()
                // We can only draw connectors if we have a pre-requisite
                .filter(research -> research.getPreRequisite() != null)
                // Only add the connectors if we know if the previous research or the current research
                .filter(research -> playerResearch.isResearched(research) || playerResearch.canResearch(research))
                .forEach(this::addConnector);

        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.getValuesCollection().stream()
                // Only add the node if we know if the previous research or the current research
                .filter(research -> playerResearch.isResearched(research) || playerResearch.canResearch(research))
                .forEach(this::addResearchButton);

        // Add our sprite sheet controllers
        this.addSpriteSheetController(nodeConnectorControllerHorizontal);
        this.addSpriteSheetController(nodeConnectorControllerVertical);
    }

    /**
     * Adds a connector from this research's pre-requisite to itself
     *
     * @param research The research to add a connector to
     */
    private void addConnector(Research research)
    {
        // Compute the button's X and Y position
        int xPos = BACKGROUND_WIDTH / 2 - 16 + DISTANCE_BETWEEN_RESEARCHES * research.getXPosition();
        int yPos = BACKGROUND_HEIGHT - 50 - DISTANCE_BETWEEN_RESEARCHES * research.getZPosition();

        // Grab the prerequisite research
        Research previous = research.getPreRequisite();

        // Depending on where the research is in relation to its previous research create an arrow
        if (research.getXPosition() < previous.getXPosition())
        {
            this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 26, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/horizontal_connector.png"), nodeConnectorControllerHorizontal));
        }
        else if (research.getXPosition() > previous.getXPosition())
        {
            this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos - 50, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/horizontal_connector.png"), nodeConnectorControllerHorizontal));
        }
        else if (research.getZPosition() > previous.getZPosition())
        {
            this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos + 30, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/vertical_connector.png"), nodeConnectorControllerVertical));
        }
        else if (research.getZPosition() < previous.getZPosition())
        {
            this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos - 46, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/vertical_connector.png"), nodeConnectorControllerVertical));
        }
    }

    /**
     * Adds a research button for a given research
     *
     * @param research The research to add a button for
     */
    private void addResearchButton(Research research)
    {
        // Compute the button's X and Y position
        int xPos = BACKGROUND_WIDTH / 2 - 16 + DISTANCE_BETWEEN_RESEARCHES * research.getXPosition();
        int yPos = BACKGROUND_HEIGHT - 50 - DISTANCE_BETWEEN_RESEARCHES * research.getZPosition();

        // Create the research button
        AOTDGuiResearchNodeButton researchNode = new AOTDGuiResearchNodeButton(xPos, yPos, research);
        // Add our pre-build listeners to this node
        researchNode.addMouseListener(this.researchNodeMouseListener);
        researchNode.addMouseMoveListener(this.researchNodeMouseMoveListener);
        // Add the node to our tree
        this.researchTree.add(researchNode);
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
        this.scrollBackground.setU(this.guiOffsetX + (this.scrollBackground.getMaxTextureWidth() - this.scrollBackground.getWidth()) / 2);
        this.scrollBackground.setV(this.guiOffsetY + (this.scrollBackground.getMaxTextureHeight() - this.scrollBackground.getHeight()));
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    @Override
    protected void checkOutOfBounds()
    {
        int backgroundWiggleRoom = (this.scrollBackground.getMaxTextureWidth() - this.scrollBackground.getWidth()) / 2;
        this.guiOffsetX = MathHelper.clamp(this.guiOffsetX, -backgroundWiggleRoom, backgroundWiggleRoom);
        this.guiOffsetY = MathHelper.clamp(this.guiOffsetY, -this.scrollBackground.getMaxTextureHeight() + this.scrollBackground.getHeight(), 0);
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
