package edu.pss.spring.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.job.JobExecution;

/**
 * Esta clase, representa la información de una ejecución pasada del job
 * (consultada en el JobRepository)
 */
public record InfoEjecucionBatch(
		Long id,
		String nombreJob,
		String estado,
		LocalDateTime inicio,
		LocalDateTime fin,
		List<String> pasos,
		Map<String, Object> contextoJob) {

	// crea la info a partir de una ejecución del job
	public static InfoEjecucionBatch desde(JobExecution jobExecution) {
		// nombre y estado de cada paso ejecutado
		List<String> pasos = jobExecution.getStepExecutions().stream()
				.map(paso -> paso.getStepName() + ": " + paso.getStatus())
				.toList();

		return new InfoEjecucionBatch(
				jobExecution.getId(),
				jobExecution.getJobInstance().getJobName(),
				jobExecution.getStatus().toString(),
				jobExecution.getStartTime(),
				jobExecution.getEndTime(),
				pasos,
				jobExecution.getExecutionContext().toMap()); // todo lo guardado en el contexto del job
	}
}
