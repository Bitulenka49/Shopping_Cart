package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.UserDtls; // Modify the package as per your project structure

import com.ecom.model.Category;
import com.ecom.model.Hidden_Places;
import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.HiddenPlacesService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController 
{
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private HiddenPlacesService hiddenPlacesService; 
	
	@ModelAttribute
	public void getUserDetails(Principal p, Model m)
	{
		if(p !=null)
		{
			String email = p.getName();
			UserDtls userDtls = userService.getUserByEmail(email);
			m.addAttribute("user",userDtls);
			Integer countCart = cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart",countCart);
		
		}
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys",allActiveCategory);
	}
	
	@GetMapping("/")
	public String index(Model m) 
	{
		List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream().limit(6).toList();
		List<Product> allActiveproducts =  productService.getAllActiveProducts("").stream()
				/* .sorted((p1,p2)->p2.getId().compareTo(p1.getId()) */
				.limit(10).toList();
		m.addAttribute("category",allActiveCategory);
		m.addAttribute("products",allActiveproducts);
		
				return "index";
				
	}
	@GetMapping("/signin")
	public String login() 
	{
		return "login";
				
	}
	@GetMapping("/register")
	public String register() 
	{
		return "register";
				
	}
	@GetMapping("/products")
	public String products(Model m,@RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue = "2")Integer pageSize) 
	{
		List<Category> categories = categoryService.getAllActiveCategory();
		 m.addAttribute("paramValue",category);
		 m.addAttribute("categories",categories);
			/*
			 * List<Product> products = productService.getAllActiveProducts(category);
			 * m.addAttribute("products",products);
			 */
			 
		 Page<Product> page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
		 List<Product> products = page.getContent();
		 m.addAttribute("products", products);
		 m.addAttribute("productsSize",products.size());
		 m.addAttribute("pageNo", page.getNumberOfElements());
		 m.addAttribute("pageSize",pageSize);
		 m.addAttribute("totalElements", page.getTotalElements());
		 m.addAttribute("totalPages", page.getTotalPages());
		 m.addAttribute("isFirst", page.isFirst());
		 m.addAttribute("isLast", page.isLast());

		 return "product";

				
	}
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model m) {
	   // System.out.println("Product ID: " + id);  // Check if the ID is passed correctly
	    Product productById = productService.getProductById(id);
	    m.addAttribute("product", productById);
	    return "view_product";
	}

	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user,@RequestParam("img") MultipartFile file,HttpSession session) throws IOException
	{
		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		UserDtls saveUser = userService.saveUser(user);
		
		if(!ObjectUtils.isEmpty(saveUser))
		{
			if(!file.isEmpty())
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				
				 Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());				
				System.out.println(path);
				Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "Saved Successfully");
		}else {
			session.setAttribute("errorMsg", "Something wrong on server");
		}
		
		return "redirect:/register";
	}
	
	@PostMapping("/save-hidden-places")
	public ResponseEntity<Hidden_Places> hidden_placeSave(@RequestBody Hidden_Places hp)
	{
		return ResponseEntity.ok(hiddenPlacesService.saveHiddenPlace(hp));
	}
	
	@GetMapping("/get-hidden-place")
	public ResponseEntity<List<Hidden_Places>> getHiddenPlace()
	{
		return ResponseEntity.ok(hiddenPlacesService.getAll());
	}
	
	@GetMapping("/search")
	public String searchProduct(@RequestParam String ch, Model m) {
	    List<Product> searchProducts = productService.searchProduct(ch);
	    // Ensure `products` is not null
	    if (searchProducts == null) {
	        searchProducts = List.of(); // Return an empty list
	    }
	    m.addAttribute("products", searchProducts);
	    List<Category> categories = categoryService.getAllActiveCategory();
		 m.addAttribute("categories",categories);
	    return "product";
	}

	
	
	
	
}