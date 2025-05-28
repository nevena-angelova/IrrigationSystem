package irrigationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

    @Getter
    private T value;

    @Getter
    private Optional<String> errorMessage;

    private boolean hasErrors = false;

    public boolean hasErrors() {
        return hasErrors;
    }
}
