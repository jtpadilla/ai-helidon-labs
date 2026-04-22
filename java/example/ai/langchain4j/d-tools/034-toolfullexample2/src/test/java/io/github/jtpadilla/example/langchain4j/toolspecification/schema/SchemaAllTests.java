package io.github.jtpadilla.example.langchain4j.toolspecification.schema;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityDataSchemaTest.class,
        CityDataListSchemaTest.class,
        CityListSchemaTest.class,
        EmptySchemaTest.class,
        LocalDateTimeSchemaTest.class
})
public class SchemaAllTests {
}
