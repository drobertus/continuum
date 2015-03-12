import spock.lang.Specification


/**
 * Created by David on 2/28/2015.
 */
class ReaderTest extends Specification {

    def "test reading task from queue"() {
        setup: "stand up the broker"

         and: " turn on a reader instance listening on the task queue"

        when: "a serialized closure is added to the queue"

        then: " then reader should pick it up and execute it"

    }
}