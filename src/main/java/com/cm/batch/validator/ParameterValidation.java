/**
 * 
 */
package com.cm.batch.validator;

import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chandresh.mishra
 *
 */
@Configuration
public class ParameterValidation {

	@Bean("batch-param-validator")
	public JobParametersValidator validator() {

		DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
				new String[] { "customerFile" }, new String[] { "currentDate" });

		defaultJobParametersValidator.afterPropertiesSet();

		return defaultJobParametersValidator;

	}

}
