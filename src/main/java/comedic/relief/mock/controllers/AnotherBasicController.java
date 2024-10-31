package comedic.relief.mock.controllers;

import comedic.relief.framework.annotations.request.Controller;
import comedic.relief.framework.annotations.request.Get;
import comedic.relief.framework.annotations.request.Path;

@Controller
public class AnotherBasicController {

    @Path(path = "/another")
    @Get
    public void testOther(){

    }

}
