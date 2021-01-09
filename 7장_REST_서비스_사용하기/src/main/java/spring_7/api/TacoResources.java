package spring_7.api;

import org.springframework.hateoas.CollectionModel;

import java.util.List;

public class TacoResources extends CollectionModel<TacoResource> {
    public TacoResources(List<TacoResource> tacoCollectionModel) {
        super(tacoCollectionModel);
    }
}