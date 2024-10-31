package comedic.relief.mock.controllers;

import comedic.relief.framework.annotations.request.Controller;
import comedic.relief.framework.annotations.request.Get;
import comedic.relief.framework.annotations.request.Path;
import comedic.relief.framework.annotations.request.Post;

@Controller
public class BasicContoller {

    @Path(path = "/test1")
    @Get
    public void test1(){}

    @Path(path = "/test2")
    @Get
    public void test2(){}


    @Path(path = "/test3")
    @Post
    public void test3(){}

}
