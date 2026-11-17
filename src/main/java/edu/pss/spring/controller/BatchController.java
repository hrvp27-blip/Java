package edu.pss.spring.controller;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pss.spring.model.InfoEjecucionBatch;
import edu.pss.spring.model.ResultadoBatch;
import edu.pss.spring.service.BatchService;

/**
 * ESTA CLASE SE DEDICA A RECEBIR PETICIONES HTTP Y CONTESTARLAS
 */
@RestController
@RequestMapping("/api/batch")
public class BatchController {

	BatchService batchService;


	public BatchController(BatchService batchService) {
		this.batchService = batchService;
	}



	//le decimos a SPRING
	//si recibimos una petición
	//POST http://localhost:8080/api/batch/ejecutar-csv
	//es aquí!
	@PostMapping("/ejecutar-csv")
	public ResultadoBatch ejecutarBatchCSV () throws Exception
	{
		 JobExecution jobExecution = this.batchService.ejecutarFicheroBatchCSV();

		 return new ResultadoBatch (
				 jobExecution.getId(),
				 jobExecution.getStatus().toString()
				 );
	}


	//TODO controlar el caso de que el trabajo consultado no existe
	@GetMapping("/consultas-pasadas/{id}")
	public ResultadoBatch ejecutarConsultasPasadas (@PathVariable Long id) throws Exception
	{
		 //JobExecution jobExecution = this.batchService.ejecucionConsulta(2);
		 JobExecution jobExecution = this.batchService.ejecucionConsulta(id);
		 return new ResultadoBatch (
				 jobExecution.getId(),
				 jobExecution.getStatus().toString()
				 );
	}


	//MI VERSIÓN: GET http://localhost:8080/api/batch/ejecucion/1
	//devuelve toda la información de un trabajo ya ejecutado y 404 si no existe
	@GetMapping("/ejecucion/{id}")
	public ResponseEntity<InfoEjecucionBatch> consultarEjecucion (@PathVariable long id) throws Exception
	{
		JobExecution jobExecution = null;

		try {
			jobExecution = this.batchService.ejecucionConsulta(id);
		} catch (EmptyResultDataAccessException e) {
			//con base de datos, si el id no existe se lanza esta excepción en vez de devolver null
		}

		//si no existe esa ejecución, contestamos 404 NOT FOUND
		if (jobExecution == null)
		{
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(InfoEjecucionBatch.desde(jobExecution));
	}

}
