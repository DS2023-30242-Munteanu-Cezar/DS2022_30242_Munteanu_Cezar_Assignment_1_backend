package MunteanuCezar.SD1.controllers;

import MunteanuCezar.SD1.dtos.DeviceDTO;
import MunteanuCezar.SD1.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @GetMapping()
    public ResponseEntity<?> getDevices(){
        return new ResponseEntity<>(deviceService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO){
        return new ResponseEntity<>(deviceService.insert(deviceDTO), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDevice(@Valid @RequestBody DeviceDTO deviceDTO){
        deviceService.update(deviceDTO);
        return new ResponseEntity<>("Device updated!", HttpStatus.OK );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") UUID id){
        deviceService.delete(id);
        return new ResponseEntity<>("Deleted device: " + id, HttpStatus.OK);
    }
}
