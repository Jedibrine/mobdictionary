package ayamitsu.mobdictionary.util;

import net.minecraft.entity.*;
import java.lang.reflect.*;
import java.util.*;

public class EntityUtils
{
    private static int allEntityValue;
    private static int allMobValue;
    
    public static boolean isLivingClass(final Class clazz) {
        return clazz != null && EntityLiving.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers());
    }
    
    public static boolean isLivingName(final String name) {
        final Class clazz = getClassFromName(name);
        return isLivingClass(clazz);
    }
    
    public static boolean containsClass(final Class clazz) {
        return getClassToStringMapping().containsKey(clazz);
    }
    
    public static boolean containsName(final String name) {
        return getStringToClassMapping().containsKey(name);
    }
    
    public static Class getClassFromName(final String name) {
        return (Class) getStringToClassMapping().get(name);
    }
    
    public static String getNameFromClass(final Class clazz) {
        return (String) getClassToStringMapping().get(clazz);
    }
    
    public static Map getStringToClassMapping() {
        try {
            final Field field = EntityList.class.getDeclaredFields()[1];
            field.setAccessible(true);
            return (Map)field.get(null);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static Map getClassToStringMapping() {
        try {
            final Field field = EntityList.class.getDeclaredFields()[2];
            field.setAccessible(true);
            return (Map)field.get(null);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static int getAllEntityValue() {
        return EntityUtils.allEntityValue;
    }
    
    public static int getAllMobValue() {
        return EntityUtils.allMobValue;
    }
    
    public static void resetAllEntityValue() {
        EntityUtils.allEntityValue = getStringToClassMapping().size();
    }
    
    public static void resetAllMobValue() {
        final Set set = new HashSet();
        final Map classToStringMapping = getClassToStringMapping();
        for (final Object obj : classToStringMapping.keySet()) {
            if (obj instanceof Class && isLivingClass((Class)obj)) {
                set.add(obj);
            }
        }
        EntityUtils.allMobValue = set.size();
    }
    
    static {
        resetAllEntityValue();
        resetAllMobValue();
    }
}
