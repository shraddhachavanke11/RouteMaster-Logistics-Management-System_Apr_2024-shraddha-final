package com.routemasterapi.api.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.routemasterapi.api.model.ParcelRequestBody;
import com.routemasterapi.api.repositories.ParcelRepository;
import com.routemasterapi.api.model.ParcelIdRequest;
import com.routemasterapi.api.service.ParcelService;
  
@RestController
@CrossOrigin
public class ParcelController {
	
	@Autowired
	private ParcelService ParcelService;	
	@Autowired
    private ParcelRepository parcelRepository;
	
	@RequestMapping(value = "/createParcel", method = RequestMethod.POST)
	public ResponseEntity<?> createParcel(@RequestBody ParcelRequestBody ParcelReqBody) throws Exception {
		return ResponseEntity.ok(ParcelService.createParcel(ParcelReqBody));
	}
	
	@RequestMapping(value = "/updateParcel", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParcel(@RequestBody ParcelRequestBody ParcelReqBody) throws Exception {
		return ResponseEntity.ok(ParcelService.updateParcel(ParcelReqBody));
	}		
	
	@RequestMapping(value = "/listAllParcels", method = RequestMethod.GET)
	public ResponseEntity<?> listAllParcels(@RequestParam(defaultValue = "0") final Integer pageNumber,
			@RequestParam(defaultValue = "10") final Integer size) throws Exception {
		return ResponseEntity.ok(ParcelService.listallparcelsfromdb(pageNumber, size));
	}		
	
	@RequestMapping(value = "/deleteParcel", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParcel(@RequestBody ParcelIdRequest parcelIdRequest) throws Exception {
		return ResponseEntity.ok(ParcelService.deleteParcel(parcelIdRequest));
	}		
	@RequestMapping(value = "/listCustomerParcelStatus", method = RequestMethod.GET)
	public ResponseEntity<?> listCustomerParcelStatus(@RequestParam(defaultValue = "0") final Integer pageNumber,
			@RequestParam(defaultValue = "10") final Integer size,
			@RequestParam("customerId") int customerId) throws Exception {
		return ResponseEntity.ok(ParcelService.listcustomerparcelstatusfromdb(pageNumber, size, customerId));
	}	
	
	@RequestMapping(value = "/listOneMonthParcels", method = RequestMethod.GET)
	public ResponseEntity<?> listOneMonthParcels(@RequestParam(defaultValue = "0") final Integer pageNumber,
			@RequestParam(defaultValue = "10") final Integer size) throws Exception {
		return ResponseEntity.ok(ParcelService.listonemonthparcelsfromdb(pageNumber, size));
	}	
	
	@RequestMapping(value = "/listOneMonthDelayedParcels", method = RequestMethod.GET)
	public ResponseEntity<?> listOneMonthDelayedParcels(@RequestParam(defaultValue = "0") final Integer pageNumber,
			@RequestParam(defaultValue = "10") final Integer size) throws Exception {
		return ResponseEntity.ok(ParcelService.listonemonthdelayedparcelsfromdb(pageNumber, size));
	}	
	//task3
	
	
	  @RequestMapping(value = "/highest-received-pincode", method = RequestMethod.GET)
	    public Map<String, Long> getHighestNumberOfParcelsReceivedPerPincode() {
	        return ParcelService.getHighestNumberOfParcelsReceivedPerPincode();
	    }

	    @RequestMapping(value = "/highest-delivered-pincode", method = RequestMethod.GET)
	    public Map<String, Long> getHighestNumberOfParcelsDeliveredPerPincode() {
	        return ParcelService.getHighestNumberOfParcelsDeliveredPerPincode();
	    }

	    @RequestMapping(value = "/summary-per-pincode", method = RequestMethod.GET)
	    public Map<String, Long> getSummaryOfParcelsPerPincode() {
	        return ParcelService.getSummaryOfParcelsPerPincode();
	    }

	    @RequestMapping(value = "/unsuccessful-per-pincode", method = RequestMethod.GET)
	    public Map<String, Long> getUnsuccessfulParcelsPerPincode() {
	        return ParcelService.getUnsuccessfulParcelsPerPincode();
	    }

	    @RequestMapping(value = "/cost-per-pincode", method = RequestMethod.GET)
	    public Map<String, Double> getCostPerPincode() {
	        return ParcelService.getTotalPaymentPerPincode();
	    }
	    
	    @RequestMapping(value="/parcelsMoreThanFiveDays", method = RequestMethod.GET)
	    public List<String> getParcelsMoreThanFiveDays() {
	        return parcelRepository.findRoutesForParcelsMoreThanFiveDays();
	    }

	    @RequestMapping(value="/daysTakenForAllParcels", method = RequestMethod.GET)
	    public List<Map<String, Object>> getDaysTakenForAllParcels() {
	        return parcelRepository.findDaysTakenForAllParcels().stream()
	                .map(record -> Map.of(
	                        "routeName", record[0],
	                        "daysTaken", record[1]
	                ))
	                .collect(Collectors.toList());
	    }
}
