package com.university.mcmaster.integrations.sheerid;

import com.university.mcmaster.exceptions.InvalidParamValueException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationRequestDto;
import com.university.mcmaster.integrations.sheerid.model.SheerIdUniversity;
import com.university.mcmaster.integrations.sheerid.model.SheetIdVerificationResponseDto;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.Utility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SheerIdService {

    private static final String baseUrlForSearch = "https://orgsearch.sheerid.net";
    private static final String baseUrlForVerification = "https://services.sheerid.com";
    private static final String universitySearchUrl = baseUrlForSearch + "/rest/organization/search";
    private static final String verificationUrl = baseUrlForVerification + "/rest/v2/verification/program/{programId}/step/collectStudentPersonalInfo";

    public static SheerIdUniversity[] searchUniversities(String searchTerm, String country){
        URI uri = UriComponentsBuilder.fromUriString(universitySearchUrl)
                .queryParam("accountId", EnvironmentVariables.SHEERID_PROGRAM_ID)
                .queryParam("country",country.toUpperCase())
                .queryParam("name",searchTerm)
                .queryParam("tags","HEI%2Cqualifying_hs%2Cqualifying_ps")
                .queryParam("type","UNIVERSITY%2CPOST_SECONDARY%2CHIGH_SCHOOL")
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SheerIdUniversity[]> res = restTemplate.getForEntity(uri, SheerIdUniversity[].class);
        return res.getBody();
    }

    public static SheetIdVerificationResponseDto verifyStudent(SheerIdVerificationRequestDto requestDto){
        verifyRequest(requestDto);
        URI uri = UriComponentsBuilder.fromUriString(verificationUrl)
                .buildAndExpand(EnvironmentVariables.SHEERID_PROGRAM_ID).toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SheetIdVerificationResponseDto> res = restTemplate.postForEntity(uri,requestDto,SheetIdVerificationResponseDto.class);
        return res.getBody();
    }

    private static void verifyRequest(SheerIdVerificationRequestDto requestDto) {
        List<String> errors = new ArrayList<>();
        String email = Optional.ofNullable(requestDto.getEmail()).map(s->s.trim().toLowerCase()).orElse("");
        String dob = Optional.ofNullable(requestDto.getBirthDate()).map(s->s.trim().toLowerCase()).orElse("");
        String fname = Optional.ofNullable(requestDto.getFirstName()).map(s->s.trim().toLowerCase()).orElse("");
        String lname = Optional.ofNullable(requestDto.getLastName()).map(s->s.trim().toLowerCase()).orElse("");
        if(email.isEmpty()) errors.add("email");
        if(dob.isEmpty()) errors.add("birth_date");
        if(fname.isEmpty()) errors.add("first_name");
        if(lname.isEmpty()) errors.add("last_name");
        if(null == requestDto.getOrganization()) errors.add("SheetIdUniversity");
        else {
            if(null == requestDto.getOrganization().getIdExtended() || requestDto.getOrganization().getIdExtended().trim().isEmpty()) errors.add("idExtended");
            if(null == requestDto.getOrganization().getName() || requestDto.getOrganization().getName().trim().isEmpty()) errors.add("organizationName");
            if(false == String.valueOf(requestDto.getOrganization().getId()).equals(requestDto.getOrganization().getIdExtended())) throw new InvalidParamValueException("organizationId");
        }
        if(false == errors.isEmpty()) throw new MissingRequiredParamException(errors.toString());
        if(false == Utility.isValidEmail(email))throw new InvalidParamValueException("email");
        requestDto.setEmail(email);
        requestDto.setFirstName(fname);
        requestDto.setLastName(lname);
        requestDto.setBirthDate(dob);
    }
}
