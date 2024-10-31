package comedic.relief.framework;

import comedic.relief.framework.annotations.request.Controller;
import comedic.relief.framework.annotations.request.Get;
import comedic.relief.framework.annotations.request.Path;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"rawtypes","unchecked"})
public class Discoverer {
    private ArrayList<Route> allAvailableRoutes;
    private static Discoverer instance;
    //hajde da ne bude toliko hardkodovano nego bi se moglo preko setter postaviti kasnije



    private Discoverer(){
    }


    public static Discoverer getInstance(){
        if(instance == null) {
            instance = new Discoverer();
            instance.allAvailableRoutes = new ArrayList<>();
            instance.mapRoutes();
        }
        return instance;
    }

    private Set<Class> findAllClasses(String pckName){
        System.out.println("FIND ALL CLASSES");
        //"aktiviraj" refleksiju
        Reflections reflections = new Reflections(pckName,new SubTypesScanner(false));
        //vrati mi skup svih klasa iz tog paketa
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

    private void mapRoutes(){
        System.out.println("MAP ALL ROUTES");
        //nadji sve klase
        Set<Class> classes = instance.findAllClasses("comedic.relief.mock.controllers");
        System.out.println(classes.size());
        //prodji kroz sve klase
        for(Class c : classes){

            //ako ima anotaciju controller hvatamo ostale vrijednosti iz anotacija
            if(c.isAnnotationPresent(Controller.class)){
                //uzmi sve metode i radi projvere nad njima da nadjemo ostale anotacije
                Method[] allMethods = c.getMethods();

                for (Method method : allMethods) {
                    if(method.isAnnotationPresent(Path.class)){
                        String httpMethod;
                        Path path = method.getAnnotation(Path.class);
                        if(method.isAnnotationPresent(Get.class)){
                            httpMethod = "GET";
                        }else{
                            httpMethod = "POST";
                        }
                        Route route = new Route(path.path(),httpMethod,c,method.getName());
                        instance.allAvailableRoutes.add(route);
                    }
                }

            }
        }
        for(Route route: allAvailableRoutes){
            System.out.println(route);
        }
    }

    public ArrayList<Route> getAllAvailableRoutes() {
        return allAvailableRoutes;
    }

    public void setAllAvailableRoutes(ArrayList<Route> allAvailableRoutes) {
        this.allAvailableRoutes = allAvailableRoutes;
    }

    public static void setInstance(Discoverer instance) {
        Discoverer.instance = instance;
    }
}
