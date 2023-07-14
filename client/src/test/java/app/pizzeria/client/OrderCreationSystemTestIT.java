package app.pizzeria.client;

import app.pizzeria.client.service.PalmettoNotificationService;
import app.pizzeria.common.model.OrderDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(topics = {"Order"}, partitions = 3)
@ExtendWith(SpringExtension.class)
public class OrderCreationSystemTestIT {

    private BlockingQueue<ConsumerRecord<String, OrderDto>> records;

    private KafkaMessageListenerContainer<String, OrderDto> container;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean
    private PalmettoNotificationService producer;

    @Captor
    ArgumentCaptor<OrderDto> orderArgumentCaptor;

    @BeforeAll
    void setUp() {
        DefaultKafkaConsumerFactory<String, OrderDto> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());
        ContainerProperties containerProperties = new ContainerProperties("Order");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, OrderDto>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.GROUP_ID_CONFIG, "consumer",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }


    @Test
    void shouldCreateOrderAndNotifyToKafka() throws InterruptedException {
        OrderDto order = createOrder();

        ResponseEntity<String> response = restTemplate.postForEntity("/api/orders", order, String.class);

        ConsumerRecord<String, OrderDto> message = records.poll(500, TimeUnit.MILLISECONDS);

        verify(producer, times(1)).notify(orderArgumentCaptor.capture());
        Long createdOrderId = orderArgumentCaptor.getValue().getId();

        assertNotNull(message);
        assertEquals(String.valueOf(createdOrderId), message.key());
        OrderDto orderReceived = message.value();
        assertNotNull(orderReceived);
        assertEquals("Pizza", orderReceived.getOrderItem());
        assertEquals("Maryna", orderReceived.getCustomerName());
    }

    private OrderDto createOrder() {
        OrderDto order = new OrderDto();
        order.setOrderItem("Pizza");
        order.setCustomerName("Maryna");
        order.setTotalAmount(1);
        order.setOrderDate("2023-07-12");
        return order;

    }
}
