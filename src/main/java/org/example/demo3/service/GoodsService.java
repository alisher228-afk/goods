package org.example.demo3.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.demo3.entity.GoodsDto;
import org.example.demo3.entity.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
        if(goodsDto.getName() == null || goodsDto.getName().isBlank()){
            throw new IllegalStateException("Name is required or empty");
        }
        if(goodsDto.getPrice() <= 0 || goodsDto.getPrice() > 1000){
            throw new IllegalStateException("Price is should be between 0 and 1000");
        }
        if (goodsDto.getQuantity() <= 0 || goodsDto.getQuantity() > 1000) {
            throw new IllegalStateException("Quantity is should be between 0 and 1000");
        }
        boolean nameIsExist = goodsDtoF.stream()
                        .anyMatch(goodsDto1 -> goodsDto1.getName().equalsIgnoreCase(goodsDto.getName()));
        if(nameIsExist){
            log.warn("{} already exists", goodsDto.getName());
            throw new IllegalStateException("Name is already exist");
        }
        goodsDto.setId(++currentId);
        goodsDtoF.add(goodsDto);
        return goodsDto;
    }

    public List<GoodsDto> getGoods() {
        if(goodsDtoF.isEmpty()){
            throw new IllegalStateException("goodsDtoF is empty");
        }
        log.info("getGoods...");
        return goodsDtoF;
    }

    public GoodsDto getGoodsById(int id) {
        log.info("getGoodsById...");
        if(goodsDtoF.isEmpty()){
            throw new IllegalStateException("No goods found");
        }
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
        if(goodsDtoF.isEmpty()){
            log.warn("No goods found");
            throw new IllegalStateException("No goods found");
        }
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

        if(goodsDto1.getName() != null && goodsDto1.getName().isBlank()){
            throw new IllegalStateException("Name is required");
        }
        if(goodsDto.getId() !=0) goodsDto1.setId(goodsDto.getId());
        if(goodsDto.getName() !=null) goodsDto1.setName(goodsDto.getName());
        if(goodsDto.getPrice() >= 0 && goodsDto.getPrice() <=1000) goodsDto1.setPrice(goodsDto.getPrice());
        if(goodsDto.getType() !=null) goodsDto1.setType(goodsDto.getType());
        if(goodsDto.getQuantity() >=0 && goodsDto.getQuantity() <=1000) goodsDto1.setQuantity(goodsDto.getQuantity());
        return goodsDto1;
    }

    public List<GoodsDto> sortByPage(int page, int size) {
        log.info("sortByPage...");
        if (goodsDtoF.isEmpty()) {
            throw new IllegalStateException("No goods found");
        }
        int start = page * size;
        int end = Math.min(start + size, goodsDtoF.size());
        if(start >= goodsDtoF.size()){
            return new ArrayList<>();
        }
        return goodsDtoF.subList(start, end);
    }

    public List<GoodsDto> sortGoods(String field, String order) {
        log.info("sortGoods...");
        ArrayList<GoodsDto> sortGoodsDto = new ArrayList<>(goodsDtoF);
        Comparator<GoodsDto> goodsDtoComparator;
        if (goodsDtoF.isEmpty()) {
            throw new IllegalStateException("No goods found");
        }
        switch (field) {
            case "name" -> goodsDtoComparator = Comparator.comparing(GoodsDto::getName , String.CASE_INSENSITIVE_ORDER);
            case "price" -> goodsDtoComparator = Comparator.comparing(GoodsDto::getPrice);
            case "quantity" -> goodsDtoComparator = Comparator.comparing(GoodsDto::getQuantity);
            default -> throw new IllegalStateException("Invalid field");
        }
        if (order.equalsIgnoreCase("asc")) {
            goodsDtoComparator = goodsDtoComparator.reversed();
        }
        sortGoodsDto.sort(goodsDtoComparator);
        return sortGoodsDto;
    }
}
