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
   * The Slack token for performing slash commands.
   */
  @NotEmpty
  private String slashCommandToken;

  public String getSlashCommandToken() {
    return slashCommandToken;
  }

  public void setSlashCommandToken(String slashCommandToken) {
    this.slashCommandToken = slashCommandToken;
  }
}
