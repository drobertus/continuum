package com.mechzombie.continuum
import com.rabbitmq.client.QueueingConsumer

/**
 * Created by David on 12/11/2014.
 */
class TaskConsumer implements Runnable {

    QueueingConsumer consumer
    def keepReading = true

    TaskConsumer(QueueingConsumer qc) {
        consumer = qc

    }

    void onRead(byte[] msg) {

    }

    void run() {
        while (keepReading) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery()
                onRead(delivery.body)
            }
            catch (Exception e) {

            }
        }
    }
}