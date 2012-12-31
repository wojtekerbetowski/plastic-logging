package pl.erbetowski.plastic.logging

import org.apache.tapestry5.internal.plastic.StandardDelegate
import org.apache.tapestry5.plastic.MethodAdvice
import org.apache.tapestry5.plastic.MethodInvocation
import org.apache.tapestry5.plastic.PlasticClass
import org.apache.tapestry5.plastic.PlasticClassTransformer
import org.apache.tapestry5.plastic.PlasticManager
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

    @Ignore def prepareServiceWithLogger(Logger logger) {

        final def advice = new MethodAdvice() {
            @Override
            void advise(MethodInvocation invocation) {
                logger.debug("Entering method")
                invocation.proceed()
            }
        }
        
        def transformer = new PlasticClassTransformer() {
            @Override
            void transform(PlasticClass plasticClass) {
                def methods = plasticClass.getMethods()

                methods[0].addAdvice()
            }
        }

        def delegate = new StandardDelegate(transformer)
        def plasticManager = PlasticManager
                .withContextClassLoader()
                .packages(['pl.erbetowski.plastic.logging'])
                .delegate(delegate)
                .create()

        def instantiator = plasticManager.getClassInstantiator('pl.erbetowski.plastic.logging.FakeService')

        return instantiator.newInstance()
    }
}
