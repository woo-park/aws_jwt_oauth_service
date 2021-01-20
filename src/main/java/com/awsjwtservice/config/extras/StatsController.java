///**
// *
// */
//package com.awsjwtservice.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.awsjwtservice.domain.Stats;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dineshonjava.prodos.domain.Product;
//import com.dineshonjava.prodos.repository.ProductRepository;
//
///**
// * @author Dinesh.Rajput
// *
// */
//
//@RestController
//public class StatsController {
//
//	private StatsController statsController;
//
//	public StatsController(StatsController statsController) {
//		super();
//		this.statsController = statsController;
//	}
//
//	@GetMapping("/stats")
//	public List<Stats> findAll(){
//		List<Stats> stats = new ArrayList<>();
//		statsController.findAll().forEach(i -> stats.add(i));
//		return stats;
//	}
//
//	/*@GetMapping("/products/{id}")
//	public Product findProductById(@PathVariable String id){
//		return productRepository.findById(id).isPresent() ? productRepository.findById(id).get() : null;
//	}*/
//
//	@GetMapping("/stats/{id}")
//	public ResponseEntity<Stats> findStatsById(@PathVariable String id){
//		return statsRepository.findById(id).isPresent() ?
//				new ResponseEntity<>(statsRepository.findById(id).get(), HttpStatus.OK) :
//					new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//	}
//
//	@PostMapping(value= "/stats", consumes="application/json")
//	@ResponseStatus(HttpStatus.CREATED)
//	public Stats postStats(@RequestBody Stats stats) {
//		return statsRepository.save(stats);
//	}
//
//	@PutMapping("/stats/{id}")
//	public Stats putStats(@RequestBody Stats stats) {
//		return statsRepository.save(stats);
//	}
//
//	@PatchMapping(path="/stats/{id}", consumes="application/json")
//	public Stats patchStats(@PathVariable String id, @RequestBody Stats patch) {
//		Stats stats = statsRepository.findById(id).get();
//		if (patch.getBrand() != null) {
//			stats.setBrand(patch.getBrand());
//		}
//		if (patch.getDescription() != null) {
//			stats.setDescription(patch.getDescription());
//		}
//		if (patch.getName() != null) {
//			stats.setName(patch.getName());
//		}
//		if (patch.getType() != null) {
//			stats.setType(patch.getType());
//		}
//		return statsRepository.save(stats);
//	}
//
//	@DeleteMapping("stats/{id}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void deleteStats(@PathVariable("id") String id) {
//		try {
//			statsRepository.deleteById(id);
//		} catch (EmptyResultDataAccessException e) {}
//	}
//
//}
