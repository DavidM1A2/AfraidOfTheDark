package com.DavidM1A2.afraidofthedark.client.gui;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedList;

/**
 * Class used to show what researches were unlocked. This code is copied from the achievement UI from MC 1.8.9
 */
public class ResearchAchievedOverlay extends Gui
{
    // The texture of the achievement background
    private static final ResourceLocation ACHIEVEMENT_BACKGROUND = new ResourceLocation(Constants.MOD_ID, "textures/gui/research_achieved.png");
    // The title of the research achieved window
    private static final String ACHIEVEMENT_TITLE = "New Research!";
    // MC reference
    private final Minecraft mc;
    // The width of the overlay
    private int width;
    // The height of hte overlay
    private int height;
    // The description of the research
    private String researchDescription;
    // The time of the last research notification
    private long notificationTime;
    // A queue of researches to display
    private LinkedList<Research> toDisplay = new LinkedList<>();

    /**
     * Constructor just calls super and initializes the MC reference
     */
    public ResearchAchievedOverlay()
    {
        super();
        this.mc = Minecraft.getMinecraft();
    }

    /**
     * Displays a given research in the overlay
     *
     * @param research The research to display
     */
    public void displayResearch(Research research)
    {
        toDisplay.push(research);
    }

    /**
     * Function provided by MC's achievement window to setup the viewport, copied and unmodified
     */
    private void updateResearchAchievedWindowScale()
    {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, this.width, this.height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    /**
     * Function provided by MC's achievement window to setup the window, copied and slightly modified to work with a queue of researches to show
     */
    public void updateResearchAchievedWindow()
    {
        // If there is no notification showing and the queue has another research to display display the next research
        if (this.notificationTime == 0 && !this.toDisplay.isEmpty())
        {
            // The research to display
            Research research = toDisplay.pop();
            // The new research description
            this.researchDescription = I18n.format(research.getUnLocalizedName());
            // Update the notification time to be the current system time
            this.notificationTime = Minecraft.getSystemTime();
        }

        // After this everything is copied from the default MC achievement class
        if (this.notificationTime != 0 && Minecraft.getMinecraft().player != null)
        {
            double d0 = (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

            if (d0 < 0.0D || d0 > 1.0D)
            {
                this.notificationTime = 0L;
                return;
            }

            this.updateResearchAchievedWindowScale();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            double d1 = d0 * 2.0D;

            if (d1 > 1.0D)
            {
                d1 = 2.0D - d1;
            }

            d1 *= 4.0D;
            d1 = 1.0D - d1;

            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1 * d1 * d1;
            final int i = this.width - 160;
            final int j = 0 - (int) (d1 * 36.0D);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
            GlStateManager.disableLighting();
            this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

            this.mc.fontRenderer.drawString(this.ACHIEVEMENT_TITLE, i + 10, j + 5, -256);
            this.mc.fontRenderer.drawString(this.researchDescription, i + 10, j + 18, -1);

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            GlStateManager.disableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
        }
    }

    /**
     * Clears any researches in the queue to be displayed
     */
    public void clearResearches()
    {
        this.toDisplay.clear();
        this.notificationTime = 0L;
    }
}
