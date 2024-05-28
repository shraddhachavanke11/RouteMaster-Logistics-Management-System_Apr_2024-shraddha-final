package com.routemasterapi.api.service;

import com.routemasterapi.api.entity.ParcelEntity;
import com.routemasterapi.api.entity.RouterEntity;
import com.routemasterapi.api.entity.customerentity;
import com.routemasterapi.api.model.ParcelIdRequest;
import com.routemasterapi.api.model.ParcelRequestBody;
import com.routemasterapi.api.repositories.CustomerRepository;
import com.routemasterapi.api.repositories.ParcelRepository;
import com.routemasterapi.api.repositories.RouterRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ParcelService  {

	@Autowired
	private ParcelRepository parcelRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private RouterRepository routeRepository;
	

	public ParcelEntity createParcel(ParcelRequestBody parcelReqBody) {
		Optional<customerentity> optionalCustomer = customerRepository.findById(parcelReqBody.getCustomerId());
		customerentity customer = optionalCustomer.orElseThrow(() -> new Error("No customer with this id") );
		
		Optional<RouterEntity> optionalRoute = routeRepository.findById(parcelReqBody.getRouteId());
		RouterEntity route = optionalRoute.orElseThrow(() -> new Error("No route with this id") );
		ParcelEntity newParcel = new ParcelEntity();
		newParcel.setSenderName(parcelReqBody.getSenderName());
		newParcel.setReceiverName(parcelReqBody.getReceiverName());
		newParcel.setDestinationAddress(parcelReqBody.getDestinationAddress());
		newParcel.setDestinationPincode(parcelReqBody.getDestinationPincode());
		newParcel.setParcelStatus(parcelReqBody.getParcelStatus());
		newParcel.setCreatedDate(parcelReqBody.getCreatedDate());
		newParcel.setTotalPayment(parcelReqBody.getTotalPayment());
		newParcel.setDeliveryDate(parcelReqBody.getDeliveryDate());
		newParcel.setCustomer(customer);
		newParcel.setRoute(route);
		return parcelRepository.save(newParcel);		 
	}

	public ParcelEntity updateParcel(ParcelRequestBody parcelReqBody) {
		Optional<customerentity> optionalCustomer = customerRepository.findById(parcelReqBody.getCustomerId());
		customerentity customer = optionalCustomer.orElseThrow(() -> new Error("No customer with this id") );
		
		Optional<RouterEntity> optionalRoute = routeRepository.findById(parcelReqBody.getRouteId());
		RouterEntity route = optionalRoute.orElseThrow(() -> new Error("No route with this id") );
		ParcelEntity newParcel = new ParcelEntity();
		newParcel.setParcelId(parcelReqBody.getParcelId());
		newParcel.setSenderName(parcelReqBody.getSenderName());
		newParcel.setReceiverName(parcelReqBody.getReceiverName());
		newParcel.setDestinationAddress(parcelReqBody.getDestinationAddress());
		newParcel.setDestinationPincode(parcelReqBody.getDestinationPincode());
		newParcel.setParcelStatus(parcelReqBody.getParcelStatus());
		newParcel.setCreatedDate(parcelReqBody.getCreatedDate());
		newParcel.setTotalPayment(parcelReqBody.getTotalPayment());
		newParcel.setDeliveryDate(parcelReqBody.getDeliveryDate());
		newParcel.setCustomer(customer);
		newParcel.setRoute(route);
		
		return parcelRepository.save(newParcel);		 
	}

	public Page<ParcelEntity> listallparcelsfromdb(int pageNumber, int size) {
		Pageable pageable = PageRequest.of(pageNumber, size);
		return parcelRepository.listallparcelsfromdb(pageable);
	}
 
	public String deleteParcel(ParcelIdRequest parcelIdReq) {
		int ParcelId= parcelIdReq.getParcelId();
		parcelRepository.deleteById(ParcelId);
		return "Record Deleted";
	}
	
	public Page<ParcelEntity> listcustomerparcelstatusfromdb(int pageNumber, int size, int customerId) {
		Pageable pageable = PageRequest.of(pageNumber, size);
		return parcelRepository.listcustomerparcelstatusfromdb(customerId, pageable);
	}
	
	public Page<ParcelEntity> listonemonthparcelsfromdb(int pageNumber, int size) {
		Pageable pageable = PageRequest.of(pageNumber, size);
		LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
		return parcelRepository.listonemonthparcelsfromdb(oneMonthAgo, pageable);
	}
	
	public Page<ParcelEntity> listonemonthdelayedparcelsfromdb(int pageNumber, int size) {
		Pageable pageable = PageRequest.of(pageNumber, size);
		LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
		return parcelRepository.listonemonthdelayedparcelsfromdb(oneMonthAgo, pageable);
	}
	
	
	//task2
	public Map<String, Long> getHighestNumberOfParcelsReceivedPerPincode() {
        List<Object[]> results = parcelRepository.findHighestNumberOfParcelsReceivedPerPincode();
        Map<String, Long> parcelsPerPincode = new HashMap<>();
        if (!results.isEmpty()) {
            Object[] highest = results.get(0);
            String pincode = (String) highest[0];
            Long count = (Long) highest[1];
            parcelsPerPincode.put(pincode, count);
        }
        return parcelsPerPincode;
    }

    public Map<String, Long> getHighestNumberOfParcelsDeliveredPerPincode() {
        List<Object[]> results = parcelRepository.findHighestNumberOfParcelsDeliveredPerPincode();
        Map<String, Long> parcelsPerPincode = new HashMap<>();
        if (!results.isEmpty()) {
            Object[] highest = results.get(0);
            String pincode = (String) highest[0];
            Long count = (Long) highest[1];
            parcelsPerPincode.put(pincode, count);
        }
        return parcelsPerPincode;
    }

    public Map<String, Long> getSummaryOfParcelsPerPincode() {
        List<Object[]> results = parcelRepository.findSummaryOfParcelsPerPincode();
        Map<String, Long> summaryPerPincode = new HashMap<>();
        for (Object[] result : results) {
            String pincode = (String) result[0];
            Long count = (Long) result[1];
            summaryPerPincode.put(pincode, count);
        }
        return summaryPerPincode;
    }

    public Map<String, Long> getUnsuccessfulParcelsPerPincode() {
        List<Object[]> results = parcelRepository.findUnsuccessfulParcelsPerPincode();
        Map<String, Long> unsuccessfulParcelsPerPincode = new HashMap<>();
        for (Object[] result : results) {
            String pincode = (String) result[0];
            Long count = (Long) result[1];
            unsuccessfulParcelsPerPincode.put(pincode, count);
        }
        return unsuccessfulParcelsPerPincode;
    }

    public Map<String, Double> getTotalPaymentPerPincode() {
        List<Object[]> results = parcelRepository.findTotalPaymentPerPincode();
        Map<String, Double> totalPaymentsPerPincode = new HashMap<>();
        for (Object[] result : results) {
            String pincode = (String) result[0];
            Double totalPayment = (Double) result[1];
            totalPaymentsPerPincode.put(pincode, totalPayment);
        }
        return totalPaymentsPerPincode;
    }

}
