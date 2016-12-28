package com.promptu.script;

import com.promptu.event.events.RegisterNashornDomainsEvent;
import com.promptu.script.functions.ConfigFunctions;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Guy on 28/12/2016.
 */
public class ScriptEngine {

    private static ScriptEngine instance;

    public static ScriptEngine getInstance() {
        if (instance == null)
            instance = new ScriptEngine();
        return instance;
    }

    final javax.script.ScriptEngine engine;
    private String scriptPrefix;

    private Set<Class<?>> functionDomains;

    private ScriptEngine() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        functionDomains = new LinkedHashSet<>();
        registerDomains();
    }

    private void registerDomains() {
        functionDomains.add(ConfigFunctions.class);
        new RegisterNashornDomainsEvent(functionDomains).fireImmediate(this);
    }

    public Set<Class<?>> getFunctionDomains() {
        return functionDomains;
    }

    public String getScriptPrefix() {
        if (scriptPrefix == null) {
            final StringBuilder sb = new StringBuilder();
            functionDomains.forEach(cls -> {
                sb.append("var ")
                        .append(cls.getSimpleName())
                        .append(" = Java.type('")
                        .append(cls.getCanonicalName())
                        .append("');\n");
            });
            scriptPrefix = sb.toString();
        }
        return scriptPrefix;
    }

    public Object execute(String script) throws ScriptException {
        script = getScriptPrefix() + script;
        return engine.eval(script);
    }

    public Object executePreface(String script) {
        try{
            return execute(script);
        }catch (Exception e) {
            return query(script);
        }
    }

    public String query(String q) {
        q = sanitize(q);
        final StringBuilder sb = new StringBuilder();
        if(q.equalsIgnoreCase("?")) {
            sb.append("Domains: \n");
            functionDomains.forEach(cls -> sb.append("\t")
                    .append(cls.getSimpleName())
                    .append("\n"));
        }else if(q.endsWith("?")) {
            String s = firstWord(q);
            sb.append(s).append(": \n");
            functionDomains.stream()
                    .filter(cls -> cls.getSimpleName().equalsIgnoreCase(s))
                    .forEach(cls -> getValidMethodsFromDomain(cls)
                            .forEach(method -> appendMethodData("\t", sb, method)));
        }
        return sb.toString();
    }

    public List<Method> getValidMethodsFromDomain(Class<?> cls) {
        List<Method> m = new ArrayList<>();
        for (Method method : cls.getDeclaredMethods()) {
            if(Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                m.add(method);
            }
        }
        return m;
    }

    private void appendMethodData(String linePrefix, final StringBuilder sb, Method m) {
        sb.append(linePrefix);
        sb.append(m.getName());
        sb.append(" - Returns ").append(m.getReturnType());
        sb.append(" | (");
        for (int i = 0; i < m.getParameterCount(); i++) {
            Class<?> cls = m.getParameterTypes()[i];
            sb.append(cls.getSimpleName());
            if(i < m.getParameterCount()-1) {
                sb.append(", ");
            }
        }

        sb.append(")");

        if(m.isAnnotationPresent(ScriptFunctionDescription.class)) {
            ScriptFunctionDescription desc = m.getAnnotation(ScriptFunctionDescription.class);
            sb.append(" - ").append(desc.value());
        }

        sb.append("\n");
    }

    private String sanitize(String q) {
        q = q.trim();
        return q;
    }

    private String firstWord(String q) {
        return q.split(" ")[0];
    }

}
