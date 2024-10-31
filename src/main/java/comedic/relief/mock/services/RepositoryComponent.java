package comedic.relief.mock.services;

import comedic.relief.framework.annotations.injection.Component;
import comedic.relief.framework.annotations.injection.Qualifier;

@Component
@Qualifier("RepositoryComponent")
public class RepositoryComponent implements RepositoryInterface{

    public RepositoryComponent() {
    }
}
