package pavel.todobot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    @NotBlank
    private String name;

    private Integer loggedHours;

    @Override
    public String toString() {
        if (loggedHours != null) {
            return String.format("- %s - %d", name, loggedHours);
        }

        return String.format("- %s", name);
    }
}
