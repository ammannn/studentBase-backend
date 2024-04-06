package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.FileType;
import lombok.*;

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
}
