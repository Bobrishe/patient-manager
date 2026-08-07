package com.pm.alexki.patientservice.service;

import com.pm.alexki.patientservice.dto.PatientRequestDto;
import com.pm.alexki.patientservice.dto.PatientResponseDto;
import com.pm.alexki.patientservice.exception.PatientAlreadyExistsException;
import com.pm.alexki.patientservice.exception.PatientNotFountException;
import com.pm.alexki.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.alexki.patientservice.mapper.PatientDtoMapper;
import com.pm.alexki.patientservice.model.Patient;
import com.pm.alexki.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final PatientDtoMapper mapper;
    private final BillingServiceGrpcClient grpcClient;

    public List<PatientResponseDto> getAllPatients() {
        List<Patient> patients = repository.findAll();

        return patients.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        Patient patientEntity = mapper.toEntity(patientRequestDto);

        String email = patientRequestDto.email();

        if (repository.existsByEmail(email)) {
            throw new PatientAlreadyExistsException("Patient with email %s already exists".formatted(email));
        }

        var newPatient = repository.save(patientEntity);

        grpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(), newPatient.getEmail());

        return mapper.toDto(newPatient);

    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto) {

        Patient patient = repository.findById(id)
                .orElseThrow(() -> new PatientNotFountException("Patient not found, id: %s".formatted(id)));

        //We won't change an email - it's permanent. Will check if someone wrongly puts the email to dto
        String email = patientRequestDto.email();
        if (email != null && repository.existsByEmailAndIdNot(email, id)) {
            throw new PatientAlreadyExistsException("Patient with email %s already exists".formatted(email));
        }

        patient.setName(patientRequestDto.name());
        patient.setAddress(patientRequestDto.address());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.dateOfBirth()));

        Patient updatedPatient = repository.save(patient);

        return mapper.toDto(updatedPatient);

    }

    public void deletePatient(UUID id) {
        repository.deleteById(id);
    }
}
