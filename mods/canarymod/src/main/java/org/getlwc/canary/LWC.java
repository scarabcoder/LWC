package org.getlwc.canary;

import net.canarymod.Canary;
import net.canarymod.api.inventory.Enchantment;
import net.canarymod.api.inventory.Item;
import net.canarymod.plugin.Plugin;
import org.getlwc.Engine;
import org.getlwc.ItemStack;
import org.getlwc.Location;
import org.getlwc.ServerLayer;
import org.getlwc.SimpleEngine;
import org.getlwc.canary.listeners.CanaryListener;
import org.getlwc.canary.permission.CanaryPermission;

import java.util.HashMap;
import java.util.Map;

public class LWC extends Plugin {

    /**
     * Internal engine handle
     */
    private SimpleEngine engine;

    /**
     * The Canary server layer
     */
    private final ServerLayer layer = new CanaryServerLayer(this);

    @Override
    public boolean enable() {
        engine = (SimpleEngine) SimpleEngine.getOrCreateEngine(layer, new CanaryServerInfo(), new CanaryConsoleCommandSender());
        engine.setPermission(new CanaryPermission());
        engine.startup();

        // Hooks
        Canary.hooks().registerListener(new CanaryListener(this), this);

        return true;
    }

    @Override
    public void disable() {
    }

    /**
     * @return the {@link Engine} object
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Wrap a native Canary player
     *
     * @param player
     * @return
     */
    public org.getlwc.entity.Player wrapPlayer(net.canarymod.api.entity.living.humanoid.Player player) {
        return layer.getPlayer(player.getName());
    }

    /**
     * Get a World object for the native Canary world
     *
     * @param worldName
     * @return
     */
    public org.getlwc.World getWorld(String worldName) {
        return layer.getWorld(worldName);
    }

    /**
     * Cast a location to our native location
     *
     * @param location
     * @return
     */
    public Location castLocation(net.canarymod.api.world.position.Location location) {
        return new Location(getWorld(location.getWorld().getName()), location.getX(), location.getY(), location.getZ());
    }

    /**
     * Cast a map of enchantments to our native enchantment mappings
     *
     * @param enchantments
     * @return
     */
    public Map<Integer, Integer> castEnchantments(Enchantment[] enchantments) {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();

        for (Enchantment enchantment : enchantments) {
            ret.put(enchantment.getType().getId(), (int) enchantment.getLevel());
        }

        return ret;
    }

    /**
     * Cast an item stack to our native ItemStack
     *
     * @param item
     * @return
     */
    public ItemStack castItemStack(Item item) {
        if (item == null) {
            return null;
        }

        return new ItemStack(item.getId(), item.getAmount(), (short) item.getDamage(), item.getMaxAmount(), castEnchantments(item.getEnchantments()));
    }

}