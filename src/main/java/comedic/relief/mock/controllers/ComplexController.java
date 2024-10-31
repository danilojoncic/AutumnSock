package comedic.relief.mock.controllers;

import comedic.relief.framework.annotations.injection.Autowired;
import comedic.relief.framework.annotations.injection.Qualifier;
import comedic.relief.framework.annotations.request.Controller;
import comedic.relief.framework.annotations.request.Get;
import comedic.relief.framework.annotations.request.Path;
import comedic.relief.mock.services.Service;
import comedic.relief.mock.services.ServiceInteface;

@Controller
public class ComplexController {

    @Autowired(verbose = true)
    public Service service;

    @Autowired(verbose = true)
    @Qualifier("ImplementedService")
    public ServiceInteface implementedInterface;



    @Path(path = "/complexTest")
    @Get
    public void complexTest(){
    }


    @Path(path = "/test6")
    @Get
    public void test6(){
    }




}
