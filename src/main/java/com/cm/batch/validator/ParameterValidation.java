/**
 * 
 */
package com.cm.batch.validator;

import com.cm.batch.modal.PersonBO;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
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
				 new String[] { "currentDate" },new String[] { "" });

		defaultJobParametersValidator.afterPropertiesSet();

		return defaultJobParametersValidator;

	}
	@Bean
	public BeanValidatingItemProcessor<PersonBO> personValidatingItemProcessor() {
		return new BeanValidatingItemProcessor<>();
	}
}
