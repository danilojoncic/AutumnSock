package comedic.relief.framework;
import comedic.relief.framework.eye_candy.ColorPicker;
@SuppressWarnings("rawtypes")
public class Route {
    private String route;
    private String requestMethod;
    private Class controller;
    private String excecutionMethod;

    public Route(String route, String requestMethod, Class controller, String excecutionMethod) {
        this.route = route;
        this.requestMethod = requestMethod;
        this.controller = controller;
        this.excecutionMethod = excecutionMethod;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Class getController() {
        return controller;
    }

    public void setController(Class controller) {
        this.controller = controller;
    }

    public String getExcecutionMethod() {
        return excecutionMethod;
    }

    public void setExcecutionMethod(String excecutionMethod) {
        this.excecutionMethod = excecutionMethod;
    }

    @Override
    public String toString() {
        //trebalo bi da farba kako treba
        return ColorPicker.ANSI_RED+"["+route+"]"+ColorPicker.ANSI_RESET +" AVAILABLE: "+ ColorPicker.ANSI_BLUE +"["+requestMethod+"]" + ColorPicker.ANSI_RESET + " CONTROLLER CLASS: "+ ColorPicker.ANSI_YELLOW + "["+ controller+"] JAVA METHOD : ["+excecutionMethod+"]" + ColorPicker.ANSI_RESET;
    }
}
