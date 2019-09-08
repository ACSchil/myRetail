package io.github.acschil.cassandra

import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification
import spock.lang.Specification

class CassandraConfigSpec extends Specification {

    CassandraConfig config = new CassandraConfig(
            contactPoints: 'contactPoints',
            port: 42,
            keyspaceName: 'keyspaceName'
    )

    def "getKeySpaceName"() {
        expect:
        config.getKeyspaceName() == 'keyspaceName'
    }

    def "getSchemaAction"() {
        expect:
        config.getSchemaAction() == SchemaAction.CREATE_IF_NOT_EXISTS
    }

    def "getCreateKeyspaceSpecifications"() {
        when:
        List<CreateKeyspaceSpecification> specs = config.getKeyspaceCreationSpecs()

        then:
        specs.size() == 1
        specs[0].getName().identifier == 'keyspacename' // cassandra is case insensitive
        specs[0].getIfNotExists() == true
        specs[0].getOptions()['replication'].values()[0] == 'SimpleStrategy'
        specs[0].getOptions()['replication'].values()[1] == 1 // replicas
    }

    def "cluster"() {
        setup:
        CreateKeyspaceSpecification createKeyspaceSpecificationMock = Mock(CreateKeyspaceSpecification)
        CassandraConfig configSpy = Spy(CassandraConfig,
                constructorArgs: [
                        [
                                contactPoints: 'contactPoints',
                                port         : 42,
                                keyspaceName : 'keyspaceName'
                        ]
                ])

        when:
        CassandraClusterFactoryBean cluster = configSpy.cluster()

        then:
        cluster.port == 42
        cluster.contactPoints == 'contactPoints'
        cluster.keyspaceCreations == [createKeyspaceSpecificationMock]

        1 * configSpy.getKeyspaceCreationSpecs() >> [createKeyspaceSpecificationMock]
    }


}
