package spring_7.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import spring_7.domain.Taco;


public class TacoResourceAssembler extends RepresentationModelAssemblerSupport<Taco, TacoResource> {

    public TacoResourceAssembler() {
        super(DesignTacoController.class, TacoResource.class);
    }

    @Override
    public TacoResource toModel(Taco taco) {
        return createModelWithId(taco.getId(), taco);
    }
}
