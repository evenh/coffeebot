package net.evenh.coffeebot.config;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties available to the user.
 */
@ConfigurationProperties(prefix = "coffeebot")
@Validated
public class AppConfig {

  /**
   * The Slack bot token.
   */
  @NotEmpty
  private String botToken;

  /**
   * The Slack token for performing slash commands.
   */
  @NotEmpty
  private String slashCommandToken;

  /**
   * Name of the bot as seen in the Slack sidebar.
   */
  @NotEmpty
  private String botName = "Coffee Bot";

  public String getBotToken() {
    return botToken;
  }

  public void setBotToken(String botToken) {
    this.botToken = botToken;
  }

  public String getSlashCommandToken() {
    return slashCommandToken;
  }

  public void setSlashCommandToken(String slashCommandToken) {
    this.slashCommandToken = slashCommandToken;
  }

  public String getBotName() {
    return botName;
  }

  public void setBotName(String botName) {
    this.botName = botName;
  }
}
