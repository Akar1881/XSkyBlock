# XSkyBlock Plugin

A feature-rich SkyBlock plugin for Minecraft servers with island management, trust system, and protection features.

## Dependencies

- Spigot/Paper/Purper 1.17+
- WorldEdit
- Vault

## Features

- Island creation and management
- Trust system with permissions
- Island expansion system
- Border visualization
- Protection system
- Scoreboard integration
- Economy integration

## Initial Setup

### Setting Up the Island Schematic

1. Build your starter island in a creative world
2. Select the island using WorldEdit:
   ```
   //wand              # Get the WorldEdit wand
   Left-click          # Set position 1
   Right-click         # Set position 2
   ```

3. Set the spawn point:
   ```
   /xmi schem spawn    # Get the spawn point setter tool
   ```
   - Break a block inside your island where you want players to spawn
   - This location will be used as the spawn point for all new islands

4. Save the schematic:
   ```
   /xmi schem change   # Save the current selection as the island schematic
   ```

5. Set the lobby location:
   ```
   /xmi setlobby      # Sets the current location as the lobby
   ```

### Configuration

1. Edit `config.yml` to customize:
   - Island prices
   - Default island size
   - Maximum island size
   - Messages

2. Edit `scoreboard.yml` to customize:
   - Scoreboard title
   - Display information
   - Server information

## Commands

### Player Commands
- `/xmi claim <name>` - Create a new island
- `/xmi menu` - Open island management menu
- `/xmi border <show|hide>` - Toggle island border visibility
- `/xmi trust <add|remove> <player>` - Manage trusted players
- `/xmi island` - Open island navigation menu
- `/xmi lobby` - Teleport to lobby

### Admin Commands
- `/xmi setlobby` - Set the lobby location
- `/xmi forcedel` - Force delete an island
- `/xmi schem change` - Save current selection as island schematic
- `/xmi schem spawn` - Set island spawn point

## Trust System

1. Island owners can trust players:
   ```
   /xmi trust add <player>
   ```

2. The target player will receive a trust request:
   - Type `accept` to join the island
   - Type `cancel` to decline
   - Request expires in 15 seconds

3. Trusted players can:
   - Access the island based on their permissions
   - Use island facilities based on granted permissions
   - View island information in their scoreboard

## Permissions

- `xskyblock.admin` - Access to admin commands

## Island Protection

The plugin protects islands from:
- Block breaking/placing by non-trusted players
- Entity damage
- Explosions
- Liquid flow
- Redstone manipulation
- Tree growth
- Falling blocks
- Wither damage

## Installation

1. Download the plugin JAR file
2. Place it in your server's `plugins` folder
3. Start/restart your server
4. Configure the plugin settings in `config.yml`

## License

GNU General Public License v3.0

## Support

For support, please create an issue on our GitHub repository or contact us through our support channels.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.