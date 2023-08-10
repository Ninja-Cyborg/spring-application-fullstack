package com.spring.patron;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patrons")
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService){
        this.patronService = patronService;
    }

    @GetMapping
    public List<Patron> getPatrons(){
        return patronService.getAllPatrons();
    }

    @GetMapping("{id}")
    public Patron getPatron(@PathVariable("id") Integer id){
         return patronService.getPatron(id);
    }

    @PostMapping
    public void registerPatron(@RequestBody
                             PatronRegistrationRequest request){
        patronService.addPatron(request);
    }

    @DeleteMapping("{id}")
    public void deletePatron(@PathVariable("id") Integer id){
        patronService.deletePatronById(id);
    }

    @PutMapping("{id}")
    public void updatePatron(@PathVariable("id") Integer id,
                           @RequestBody PatronUpdateRequest request){

        patronService.updatePatron(id, request);
    }
}
