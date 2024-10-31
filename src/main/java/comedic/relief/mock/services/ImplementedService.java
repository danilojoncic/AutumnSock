package comedic.relief.mock.services;

import comedic.relief.framework.annotations.injection.Qualifier;
import comedic.relief.framework.annotations.injection.Service;

@Service
@Qualifier("ImplementedService")
public class ImplementedService implements ServiceInteface{

    public ImplementedService() {
    }
}
