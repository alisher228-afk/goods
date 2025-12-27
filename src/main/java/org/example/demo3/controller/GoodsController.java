package org.example.demo3.controller;

import jakarta.validation.Valid;
import org.example.demo3.entity.GoodsDto;
import org.example.demo3.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    public static final Logger log = LoggerFactory.getLogger(GoodsController.class);

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    public  List<GoodsDto> getGoods() {
        log.info("getGoods...");
        return goodsService.getGoods();
    }

    @PostMapping("/create")
    public GoodsDto createGoods(@Valid @RequestBody GoodsDto goodsDto) {
        log.info("createGoods...");
        return goodsService.createGoods(goodsDto);
    }

    @GetMapping("/{id}")
    public GoodsDto getGoodsById(@PathVariable int id) {
        log.info("getGoodsById...");
        return goodsService.getGoodsById(id);
    }

    @DeleteMapping("/delete/{id}")
    public GoodsDto deleteGoodsById(@PathVariable int id) {
        log.info("deleteGoods...");
        return goodsService.deleteGoodsById(id);
    }

    @GetMapping("/count")
    public String countGoods() {
        log.info("countGoods...");
        return goodsService.countGoods();
    }
    @PutMapping("/update/{id}")
    public GoodsDto updateGoods(@PathVariable int id, @Valid @RequestBody GoodsDto goodsDto) {
        log.info("updateGoods...");
        return goodsService.updateGoods(id , goodsDto);
    }

}
