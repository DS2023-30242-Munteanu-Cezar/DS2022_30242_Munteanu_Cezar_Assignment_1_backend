package MunteanuCezar.SD1.services;

import MunteanuCezar.SD1.dtos.MeasurementDTO;
import MunteanuCezar.SD1.entities.Device;
import MunteanuCezar.SD1.entities.Measurement;
import MunteanuCezar.SD1.rabbitMQ.Worker;
import MunteanuCezar.SD1.rabbitMQ.entity.Measure;
import MunteanuCezar.SD1.repositories.DeviceRepository;
import MunteanuCezar.SD1.repositories.MeasurementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeasurementService {
    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, DeviceRepository deviceRepository){
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
    }

    public Device addMeasurementFromRabbitMQ(Measure measure){
        Optional<Device> device = deviceRepository.findById(measure.getId());

        if(!device.isPresent()){
            log.error("Device with id " + measure.getId() + " not found in db!");
        }

        Measurement measurement = new Measurement();
        measurement.setTimestamp(measure.getTimestamp());
        measurement.setEnergyConsumption((int) measure.getConsumption());
        measurement.setDevice(device.get());
        measurementRepository.save(measurement);
        log.info("Measurement inserted in database!");

        return device.get();
    }

    public Measurement dtoToMeasurement(MeasurementDTO measurementDTO){

        Optional<Device> device = deviceRepository.findById(measurementDTO.getDeviceId());
        if (!device.isPresent()){
            log.error("Wrong device id for measurement " + measurementDTO.getId());
        }

        Measurement measurement = Measurement.builder()
                .energyConsumption(measurementDTO.getEnergyConsumption())
                .timestamp(measurementDTO.getTimestamp())
                .device(device.get())
                .build();

        return  measurement;
    }

    public MeasurementDTO measurementToDto(Measurement measurement){
        MeasurementDTO measurementDTO = MeasurementDTO.builder()
                .id(measurement.getId())
                .energyConsumption(measurement.getEnergyConsumption())
                .timestamp(measurement.getTimestamp())
                .deviceId(measurement.getDevice().getId())
                .build();
        return measurementDTO;
    }

    public List<MeasurementDTO> findAll(){
        List<Measurement> measurements = measurementRepository.findAll();
        List<MeasurementDTO> measurementDTOS =measurements.stream().map(measurement -> measurementToDto(measurement)).collect(Collectors.toList());
        log.info("Show all measurements!");
        return measurementDTOS;
    }

    public UUID insert(MeasurementDTO measurementDTO){
        Measurement measurement = dtoToMeasurement(measurementDTO);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        measurement.setTimestamp(timestamp);
        return measurementRepository.save(measurement).getId();
    }

    public void delete(UUID id){
        Optional<Measurement> measurement = measurementRepository.findById(id);

        if(measurement.isPresent()){
            log.info("Measurement " + id + " deleted!");
            measurementRepository.delete(measurement.get());
        }
        else {
            log.error("Measurement " + id + " not found for delete!");
        }
    }

    public void update(MeasurementDTO measurementDTO){
        Optional<Measurement> measurement = measurementRepository.findById(measurementDTO.getId());
        Measurement myMeasurement;

        if(measurement.isPresent()){
            myMeasurement= measurement.get();
            log.info("Measurement find for update!");
        }
        else{
            log.error("Measurement not found for update!");
            return;
        }

        if(measurementDTO.getEnergyConsumption() != myMeasurement.getEnergyConsumption()){
            myMeasurement.setEnergyConsumption(measurementDTO.getEnergyConsumption());
        }
        if(measurementDTO.getDeviceId() != myMeasurement.getDevice().getId()) {
            myMeasurement.setDevice(deviceRepository.findById(measurementDTO.getDeviceId()).get());
        }

        measurementRepository.save(myMeasurement);
    }
}
