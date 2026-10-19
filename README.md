# Alch Aligner
While the standard spellbook is open, shows where each inventory slot's clickable area will be, so you can line up High Level Alchemy (or optionally Low Level Alchemy) with the item you want to alch.

## Development

Requires JDK 11 (Eclipse Temurin 11 is installed at `C:\Program Files\Eclipse Adoptium\jdk-11.0.32.101-hotspot`).

- Build: `.\gradlew.bat build`
- Launch a RuneLite dev client with this plugin loaded: `.\gradlew.bat run`
- Log in to the dev client with a Jagex account: https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts

Plugin code lives in `src/main/java`. `src/test/java/com/alchaligner/AlchAlignerPluginTest.java` is the dev-client launcher.
See `AGENTS.md` for RuneLite's coding rules and Plugin Hub restrictions.
