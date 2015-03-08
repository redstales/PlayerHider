package io.github.redinzane.playerhider;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class PlayerHiderConfiguration {
    private final Configuration config;

    private static final String SECTION_COOLDOWNS = "cooldowns";
    private static final String SECTION_DISTANCES = "distances";
    private static final String SECTION_FEATURES = "features";

    private static final String COOLDOWN_KEY = "updatecooldown";
    private static final String DISTANCE_KEY = "sneakdistance";
    private static final String LOS_KEY = "los";
    private static final String COMBATTAG_KEY = "combattag";

    public PlayerHiderConfiguration(Configuration config) {
        this.config = config;
    }

    /**
     * Retrieve the cooldown in milliseconds.
     * 
     * @return Cooldown in milliseconds.
     */
    public int getCooldown() {
        return getSectionOrDefault(SECTION_COOLDOWNS).getInt(COOLDOWN_KEY);
    }

    /**
     * Set the cooldown in milliseconds.
     * 
     * @param value
     *            - new cooldown.
     */
    public void setCooldown(int value) {
        getSectionOrDefault(SECTION_COOLDOWNS).set(COOLDOWN_KEY, value);
    }

    /**
     * Retrieve the distance in blocks.
     * 
     * @return Distance in milliseconds.
     */
    public int getDistance() {
        return getSectionOrDefault(SECTION_DISTANCES).getInt(DISTANCE_KEY);
    }

    /**
     * Set the distance in milliseconds.
     * 
     * @param value
     *            - new distance.
     */
    public void setDistance(int value) {
        getSectionOrDefault(SECTION_DISTANCES).set(DISTANCE_KEY, value);
    }

    private ConfigurationSection getSectionOrDefault(String name) {
        ConfigurationSection section = config.getConfigurationSection(name);

        if (section != null)
            return section;
        else
            return config.createSection(name);
    }

    /**
     * Retrieve the LoS on/off boolean
     * 
     * @return value - on/off boolean
     */
    public boolean getLoS() {
        boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(LOS_KEY);
        return value;
    }

    /**
     * Set the LoS on/off boolean.
     * 
     * @param value
     *            - new boolean
     */
    public void setLoS(boolean value) {
        getSectionOrDefault(SECTION_FEATURES).set(LOS_KEY, value);
    }
    
    /**
     * Retrieve the LoS on/off boolean
     * 
     * @return value - on/off boolean
     */
    public boolean getCombatTag() {
        boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(COMBATTAG_KEY);
        return value;
    }

    /**
     * Set the LoS on/off boolean.
     * 
     * @param value
     *            - new boolean
     */
    public void setCombatTag(boolean value) {
        getSectionOrDefault(SECTION_FEATURES).set(COMBATTAG_KEY, value);
    }

}
