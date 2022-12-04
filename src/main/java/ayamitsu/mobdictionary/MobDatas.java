package ayamitsu.mobdictionary;

import ayamitsu.mobdictionary.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.*;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.*;

public final class MobDatas
{
    private static Set<String> nameList;
    private static Map<UUID, Set<String>> nameListMap;
    private static int allMobValue;
    
    public static void addInfo(final Object obj) {
        String name = null;
        if (obj instanceof Class) {
            name = EntityUtils.getNameFromClass((Class)obj);
        }
        else if (obj instanceof String) {
            name = (String)obj;
        }
        if (name == null) {
            return;
        }
        addInfo(name);
    }
    
    public static void addInfoOnDedicatedServer(final Object obj, final EntityPlayerMP player) {
        String name = null;
        if (obj instanceof Class) {
            name = EntityUtils.getNameFromClass((Class)obj);
        }
        else if (obj instanceof String) {
            name = (String)obj;
        }
        if (name == null) {
            return;
        }
        if (MobDictionary.proxy.isDedicatedServer()) {
            addInfoOnDedicatedServer(name, player);
        }
    }
    
    private static Boolean addInfo(final Class clazz) {
        return !MobDatas.nameList.add(EntityUtils.getNameFromClass(clazz));
    }
    
    private static Boolean addInfo(final String name) {
        return !MobDatas.nameList.add(name);
    }
    
    private static Boolean addInfoOnDedicatedServer(final String name, final EntityPlayerMP player) {
        final UUID uuid = player.getUniqueID();
        if (!MobDatas.nameListMap.containsKey(uuid)) {
            MobDatas.nameListMap.put(uuid, new HashSet<String>());
        }
        return !MobDatas.nameListMap.get(uuid).add(name);
    }
    
    public static boolean contains(final Class clazz) {
        return contains(EntityUtils.getNameFromClass(clazz));
    }
    
    public static boolean contains(final String name) {
        return MobDatas.nameList.contains(name);
    }
    
    public static boolean containsOnDedicatedServer(final String name, final EntityPlayerMP player) {
        final UUID uuid = player.getUniqueID();
        if (!MobDatas.nameListMap.containsKey(uuid)) {
            MobDatas.nameListMap.put(uuid, new HashSet<String>());
            return false;
        }
        return MobDatas.nameListMap.get(uuid).contains(name);
    }
    
    public static void clearNameList() {
        MobDatas.nameList.clear();
    }
    
    public static void initAllMobValue() {
        MobDatas.allMobValue = EntityUtils.getAllMobValue();
    }
    
    public static int getAllMobValue() {
        return MobDatas.allMobValue;
    }
    
    public static int getRegisteredValue() {
        return MobDatas.nameList.size();
    }
    
    public static int getRegisteredValueOnDedicatedServer(final EntityPlayerMP player) {
        final UUID uuid = player.getUniqueID();
        if (!MobDatas.nameListMap.containsKey(uuid)) {
            return 0;
        }
        return MobDatas.nameListMap.get(uuid).size();
    }
    
    public static String[] toArray() {
        return MobDatas.nameList.toArray(new String[0]);
    }
    
    public static String[] toArrayOnDedicatedServer(final EntityPlayerMP player) {
        final UUID uuid = player.getUniqueID();
        if (!MobDatas.nameListMap.containsKey(uuid)) {
            return new String[0];
        }
        final Set<String> set = MobDatas.nameListMap.get(uuid);
        return set.toArray(new String[0]);
    }
    
    public static void load() throws IOException {
        MobDatas.nameList.clear();
        final File dir = DimensionManager.getCurrentSaveRootDirectory();
        final File file = new File(dir, "/mobdic.md").getAbsoluteFile();
        if (!file.exists()) {
            return;
        }
        if (!file.canRead()) {
            throw new IOException("Can not read dictionary data:" + file.getPath());
        }
        final BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            addInfo(line.trim());
        }
        br.close();
    }
    
    public static void loadOnDedicatedServer(final EntityPlayerMP player) throws IOException {
        MobDatas.nameList.clear();
        final File dir = MobDictionary.proxy.getSaveDirectory();
        final File file = new File(dir, "/" + player.getUniqueID().toString()).getAbsoluteFile();
        if (!file.exists()) {
            return;
        }
        if (!file.canRead()) {
            throw new IOException("Can not read dictionary data:" + file.getPath());
        }
        final BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            addInfoOnDedicatedServer(line.trim(), player);
        }
        br.close();
    }
    
    public static void save() throws IOException {
        final File dir = DimensionManager.getCurrentSaveRootDirectory();
        final File file = new File(dir,"/mobdic.md").getAbsoluteFile();
        if (dir==null) {
        	throw new IOException("Dir does not exist, cannot get current save root directory");
        };
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }
        if (!file.canWrite()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }
        final PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        for (final String name : MobDatas.nameList) {
            pw.println(name);
        }
        pw.close();
    }
    
    public static void saveOnDedicatedServer(final EntityPlayerMP player) throws IOException {
        final File dir = MobDictionary.proxy.getSaveDirectory();
        final File file = new File(dir, "/" + player.getUniqueID().toString()).getAbsoluteFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }
        if (!file.canWrite()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }
        final PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        final UUID uuid = player.getUniqueID();
        if (MobDatas.nameListMap.containsKey(uuid)) {
            for (final String name : MobDatas.nameListMap.get(uuid)) {
                pw.println(name);
            }
        }
        pw.close();
    }
    
    static {
        MobDatas.nameList = new HashSet<String>();
        MobDatas.nameListMap = new HashMap<UUID, Set<String>>();
    }
}
