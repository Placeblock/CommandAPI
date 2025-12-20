package de.codelix.commandapi.paper.dtogenerator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DtoGenerator {

    @Data
    static class TestData {
        Integer amount = 0;
    }

    @Data
    static class Test {
        String name = "test";
        Integer amount = 0;
        List<String> tags = new ArrayList<>();
        TestData data = new TestData();
    }

    public static void main(String[] args) {
        JsonMapper mapper =JsonMapper.builder()
            .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build(), DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY)
            .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
            .build();
        Test test = new Test();
        String value = mapper.writeValueAsString(test);
        System.out.println(value);

        JsonNode jsonNode = mapper.readValues(value);
        for (JsonNode node : jsonNode) {
            System.out.println(node);
        }


    }

}
