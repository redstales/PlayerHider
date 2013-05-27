package io.github.redinzane.playerhider;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class PlayerHiderConfiguration 
{
	private final Configuration config;
	
	private static final String SECTION_COOLDOWNS = "cooldowns";
	private static final String SECTION_DISTANCES = "distances";
	
	private static final String COOLDOWN_KEY = "update_cooldown";
	private static final String DISTANCE_KEY = "sneakdistance";
	
	private static final int DEFAULT_COOLDOWN = 500;
	private static final int DEFAULT_DISTANCE = 30;
	
	public PlayerHiderConfiguration(Configuration config) {
		this.config = config;
	}
	
	
	/**
	 * Retrieve the cooldown in milliseconds.
	 * @return Cooldown in milliseconds.
	 */
	public int getCooldown() {
		Object value = getSectionOrDefault(SECTION_COOLDOWNS).get(COOLDOWN_KEY);
		
		if (value == null)
			return DEFAULT_COOLDOWN;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the cooldown in milliseconds.
	 * @param value - new cooldown.
	 */
	public void setCooldown(int value) {
		getSectionOrDefault(SECTION_COOLDOWNS).get(COOLDOWN_KEY, value);
	}
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in milliseconds.
	 */
	public int getDistance() {
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCE_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCE;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistance(int value) {
		getSectionOrDefault(SECTION_DISTANCES).get(DISTANCE_KEY, value);
	}
	
	private ConfigurationSection getSectionOrDefault(String name) {
		ConfigurationSection section = config.getConfigurationSection(name);
		
		if (section != null)
			return section;
		else
			return config.createSection(name);
	}
	
	
}
