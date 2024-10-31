package comedic.relief.framework;

import comedic.relief.framework.annotations.injection.Qualifier;
import comedic.relief.framework.exceptions.QualifierNotUniqueException;
import comedic.relief.framework.exceptions.RegistrationException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DIContainer {
    private Map<String,Class> mapOfSpecificationsAndTheirImplementations;

    public DIContainer() {
        mapOfSpecificationsAndTheirImplementations = new HashMap<>();
    }

    public Class returnImplementation(String qualifier) throws RegistrationException {
        if(mapOfSpecificationsAndTheirImplementations.containsKey(qualifier))return mapOfSpecificationsAndTheirImplementations.get(qualifier);
        else throw new RegistrationException("Qualifier is not present in Dependency Container!");
    }

    public Set<Class> findAllClasses(String packageName){
        System.out.println("FIND ALL CLASSES CALLED FROM DI CONTAINER");
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false), new TypeAnnotationsScanner());
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

    public void mapQualifiers(){
        Set<Class> classes = this.findAllClasses("comedic.relief.mock.services");
        System.out.println("NUMBER OF CLASS FROM CONTAINER " + classes.size());
        classes.forEach(clas ->{
            if(clas.getAnnotation(Qualifier.class) != null){
                Qualifier qualifier = (Qualifier) clas.getAnnotation(Qualifier.class);
                if(mapOfSpecificationsAndTheirImplementations.containsKey(qualifier.value())){
                    try {
                        throw new QualifierNotUniqueException("Value of Qualifier annotation must be unique");
                    } catch (QualifierNotUniqueException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    mapOfSpecificationsAndTheirImplementations.put(qualifier.value(),clas);
                }
            }
        });
        System.out.println(mapOfSpecificationsAndTheirImplementations.toString());

    }
}
