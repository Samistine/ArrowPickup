package com.samistine.arrowpickup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Sycholic
 * @author Samistine
 *
 */
public final class ArrowPickup extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onProjectileHitEvent(ProjectileHitEvent e) {
        if (!e.getEntity().getClass().getName().endsWith("CraftArrow")) {
            return;
        }
        try {
            //instead of using null I used (new Class<?>[0]) and (new Object[0])
            //More on that http://stackoverflow.com/questions/5586862/weird-behaviour-of-getmethod-in-java-reflection-api-can-someone-explain-this
            Method method = e.getEntity().getClass().getMethod("getHandle", new Class<?>[0]); //use reflection to do equivelent of (((CraftArrow) e.getEntity()).***getHandle()***.fromPlayer = 1;)
            try {
                Object instance = method.invoke(e.getEntity(), new Object[0]); //pass in the instance
                //of the entity's class and null since it takes no params
                Field field = instance.getClass().getField("fromPlayer"); //get fromPlayer object
                //this happens to be an integer
                int value = 1; //a value of 1 allows us to pick it up
                field.set(instance, value); //finally set "fromPlayer" to "value", which is 1
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException ex) {
                ex.printStackTrace();
            }
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            getLogger().log(Level.SEVERE, "* * * METHOD NOT FOUND * * *");
        }
    }
}
