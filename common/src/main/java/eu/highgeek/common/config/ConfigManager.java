package eu.highgeek.common.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonLogger;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    @Getter
    private static YamlDocument config;

    public static void setupConfig(CommonLogger logger, File file){

        try {
            config = YamlDocument.create(new File(file, "config.yml"),
                    CommonMain.getCommonMainInstance().getClass().getClassLoader().getResourceAsStream("config.yml"), GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());

            config.update();
            config.save();
        } catch (IOException e){
            logger.severe(
                    "Could not create/load plugin config, disabling! Additional info: "
                            + e);
            // TODO ADD CORE DISABLE PLUGIN IMPORTANT!!!!!
            return;
        }
    }

    public static YamlDocument getPluginConfig() {
        return config;
    }
}
