package com.pm.alexki.patientservice.controller;

import com.pm.alexki.patientservice.dto.PatientRequestDto;
import com.pm.alexki.patientservice.dto.PatientResponseDto;
import com.pm.alexki.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String uuidPatternMessage = "The provided ID must be a valid UUID format";

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        var patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            log.info("PatientController: GetPatient(): No patient found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {
        PatientResponseDto newPatient = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok().body(newPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable
                                                            @Pattern(regexp = uuidPattern, message = uuidPatternMessage)
                                                            UUID id,
                                                            @RequestBody PatientRequestDto requestDto) {

        var updatedPatient = patientService.updatePatient(id, requestDto);
        return ResponseEntity.ok().body(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable
                              @Pattern(regexp = uuidPattern, message = uuidPatternMessage)
                              UUID id) {
        patientService.deletePatient(id);
        
        return ResponseEntity.noContent().build();
    }

}
