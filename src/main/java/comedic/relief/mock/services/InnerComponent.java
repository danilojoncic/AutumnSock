package comedic.relief.mock.services;

import comedic.relief.framework.annotations.injection.Autowired;
import comedic.relief.framework.annotations.injection.Component;
import comedic.relief.framework.annotations.injection.Qualifier;

@Component
@Qualifier("InnerComponent")
public class InnerComponent {
    @Autowired(verbose = true)
    public InnerInnerComponent innerInnerComponent;

    public InnerComponent() {
    }
}
