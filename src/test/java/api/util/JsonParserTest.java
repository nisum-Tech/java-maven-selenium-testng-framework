package api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;

public class JsonParserTest {

    // Helper class for testing
    public static class Person {
        public String name;
        public int age;

        public Person() {} // Needed for Jackson deserialization

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    @Test
    public void testToJson() {
        Person person = new Person("John Doe", 30);
        String json = JsonParser.toJson(person);
        Assert.assertTrue(json.contains(""name":"John Doe""));
        Assert.assertTrue(json.contains(""age":30"));
    }

    @Test
    public void testToPrettyJson() {
        Person person = new Person("John Doe", 30);
        String json = JsonParser.toPrettyJson(person);
        Assert.assertTrue(json.contains("  "name" : "John Doe""));
        Assert.assertTrue(json.contains("  "age" : 30"));
        Assert.assertTrue(json.contains("\n"));
    }

    @Test
    public void testFromJsonToClass() {
        String json = "{"name":"Jane Doe","age":25}";
        Person person = JsonParser.fromJson(json, Person.class);
        Assert.assertEquals(person.name, "Jane Doe");
        Assert.assertEquals(person.age, 25);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testFromJsonInvalidJson() {
        JsonParser.fromJson("not a json", Person.class);
    }

    @Test
    public void testFromJsonToTypeReference() {
        String json = "[{"name":"Jane Doe","age":25}, {"name":"John Doe","age":30}]";
        List<Person> people = JsonParser.fromJson(json, new TypeReference<List<Person>>() {});
        Assert.assertEquals(people.size(), 2);
        Assert.assertEquals(people.get(0), new Person("Jane Doe", 25));
        Assert.assertEquals(people.get(1), new Person("John Doe", 30));
    }

    @Test
    public void testParse() {
        String json = "{"key":"value"}";
        Assert.assertNotNull(JsonParser.parse(json));
        Assert.assertEquals(JsonParser.parse(json).get("key").asText(), "value");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testParseInvalidJson() {
        JsonParser.parse("not a json");
    }

    @Test
    public void testGetString() {
        String json = "{"name":"test","value":123}";
        Assert.assertEquals(JsonParser.getString(json, "name"), "test");
    }

    @Test
    public void testGetStringMissingField() {
        String json = "{"value":123}";
        Assert.assertNull(JsonParser.getString(json, "name"));
    }

    @Test
    public void testGetInt() {
        String json = "{"name":"test","value":123}";
        Assert.assertEquals(JsonParser.getInt(json, "value"), 123);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetIntMissingField() {
        String json = "{"name":"test"}";
        JsonParser.getInt(json, "value");
    }
}
