package ayamitsu.mobdictionary.client.gui;

import ayamitsu.mobdictionary.util.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.input.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.init.*;
import ayamitsu.mobdictionary.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import ayamitsu.mobdictionary.item.*;
import net.minecraft.entity.player.*;

public class GuiMobDictionary extends GuiScreen
{
    private static final ResourceLocation dictionaryResource;
    protected int xSize;
    protected int ySize;
    protected int stringColor;
    protected int stringHeight;
    protected int stringYMargin;
    protected int scrollAmount;
    protected int sizeNameAreaX;
    protected int currentNo;
    protected int topEdge;
    protected int bottomEdge;
    protected int namesCenterOffsetX;
    protected NamePair[] nameList;
    protected Entity displayEntity;
    protected float entityScale;
    protected double yaw;
    protected double yaw2;
    private static final List<String> tooltipStringList;
    
    public GuiMobDictionary() {
        this.xSize = 176;
        this.ySize = 166;
        this.stringColor = 3158064;
        this.stringYMargin = 6;
        this.scrollAmount = 0;
        this.sizeNameAreaX = 60;
        this.currentNo = 0;
        this.topEdge = 20;
        this.bottomEdge = 11;
        this.namesCenterOffsetX = 126;
        this.entityScale = 1.0f;
        this.yaw = 0.0;
        this.yaw2 = 0.0;
    }
    
    public void initGui() {
        this.stringHeight = this.fontRendererObj.FONT_HEIGHT;
        final String[] names = MobDatas.toArray();
        this.nameList = new NamePair[names.length];
        for (int i = 0; i < names.length; ++i) {
            if (EntityUtils.containsName(names[i])) {
                this.nameList[i] = new NamePair(names[i], StatCollector.translateToLocal("entity." + names[i] + ".name"));
            }
            else {
                this.nameList[i] = new NamePair(names[i], names[i]);
            }
        }
        Arrays.sort(this.nameList);
        final int originX = this.width - this.xSize >> 1;
        final int originY = this.height - this.ySize >> 1;
        final GuiButton button = (GuiButton)new GuiButtonMobDictionary(0, originX + 19, originY + 136, "");
        button.enabled = (this.nameList.length > 0);
        this.buttonList.add(button);
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.yaw2 = this.yaw;
        this.drawGuiBackgroundLayer(mouseX, mouseY, partialTicks);
        if (this.nameList.length > 0) {
            this.drawMobModel(mouseX, mouseY, partialTicks, this.nameList[this.currentNo].unlocalized);
        }
        this.drawMobInfo(mouseX, mouseY, partialTicks);
        this.drawMobNames(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawTooltip(mouseX, mouseY, partialTicks);
        //this.yaw = 2.0 + this.yaw2 + (this.yaw2 - this.yaw) * partialTicks;
        this.yaw = 20.0;
    }
    
    public void drawGuiBackgroundLayer(final int mouseX, final int mouseY, final float partialTicks) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiMobDictionary.dictionaryResource);
        final int k = (this.width - this.xSize) / 2;
        final int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
    
    public void drawMobInfo(final int mouseX, final int mouseY, final float partialTicks) {
        final int originX = this.width - this.xSize >> 1;
        final int originY = this.height - this.ySize >> 1;
        if (this.nameList.length > 0) {
            final NamePair pair = this.nameList[this.currentNo];
            this.fontRendererObj.drawString("Name:", originX + 19, originY + 85, this.stringColor);
            this.fontRendererObj.drawString(pair.localized, originX + 19, originY + 95, this.stringColor);
            if (this.displayEntity instanceof EntityLivingBase) {
                final EntityLivingBase living = (EntityLivingBase)this.displayEntity;
                this.fontRendererObj.drawString("Health:", originX + 19, originY + 106, this.stringColor);
                this.fontRendererObj.drawString(String.format("%.1f", living.getMaxHealth()), originX + 19, originY + 116, this.stringColor);
            }
        }
    }
    
    public void drawMobNames(final int mouseX, final int mouseY, final float partialTicks) {
        final int originX = (this.width - this.xSize) / 2;
        final int originY = (this.height - this.ySize) / 2;
        final String str = MobDatas.getRegisteredValue() + "/" + MobDatas.getAllMobValue();
        this.fontRendererObj.drawString(str, originX + this.namesCenterOffsetX - this.fontRendererObj.getStringWidth(str) / 2, originY + 8, this.stringColor);
        if (this.nameList.length > 0) {
            for (int i = 0; i < this.nameList.length; ++i) {
                if (this.topEdge + (i + 1) * (this.stringHeight + this.stringYMargin) > this.ySize - this.bottomEdge) {
                    break;
                }
                final int var1 = i + this.scrollAmount;
                final String translatedName = this.nameList[var1].localized;
                final int stringWidth = this.fontRendererObj.getStringWidth(translatedName);
                final int color = (var1 == this.currentNo || this.isMouseInArea(originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2, originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2, originY + this.topEdge + i * (this.stringHeight + this.stringYMargin), originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin) + this.stringHeight))) ? 16777215 : this.stringColor;
                this.fontRendererObj.drawString(translatedName, originX + this.namesCenterOffsetX - stringWidth / 2, originY + this.topEdge + i * (this.stringHeight + this.stringYMargin), color);
            }
        }
    }
    
    protected void drawMobModel(final int mouseX, final int mouseY, final float partialTicks, final String unlocalizedName) {
        final int originX = (this.width - this.xSize) / 2;
        final int originY = (this.height - this.ySize) / 2;
        if (unlocalizedName == null) {
            this.displayEntity = null;
        }
        else if (this.displayEntity == null || !EntityList.getEntityString(this.displayEntity).equals(unlocalizedName)) {
            this.displayEntity = EntityList.createEntityByName(unlocalizedName, (World)this.mc.theWorld);
        }
        if (this.displayEntity == null) {
            final String str = "null";
            this.fontRendererObj.drawString(str, originX + 58 - this.fontRendererObj.getStringWidth(str), originY + 44, this.stringColor);
        }
        else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(originX + 49), (float)(originY + 70), 50.0f);
            final float scale = 25.0f;
            GL11.glScalef(-scale, scale, scale);
            GL11.glScalef(this.entityScale, this.entityScale, this.entityScale);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            final float xRot = (float)(this.yaw2 + (this.yaw - this.yaw2) * partialTicks * 10.0);
            GL11.glRotatef(xRot, 0.0f, 1.0f, 0.0f);
            final RenderManager manager = RenderManager.instance;
            manager.playerViewY = 180.0f;
            manager.renderEntityWithPosYaw(this.displayEntity, 0.0, 0.0, 0.0, 0.0f, 0.0f);
            GL11.glPopMatrix();
            GL11.glDisable(32826);
        }
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    protected boolean isMouseInArea(final int x1, final int x2, final int y1, final int y2) {
        final int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight;
        final boolean flag = x1 <= mouseX && mouseX < x2 && y1 <= mouseY && mouseY < y2;
        return flag;
    }
    
    protected void drawTooltip(final int mouseX, final int mouseY, final float partialTicks) {
        if (isMouseOverButton((GuiButton) this.buttonList.get(0))) {
            final List<String> list = new ArrayList<String>();
            for (final Object obj : GuiMobDictionary.tooltipStringList) {
                list.add(StatCollector.translateToLocal((String)obj));
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, list.get(i));
                }
                else {
                    list.set(i, EnumChatFormatting.GRAY + list.get(i));
                }
            }
            this.drawHoveringText((List)list, mouseX, mouseY, this.fontRendererObj);
        }
    }
    
    private static boolean isMouseOverButton(final GuiButton button) {
        return button.func_146115_a();
    }
    
    public void updateScreen() {
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0 && this.nameList.length > 0) {
            final EntityPlayer player = (EntityPlayer)this.mc.thePlayer;
            final InventoryPlayer inventory = player.inventory;
            if (inventory.hasItem(Items.paper) && inventory.getFirstEmptyStack() > 0) {
                final ItemStack itemStack = new ItemStack(MobDictionary.mobData);
                final NBTTagCompound nbt = new NBTTagCompound();
                ItemMobData.setEntityNameToNBT(this.nameList[this.currentNo].unlocalized, nbt);
                itemStack.setTagCompound(nbt);
                inventory.consumeInventoryItem(Items.paper);
                inventory.addItemStackToInventory(itemStack);
            }
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final int originX = (this.width - this.xSize) / 2;
        final int originY = (this.height - this.ySize) / 2;
        for (int i = 0; i < this.nameList.length; ++i) {
            if (this.topEdge + (i + 1) * (this.stringHeight + this.stringYMargin) > this.ySize - this.bottomEdge) {
                break;
            }
            final int var1 = i + this.scrollAmount;
            if (this.isMouseInArea(originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2, originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2, originY + this.topEdge + i * (this.stringHeight + this.stringYMargin), originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin) + this.stringHeight))) {
                if (this.currentNo != var1) {
                    this.entityScale = 1.0f;
                }
                this.currentNo = var1;
                break;
            }
        }
    }
    
    public void handleMouseInput() {
        super.handleMouseInput();
        this.mouseWheeled();
    }
    
    protected void mouseWheeled() {
        final int originX = (this.width - this.xSize) / 2;
        final int originY = (this.height - this.ySize) / 2;
        final int wheel = Mouse.getDWheel();
        if (this.nameList.length > 0 && this.nameList.length > (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin) && this.isMouseInArea(originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2, originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2, originY + this.topEdge, originY + this.ySize - this.bottomEdge)) {
            if (wheel < 0) {
                ++this.scrollAmount;
                if (this.scrollAmount > this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin)) {
                    this.scrollAmount = this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin);
                }
            }
            else if (wheel > 0) {
                --this.scrollAmount;
                if (this.scrollAmount < 0) {
                    this.scrollAmount = 0;
                }
            }
        }
        if (this.nameList.length > 0 && this.isMouseInArea(originX, originX + 98, originY, originY + 80)) {
            if (wheel < 0) {
                this.entityScale /= 1.2f;
            }
            else if (wheel > 0) {
                this.entityScale *= 1.2f;
            }
        }
    }
    
    static {
        dictionaryResource = new ResourceLocation("mobdictionary:textures/gui/dictionary.png");
        tooltipStringList = Arrays.asList("mobdictionary.common.output_piece", "mobdictionary.common.need_a_paper");
    }
    
    static class NamePair implements Comparable
    {
        public String unlocalized;
        public String localized;
        
        public NamePair(final String unlocalized, final String localized) {
            this.unlocalized = unlocalized;
            this.localized = localized;
        }
        
        @Override
        public int compareTo(final Object o) {
            final NamePair other = (NamePair)o;
            return this.localized.compareTo(other.localized);
        }
    }
}
