package org.musinsa.exhibition.infrastructure.persistence

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.musinsa.exhibition.domain.product.entity.Brand
import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.model.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


@DataJpaTest(showSql = true)
class ProductRepositoryTest {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var brandRepository: BrandRepository

    @Autowired
    lateinit var entityManager: EntityManager

    private final val TEST_BRANE_NAME1 = "Test"
    private final val TEST_BRANE_NAME2 = "Test"

    @BeforeEach
    fun insertBrand() {
        val newBrand1 = Brand(name = TEST_BRANE_NAME1)
        val newBrand2 = Brand(name = TEST_BRANE_NAME2)

        // 저장
        brandRepository.saveAll(listOf(newBrand1, newBrand2))
    }

    @Test
    fun insert() {
        val brand = brandRepository.findAll().first { it.name == TEST_BRANE_NAME1 }
        assertNotNull(brand)

        val product = Product(brand = brand, category = Category.BAG, price = 3000)
        assert(product.productId == null)

        // 저장
        productRepository.save(product)

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val newProduct = productRepository.findByIdOrNull(product.productId)
        assertNotNull(newProduct)
        assertEquals(product.productId, newProduct.productId)
        assertEquals(product.brand.brandId, newProduct.brand.brandId)
        assertEquals(product.category, newProduct.category)
        assertEquals(product.price, newProduct.price)
    }

    @Test
    fun update() {
        val brand = brandRepository.findAll().first { it.name == TEST_BRANE_NAME1 }
        val updateBrand = brandRepository.findAll().first { it.name == TEST_BRANE_NAME2 }
        assertNotNull(brand)
        assertNotNull(updateBrand)

        val product = Product(brand = brand, category = Category.BAG, price = 3000)
        assert(product.productId == null)

        // 저장
        productRepository.save(product)

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val newProduct = productRepository.findByIdOrNull(product.productId)
        assertNotNull(newProduct)
        assertEquals(product.productId, newProduct.productId)
        assertEquals(product.brand.brandId, newProduct.brand.brandId)
        assertEquals(product.category, newProduct.category)
        assertEquals(product.price, newProduct.price)

        // 변경
        newProduct.brand = updateBrand
        newProduct.category = Category.ACCESSORIES
        newProduct.price = 4000

        productRepository.save(newProduct)
        productRepository.flush()

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val updateProduct = productRepository.findByIdOrNull(product.productId)
        assertNotNull(updateProduct)
        assertEquals(product.productId, updateProduct.productId)
        assertEquals(updateBrand.brandId, updateProduct.brand.brandId)
        assertEquals(Category.ACCESSORIES, updateProduct.category)
        assertEquals(4000, updateProduct.price)
    }

    @Test
    fun delete() {
        val brand = brandRepository.findAll().first { it.name == TEST_BRANE_NAME1 }
        assertNotNull(brand)

        val product = Product(brand = brand, category = Category.BAG, price = 3000)
        assert(product.productId == null)

        // 저장
        productRepository.save(product)

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val newProduct = productRepository.findByIdOrNull(product.productId)
        assertNotNull(newProduct)
        assertEquals(product.productId, newProduct.productId)
        assertEquals(product.brand.brandId, newProduct.brand.brandId)
        assertEquals(product.category, newProduct.category)
        assertEquals(product.price, newProduct.price)

        // 삭제
        productRepository.delete(newProduct)
        productRepository.flush()

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val deleteProduct = productRepository.findByIdOrNull(product.productId)
        assertNull(deleteProduct)
    }
}

