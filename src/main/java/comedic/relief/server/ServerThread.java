package comedic.relief.server;


import comedic.relief.framework.DIEngine;
import comedic.relief.framework.Discoverer;
import comedic.relief.framework.Route;
import comedic.relief.framework.exceptions.AutowiredException;
import comedic.relief.framework.exceptions.InterfaceQualifierException;
import comedic.relief.framework.exceptions.RegistrationException;
import comedic.relief.framework.request.Header;
import comedic.relief.framework.request.Helper;
import comedic.relief.framework.request.Request;
import comedic.relief.framework.request.enums.Method;
import comedic.relief.framework.request.exceptions.RequestNotValidException;
import comedic.relief.framework.response.JsonResponse;
import comedic.relief.framework.response.Response;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Discoverer discoverer;
    private DIEngine diEngine;

    public ServerThread(Socket socket){
        this.socket = socket;
        discoverer = Discoverer.getInstance();
        diEngine = DIEngine.getInstance();

        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {

            Request request = this.generateRequest();
            if (request == null) {
                in.close();
                out.close();
                socket.close();
                return;
            }

            for (Route route : discoverer.getAllAvailableRoutes()) {
                if (route.getRoute().equals(request.getLocation())
                        && route.getRequestMethod().equals(request.getMethod().toString())) {
                    String className = route.getController().getName();
                    diEngine.initDependencies(className);
                    break;
                }
            }


            // Response example
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("route_location", request.getLocation());
            responseMap.put("route_method", request.getMethod().toString());
            responseMap.put("parameters", request.getParameters());
            Response response = new JsonResponse(responseMap);

            out.println(response.render());

            in.close();
            out.close();
            socket.close();

        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        } catch (InterfaceQualifierException e) {
            throw new RuntimeException(e);
        } catch (RequestNotValidException e) {
            throw new RuntimeException(e);
        } catch (AutowiredException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Request generateRequest() throws IOException, RequestNotValidException {
        String command = in.readLine();
        if(command == null) {
            return null;
        }

        String[] actionRow = command.split(" ");
        Method method = Method.valueOf(actionRow[0]);
        String route = actionRow[1];
        Header header = new Header();
        HashMap<String, String> parameters = Helper.getParametersFromRoute(route);

        do {
            command = in.readLine();
            String[] headerRow = command.split(": ");
            if(headerRow.length == 2) {
                header.add(headerRow[0], headerRow[1]);
            }
        } while(!command.trim().equals(""));

        if(method.equals(Method.POST)) {
            int contentLength = Integer.parseInt(header.get("content-length"));
            char[] buff = new char[contentLength];
            in.read(buff, 0, contentLength);
            String parametersString = new String(buff);

            HashMap<String, String> postParameters = Helper.getParametersFromString(parametersString);
            for (String parameterName : postParameters.keySet()) {
                parameters.put(parameterName, postParameters.get(parameterName));
            }
        }

        Request request = new Request(method, route, header, parameters);

        return request;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public Discoverer getDiscoverer() {
        return discoverer;
    }

    public void setDiscoverer(Discoverer discoverer) {
        this.discoverer = discoverer;
    }
}
