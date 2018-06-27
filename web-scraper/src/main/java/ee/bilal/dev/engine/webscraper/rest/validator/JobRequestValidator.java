package ee.bilal.dev.engine.webscraper.rest.validator;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class JobRequestValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return JobRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JobRequestDTO jobRequest = (JobRequestDTO) target;

        boolean isValidUrl = UrlValidator.getInstance().isValid(jobRequest.getUrl());
        if(!isValidUrl) {
            errors.reject("url", "Invalid Url");
        }
    }
}
