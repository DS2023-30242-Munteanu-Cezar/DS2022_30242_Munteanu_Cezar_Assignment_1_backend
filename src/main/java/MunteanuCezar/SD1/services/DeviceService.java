package MunteanuCezar.SD1.services;


import MunteanuCezar.SD1.dtos.DeviceDTO;
import MunteanuCezar.SD1.dtos.MeasurementDTO;
import MunteanuCezar.SD1.entities.Device;
import MunteanuCezar.SD1.entities.Measurement;
import MunteanuCezar.SD1.repositories.DeviceRepository;
import MunteanuCezar.SD1.repositories.MeasurementRepository;
import MunteanuCezar.SD1.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    public Device dtoToDevice(DeviceDTO deviceDTO){
        List<Optional<Measurement>> measurements= new ArrayList<>();
        deviceDTO.getMeasurementIds().stream().map(measurement -> measurements.add(measurementRepository.findById(measurement)));
        List<Measurement> measurementList = null;
        for(Optional<Measurement> m : measurements){
            if(m.isPresent()){
                measurementList.add(m.get());
            }
        }
        Device device = Device.builder()
                .user(userRepository.findByUsername(deviceDTO.getUserUsername()))
                .energyConsumption(deviceDTO.getEnergyConsumption())
                .adress(deviceDTO.getAdress())
                .description(deviceDTO.getDescription())
                .measurementList(measurementList)
                .build();

        return device;
    }

    public DeviceDTO deviceToDto(Device device){
        List<UUID> measurementsId = new ArrayList<>();
        if(device.getMeasurementList() == null){
            measurementsId.add(new UUID(5,5));
        }
        else {
            for(Measurement m:device.getMeasurementList()){
                measurementsId.add(m.getId());
            }
        }

        DeviceDTO deviceDTO = DeviceDTO.builder()
                .id(device.getId())
                .userUsername(device.getUser().getUsername())
                .energyConsumption(device.getEnergyConsumption())
                .adress(device.getAdress())
                .description(device.getDescription())
                .measurementIds(measurementsId)
                .build();

        return deviceDTO;
    }


    public List<DeviceDTO> findAll(){
        List<Device> devices = deviceRepository.findAll();
        List<DeviceDTO> deviceDTOS = devices.stream().map(device -> deviceToDto(device)).collect(Collectors.toList());
        log.info("Show all devices!");
        return deviceDTOS;
    }

    public UUID insert(DeviceDTO deviceDTO){
        Device device = dtoToDevice(deviceDTO);
        log.info("Device " + device.getId() + " inserted!");
        return deviceRepository.save(device).getId();
    }

    public void delete(UUID id){
        Optional<Device> device = deviceRepository.findById(id);
        if(device.isPresent()){
            log.info("Device " + id + " deleted!" );
            deviceRepository.delete(device.get());
        }
        else {
            log.error("Device with id: " + id + " not found for delete!");
        }
    }

    public void update(DeviceDTO deviceDTO){
        Optional<Device> device = deviceRepository.findById(deviceDTO.getId());
        Device myDevice;
        if(device.isPresent()){
            log.info("Found device to update!");
            myDevice = device.get();
        }
        else {
            log.error("Device not found for update!");
            return;
        }

        if(!myDevice.getDescription().isEmpty()){
            myDevice.setDescription(deviceDTO.getDescription());
        }
        if(myDevice.getEnergyConsumption() != deviceDTO.getEnergyConsumption()){
            myDevice.setEnergyConsumption(deviceDTO.getEnergyConsumption());
        }
        if(myDevice.getAdress() != deviceDTO.getAdress()){
            myDevice.setAdress(deviceDTO.getAdress());
        }
        if(myDevice.getUser().getUsername() != deviceDTO.getUserUsername()){
            myDevice.setUser(userRepository.findByUsername(deviceDTO.getUserUsername()));
        }

        deviceRepository.save(myDevice);
    }
}
