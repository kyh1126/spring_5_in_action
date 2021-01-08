package spring.rest;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import spring.domain.Taco;

public class TacoModelAssembler extends RepresentationModelAssemblerSupport<Taco, TacoModel> {

    public TacoModelAssembler() {
        super(DesignTacoController.class, TacoModel.class);
    }

    @Override
    protected TacoModel instantiateModel(Taco taco) {
        return new TacoModel(taco);
    }

    @Override
    public TacoModel toModel(Taco taco) {
        return createModelWithId(taco.getId(), taco);
    }
}
