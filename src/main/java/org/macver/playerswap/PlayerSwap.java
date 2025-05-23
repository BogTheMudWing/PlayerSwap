package org.macver.playerswap;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public final class PlayerSwap extends JavaPlugin {

    private static PlayerSwap plugin;

    private static boolean started = false;
    private static final int ticksBetweenSwaps = 30 * 20;
    private static int ticksUntilSwap = ticksBetweenSwaps;
    private static Player activePlayer = null;

    public static final Map<String, String> minecraftToName = new HashMap<>();
    public static final Map<String, Color> minecraftToColor = new HashMap<>();
    
    public static void startGame() {
        Player bog = Bukkit.getPlayer("BogTheMudWing");
        if (bog != null && bog.isOnline()) activePlayer = bog;
        started = true;
    }

    private static void newActivePlayer() {

        plugin.getLogger().info("Rolling new player");

        List<Player> players = ImmutableList.copyOf(Bukkit.getOnlinePlayers()); // This is the recommended method for getting an iterable snapshot of players

        Player formerActive = activePlayer;

        // Chose a random player to go next
        int i = new Random().nextInt(0, players.size());
        Player newActive = players.get(i);

        if (formerActive != null) {
            // Copy data to next player
            newActive.setGliding(formerActive.isGliding());
            newActive.setVelocity(formerActive.getVelocity());
            newActive.setExperienceLevelAndProgress(formerActive.getTotalExperience());
            newActive.setHealth(formerActive.getHealth());
            newActive.setRespawnLocation(formerActive.getRespawnLocation());
            newActive.setActiveItemRemainingTime(formerActive.getActiveItemRemainingTime());
            newActive.setShieldBlockingDelay(formerActive.getShieldBlockingDelay());
            newActive.setSaturation(formerActive.getSaturation());
            newActive.setRemainingAir(formerActive.getRemainingAir());
            newActive.setPortalCooldown(formerActive.getPortalCooldown());
            newActive.setNoDamageTicks(formerActive.getNoDamageTicks());
            newActive.setNoActionTicks(formerActive.getNoActionTicks());
            newActive.setFreezeTicks(formerActive.getFreezeTicks());
            newActive.setFoodLevel(formerActive.getFoodLevel());
            newActive.setFireTicks(formerActive.getFireTicks());
            newActive.setFallDistance(formerActive.getFallDistance());
            newActive.setExhaustion(formerActive.getExhaustion());
            Collection<PotionEffect> activePotionEffects = formerActive.getActivePotionEffects();
            for (PotionEffect activePotionEffect : activePotionEffects) {
                newActive.addPotionEffect(activePotionEffect);
            }
        }

        activePlayer = newActive;

        // Send message
        String name = newActive.getName();
        String customName = minecraftToName.get(activePlayer.getName());
        if (customName != null) name = customName;
        TextColor color = TextColor.color(minecraftToColor.get(activePlayer.getName()).asRGB());

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(name).color(color).append(Component.text(" is now in control.").color(TextColor.color(0xffffff))));
        }

        Bukkit.getLogger().info("Done");

    }

    public static boolean isStarted() {
        return started;
    }

    public static void stopGame() {
        started = false;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        minecraftToName.put("BogTheMudWing", "Bog");
        minecraftToName.put("tetchytick", "tetchy");
        minecraftToName.put("iHeron_", "Heron");
        minecraftToName.put("Jauari", "Jauari");
        minecraftToName.put("ratttatoullie", "Goober");
        minecraftToName.put("_SilverRain", "Silver");
        minecraftToName.put("SynchroSky", "Sky");
        minecraftToName.put("Just_Blizzard", "Blizzard");
        minecraftToName.put("Piccolo", "Piccolo");
        minecraftToName.put("Laniebug_", "Lanie");
        minecraftToName.put("fffgsdfd", "Seaviper");
        minecraftToName.put("Over4247", "Shiny");
        minecraftToName.put("Just_Meadows", "Viper");
        minecraftToName.put("Godzilla1005", "Zilla");
        minecraftToName.put("PhenTheFireGirl", "Phen");
        minecraftToName.put("PeekaPlay", "Raina");
        minecraftToName.put("fog_deity", "Fog");
        minecraftToName.put("SunsetSequoia", "Sunset");
        minecraftToName.put("SerpentSages", "Sunset");

        minecraftToColor.put("BogTheMudWing", Color.fromRGB(0x269FE4));
        minecraftToColor.put("tetchytick", Color.fromRGB(0xeca138));
        minecraftToColor.put("iHeron_", Color.fromRGB(0x386e6e));
        minecraftToColor.put("Jauari", Color.fromRGB(0xffe60f));
        minecraftToColor.put("ratttatoullie", Color.fromRGB(0x05ff2b));
        minecraftToColor.put("_SilverRain", Color.fromRGB(0xA0FFCA));
        minecraftToColor.put("SynchroSky", Color.fromRGB(0xffe194));
        minecraftToColor.put("Just_Blizzard", Color.WHITE);
        minecraftToColor.put("Piccolo", Color.fromRGB(0x38FF11));
        minecraftToColor.put("Laniebug_", Color.fromRGB(0x6932c4));
        minecraftToColor.put("fffgsdfd", Color.fromRGB(0x8f1ba6));
        minecraftToColor.put("Over4247", Color.fromRGB(0xc096ff));
        minecraftToColor.put("Just_Meadows", Color.fromRGB(0x17422f));
        minecraftToColor.put("Godzilla1005", Color.fromRGB(0xC0FFE3));
        minecraftToColor.put("PhenTheFireGirl", Color.fromRGB(0x40ffd2));
        minecraftToColor.put("PeekaPlay", Color.fromRGB(0x9b8aff));
        minecraftToColor.put("fog_deity", Color.fromRGB(0x42505C));
        minecraftToColor.put("SunsetSequoia", Color.WHITE);
        minecraftToColor.put("SerpentSages", Color.WHITE);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            if (!started) return;

            ticksUntilSwap--;
            if (activePlayer == null || !activePlayer.isOnline() || ticksUntilSwap <= 0) {
                ticksUntilSwap = ticksBetweenSwaps;
                newActivePlayer();
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!Objects.equals(player.getUniqueId(), activePlayer.getUniqueId())) {
                    player.setGameMode(GameMode.SPECTATOR);
                    if (ticksUntilSwap < ticksBetweenSwaps - 5 && (ticksUntilSwap % 5 == 0)) player.setSpectatorTarget(activePlayer); // doing this instantly breaks it fsr
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }

        }, 1, 1);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
