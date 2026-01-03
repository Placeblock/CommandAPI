package de.codelix.commandapi.paper.dtogenerator;

import de.codelix.commandapi.core.parameter.impl.*;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import lombok.Data;
import net.kyori.adventure.text.TextComponent;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.*;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;

public class DtoGenerator {
    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Min {
        int value();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Max {
        int value();
    }

    @Data
    static class TestData {
        Integer amount = 0;
        MultipleGradientPaint.ColorSpaceType material;
    }

    @Data
    static class Test {
        String name = "test";
        @Min(2)
        @Max(5)
        Integer amount = 0;
        List<String[]> tags = new LinkedList<>();
        Set<Test> recursiveTests;
        TestData data = new TestData();
        String[][] array;
    }

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(serialize(test));
    }


    public static String serialize(Object object) {
        ObjectNode configNode = JsonNodeFactory.instance.objectNode();
        ObjectNode typesNode = configNode.putObject("types");
        typeArray(typesNode, object.getClass());
        configNode.put("type", object.getClass().getTypeName());
        configNode.putPOJO("default-value", object);
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(configNode);
    }

    public static <S extends PaperSource<P>, P> JsonNode generate(JsonNode defaultValues, JsonNode types, String typeName, NodeBuilder<?, ?, S, TextComponent> parent) {
        ObjectNode jsonNode = (ObjectNode) types.get(typeName);
        String type = jsonNode.get("type").asString();
        switch (type) {
            case "complex":
                ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                ArrayNode fields = (ArrayNode) jsonNode.get("fields");
                for (JsonNode field : fields) {
                    ObjectNode fieldData = (ObjectNode) field;
                    String fieldName = fieldData.get("name").asString();
                    String fieldType = fieldData.get("type").asString();
                    JsonNode fieldDefaultValue = null;
                    if (defaultValues != null) {
                        fieldDefaultValue = defaultValues.get(fieldName);
                    }
                    CallbackLiteralBuilder<S, P> fieldLiteral = new CallbackLiteralBuilder<>("fieldName");
                    JsonNode fieldNode = generate(fieldDefaultValue, types, fieldType, fieldLiteral);
                    fieldLiteral.callback((_, cmd) -> {
                        cmd.storeMetadata("json-node", fieldNode);
                    });
                    parent.then(fieldLiteral);
                    objectNode.set(fieldName, fieldNode);
                }
                return objectNode;
            case "list", "set":
                String elementType = jsonNode.get("element-type").asString();
                ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                DefaultPaperLiteralBuilder<S, P> addLiteral = new DefaultPaperLiteralBuilder<>("add");
                CallbackArgumentBuilder<Integer, S, P> indexArgument = new CallbackArgumentBuilder<Integer, S, P>(
                    "index",
                    new DynamicIntegerParameter<>(
                        (_, _) -> 0,
                        (_, _) -> arrayNode.size()
                    )
                ).callback((_, cmd, index) -> {
                    cmd.storeMetadata("json-node", arrayNode.get(index));
                });
                JsonNode elementDefaultValue = null;
                if (defaultValues != null) {
                    elementDefaultValue = defaultValues.get(0);
                }
                JsonNode elementNode = generate(elementDefaultValue, types, elementType, indexArgument);
                parent.then(addLiteral.run((_) ->
                    arrayNode.add(elementNode.deepCopy())));
                return arrayNode;
            case "map":
                throw new IllegalStateException("Map not implemented");
            case "enum":
                throw new IllegalStateException("Enum not implemented");
            case "int":
                parent.then(new DefaultPaperArgumentBuilder<Integer, S, P>("int", new IntegerParameter<>(Integer.MIN_VALUE, Integer.MAX_VALUE))
                    .runNative((_, cmd) -> {
                        JsonNode node = (JsonNode) cmd.getMetadata("json-node");
                        node.intValue(((Integer) cmd.getArgument("int")));
                    })
                );
                return JsonNodeFactory.instance.numberNode(defaultValues.intValue());
            case "double":
                parent.then(new DefaultPaperArgumentBuilder<Double, S, P>("double", new DoubleParameter<>(Double.MIN_VALUE, Double.MAX_VALUE))
                    .runNative((_, cmd) -> {
                        JsonNode node = (JsonNode) cmd.getMetadata("json-node");
                        node.doubleValue(((Double) cmd.getArgument("double")));
                    })
                );
                return JsonNodeFactory.instance.numberNode(defaultValues.doubleValue());
            case "float":
                parent.then(new DefaultPaperArgumentBuilder<Double, S, P>("float", new DoubleParameter<>(Double.MIN_VALUE, Double.MAX_VALUE))
                    .runNative((_, cmd) -> {
                        JsonNode node = (JsonNode) cmd.getMetadata("json-node");
                        node.floatValue(((Float) cmd.getArgument("float")));
                    })
                );
                return JsonNodeFactory.instance.numberNode(defaultValues.floatValue());
            case "string":
                parent.then(new DefaultPaperArgumentBuilder<String, S, P>("string", new GreedyParameter<>())
                    .runNative((_, cmd) -> {
                        JsonNode node = (JsonNode) cmd.getMetadata("json-node");
                        node.stringValue(((String) cmd.getArgument("string")));
                    })
                );
                return JsonNodeFactory.instance.stringNode(defaultValues.stringValue());
            case "boolean":
                parent.then(new DefaultPaperArgumentBuilder<Boolean, S, P>("bool", new BooleanParameter<>())
                    .runNative((_, cmd) -> {
                        JsonNode node = (JsonNode) cmd.getMetadata("json-node");
                        node.booleanValue(((Boolean) cmd.getArgument("bool")));
                    })
                );
                return JsonNodeFactory.instance.booleanNode(defaultValues.booleanValue());
            default:
                throw new IllegalStateException("Unknown type: " + type);
        }
    }

    private static final List<Type> KNOWN_TYPES = List.of(
    );
    private static void typeArray(ObjectNode types, Type type) {
        String typeName = type.getTypeName();
        if (types.has(typeName)) return;

        ObjectNode listNode = types.putObject(typeName);
        if (type instanceof ParameterizedType pType) {
            if (pType.getRawType().equals(List.class)) {
                listNode.put("type", "list");
                Type elementType = pType.getActualTypeArguments()[0];
                listNode.put("element-type", elementType.getTypeName());
                typeArray(types, elementType);
                return;
            } else if (pType.getRawType().equals(Map.class)) {
                listNode.put("type", "map");
                Type keyType = pType.getActualTypeArguments()[0];
                Type valueType = pType.getActualTypeArguments()[1];
                listNode.put("key-type", keyType.getTypeName());
                listNode.put("value-type", valueType.getTypeName());
                typeArray(types, keyType);
                typeArray(types, valueType);
                return;
            } else if (pType.getRawType().equals(Set.class)) {
                listNode.put("type", "set");
                Type elementType = pType.getActualTypeArguments()[0];
                listNode.put("element-type", elementType.getTypeName());
                typeArray(types, elementType);
                return;
            }
            throw new IllegalStateException("Illegal type " + typeName);
        }
        if (type instanceof Class<?> clazz) {
            if (clazz.isEnum()) {
                listNode.put("type", "enum");
                listNode.put("enum-type", clazz.getTypeName());
                if (!KNOWN_TYPES.contains(clazz)) {
                    ArrayNode enumValues = listNode.putArray("enum-values");
                    for (Object enumConstant : clazz.getEnumConstants()) {
                        enumValues.add(enumConstant.toString());
                    }
                }
                return;
            }
            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                listNode.put("type", "int");
                return;
            }
            if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                listNode.put("type", "double");
                return;
            }
            if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                listNode.put("type", "float");
                return;
            }
            if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                listNode.put("type", "boolean");
                return;
            }
            if (clazz.equals(String.class)) {
                listNode.put("type", "string");
                return;
            }
            listNode.put("type", "complex");
            ArrayNode fieldsNode = listNode.putArray("fields");
            for (Field declaredField : clazz.getDeclaredFields()) {
                ObjectNode fieldNode = fieldsNode.addObject();
                fieldNode.put("name", declaredField.getName());
                fieldNode.put("type", declaredField.getGenericType().getTypeName());
                Min min = declaredField.getAnnotation(Min.class);
                if (min != null) {
                    fieldNode.put("min", min.value());
                }
                Max max = declaredField.getAnnotation(Max.class);
                if (max != null) {
                    fieldNode.put("max", max.value());
                }
                typeArray(types, declaredField.getGenericType());
            }
        }
    }

}
