/**
 * 
 */
package com.cm.batch.parameters;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

/**
 * @author chandresh.mishra
 *
 */
@Component("batch-param-incrementer")
public class JobTimeStamper implements JobParametersIncrementer {

	@Override
	public JobParameters getNext(JobParameters parameters) {
		
		return new JobParametersBuilder(parameters).addDate("currentDate", new Date()).toJobParameters();
	}

}
