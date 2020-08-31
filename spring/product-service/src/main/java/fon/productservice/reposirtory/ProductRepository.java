package fon.productservice.reposirtory;

import fon.productservice.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByProductCode(String productCode);

    List<Product> findByType(String type);

    Page<Product> findAll(Pageable pageable);
}
