package lab.dto;

import java.util.Optional;

public record User(String Name,
                   String LastName,
                   Optional<Long> pasportSerie,
                   Optional<String> email,
                   Long id) {
}
