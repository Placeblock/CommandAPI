package de.codelix.commandapi.core;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
    private final List<Node> nodes = new ArrayList<>();
    private final Map<Parameter, Object> parameters = new HashMap<>();

    public void storeParam(Parameter parameter, Object value) {
        this.parameters.put(parameter, value);
    }

    public void invokeWithParameters(Object obj, Method method) throws InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            java.lang.reflect.Parameter parameter = parameters[i];
            Param annotation = parameter.getAnnotation(Param.class);
            if (annotation != null) {
                String name = annotation.name();
                params[i] = this.getParameter(name);
            } else {
                params[i] = null;
            }
        }
        method.invoke(obj, params);
    }

    public Object getParameter(String name) {
        for (Map.Entry<Parameter, Object> parameterEntry : this.parameters.entrySet()) {
            if (parameterEntry.getKey().getName().equals(name)) {
                return this.parameters.get(parameterEntry.getKey());
            }
        }
        return null;
    }
}
