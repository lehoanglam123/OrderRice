package com.bezkoder.spring.datajpa.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotNull(message = "Name is null!")
	@NotEmpty(message = "Missing full Name!")
	private String fullName;

	@NotNull(message = "Department is null!")
	@NotEmpty(message = "Missing department!")
	private String department;

	@NotNull(message = "Phone is null!")
	@NotEmpty(message = "Missing phone!")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number format is incorrect!")
	private String phone;

	@NotNull(message = "Account is null!")
	@NotEmpty(message = "Missing Account!")
	@Length(min = 2, max = 20, message = "Account must at most 20 characters and at least 2 characters!")
	private String account;

	@NotNull(message = "Password is null!")
	@NotEmpty(message = "Missing password")
	@Size(min = 8, max = 16, message = "Password '${validatedValue}' has only {validation.password.size} characters while the required password needs at least {min} characters and at most {max} characters!")
	private String password;

	private Integer idPermission;
}
