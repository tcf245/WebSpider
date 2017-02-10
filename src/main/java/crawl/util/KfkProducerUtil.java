package crawl.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KfkProducerUtil {

    private Producer<String, String> producer;
    private Properties props;
    private static KfkProducerUtil kfkProducer;
    private static final Log logger = LogFactory.getLog(KfkProducerUtil.class.getName());// log2配置

    public static KfkProducerUtil getInstance(){
        if(kfkProducer == null){
            synchronized (KfkProducerUtil.class){
                if(kfkProducer == null){
                    kfkProducer = new KfkProducerUtil();
                }
            }
        }
        return kfkProducer;
    }

    // 初始化kafka的配置
    private void init(){
        props = new Properties();
        props.put("bootstrap.servers", Config.getConfigStr("bootstrap.servers"));// 此处配置的是kafka的Ip和端口
        logger.info("kafka的ip以及端口=" + Config.getConfigStr("bootstrap.servers"));
        // 0表示不确认主服务器是否收到消息,马上返回,低延迟但最弱的持久性,数据可能会丢失
        // 1表示确认主服务器收到消息后才返回,持久性稍强,可是如果主服务器死掉,从服务器数据尚未同步,数据可能会丢失
        // -1表示确认所有服务器都收到数据,完美!
        props.put("acks", Config.getConfigStr("acks")); // 异步生产,批量存入缓存后再发到服务器去
        props.put("producer.type", Config.getConfigStr("producer.type")); // 配置key的序列化类
        props.put("key.serializer", Config.getConfigStr("key.serializer"));// 配置value的序列化类
        props.put("value.serializer", Config.getConfigStr("value.serializer"));
        // 其他配置
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        producer = new KafkaProducer(props);// 填充配置,初始化生产者
    }

    private KfkProducerUtil() {
        init();
    }

    /**
     * 同一个topic发生一条信息
     *
     * @param topic
     *            主题
     * @param message
     *            信息
     * @return
     */
    public boolean send(String topic, String message) {
        // 应对kafka挂了的情况
        while (!executeSend(topic, message)) {
            logger.error("发生信息到kafka主题" + topic + " 消息是" + message + "状态是失败");
        }
        return true;
    }

    /**
     * 同一个topic发生多条消息
     *
     * @param topic
     *            主题
     * @param messages
     *            信息集合
     * @return
     */
    public boolean send(String topic, List<String> messages) {
        // 应对kafka挂了的情况
        while (!executeSend(topic, messages)) {
            logger.error("发生信息到kafka主题" + topic + " 消息是" + messages + "状态是失败");
        }
        return true;
    }

    /**
     * 不同topic发生不同消息
     *
     * @return
     */
    public boolean send(Map<String, String> map) {
        // 应对kafka挂了的情况
        while (!executeSend(map)) {
            logger.error("没有全部成功发生信息到kafka中");
        }
        return true;
    }

    /**
     * 同一个topic发生一条信息
     *
     * @param topic
     *            主题
     * @param message
     *            信息
     * @return
     */
    public boolean executeSend(String topic, String message) {
        // 应对kafka挂了的情况
        while (producer == null) {
            // 填充配置,初始化生产者
            producer = new KafkaProducer(props);
        }
        // 判断主题或者消息是否为空
        if (EmptyUtil.isEmpty(topic) || EmptyUtil.isEmpty(message)) {
            logger.warn("主题或者消息为空");
            return false;
        }
        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
            producer.send(producerRecord);
            logger.info("发送信息到kafka主题" + topic + " 消息是" + message);
        } catch (Exception e) {
            logger.error("发送信息到kafka主题" + topic + "失败 错误信息为" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 同一个topic发生多条消息
     *
     * @param topic
     *            主题
     * @param messages
     *            信息集合
     * @return
     */
    public boolean executeSend(String topic, List<String> messages) {
        // 应对kafka挂了的情况
        while (producer == null) {
            // 填充配置,初始化生产者
            producer = new KafkaProducer(props);
        }
        // 判断主题或者消息是否为空
        if (EmptyUtil.isEmpty(topic) || EmptyUtil.isEmpty(messages)) {
            logger.warn("主题或者消息为空");
            return false;
        }
        try {
            // 循环发生消息
            for (String message : messages) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
                producer.send(producerRecord);
                logger.info("发生信息到kafka主题" + topic + " 消息是" + message);
            }
        } catch (Exception e) {
            logger.error("发生信息到kafka主题" + topic + "失败 错误信息为" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 不同topic发生不同消息
     *
     * @param map
     * @return
     */
    private boolean executeSend(Map<String, String> map) {
        // 应对kafka挂了的情况
        while (producer == null) {
            // 填充配置,初始化生产者
            producer = new KafkaProducer(props);
        }
        // 判断主题或者消息是否为空
        if (EmptyUtil.isEmpty(map)) {
            logger.warn("主题或者消息为空");
            return false;
        }
        try {
            // 循环发生消息
            for (String key : map.keySet()) {
                String value = map.get(key);
                ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(key, value);
                producer.send(producerRecord);
                logger.info("发生信息到kafka主题" + key + " 消息是" + value);
            }
        } catch (Exception e) {
            logger.error("没有全部成功发生信息到kafka中 错误信息为" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 关闭producer
     */
/*	public void close() {
		if (producer != null) {
			try {
				producer.close();
			} catch (Exception e) {
				logger.error("关闭producer失败 错误信息为" + e);
			} finally {
				producer = null;
			}
		}
	}*/

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            KfkProducerUtil.getInstance().executeSend("test_spider_tast11", "{\"APP_NO\": \"101600003280457313\",	\"CHANNEL_ID\": \"meiyifen\",	\"QueryINDIDType\": \"身份证\",	\"QueryINDID\": \"370521196810170213\",	\"Name\": \"美国人\",	\"Company\": \"开发中心\",	\"MobileNo\": \"13904108866\",	\"LiveAddress\": \"马家楼附近\",	\"MateName\": \"美国人\",	\"WorkTelephone\": \"01060242299\",	\"MateMobile\": \"3254325346\",	\"SendTime\": \"2016-10-06 10:00:09\"}");
        }
        //KfkProducer.getInstance().close();
    }

}
