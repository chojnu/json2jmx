package com.srdcloud.taas.common.conf;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
public class ConsulConfigPostProcessor implements BeanPostProcessor {
    @Autowired
    private ObjectMapper om;
    @Autowired
    private ConsulClient client;
    @Autowired
    private ConsulConfigProperties properties;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (bean instanceof KafkaProperties) {
                Response<GetValue> valueResponse = client.getKVValue("ServicePublicConfig/KafkaSetting");
                initKafkaProp((KafkaProperties) bean, valueResponse.getValue().getDecodedValue());
            } else if (bean instanceof MongoProperties) {
                Response<GetValue> valueResponse = client.getKVValue("ServicePublicConfig/MongoDBSetting");
                initMongoDBProp((MongoProperties) bean, valueResponse.getValue().getDecodedValue());
            } else if (bean instanceof DataSourceProperties) {
                Response<GetValue> valueResponse = client.getKVValue("ServicePublicConfig/MysqlSetting");
                initMysqlProp((DataSourceProperties) bean, valueResponse.getValue().getDecodedValue());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BeanCreationException(e.getMessage(), e);
        }
        return bean;
    }

    public void initKafkaProp(KafkaProperties kafkaProperties, String jsonStr) throws JsonProcessingException {
        JsonNode jn = om.readTree(jsonStr);
        String[] brokers = StringUtils.split(jn.get("brokers").asText(), ",");
        kafkaProperties.setBootstrapServers(Arrays.asList(brokers));
    }

    public void initMongoDBProp(MongoProperties mongoProperties, String jsonStr) throws JsonProcessingException {
        JsonNode jn = om.readTree(jsonStr);
        mongoProperties.setHost(jn.get("brokers").asText());
        mongoProperties.setDatabase(jn.get("dbname").asText());
    }

    public void initMysqlProp(DataSourceProperties dataSourceProperties, String jsonStr) throws JsonProcessingException {
        JsonNode jn = om.readTree(jsonStr);
        String url = String.format("jdbc:mysql://%s:%s/%s", jn.get("m_host").asText(), jn.get("m_port").asText(), jn.get("m_db").asText());
        dataSourceProperties.setUrl(url);
        dataSourceProperties.setUsername(jn.get("m_username").asText());
        dataSourceProperties.setPassword(jn.get("m_password").asText());
    }
}
