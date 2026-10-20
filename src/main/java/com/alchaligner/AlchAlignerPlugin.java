package com.alchaligner;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.CanvasSizeChanged;
import net.runelite.api.events.ResizeableChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.SpriteID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
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

	private boolean loggedMissingSprite;

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
	 * Requires the standard spellbook; High Level Alchemy, and Low Level Alchemy
	 * when enabled in the config, are included while the game shows them as castable.
	 */
	List<Integer> getAlchemySpellComponents()
	{
		List<Integer> spells = new ArrayList<>(2);
		if (client.getVarbitValue(VarbitID.SPELLBOOK) != STANDARD_SPELLBOOK)
		{
			return spells;
		}

		int magicLevel = client.getBoostedSkillLevel(Skill.MAGIC);
		if (isCastable(InterfaceID.MagicSpellbook.HIGH_ALCHEMY, SpriteID.Magicon.HIGH_LEVEL_ALCHEMY,
			SpriteID.Magicoff.HIGH_LEVEL_ALCHEMY, magicLevel >= HIGH_ALCHEMY_LEVEL))
		{
			spells.add(InterfaceID.MagicSpellbook.HIGH_ALCHEMY);
		}
		if (config.lowAlchemy() && isCastable(InterfaceID.MagicSpellbook.LOW_ALCHEMY, SpriteID.Magicon.LOW_LEVEL_ALCHEMY,
			SpriteID.Magicoff.LOW_LEVEL_ALCHEMY, magicLevel >= LOW_ALCHEMY_LEVEL))
		{
			spells.add(InterfaceID.MagicSpellbook.LOW_ALCHEMY);
		}
		return spells;
	}

	/**
	 * The game draws a spell with its "on" sprite when the player has the level and runes
	 * to cast it, and its "off" sprite otherwise. If neither sprite is found, fall back to
	 * the level requirement.
	 */
	private boolean isCastable(int component, int onSprite, int offSprite, boolean meetsLevel)
	{
		Widget spell = client.getWidget(component);
		if (spell == null)
		{
			return false;
		}

		Boolean castable = findCastableSprite(spell, onSprite, offSprite);
		if (castable != null)
		{
			return castable;
		}

		if (!loggedMissingSprite)
		{
			loggedMissingSprite = true;
			log.debug("No castable sprite found for spell component {} (sprite {}), using the Magic level instead",
				component, spell.getSpriteId());
		}
		return meetsLevel;
	}

	private static Boolean findCastableSprite(Widget spell, int onSprite, int offSprite)
	{
		Boolean castable = spriteState(spell.getSpriteId(), onSprite, offSprite);
		if (castable != null)
		{
			return castable;
		}

		for (Widget[] children : new Widget[][]{spell.getStaticChildren(), spell.getDynamicChildren()})
		{
			if (children == null)
			{
				continue;
			}
			for (Widget child : children)
			{
				castable = spriteState(child.getSpriteId(), onSprite, offSprite);
				if (castable != null)
				{
					return castable;
				}
			}
		}
		return null;
	}

	private static Boolean spriteState(int spriteId, int onSprite, int offSprite)
	{
		if (spriteId == onSprite)
		{
			return true;
		}
		if (spriteId == offSprite)
		{
			return false;
		}
		return null;
	}

	@Provides
	AlchAlignerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AlchAlignerConfig.class);
	}
}
