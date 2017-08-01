package net.evenh.coffeebot

import net.evenh.coffeebot.config.AppConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(AppConfig::class)
@SpringBootApplication(scanBasePackages = arrayOf("net.evenh.coffeebot", "me.ramswaroop.jbot"))
class App

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
