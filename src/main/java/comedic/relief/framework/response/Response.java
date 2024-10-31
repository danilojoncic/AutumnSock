package comedic.relief.framework.response;


import comedic.relief.framework.request.Header;

public abstract class Response {
    protected Header header;

    public Response() {
        this.header = new Header();
    }

    public abstract String render();
}
