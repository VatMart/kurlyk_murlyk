package temp;
import io.netty.handler.codec.http.HttpRequest;

@Mapped(uri = "/")
public class UriHandlerSample extends UriHandlerBased {
 
    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        buff.append("HELLO HANDLER1!");
    }
}