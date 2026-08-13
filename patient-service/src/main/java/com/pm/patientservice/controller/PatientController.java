package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@org.springframework.web.bind.annotation.RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {
    private final PatientService patientService;
    private final String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String uuidPatternMessage = "The provided ID must be a valid UUID format";

    @GetMapping
    @Operation(
            summary = "Get all patients",
            description = "Returns a list of all patients registered in the system. Returns 404 if no patients are found.",
            tags = {"Patient"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of patients",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponseDto.class),
                            examples = @ExampleObject(
                                    name = "patient-list-example",
                                    value = """
                                            [
                                              {
                                                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                                "name": "John Doe",
                                                "email": "john.doe@example.com",
                                                "address": "123 Main St, Springfield",
                                                "dateOfBirth": "1990-01-15"
                                              },
                                              {
                                                "id": "4fa85f64-5717-4562-b3fc-2c963f66afa7",
                                                "name": "Jane Smith",
                                                "email": "jane.smith@example.com",
                                                "address": "456 Oak Ave, Shelbyville",
                                                "dateOfBirth": "1985-07-22"
                                              }
                                            ]"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No patients found",
                    content = @Content
            )
    })
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        var patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            log.info("PatientController: GetPatient(): No patient found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(
            summary = "Create a new patient",
            description = "Creates a new patient record with the provided information.",
            tags = {"Patient"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponseDto.class),
                            examples = @ExampleObject(
                                    name = "patient-response-example",
                                    value = """
                                            {
                                              "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                              "name": "John Doe",
                                              "email": "john.doe@example.com",
                                              "address": "123 Main St, Springfield",
                                              "dateOfBirth": "1990-01-15"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data — validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "validation-error-example",
                                    value = """
                                            {
                                              "name": "Name is required",
                                              "email": "Enter a correct email"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<PatientResponseDto> createPatient(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient object that needs to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientRequestDto.class),
                            examples = @ExampleObject(
                                    name = "patient-request-example",
                                    value = """
                                            {
                                              "name": "John Doe",
                                              "email": "john.doe@example.com",
                                              "address": "123 Main St, Springfield",
                                              "dateOfBirth": "1990-01-15",
                                              "registeredDate": "2026-01-01"
                                            }"""
                            )
                    )
            )
            PatientRequestDto patientRequestDto) {
        PatientResponseDto newPatient = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok().body(newPatient);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing patient",
            description = "Updates a patient's information based on their unique ID.",
            tags = {"Patient"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponseDto.class),
                            examples = @ExampleObject(
                                    name = "patient-response-example",
                                    value = """
                                            {
                                              "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                              "name": "John Doe",
                                              "email": "john.doe@example.com",
                                              "address": "123 Main St, Springfield",
                                              "dateOfBirth": "1990-01-15"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID or input data — validation failed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content
            )
    })
    public ResponseEntity<PatientResponseDto> updatePatient(
            @Parameter(
                    description = "Unique identifier of the patient to update",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid"),
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable
            @Pattern(regexp = uuidPattern, message = uuidPatternMessage)
            UUID id,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated patient object",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientRequestDto.class),
                            examples = @ExampleObject(
                                    name = "patient-request-example",
                                    value = """
                                            {
                                              "name": "John Doe",
                                              "email": "john.doe@example.com",
                                              "address": "123 Main St, Springfield",
                                              "dateOfBirth": "1990-01-15",
                                              "registeredDate": "2026-01-01"
                                            }"""
                            )
                    )
            )
            PatientRequestDto requestDto) {

        var updatedPatient = patientService.updatePatient(id, requestDto);
        return ResponseEntity.ok().body(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a patient",
            description = "Deletes a patient record based on their unique ID.",
            tags = {"Patient"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Patient deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deletePatient(
            @Parameter(
                    description = "Unique identifier of the patient to delete",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid"),
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable
            @Pattern(regexp = uuidPattern, message = uuidPatternMessage)
            UUID id) {
        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }

}