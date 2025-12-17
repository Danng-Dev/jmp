package top.archiem.jmp.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class LuckPermsHook {
    private LuckPerms luckPerms = null;

    public void initializeHook(){
        this.luckPerms = LuckPermsProvider.get();
    }

    public void removeHook(){this.luckPerms = null;}

    public boolean isHookActive() { return luckPerms != null;}

    public LuckPerms getHook(){return this.luckPerms;}
}
