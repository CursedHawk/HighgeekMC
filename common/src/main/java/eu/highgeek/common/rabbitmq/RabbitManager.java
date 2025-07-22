package eu.highgeek.common.rabbitmq;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.config.RabbitConfig;
import lombok.Getter;

public class RabbitManager {

    @Getter
    private static RabbitListener rabbitListener;


    public RabbitManager(CommonMain main){
        rabbitListener = new RabbitListener(main.getCommonPlugin().getCommonLogger(), new RabbitConfig());
        startListener();
    }

    public void startListener(){
        rabbitListener.start();
    }

    public void stopListener(){
        if (rabbitListener != null) rabbitListener.stopConsumer();
    }

}
