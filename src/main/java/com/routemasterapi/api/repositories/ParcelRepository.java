package com.routemasterapi.api.repositories;

import java.time.LocalDate;
import java.util.List;

 import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.routemasterapi.api.entity.ParcelEntity;
import com.routemasterapi.api.model.ParcelRequestBody;
 
@Repository
public interface  ParcelRepository extends CrudRepository<ParcelEntity,Integer> {

	@Query(value = "select * from  shraddhaparcel ", nativeQuery = true)
	Page<ParcelEntity> listallparcelsfromdb(Pageable pageable);

	@Query(value = "SELECT * FROM shraddhatrack_parcel WHERE CustomerID = :customerId", nativeQuery = true)
	Page<ParcelEntity> listcustomerparcelstatusfromdb(@Param("customerId") int customerId, Pageable pageable);

	@Query(value = "SELECT * FROM shraddhatrack_parcel WHERE createdDate >= :oneMonthAgo ", nativeQuery = true)
	Page<ParcelEntity> listonemonthparcelsfromdb(@Param("oneMonthAgo") LocalDate oneMonthAgo, Pageable pageable);
	
	@Query(value = "SELECT * FROM shraddhatrack_parcel WHERE createdDate >= :oneMonthAgo AND parcelStatus = 'Delayed' ", nativeQuery = true)
	Page<ParcelEntity> listonemonthdelayedparcelsfromdb(@Param("oneMonthAgo") LocalDate oneMonthAgo, Pageable pageable);

	 @Query("SELECT SUM(p.totalPayment) FROM ParcelEntity p")
	    double sumTotalPayment();
	 @Query("SELECT p.route.name, COUNT(p) FROM ParcelEntity p GROUP BY p.route.name")
	 List<Object[]> countParcelsByRoute();
	 
	 //task3
	 

	    @Query("SELECT p.destinationPincode, COUNT(p) as parcelCount FROM ParcelEntity p GROUP BY p.destinationPincode ORDER BY parcelCount DESC")
	    List<Object[]> findHighestNumberOfParcelsReceivedPerPincode();

	    @Query("SELECT p.destinationPincode, COUNT(p) as parcelCount FROM ParcelEntity p WHERE p.parcelStatus = 'Delivered' GROUP BY p.destinationPincode ORDER BY parcelCount DESC")
	    List<Object[]> findHighestNumberOfParcelsDeliveredPerPincode();

	    @Query("SELECT p.destinationPincode, COUNT(p) FROM ParcelEntity p GROUP BY p.destinationPincode")
	    List<Object[]> findSummaryOfParcelsPerPincode();

	    @Query("SELECT p.destinationPincode, COUNT(p) FROM ParcelEntity p WHERE p.parcelStatus <> 'Delivered' GROUP BY p.destinationPincode")
	    List<Object[]> findUnsuccessfulParcelsPerPincode();

	    @Query("SELECT p.destinationPincode, SUM(p.totalPayment) FROM ParcelEntity p GROUP BY p.destinationPincode")
	    List<Object[]> findTotalPaymentPerPincode();
	    
	    @Query(value = "SELECT r.name " +
                "FROM shraddhatrack_parcel p " +
                "JOIN shraddharoutes r ON p.RouteID = r.RouteID " +
                "WHERE DATEDIFF(p.deliveryDate, p.createdDate) > 5", nativeQuery = true)
 List<String> findRoutesForParcelsMoreThanFiveDays();

	    @Query(value = "SELECT r.name, " +
                "       CASE " +
                "           WHEN p.deliveryDate IS NULL THEN DATEDIFF(CURDATE(), p.createdDate) " +
                "           ELSE DATEDIFF(p.deliveryDate, p.createdDate) " +
                "       END AS daysTaken " +
                "FROM shraddhatrack_parcel p " +
                "JOIN shraddharoutes r ON p.RouteID = r.RouteID", nativeQuery = true)
 List<Object[]> findDaysTakenForAllParcels();

	
}
