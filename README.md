# WandoriaLoot
MMO Lootchests like on wynncraft
By sending the lootchest as a packet to the player, it allows every player to open a chest only once

## TODO
- [x] Make it barely work (0.8.2)
- [ ] Make Lootchests deletable ingame.
- [ ] Close the chest, when its closed.
- [ ] Particles
- [ ] Better Commands 
- [ ] Stop "Flying is not allowed" kick, when standing on the chests 
- [ ] Make lootgeneration based on the LootItem Attributes instead of randomly generating.



## Dependencies

This plugin requires [ProtocolLib 5.0.0 SNAPSHOT](https://ci.dmulloy2.net/job/ProtocolLib/) to be installed on your server.


## Commands

This plugin provides one command:

- `/setup`: Toggles setup mode and everything you interact with, will become a lootchest.

## Videos
Go into setup mode, to configure/create lootchests
![Setup Command](https://raw.githubusercontent.com/EinJOJO/WandoriaLoot/21c16ec46cfcb79a996b5ef36d1446e27157e46f/assets/command.gif)

Put Items in, that should be generated.
![How to configure loottable](https://github.com/EinJOJO/WandoriaLoot/blob/dev/assets/easy%20setup.gif?raw=true)

Open it, after 2 seconds it will disappear.
![Chest Opening GIF](https://github.com/EinJOJO/WandoriaLoot/blob/21c16ec46cfcb79a996b5ef36d1446e27157e46f/assets/chest%20opening.gif)

## Compilation

To compile this plugin, use the Gradle build tool. The necessary Gradle scripts are included in the repository. Simply navigate to the project root directory in your terminal and run the following command:

./gradlew build

This will compile the plugin and generate a JAR file in the `build/libs/` directory.


## License

This plugin is released under the MIT License. See the `LICENSE` file for more information.
