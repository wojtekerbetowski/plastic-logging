package pl.erbetowski.plastic.logging

import org.slf4j.Logger
import spock.lang.Ignore
import spock.lang.Specification

class LoggingSpec extends Specification {

    def "fake service should log a message"() {
        given:
        def logger = Mock(Logger)

        and: 'an instance of fake service'
        def service = prepareServiceWithLogger(logger)

        when:
        service.myBusinessMethod()

        then:
        1 * logger.debug("Entering method") // should log once
    }

    @Ignore def prepareServiceWithLogger(def logger) {
        new FakeService()
    }
}
