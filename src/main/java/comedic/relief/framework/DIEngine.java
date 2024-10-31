package comedic.relief.framework;

import comedic.relief.framework.annotations.injection.Autowired;
import comedic.relief.framework.annotations.injection.Qualifier;
import comedic.relief.framework.exceptions.AutowiredException;
import comedic.relief.framework.exceptions.InterfaceQualifierException;
import comedic.relief.framework.exceptions.RegistrationException;
import comedic.relief.framework.annotations.injection.Bean;
import comedic.relief.framework.annotations.injection.Service;
import comedic.relief.framework.annotations.injection.Component;
import comedic.relief.framework.eye_candy.ColorPicker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DIEngine {
    private Map<String, Object> controllerSingletons;
    private DIContainer diContainer;
    private static DIEngine instance;

    private DIEngine() {
    }

    public static DIEngine getInstance() {
        if (instance == null) {
            instance = new DIEngine();
            instance.controllerSingletons = new HashMap<>();
            instance.diContainer = new DIContainer();
            instance.diContainer.mapQualifiers();
        }
        return instance;
    }

    public void initDependencies(String controllerName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RegistrationException, InterfaceQualifierException, AutowiredException {
        Class clas = Class.forName(controllerName);
        Object controller = this.returnInstance(clas);
        this.controllerSingletons.put(controller.getClass().getName(), controller);
        System.out.println("HASHCODE:" + Objects.requireNonNull(controller).hashCode());
        Field[] controllerFields = clas.getFields();
        recursiveInit(controller, controllerFields);
    }

    private void recursiveInit(Object parentObj, Field[] fields) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, AutowiredException, InterfaceQualifierException, RegistrationException {
        System.out.println("POZIV REKURZIVNE INICIJALIZACIJE");
        System.out.println("BROJ POLJA za objekat :"+ parentObj.getClass().getName()+ " "  + fields.length);
        for (Field field : fields) {
            System.out.println("POLJE: " + field);

            if (field.isAnnotationPresent(Autowired.class)) {
                Object obj = null;
                Class cl = null;
                Constructor constructor;

                // Handle Qualifier for interface injection
                if (field.getType().isInterface()) {
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);
                    if (qualifier != null) {
                        cl = this.diContainer.returnImplementation(qualifier.value());
                    } else {
                        throw new InterfaceQualifierException("An interface attribute is missing a @Qualifier annotation.");
                    }
                } else {
                    cl = Class.forName(field.toString().split(" ")[1]);
                }

                // Instantiate object based on scope
                constructor = Objects.requireNonNull(cl).getDeclaredConstructor();

                if (cl.isAnnotationPresent(Bean.class)) {
                    Bean bean = (Bean) cl.getAnnotation(Bean.class);
                    if (bean.scope().equals("singleton")) {
                        obj = this.returnInstance(cl);
                        this.controllerSingletons.put(obj.getClass().getName(), obj);
                    } else if (bean.scope().equals("prototype")) {
                        obj = constructor.newInstance();
                    }
                } else if (cl.isAnnotationPresent(Service.class)) {
                    obj = this.returnInstance(cl);
                    this.controllerSingletons.put(obj.getClass().getName(), obj);
                } else if (cl.isAnnotationPresent(Component.class)) {
                    obj = constructor.newInstance();
                } else {
                    throw new AutowiredException("The @Autowired annotation is used on an invalid attribute.");
                }

                field.setAccessible(true);
                field.set(parentObj, obj);

                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired.verbose()) {
                    System.out.println(ColorPicker.ANSI_PURPLE + "Initialized " + field.getType() + " " + field.getName() + " in " + parentObj.getClass().getName() +
                            " on " + LocalDateTime.now() + " with hash code " + Objects.requireNonNull(obj).hashCode() + ColorPicker.ANSI_RESET);
                }
                recursiveInit(obj, cl.getDeclaredFields());
            }
        }
    }

    public Object returnInstance(Class clas) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (controllerSingletons.containsKey(clas.getName())) {
            return controllerSingletons.get(clas.getName());
        } else {
            Constructor constructor = clas.getDeclaredConstructor();
            return constructor.newInstance();
        }
    }
}
