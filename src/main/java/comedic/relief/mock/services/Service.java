package comedic.relief.mock.services;

import comedic.relief.framework.annotations.injection.Autowired;
import comedic.relief.framework.annotations.injection.Bean;
import comedic.relief.framework.annotations.injection.*;

@Bean
@Qualifier("Service")
public class Service implements ServiceInteface{
    @Autowired(verbose = true)
    private RepositoryComponent repositoryComponent;

    @Autowired(verbose = true)
    public InnerComponent innerComponent;

    public Service() {}
}
