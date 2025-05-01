package stranik.dev.smallifyMob;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.TreeType;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class SmallifyEventHandler implements Listener {
    @EventHandler
    public void onSilence(PlayerInteractAtEntityEvent event) {
        if (!event.getPlayer().hasPermission("smallify.useNametag")) return;
        Entity entity = event.getRightClicked();
        
        if (!SmallifyMob.getInstance().getConfig().getBoolean("enable")) return;
        
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SmallifyMob.getInstance(), () -> {
                if (!(entity instanceof Ageable ageable)) return;
                if (!(entity instanceof Breedable breedable)) return;
                
                if (entity.customName() == null) return;
                
                if (!(entity.customName() instanceof TextComponent textComponent)) return;

                if (!textComponent
                    .content()
                    .trim()
                    .equalsIgnoreCase(SmallifyMob.getInstance().getConfig().getString("activation-name"))) {
                    
                    if (breedable.getAgeLock() && SmallifyMob.getInstance().getConfig().getBoolean("enable-grow-after-rename"))
                        breedable.setAgeLock(false);

                    return;
                }

                if (ageable.isAdult()) return;

                breedable.setAgeLock(true);
            },  10L);
        } catch (NoClassDefFoundError ignored) {
            SmallifyMob.getInstance().getLogger().info(ignored.getMessage());
        }
    }
}