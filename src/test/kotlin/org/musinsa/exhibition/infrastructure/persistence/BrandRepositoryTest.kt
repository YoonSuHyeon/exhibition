package org.musinsa.exhibition.infrastructure.persistence

import jakarta.persistence.EntityManager
import org.musinsa.exhibition.domain.product.entity.Brand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


@DataJpaTest(showSql = true)
class BrandRepositoryTest {

    @Autowired
    lateinit var brandRepository: BrandRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun insert() {
        val newBrand = Brand(name = "Test")
        assert(newBrand.brandId == null)

        // 저장
        brandRepository.save(newBrand)


        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val brand = brandRepository.findByIdOrNull(newBrand.brandId)
        assertNotNull(brand)
        assertEquals(newBrand.brandId, brand.brandId)
        assertEquals(newBrand.name, brand.name)
    }

    @Test
    fun update() {
        val newBrand = Brand(name = "Test")
        assert(newBrand.brandId == null)

        // 저장
        brandRepository.save(newBrand)

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val brand = brandRepository.findByIdOrNull(newBrand.brandId)
        assertNotNull(brand)
        assertEquals(newBrand.brandId, brand.brandId)
        assertEquals(newBrand.name, brand.name)

        // 변경
        brand.name = "Test2"
        brandRepository.save(brand)
        brandRepository.flush()
        entityManager.clear()

        // assert
        val updateBrand = brandRepository.findByIdOrNull(brand.brandId)
        assertNotNull(updateBrand)
        assertEquals(updateBrand.brandId, updateBrand.brandId)
        assertEquals("Test2", updateBrand.name)


    }

    @Test
    fun delete() {
        val newBrand = Brand(name = "Test")
        assert(newBrand.brandId == null)

        // 저장
        brandRepository.save(newBrand)

        //영속성 컨텍스트 초기화
        entityManager.clear()

        // assert
        val brand = brandRepository.findByIdOrNull(newBrand.brandId)
        assertNotNull(brand)
        assertEquals(newBrand.brandId, brand.brandId)
        assertEquals(newBrand.name, brand.name)

        // 삭제
        brandRepository.delete(brand)
        brandRepository.flush()

        // assert
        val deleteBrand = brandRepository.findByIdOrNull(newBrand.brandId)
        assertNull(deleteBrand)
    }
}
