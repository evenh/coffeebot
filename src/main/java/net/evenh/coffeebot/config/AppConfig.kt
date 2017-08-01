package net.evenh.coffeebot.config

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

/**
 * Configuration properties available to the user.
 */
@ConfigurationProperties("coffeebot")
@Validated
data class AppConfig(@NotEmpty var slashCommandToken : String? = null)
