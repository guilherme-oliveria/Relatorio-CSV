package br.jus.tjro.gabinete.util;

public class KafkaHandlingUtil {

    public static final String first = "_retry_5m";
    public static final String second = "_retry_30m";
    public static final String third = "_retry_1h";
    public static final String fail = "_fail";



    public static String procuraProximoTopico(String topico) {
        if(topico.contains(first))
            return topico.replace(first, second);
        if(topico.contains(second))
            return topico.replace(second, third);
        if(topico.contains(third))
            return topico.replace(third, fail);
        if(topico.contains(fail))
            return "";
        else
            return topico+first;
    }


    public static void espera(String topic) throws InterruptedException {
        if(topic.contains(first)) {
            Thread.sleep(300000);
        }
        if(topic.contains(second)) {
            Thread.sleep(18000000);
        }
        if(topic.contains(third)) {
            Thread.sleep(36000000);
        }

    }

}
