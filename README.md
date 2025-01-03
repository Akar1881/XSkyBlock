# XMyIsland

A powerful and feature-rich island management plugin for Minecraft servers running Spigot 1.17+.

## Features

- **Island Management**
  - Create and manage personal islands
  - Customizable island sizes
  - Visual border particles
  - Island expansion system with economy integration

- **Advanced Protection**
  - Comprehensive grief prevention
  - Protection against explosions, liquids, and redstone
  - Tree growth control
  - Advanced block protection (pistons, dispensers, etc.)
  - Falling block protection

- **Trust System**
  - Granular permission system
  - Per-player trust management
  - Permission inheritance
  - GUI-based permission management

- **Economy Integration**
  - Island expansion costs
  - Refund system for island deletion
  - Configurable prices

## Requirements

- Spigot/Paper 1.17+
- Vault
- Any economy plugin compatible with Vault

## Installation

1. Download the latest release in [Modrinth](https://modrinth.com/plugin/xmyisland)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/XMyIsland/config.yml`
5. You can download the latest releases in github releases page

## Commands

- `/xmi claim <name>` - Claim a new island
- `/xmi menu` - Open island management menu
- `/xmi border <show|hide>` - Toggle border visibility
- `/xmi trust <add|remove> <player>` - Manage trusted players
- `/xmi help` - Show help message

Admin Commands:
- `/xmi forcedel` - Force delete an island (Admin only)

## Permissions

Default permissions are handled automatically. Operators have access to all commands.

## Configuration

The plugin is highly configurable. See `config.yml` for all options:

```yaml
settings:
  price-per-one-block: 20.0
  delete-island-refund-percentage: 50
  default-island-size: 9
  max-island-size: 101
  min-island-distance: 10
```

## Building

This project uses Maven. To build:

```bash
mvn clean package
```

## Contributing

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Support

For support:
1. Check the [Issues](https://github.com/Akar1881/XMyIsland/issues) section
2. Create a new issue if needed

## Authors

- XMyIsland Team

## Acknowledgments

- Spigot Community
- Vault API Team