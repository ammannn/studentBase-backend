package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.FileType;
import com.university.mcmaster.enums.RentalUnitElement;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUploadUrlForFileRequestDto {
    private String contentType;
    private FileType type;
    private FilePurpose filePurpose;
    private String rentalUnitId;
    private String fileName;
    private RentalUnitElement rentalUnitElement;
    private String applicationId;
}
