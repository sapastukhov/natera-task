package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Reloadable;

import java.util.UUID;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "system:env","classpath:application.properties"})
public interface AppConfig extends Reloadable {

    @Key("user.token")
    String userToken();

    @Key("api.url")
    String apiUrl();
}
