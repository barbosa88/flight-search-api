package pt.flightin.flightsearch.core.validation;

import org.springframework.validation.annotation.Validated;
import pt.flightin.flightsearch.core.exception.BaseException;

import java.util.List;
import javax.validation.constraints.NotEmpty;

@Validated
@FunctionalInterface
public interface ApiCodeValidator {

    void validate(@NotEmpty List<String> codeList) throws BaseException;
}
