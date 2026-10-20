# Alch Aligner

Line up your alchemy casts without moving the mouse.

When you cast High Level Alchemy, the game switches to the inventory, you click the item, and it switches back to the spellbook. If the spell sits over the item you want to alch, every cast is clicked from the same spot. Alch Aligner shows, while the spellbook is open, where the inventory slots near the alchemy spell will be once the game switches to the inventory, so you can position the spell over the right item.

## Features

- While the standard spellbook is open, outlines the inventory slots near the High Level Alchemy spell.
- Highlights the part of each slot that the spell covers. A large spell icon (for example with the game's spell filtering enabled) can cover several slots, and each one is shown.
- Includes empty slots, so you can see where an item will land before you move it there.
- Only active on the standard spellbook while the game shows High Level Alchemy as castable, meaning you have 55+ Magic and the runes (staffs and rune pouch count).
- Optional Low Level Alchemy support, shown while that spell is castable. It is off by default, since Low Level Alchemy gives far fewer coins.

The plugin only draws on screen. It doesn't click, move or resize anything; you still cast every alch yourself.

## Settings

| Setting | Default | Description |
| --- | --- | --- |
| Allow Low Level Alchemy | Off | Also show slots near Low Level Alchemy from 21 Magic |
| Nearby range | 10 px | How close a slot must be to the spell to be shown. 0 shows only the slots the spell covers |
| Slot fill / Slot border | Faint cyan | Colours of the outlined slots |
| Highlight target slots | On | Highlight the part of each slot covered by the spell |
| Target slots | Yellow | Colour of the covered areas |

## Usage

1. Open the inventory once so the plugin knows where the slots are.
2. Switch to the spellbook. Slots near High Level Alchemy are outlined, and the area the spell covers is highlighted.
3. Move the item you want to alch into a highlighted slot, or reposition the spell with the game's spell filtering or RuneLite's spellbook reordering.

## Development

Requires JDK 11.

- Build: `./gradlew build`
- Run a RuneLite development client with the plugin loaded: `./gradlew run`
- To log in with a Jagex Account, see [Using Jagex Accounts](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts).
