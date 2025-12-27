package org.example.demo3.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.demo3.entity.GoodsDto;
import org.example.demo3.entity.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    public static final Logger log = LoggerFactory.getLogger(GoodsService.class);

    private final List<GoodsDto> goodsDtoF =  new ArrayList<>();
    private int currentId = 0;

    public GoodsDto createGoods(GoodsDto goodsDto) {
        log.info("createGoods...");
        goodsDto.setId(++currentId);
        goodsDtoF.add(goodsDto);
        return goodsDto;
    }

    public List<GoodsDto> getGoods() {
        log.info("getGoods...");
        return goodsDtoF;
    }

    public GoodsDto getGoodsById(int id) {
        log.info("getGoodsById...");
        return goodsDtoF.stream().filter(goodsDto -> goodsDto.getId() == id)
                .findFirst().orElseThrow(()-> new EntityNotFoundException("No goods found with id " + id));
    }

    public GoodsDto deleteGoodsById(int id) {
        log.info("delete goods by id {}...", id);
        GoodsDto goodsDto = goodsDtoF.stream()
                .filter(goodsDto1 -> goodsDto1.getId() == id)
                .findFirst().orElseThrow(()-> new EntityNotFoundException("No goods found with id " + id));
        goodsDtoF.remove(goodsDto);
        return goodsDto;
    }

    public String countGoods() {
        log.info("Counting goods...");
        Map<Type, Long> goodsDtoM = goodsDtoF.stream()
                .collect(Collectors.groupingBy(GoodsDto::getType , Collectors.counting()));
        StringBuilder sb = new  StringBuilder("Total count by type: \n");
        goodsDtoM.forEach((type,count)-> sb.append(type).append(": ").append(count).append("\n"));

        return sb.append("\nTotal number of Goods: \n").append(goodsDtoF.size()).toString();
    }

    public GoodsDto updateGoods(int id, GoodsDto goodsDto) {
        log.info("updateGoods...");
        GoodsDto goodsDto1 = goodsDtoF.stream()
                .filter(s -> s.getId() == id)
                .findFirst().orElseThrow(()-> new EntityNotFoundException("No goods found with id " + id));

        if(goodsDto.getId() !=0) goodsDto1.setId(goodsDto.getId());
        if(goodsDto1.getName() !=null) goodsDto1.setName(goodsDto1.getName());
        if(goodsDto1.getPrice() !=0) goodsDto1.setPrice(goodsDto1.getPrice());
        if(goodsDto1.getType() !=null) goodsDto1.setType(goodsDto.getType());
        if(goodsDto1.getQuantity() !=0) goodsDto1.setQuantity(goodsDto1.getQuantity());
        return goodsDto1;
    }
}
