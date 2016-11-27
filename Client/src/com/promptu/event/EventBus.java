package com.promptu.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EventBus {

    private static EventBus instance;
    public static EventBus getInstance() {
        if (instance == null) instance = new EventBus();
        return instance;
    }

    protected Thread eventThread;
    protected List<Runnable> eventTasks;
    private HashMap<Class<? extends AbstractEvent>, List<AbstractMap.SimpleEntry<Object, Method>>> eventSubscribers;

    private EventBus() {
        this.eventSubscribers = new HashMap<>();
        eventTasks = new ArrayList<>();
        eventThread = new Thread(this::eventLoop, "Event Bus");
        eventThread.setDaemon(true);
        eventThread.start();
    }

    protected void postTask(Runnable task) {
        eventTasks.add(task);
    }

    public void postImmediate(final Object source, final AbstractEvent event) {
        fireRelatedEvents(source, event);
    }

    private void addSubscriber(Class<? extends AbstractEvent> event, Object subscriber, Method method) {
        if(!eventSubscribers.containsKey(event))
            eventSubscribers.put(event, new ArrayList<>());
        eventSubscribers.get(event).add(new AbstractMap.SimpleEntry<>(subscriber, method));
    }

    public void register(final Object subscriber) {
        postTask(() -> {
            for(Method method : subscriber.getClass().getDeclaredMethods()) {
                if(isSubscriber(method)) {
                    Class<?> param0 = method.getParameterTypes()[1];
                    if(AbstractEvent.class.isAssignableFrom(param0))
                        addSubscriber((Class<? extends AbstractEvent>) param0, subscriber, method);
                }
            }
        });
    }

    public void unregister(final Object subscriber) {
        postTask(() -> eventSubscribers.entrySet()
                .forEach(entry -> {
                    for (AbstractMap.SimpleEntry<Object, Method> e : entry.getValue().stream().collect(Collectors.toList())) {
                        if(e.getKey().equals(subscriber))
                            entry.getValue().remove(subscriber);
                    }
                }));
    }

    public void post(final Object source, final AbstractEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.printf("[%s] Firing event %s [%s] from %s [%s]\n", dateFormat.format(new Date()), event.getClass().getSimpleName(), event.toString(), source.getClass().getSimpleName(), source.toString());
        postTask(() -> fireRelatedEvents(source, event));
    }

    private void fireRelatedEvents(final Object src, final AbstractEvent event) {
        final Class eventType = event.getClass();
        if(eventSubscribers.containsKey(eventType)) {
            eventSubscribers.get(eventType).forEach(entry -> {
                try{
                    entry.getValue().invoke(entry.getKey(), src, event);
                }catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private boolean isSubscriber(Method method) {
        if(method.getParameterCount() < 2) return false;
        boolean isSub = method.isAnnotationPresent(Subscribe.class);
        if(isSub) return true;

        Class[] interfaces = method.getDeclaringClass().getInterfaces();
        for(Class i : interfaces) {
            try{
                Method interfaceMethod = i.getMethod(method.getName(), method.getParameterTypes());
                if(interfaceMethod != null) {
                    isSub = interfaceMethod.isAnnotationPresent(Subscribe.class);
                    if(isSub) return true;
                }
            }catch (NoSuchMethodException nsme) {

            }
        }
        return false;
    }

    protected Runnable getEventTask() {
        if(eventTasks.size() <= 0) return null;
        return eventTasks.remove(0);
    }

    protected void eventLoop() {
        while(true) {
            Runnable task = getEventTask();
            if (task == null) try { Thread.sleep(100); } catch (InterruptedException e) {}
            else task.run();
        }
    }


}