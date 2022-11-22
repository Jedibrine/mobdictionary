package ayamitsu.mobdictionary.client.gui;

import net.minecraft.util.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;

public class GuiButtonMobDictionary extends GuiButton
{
    protected static final ResourceLocation buttonTexture;
    
    public GuiButtonMobDictionary(final int buttonId, final int x, final int y, final String buttonText) {
        super(buttonId, x, y, buttonText);
        this.width = 9;
        this.height = 9;
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiButtonMobDictionary.buttonTexture);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.field_146123_n = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(770, 771);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0 + k * 9, 0, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int l = 14737632;
            if (this.packedFGColour != 0) {
                l = this.packedFGColour;
            }
            else if (!this.enabled) {
                l = 10526880;
            }
            else if (this.field_146123_n) {
                l = 16777120;
            }
        }
    }
    
    static {
        buttonTexture = new ResourceLocation("mobdictionary:textures/gui/button.png");
    }
}
