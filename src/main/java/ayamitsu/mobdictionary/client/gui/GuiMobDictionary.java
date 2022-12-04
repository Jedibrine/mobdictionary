package ayamitsu.mobdictionary.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import ayamitsu.mobdictionary.MobDatas;
import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.item.ItemMobData;
import ayamitsu.mobdictionary.util.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

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
    protected double pitch;
    private static final List<String> tooltipStringList;
    
    public GuiMobDictionary() {
        this.xSize = 176;
        this.ySize = 166;
        this.stringColor = 3158064;
        this.stringYMargin = 3;
        this.scrollAmount = 0;
        this.sizeNameAreaX = 60;
        this.currentNo = 0;
        this.topEdge = 20;
        this.bottomEdge = 11;
        this.namesCenterOffsetX = 126;
        this.entityScale = 1.0f;
        this.yaw = 0.0;
        this.yaw2 = 0.0;
        this.pitch = 0.0;
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
        final GuiButton button = (GuiButton)new GuiButtonMobDictionary(0, originX + 18, originY + 136, "");
        button.enabled = (this.nameList.length > 0);
        this.buttonList.add(button);
    }
    
    public boolean doesGuiPauseGame() {
    	return true;
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
        this.yaw = 2.0;
        this.pitch = 0.0;
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
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int sheight = sr.getScaledHeight();
        int swidth = sr.getScaledWidth();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        if (this.nameList.length > 0) {
            final NamePair pair = this.nameList[this.currentNo];
            this.fontRendererObj.drawString("Name:", swidth-148, sheight, this.stringColor);
            this.fontRendererObj.drawString(pair.localized, swidth-148, sheight+8, this.stringColor);
            if (this.displayEntity instanceof EntityLivingBase) {
                final EntityLivingBase living = (EntityLivingBase)this.displayEntity;
                this.fontRendererObj.drawString("Health:", swidth-148, sheight+20, this.stringColor);
                this.fontRendererObj.drawString(String.format("%.1f", living.getMaxHealth()), swidth-148, sheight+28, this.stringColor);
                //any future attributes to show added here
            }
        }
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public void drawMobNames(final int mouseX, final int mouseY, final float partialTicks) {
        final int originX = (this.width - this.xSize) / 2;
        final int originY = (this.height - this.ySize) / 2;
        String str = MobDatas.getRegisteredValue() + "/" + MobDatas.getAllMobValue();
        int blacklistlength = 1;
        char[] blacklist = MobDictionary.blacklistedEntities.toCharArray();
        char comma = ',';
        for (int i=0; i<blacklist.length; i++) {
        	if (blacklist[i]==comma) {
        		blacklistlength++;
        	}
        }
        if (MobDictionary.maxNumber==0) {
        	str = MobDatas.getRegisteredValue() + "/" + MobDatas.getAllMobValue();
        }
        else if (MobDictionary.maxNumber==-1) {
        	int outof =  MobDatas.getAllMobValue()-blacklistlength;
        	str = MobDatas.getRegisteredValue() + "/" + outof;
        }
        else if (MobDictionary.maxNumber%1==0){
            str = MobDatas.getRegisteredValue() + "/" + MobDictionary.maxNumber;
        }
        this.fontRendererObj.drawString(str, originX + this.namesCenterOffsetX - this.fontRendererObj.getStringWidth(str) / 2, originY + 8, this.stringColor);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int sheight = sr.getScaledHeight();
        int swidth = sr.getScaledWidth();
        if (this.nameList.length > 0) {
            for (int i = 0; i < this.nameList.length; ++i) {
            	if (this.nameList.length>21 && this.scrollAmount>(this.nameList.length-21)) {
            		this.scrollAmount=this.nameList.length-21;
            	}
                if (this.topEdge + (i + 1) * (this.stringHeight + this.stringYMargin) > this.ySize + 120 - this.bottomEdge) {
                	break;
                }
                int var2 = i + this.scrollAmount;
                if (var2>this.nameList.length) {
                	break;
                }
                if (i>21) {
	                while (var2>=this.nameList.length) {
	                	var2=this.nameList.length-1;
	                	this.scrollAmount-=1;
	                	var2 = i + this.scrollAmount;
	            	}
                }
                if (var2>=this.nameList.length) {
                	var2=this.nameList.length-1;
                }
                String translatedName = this.nameList[var2].localized;
                final int stringWidth = this.fontRendererObj.getStringWidth(translatedName);
                final int color = (var2 == this.currentNo || this.isMouseInArea(-6 + originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2, originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2, originY + this.topEdge + i * (this.stringHeight + this.stringYMargin), originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin) + this.stringHeight))) ? 16777215 : this.stringColor;
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                this.fontRendererObj.drawString(translatedName, swidth + 6, sheight - 150 + this.topEdge + i * (this.stringHeight + this.stringYMargin), color);
                GL11.glScalef(2.0f, 2.0f, 2.0f);
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
            float heightdiff = this.displayEntity.height*10;
            while (heightdiff>=25) heightdiff/=2;
            GL11.glTranslatef((float)(originX + 49), (float)(originY + 62+(heightdiff)), 50.0f);
            float scale = 90.0f/(2*heightdiff/10)+5.0f;
            while (scale>=50.0f) scale/=2;
            if (this.displayEntity.height>=2.5) {
            	heightdiff+=50;
            	scale/=2;
            }
            if (this.displayEntity.getCommandSenderName().contains("Ghast")) {
            	scale/=1.5;
            	GL11.glTranslatef(0.0f, -35.0f, 0.0f);
            }
            GL11.glScalef(-scale, scale, scale);
            GL11.glScalef(this.entityScale, this.entityScale, this.entityScale);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            final float xRot = (float)(this.yaw*10.0f);
            final float yRot = (float)(this.pitch*10.0f);
            GL11.glRotatef(xRot, yRot, 1.0f, 0.0f);
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
    	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int sheight = sr.getScaledHeight();
        int swidth = sr.getScaledWidth();
        final int mouseX = swidth * Mouse.getEventX() / this.mc.displayWidth;
        final int mouseY = sheight + 65 + sheight/2 - Mouse.getEventY();
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
    	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int sheight = sr.getScaledHeight();
        int swidth = sr.getScaledWidth();
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final int originX = ((this.width - this.xSize) / 2);
        final int originY = (sheight - this.ySize) / 2;
        for (int i = 0; i < this.nameList.length; ++i) {
        	if (i>=(this.nameList.length)) {
        		break;
        	}
            if (this.topEdge + (i + 1) * (this.stringHeight + this.stringYMargin) > (sheight +33)) {
                break;
            }
            final int var1 = i + this.scrollAmount;
            if (this.isMouseInArea(-6 + originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2, originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2, originY + this.topEdge + i * (this.stringHeight + this.stringYMargin), originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin) + this.stringHeight))) {
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
        if (this.nameList.length > 0 && this.nameList.length > (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin) && this.isMouseInArea(originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2, originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2, originY, originY + 2*this.ySize - 60)) {
	         if (this.nameList.length>21) {
        		if (wheel < 0) {
	                if (this.scrollAmount >= this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin)) {
	                	this.scrollAmount = this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin);
	                }
	                else {
	                	++this.scrollAmount;
	                }
	            }
	            else if (wheel > 0) {
	                --this.scrollAmount;
	                if (this.scrollAmount < 0) {
	                    this.scrollAmount = 0;
	                }
	            }
	         }
        }
        if (this.nameList.length > 0 && this.isMouseInArea(originX, originX + 98, originY, originY + 120)) {
            if (wheel < 0) {
                this.entityScale /= 1.1f;
            }
            else if (wheel > 0) {
                this.entityScale *= 1.1f;
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
