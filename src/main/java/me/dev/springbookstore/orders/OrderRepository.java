package me.dev.springbookstore.orders;

import me.dev.springbookstore.orders.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @EntityGraph(attributePaths = {"items", "items.book"})
    Page<OrderEntity> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.book"})
    Optional<OrderEntity> findByIdAndUser_Id(Long id, Long userId);

    @EntityGraph(attributePaths = {"items", "items.book"})
    @Query("SELECT o FROM OrderEntity o WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(@Param("id") Long id);
}
