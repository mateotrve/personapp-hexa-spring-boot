package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {
    
    @Autowired
    private ProfesionInputAdapterRest profesionInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfesionResponse> profesiones(@PathVariable String database) {
        log.info("Into profesion REST API");
        return profesionInputAdapterRest.historial(database.toUpperCase());
    }

    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfesionResponse crearProfesion(@RequestBody ProfesionRequest request) {
        log.info("esta en el metodo crearProfesion en el controller del api");
        return profesionInputAdapterRest.crearProfesion(request);
    }

    @ResponseBody
    @GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfesionResponse obtenerProfesion(@PathVariable String database, @PathVariable Integer id) {
        log.info("esta en el metodo obtenerProfesion en el controller del api");
        return profesionInputAdapterRest.obtenerProfesion(database.toUpperCase(), id);
    }

    @ResponseBody
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfesionResponse editarProfesion(@RequestBody ProfesionRequest request) {
        log.info("esta en el metodo editarProfesion en el controller del api");
        return profesionInputAdapterRest.editarProfesion(request);
    }

    @ResponseBody
    @DeleteMapping(path = "/{database}/{id}")
    public Boolean eliminarProfesion(@PathVariable String database, @PathVariable Integer id) {
        log.info("esta en el metodo eliminarProfesion en el controller del api");
        return profesionInputAdapterRest.eliminarProfesion(database, id);
    }
    
}
