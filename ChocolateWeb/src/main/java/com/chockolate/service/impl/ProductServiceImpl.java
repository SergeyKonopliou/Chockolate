package com.chockolate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.model.TypeProduct;
import com.chockolate.repository.ProductRepository;
import com.chockolate.repository.TypeProductRepository;
import com.chockolate.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository repository;
	@Autowired
	@Qualifier("typeProductRepository")
	private TypeProductRepository typeRepository;
	
	/**
	 * Метод для получения объекта Pageable с сортировкой по цене(если сортировка не нужна,то тип сортировки пишем "none")
	 */
	private Pageable getPageable(Pageable pageable, String priceSortType) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		Pageable page;
		if (priceSortType.equals("high")) {
			page = PageRequest.of(currentPage, pageSize, Sort.by("price").descending());
		} else if(priceSortType.equals("low")) {
			page = PageRequest.of(currentPage, pageSize, Sort.by("price").ascending());
		}else {
			page = PageRequest.of(currentPage, pageSize);
		}
		return page;
	}
	
	/**
	 * Метод загрузки всех товаров и базы данных с пагинацией
	 */
	public Page<Product> loadAllPaginated(Pageable pageable) throws ServiceException {
        try {
        Pageable page = getPageable(pageable, "none");
        Page<Product> allProducts = repository.findAll(page);
        return allProducts;
        } catch (Exception e) {
			throw new ServiceException("Problems with loading all products from DB service method with pagination " + e.getMessage(),
					e);
		}
    }
	
	/**
	 * Метод поиска товара по id
	 */
	@Override
	public Product loadProductById(Long id) throws ServiceException {
		Product product = new Product();
		try {
			product = repository.findProductById(id);
		} catch (Exception e) {
			throw new ServiceException("Problems with loading all products from DB service method " + e.getMessage(),
					e);
		}
		return product;
	}

	/**
	 * Метод загрузки всех товаров и базы данных по названию
	 */
	@Override
	public Page<Product> loadAllProductByName(Pageable pageable,String name) throws ServiceException {
		try {
			Pageable page = getPageable(pageable, "none");
	        Page<Product> allProducts = repository.findProductByNameContainsIgnoreCase(name,page);
	        return allProducts;
		} catch (Exception e) {
			throw new ServiceException(
					"Problems with loading searching product from DB service method " + e.getMessage(), e);
		}
	}

	/**
	 * Метод поиска товара по названию(возвращает один товар из найденых по этому имени)
	 */
	@Override
	public Product loadOneProductByName(String name) throws ServiceException {
		Product product = new Product();
		try {
			product = repository.findProductByName(name);
		} catch (Exception e) {
			throw new ServiceException(
					"Problems with loading searching product from DB service method " + e.getMessage(), e);
		}
		return product;
	}

	/**
	 * Метод добавления нового товара в базу данных
	 */
	@Override
	public void add(Product object, TypeProduct givenTypeProduct) throws ServiceException {
		try {
			Product product = new Product();
			product.setName(object.getName());
			product.setDescription(object.getDescription());
			product.setPrice(object.getPrice());
			product.setImage(object.getImage());
			TypeProduct foundTypeProduct = typeRepository.findTypeProductByName(givenTypeProduct.getName());
			if (foundTypeProduct != null) {
				product.setTypeProduct(foundTypeProduct);
				foundTypeProduct.getProducts().add(product);
				typeRepository.save(foundTypeProduct);
			} else {
				product.setTypeProduct(givenTypeProduct);
				givenTypeProduct.getProducts().add(product);
				typeRepository.save(givenTypeProduct);
			}
			repository.save(product);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new ServiceException("Problems with adding new products to DB service method  " + e.getMessage(), e);
		}

	}

	/**
	 * Метод удаления товара по id из базы данных
	 */
	@Override
	public void delete(Long id) throws ServiceException {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			throw new ServiceException("Problems with deleting product to DB service method " + e.getMessage(), e);
		}
	}

	/**
	 * Метод изменения данных о выбранном товаре в базе данных
	 */
	@Override
	public void update(Product object, TypeProduct givenTypeProduct) throws ServiceException {
		try {
			Product product = new Product();
			product.setId(object.getId());
			product.setName(object.getName());
			product.setDescription(object.getDescription());
			product.setPrice(object.getPrice());
			product.setImage(object.getImage());
			TypeProduct foundTypeProduct = typeRepository.findTypeProductByName(givenTypeProduct.getName());	
			if (foundTypeProduct != null) {
				product.setTypeProduct(foundTypeProduct);
				foundTypeProduct.getProducts().add(product);
				typeRepository.save(foundTypeProduct);
			} else {
				product.setTypeProduct(givenTypeProduct);
				givenTypeProduct.getProducts().add(product);
				typeRepository.save(givenTypeProduct);
			}
			repository.save(product);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("Problems with updating product to DB service method  " + e.getMessage(), e);
		}

	}

	/**
	 * Метод загрузки всех названий типов продукта из базы данных
	 */
	@Override
	public List<TypeProduct> loadAllTypeProduct() throws ServiceException {
		return typeRepository.findAll();
	}

	/**
	 * Метод загрузки всех продуктов по названию типов продукта из базы данных.
	 * Ищем сначала тип продукта по названию типа,затем у найденного типа получаем его id и 
	 * затем ещем все продукты по id типа продукта
	 */
	@Override
	public Page<Product> loadAllProductByTypeProductId(Pageable pageable,String typeProduct) throws ServiceException {
		try {
			TypeProduct type = typeRepository.findTypeProductByName(typeProduct);
			Pageable page = getPageable(pageable, "none");
	        Page<Product> products = repository.findProductByTypeProductId(type.getId(),page);
	        return products;
	        } catch (Exception e) {
				throw new ServiceException("Problems with loading all products from DB service method with pagination " + e.getMessage(),
						e);
			}
	}

	/**
	 * Метод загрузки всех продуктов из базы данных по названию и типу продукта
	 */
	@Override
	public Page<Product> loadAllProductByTypeProductIdAndProductName(Pageable pageable,String typeName, String productName)
			throws ServiceException {
		try {
			TypeProduct type = typeRepository.findTypeProductByName(typeName);
			Pageable page = getPageable(pageable, "none");
	        Page<Product> products = repository.findProductByTypeProductIdAndNameContainsIgnoreCase(type.getId(), productName,page);
	        return products;
	        } catch (Exception e) {
				throw new ServiceException("Problems with loading all products from DB service method with pagination " + e.getMessage(),
						e);
			}
		
	}
	
	/**
	 * Метод загрузки всех продуктов из базы данных с сортировкой по цене
	 */
	@Override
	public Page<Product> loadAllProductByPrice(Pageable pageable,String priceSortType) throws ServiceException {
		try {
	        Page<Product> products;
	        Pageable page = getPageable(pageable, priceSortType);
			products = repository.findAll(page);
	        return products;
	        } catch (Exception e) {
				throw new ServiceException("Problems with loading all products from DB service method with pagination " + e.getMessage(),
						e);
			}
	}

	/**
	 * Метод загрузки всех продуктов из базы данных по типу и с сортировкой по цене
	 */
	@Override
	public Page<Product> loadAllProductByTypeProductIdAndPrice(Pageable pageable,String typeName, String priceSortType)
			throws ServiceException {
		try {
			TypeProduct type = typeRepository.findTypeProductByName(typeName);
	        Page<Product> products;
	        Pageable page = getPageable(pageable, priceSortType);
	        products = repository.findProductByTypeProductId(type.getId(),page);
	        return products;
	        } catch (Exception e) {
				throw new ServiceException("Problems with loading all products from DB service method with pagination " + e.getMessage(),
						e);
			}
	}

	/**
	 * Метод загрузки всех продуктов из базы данных по названию и с сортировкой по цене
	 */
	@Override
	public Page<Product> loadAllProductByNameContainsIgnoreCaseAndPrice(Pageable pageable,String name, String priceSortType)
			throws ServiceException {
		try {
		    Page<Product> products;
		    Pageable page = getPageable(pageable, priceSortType);
			products = repository.findProductByNameContainsIgnoreCase(name,page);
			return products;
		} catch (Exception e) {
			throw new ServiceException(
					"Problems with loading searching product by price and product name from DB service method "
							+ e.getMessage(),
					e);
		}
	}

	/**
	 * Метод загрузки всех продуктов из базы данных по типу,названию и с сортировкой по цене
	 */
	@Override
	public Page<Product> loadAllProductByTypeProductIdAndPriceAndNameContainsIgnoreCase(Pageable pageable,String typeName,
			String priceSortType, String name) throws ServiceException {
		try {
		    Page<Product> products;
			TypeProduct type = typeRepository.findTypeProductByName(typeName);
			Pageable page = getPageable(pageable, priceSortType);
			products = repository.findProductByTypeProductIdAndNameContainsIgnoreCase(type.getId(),name,page);
			return products;
		} catch (Exception e) {
			throw new ServiceException(
					"Problems with loading searching product by price and product type and product name from DB service method "
							+ e.getMessage(),
					e);
		}
	}

}
