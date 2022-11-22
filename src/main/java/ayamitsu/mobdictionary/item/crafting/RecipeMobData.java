package ayamitsu.mobdictionary.item.crafting;

import net.minecraft.item.crafting.*;
import net.minecraft.item.*;
import ayamitsu.mobdictionary.*;
import net.minecraft.inventory.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;

public class RecipeMobData implements IRecipe
{
    private final ItemStack recipeOutput;
    
    public RecipeMobData() {
        this.recipeOutput = new ItemStack(MobDictionary.mobData);
    }
    
    public boolean matches(final InventoryCrafting p_77569_1_, final World worldIn) {
        int dataCount = 0;
        int paperCount = 0;
        ItemStack data = null;
        for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i) {
            final ItemStack item = p_77569_1_.getStackInSlot(i);
            if (item != null) {
                if (item.getItem() == MobDictionary.mobData) {
                    ++dataCount;
                    data = item;
                }
                else if (item.getItem() == Items.paper) {
                    ++paperCount;
                }
            }
        }
        if (dataCount == 1 && paperCount == 1 && data != null) {
            this.recipeOutput.setTagCompound((NBTTagCompound)data.getTagCompound().copy());
            return true;
        }
        return false;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting p_77572_1_) {
        return this.recipeOutput.copy();
    }
    
    public int getRecipeSize() {
        return 2;
    }
    
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
}
