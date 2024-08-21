package com.example.capstone_project.controller.body.supplier;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewSupplierBody {
    @NotEmpty(message = "New supplier name can't be empty")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ0-9]{1,50}(?: [a-zA-ZÀ-ỹ0-9]+)*$", message = "Supplier name must only contain letters and numbers and be up to 50 characters long")
    private String supplierName;
}
