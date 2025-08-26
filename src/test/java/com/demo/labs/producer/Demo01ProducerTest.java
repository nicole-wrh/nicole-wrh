package com.demo.labs.producer;

import com.demo.labs.LabsApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class Demo01ProducerTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Demo01Producer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        SendResult result = producer.syncSend(id);
        logger.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    @Test
    public void testASyncSend() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.asyncSend(id).thenAccept(result -> {
                    // 成功回调
                    System.out.println("消息发送成功: "
                            + result.getProducerRecord().value()
                            + " -> offset=" + result.getRecordMetadata().offset());
                })
                .exceptionally(ex -> {
                    // 失败回调
                    System.err.println("消息发送失败: " + ex.getMessage());
                    return null;
                });

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }
}