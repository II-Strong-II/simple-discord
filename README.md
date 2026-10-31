# DiscordPlugin

A simple Minecraft plugin to link your Discord server in-game. It gives players a clickable link when they use `/discord`, and lets admins manage everything easily without touching server files.

## Features

* **Clickable Chat Link:** Typing `/discord` (or `/dc`) sends a chat message with a direct link to your Discord server.
* **Easy Setup:** Update the link anytime in-game using `/discord link <url>`.
* **Automatic Messages:** Send the Discord link automatically when players join or on a timed interval.
* **Admin Security:** Commands like reload and link updates are restricted to OPs and admins.

## Commands & Permissions

| Command | Description | Permission | Default |
| :--- | :--- | :--- | :--- |
| `/discord` | Shows the Discord link in chat. | None | Everyone |
| `/discord link <url>` | Updates the Discord link. | `discordplugin.admin` | OP / Admins |
| `/discord reload` | Reloads the configuration. | `discordplugin.admin` | OP / Admins |
| `/discord broadcast` | Sends the Discord message to all players. | `discordplugin.admin` | OP / Admins |

## Configuration (`config.yml`)

```yaml
discord-url: "https://discord.gg/74Bu83ruhb"

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
