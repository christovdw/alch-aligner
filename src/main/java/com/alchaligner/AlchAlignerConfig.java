package com.alchaligner;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(AlchAlignerConfig.GROUP)
public interface AlchAlignerConfig extends Config
{
	String GROUP = "alchaligner";

	@ConfigItem(
		keyName = "lowAlchemy",
		name = "Allow Low Level Alchemy",
		description = "Also show the overlay from 21 Magic using Low Level Alchemy. High Level Alchemy is used from 55 Magic.",
		warning = "Low Level Alchemy gives far fewer coins than High Level Alchemy. Only enable this if you are below 55 Magic.",
		position = 0
	)
	default boolean lowAlchemy()
	{
		return false;
	}

	@Range(max = 200)
	@Units(Units.PIXELS)
	@ConfigItem(
		keyName = "nearbyRange",
		name = "Nearby range",
		description = "Only show inventory slots within this distance of the alchemy spell. 0 shows only the slots the spell covers.",
		position = 1
	)
	default int nearbyRange()
	{
		return 10;
	}

	@Alpha
	@ConfigItem(
		keyName = "slotFillColor",
		name = "Slot fill",
		description = "Fill colour of each inventory slot's clickable area",
		position = 2
	)
	default Color slotFillColor()
	{
		return new Color(0, 255, 255, 25);
	}

	@Alpha
	@ConfigItem(
		keyName = "slotBorderColor",
		name = "Slot border",
		description = "Outline colour of each inventory slot's clickable area",
		position = 3
	)
	default Color slotBorderColor()
	{
		return new Color(0, 255, 255, 90);
	}

	@ConfigItem(
		keyName = "highlightTargetSlot",
		name = "Highlight target slots",
		description = "Highlight the part of each inventory slot covered by the alchemy spell. Large spell icons (e.g. with spell filtering) can cover several slots.",
		position = 4
	)
	default boolean highlightTargetSlot()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "targetSlotColor",
		name = "Target slots",
		description = "Colour of the inventory slot areas covered by the alchemy spell",
		position = 5
	)
	default Color targetSlotColor()
	{
		return new Color(255, 255, 0, 70);
	}
}
