package MunteanuCezar.SD1.controllers;

import MunteanuCezar.SD1.dtos.MeasurementDTO;
import MunteanuCezar.SD1.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/measurements")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

    @GetMapping()
    public ResponseEntity<?> getMeasurements(){
        return new ResponseEntity<>(measurementService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO){
        return new ResponseEntity<>(measurementService.insert(measurementDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMeasurement(@PathVariable("id") UUID id){
        measurementService.delete(id);
        return new ResponseEntity<>("Deleted measurement: " + id, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO){
        measurementService.update(measurementDTO);
        return new ResponseEntity<>("Measurement updated!", HttpStatus.OK);
    }
}
