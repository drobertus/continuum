package com.mechzombie.continuum

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer

/**
 * Created by David on 2/28/2015.
 */
class TaskQueueReader {

    Connection connection
    Channel channel

    final def brokerAddress
    final def brokerPort
    final def brokerUserName
    final def brokerPassword
    final def useSSL
    final def taskQueueName



    TaskQueueReader(def add, def port, def user, def pass, def ssl, def taskQueueName) {
        this.brokerAddress = add
        brokerPort = port
        brokerUserName = user
        brokerPassword = pass
       this.useSSL = ssl
        this.taskQueueName = taskQueueName
    }

    def startup() {
        ConnectionFactory factory = new ConnectionFactory()
        factory.setHost(brokerAddress)
        factory.setPassword(brokerPassword)
        factory.setPort(brokerPort)
        factory.setPassword(brokerPassword)
        if(useSSL) {
            factory.useSslProtocol()
        }
        this.connection = factory.newConnection()
        channel  = connection.createChannel()
        //TODO: bind to the TASKS queue with a Consumer
        //channel.queueDeclare()
        //channel.basicConsume(...)

        def consumer = getExchangeConsumer('', taskQueueName, '')
        def tc = new TaskConsumer(consumer)
        //scope of thread is wrong
        Thread.start tc
    }

    QueueingConsumer getExchangeConsumer(String exchangeName, String queueToBind, String route) {

        def channel = connection.createChannel()

        def declareOk = channel.queueDeclare(queueToBind,true, true, true, null)
        def ok = channel.queueBind(queueToBind, exchangeName, route, null)
        log.info ("okay=${ok}")

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueToBind, true, consumer);

        //TODO: this channel is orphaned from the consumer.
        return consumer
    }


}
