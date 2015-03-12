import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.util.logging.Log
import org.apache.qpid.server.Broker
import org.apache.qpid.server.BrokerOptions
import spock.lang.Shared
import spock.lang.Specification
import com.google.common.io.Files
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.util.logging.Log
import org.apache.commons.io.FileUtils
import org.apache.qpid.server.Broker
import org.apache.qpid.server.BrokerOptions
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by David on 2/28/2015.
 */
class BrokerTestHarness extends Specification {




        @Shared def tmpFolder = Files.createTempDir()
        @Shared Broker broker
        ConnectionFactory factory

        @Shared def amqpPort = 9234
        @Shared def httpPort = 9235

        @Shared def qpidHomeDir = 'src/test/resources/'
        @Shared def configFileName = "/test-config.json"

        Connection getConnection() {
            if (!factory) {
                factory = new ConnectionFactory()
                factory.setUri(getConnectionURL())
                factory.useSslProtocol()
            }
            return factory.newConnection()
        }

        String getConnectionURL() {
            "amqp://guest:password@localhost:${amqpPort}"
        }

        void setupSpec() {
            broker = new Broker();
            def brokerOptions = new BrokerOptions()

            File file = new File(qpidHomeDir)
            String homePath = file.getAbsolutePath();
            log.info(' qpid home dir=' + homePath)
            log.info(' qpid work dir=' + tmpFolder.absolutePath)

            brokerOptions.setConfigProperty('qpid.work_dir', tmpFolder.absolutePath);

            brokerOptions.setConfigProperty('qpid.amqp_port',"${amqpPort}")
            brokerOptions.setConfigProperty('qpid.http_port', "${httpPort}")
            brokerOptions.setConfigProperty('qpid.home_dir', homePath);


            brokerOptions.setInitialConfigurationLocation(homePath + configFileName)
            broker.startup(brokerOptions)
            log.info('broker started')

        }

        void cleanupSpec() {
            broker.shutdown()
            FileUtils.deleteDirectory(tmpFolder)
        }


}