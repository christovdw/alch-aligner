package com.alchaligner;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.CanvasSizeChanged;
import net.runelite.api.events.ResizeableChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Alch Aligner",
	description = "Shows inventory slot click areas over the spellbook to line up alchemy casts",
	tags = {"alch", "alchemy", "high alch", "low alch", "magic", "spellbook", "inventory"}
)
public class AlchAlignerPlugin extends Plugin
{
	private static final int STANDARD_SPELLBOOK = 0;
	private static final int LOW_ALCHEMY_LEVEL = 21;
	private static final int HIGH_ALCHEMY_LEVEL = 55;

	@Inject
	private Client client;

	@Inject
	private AlchAlignerConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AlchAlignerOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		overlay.clearSlots();
	}

	@Subscribe
	public void onCanvasSizeChanged(CanvasSizeChanged event)
	{
		// The side panel moves, so the remembered slot positions are stale until the inventory is shown again
		overlay.clearSlots();
	}

	@Subscribe
	public void onResizeableChanged(ResizeableChanged event)
	{
		overlay.clearSlots();
	}

	/**
	 * The alchemy spells the player can cast right now, empty if none.
	 * Requires the standard spellbook; High Level Alchemy from 55 Magic, and
	 * Low Level Alchemy from 21 Magic when enabled in the config.
	 */
	List<Integer> getAlchemySpellComponents()
	{
		List<Integer> spells = new ArrayList<>(2);
		if (client.getVarbitValue(VarbitID.SPELLBOOK) != STANDARD_SPELLBOOK)
		{
			return spells;
		}

		int magicLevel = client.getBoostedSkillLevel(Skill.MAGIC);
		if (magicLevel >= HIGH_ALCHEMY_LEVEL)
		{
			spells.add(InterfaceID.MagicSpellbook.HIGH_ALCHEMY);
		}
		if (config.lowAlchemy() && magicLevel >= LOW_ALCHEMY_LEVEL)
		{
			spells.add(InterfaceID.MagicSpellbook.LOW_ALCHEMY);
		}
		return spells;
	}

	@Provides
	AlchAlignerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AlchAlignerConfig.class);
	}
}
