package com.alchaligner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * While the spellbook is open, draws where the inventory slots near the alchemy spells
 * will be clickable once the game switches back to the inventory.
 * Slot positions are remembered from the last frame the inventory was visible.
 */
class AlchAlignerOverlay extends Overlay
{
	private final Client client;
	private final AlchAlignerPlugin plugin;
	private final AlchAlignerConfig config;

	private final List<Rectangle> slots = new ArrayList<>();

	@Inject
	AlchAlignerOverlay(Client client, AlchAlignerPlugin plugin, AlchAlignerConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	void clearSlots()
	{
		slots.clear();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Widget inventory = client.getWidget(InterfaceID.Inventory.ITEMS);
		if (inventory != null && !inventory.isHidden())
		{
			rememberSlots(inventory);
			return null;
		}

		Widget spellbook = client.getWidget(InterfaceID.MagicSpellbook.UNIVERSE);
		if (spellbook == null || spellbook.isHidden() || slots.isEmpty())
		{
			return null;
		}

		List<Rectangle> spells = getSpellBounds();
		if (spells.isEmpty())
		{
			return null;
		}

		int range = config.nearbyRange();
		Color targetColor = config.targetSlotColor();
		Color targetBorderColor = new Color(targetColor.getRGB());

		for (Rectangle slot : slots)
		{
			if (!isNearAnySpell(slot, spells, range))
			{
				continue;
			}

			graphics.setColor(config.slotFillColor());
			graphics.fill(slot);

			// A large spell icon (e.g. with spell filtering) can cover several slots.
			// Only the overlap is where a click hits both the spell and that slot.
			boolean covered = false;
			if (config.highlightTargetSlot())
			{
				graphics.setColor(targetColor);
				for (Rectangle spell : spells)
				{
					Rectangle overlap = slot.intersection(spell);
					if (!overlap.isEmpty())
					{
						graphics.fill(overlap);
						covered = true;
					}
				}
			}

			graphics.setColor(covered ? targetBorderColor : config.slotBorderColor());
			graphics.draw(slot);
		}

		return null;
	}

	private static boolean isNearAnySpell(Rectangle slot, List<Rectangle> spells, int range)
	{
		for (Rectangle spell : spells)
		{
			Rectangle nearby = new Rectangle(spell);
			nearby.grow(range, range);
			if (nearby.intersects(slot))
			{
				return true;
			}
		}
		return false;
	}

	private void rememberSlots(Widget inventory)
	{
		Widget[] items = inventory.getDynamicChildren();
		Point origin = inventory.getCanvasLocation();
		if (items == null || origin == null || origin.getX() < 0)
		{
			return;
		}

		// Empty slots aren't drawn, so their own bounds are (-1, -1). Their layout position
		// within the inventory is still set, so place every slot relative to the inventory.
		slots.clear();
		for (Widget item : items)
		{
			slots.add(new Rectangle(
				origin.getX() + item.getRelativeX(),
				origin.getY() + item.getRelativeY(),
				item.getWidth(),
				item.getHeight()));
		}
	}

	private List<Rectangle> getSpellBounds()
	{
		List<Rectangle> bounds = new ArrayList<>();
		for (int component : plugin.getAlchemySpellComponents())
		{
			Widget spell = client.getWidget(component);
			if (spell != null && !spell.isHidden())
			{
				bounds.add(spell.getBounds());
			}
		}
		return bounds;
	}
}
