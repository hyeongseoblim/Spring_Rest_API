package com.inflearn.study.index;

import com.inflearn.study.event.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {
    /*
    ResourceSupport changed to RepresentationModel
    Resource changed to EntityModel
    Resources changed to CollectionModel
    PagedResources changed to PagedModel
    ResourceAssembler changed to RepresentationModelAssembler
    */


    @GetMapping("/api")
    public RepresentationModel index() {
        var index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
