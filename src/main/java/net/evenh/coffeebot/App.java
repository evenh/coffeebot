package net.evenh.coffeebot;

import net.evenh.coffeebot.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication(scanBasePackages = {
    "net.evenh.coffeebot",
    "me.ramswaroop.jbot"
})
public class App {
  public static void main(String... args) {
    SpringApplication.run(App.class, args);
  }
}
