package com.rowland.engineering.ecommerce.repository;

import com.rowland.engineering.ecommerce.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

//    @Query(
//            nativeQuery = true,
//            value = "SELECT * FROM ecommerce.favourite WHERE user_id = :userId"
//    )
//    List<Favourite> findAllFavouriteByUserId(@Param("userId") Long userId);

//    @Query(nativeQuery = true, value = "SELECT f FROM Favourite f WHERE f.user_Id = :userId")
    List<Favourite> findAllFavouriteByUserId(Long userId);

    List<Favourite> findAllByProductId(Long id);


//
//    @Query("SELECT f.product.id FROM Favourite f WHERE f.user.id = :userId")
//    List<Long> findAllProductIdsByUserId(@Param("userId") Long userId);
//    @Query(
//            nativeQuery = true,
//            value = "SELECT * FROM ecommerce.favourite WHERE user_id = :userId"
//    )
//    List<Long> findAllByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT f FROM Favourite f where f.user.id = :userId and f.product.id in :productIds")
//    List<Favourite> findByUserIdAndProductIdIn(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);
}
