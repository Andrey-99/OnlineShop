package com.ibs21.OnlineShop.controllers;

import com.ibs21.OnlineShop.domain.Product;
import com.ibs21.OnlineShop.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
@RequestMapping("/home")
public class TopController {

    @Autowired
    private ProductRepository productRepository;


    @Value("${upload.path}")
    private String uploadPath;
    private Object HibernateUtil;

    @GetMapping("")
    public String home(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Page<Product> page;
        page = productRepository.findAll(pageable);
        model.addAttribute("page", page);
//        model.addAttribute("url", "/home");
        return "home-top";
    }

    @GetMapping("/{id}")
    public String productDetails(@PathVariable(value = "id") Long id, Model model, Pageable pageable){
        Page<Product> page;
        page = productRepository.findById(id, pageable);
        model.addAttribute("page", page);
//        model.addAttribute("url", "/home");
        return "products/product-detail";
    }


//    @PreAuthorize("hasRole('SELLER')")
//    @GetMapping("/{id}/edit")
//    public String productEdit(@PathVariable(value = "id") Long id, Model model, Pageable pageable) {
//        if (!productRepository.existsById(id)) {
//            return "redirect:/home";
//        }
//        Page<Product> page;
//        page = productRepository.findById(id, pageable);
//        model.addAttribute("page", page);
//
//        return "products/product-edit";
//    }
//
////    @PreAuthorize("hasRole('SELLER')")
//    @PostMapping("/{id}/edit")
//        public String productUpdate(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user,
//                @RequestParam("productname") String productname,
//                @RequestParam("description") String description,
//        @RequestParam("count") int count,
//        @RequestParam("price") int price,
//        @RequestParam("file") MultipartFile file) throws IOException {
//            if (productname != null && description != null && count > 0 && price > 0) {
//                Product product = productRepository.findById(id).orElseThrow();
//                product.setProducttitle(productname);
//                product.setDescription(description);
//                product.setCount(count);
//                product.setPrice(price);
//                product.setRaiting(0);
//                product.setSeller(user);
//                if(file != null && !file.getOriginalFilename().isEmpty()) {
//                    File uploadDir = new File(uploadPath);
//                    if (!uploadDir.exists()) {
//                        uploadDir.mkdir();
//                    }
//                    String uuidFile = UUID.randomUUID().toString();
//                    String resultFilename = uuidFile + "." + file.getOriginalFilename();
//
//                    file.transferTo(new File(uploadPath + "/" + resultFilename));
//                    product.setFilename(resultFilename);
//                }
//                productRepository.save(product);
//            }
//            return "redirect:/home";
//        }
//
//
//    @PostMapping("/{id}/remove")
//    public String productDelete(@PathVariable(value = "id") Long id, Model model) {
//        Product product = productRepository.findById(id).orElseThrow();
//        productRepository.delete(product);
//        return "redirect:/home";
//    }

    @PostMapping("search")
    public String filter(@RequestParam String filter, Model model, Pageable pageable){
        String message = "Такого товара нет";
        Page<Product> page;
        if (filter != null && !filter.isEmpty()){
            page = productRepository.findByProducttitle(filter, pageable);
            if(page == null || page.isEmpty()){
                model.addAttribute("message", message);
            }
        }
        else if(filter == null || filter.isEmpty()){
            return "redirect:/home";
        }
        else {
            page = productRepository.findAll(pageable);
        }
            model.addAttribute("page", page);
        return "home-top";
    }

    @PostMapping("filter")
    public String filterProduct(
            @RequestParam(value = "filterProduct", required = false, defaultValue = "Гигиена,Книги,Игры,Сувениры,Электроника") List<String> categoryes,
            @RequestParam(value = "minprice", defaultValue = "0") int minprice,
            @RequestParam(value = "maxprice", defaultValue = "9999999" ) int maxprice,
            Model model,
            Pageable pageable
    ){
        if(categoryes != null){
            Page<Product> page = null;
//            page = productRepository.findByCategoryIn(categoryes, pageable);
            page = productRepository.findByCategoryInAndPriceBetween(categoryes, minprice, maxprice, pageable);
            model.addAttribute("page", page);
        }else if(categoryes == null){
            return "redirect:/home";
        }

        return "home-top";
    }


}
