# DiscordPlugin

> **DiscordPlugin** is a lightweight, clean, and fully customizable Minecraft plugin designed to seamlessly share your community Discord server link with your players.

## ✨ Features

* **Interactive Messages:** Players simply type `/discord` (or `/dc`) to receive a clean, clickable text component that opens your invitation link directly.
* **In-Game Management:** Administrators can update the Discord link instantly using `/discord link <url>` without needing file access or server restarts.
* **Auto-Broadcasts:** Configure a periodic timer to automatically broadcast the Discord invite message to all online players at a defined interval.
* **Join Notifications:** Automatically send the invite when a player joins for the first time or on every connection.
* **Security:** Admin commands are strictly restricted to operators or dedicated permissions.

## 📜 Commands & Permissions

| Command | Description | Permission | Default |
| :--- | :--- | :--- | :--- |
| `/discord` | Displays the interactive Discord link. | None | Everyone |
| `/discord link <url>` | Updates the invitation link. | `discordplugin.admin` | OP / Admins |
| `/discord reload` | Reloads the plugin configuration. | `discordplugin.admin` | OP / Admins |
| `/discord broadcast` | Triggers an immediate server-wide broadcast. | `discordplugin.admin` | OP / Admins |

## ⚙️ Configuration (`config.yml`)

```yaml
discord-url: "[https://discord.gg/Aaq4hHWEt](https://discord.gg/Aaq4hHWEt)"

first-join:
  enabled: true

every-join:
  enabled: false

broadcast:
  enabled: true
  interval-minutes: 60

messages:
  header: "&8&m----------------------------------------"
  title: "&5&lDISCORD &7» Join our community!"
  button-text: "&d&l[CLICK HERE TO JOIN]"
  button-hover: "&eClick to open the Discord link"
  footer: "&8&m----------------------------------------"
