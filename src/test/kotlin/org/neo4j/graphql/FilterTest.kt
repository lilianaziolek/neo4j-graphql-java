package org.neo4j.graphql

import demo.org.neo4j.graphql.TckTest
import demo.org.neo4j.graphql.TckTest.Companion.assertQuery
import org.codehaus.jackson.map.ObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class FilterTest {

    val schema = """
enum Gender { female, male }
type Person {
    id : ID!
    name: String
    age: Int
    height: Float
    fun: Boolean
    gender: Gender
    company: Company @relation(name:"WORKS_AT")
}
type Company {
    name: String
    employees: [Person] @relation(name:"WORKS_AT", direction: IN)
}
type Query {
    person : [Person]
}
"""

    @Test
    fun simpleFilter() {

        val expected = "MATCH (person:Person) WHERE person.gender = \$filterPersonGender RETURN person { .name } AS person"
        val query = "{ person(filter: { gender: male }) { name }}"
        assertQuery(schema, query, expected, mapOf("filterPersonGender" to "male"))
    }

    @Test
    fun testTck() {
        val EXPECTED_FAILURES = 59
        val fileName = "filter-test.md"
        TckTest(schema).testTck(fileName, EXPECTED_FAILURES)
    }

}