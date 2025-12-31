package org.example.demo3.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.demo3.dto.GoodsDto;
import org.example.demo3.entity.GoodsForDb;
import org.example.demo3.entity.Type;
import org.example.demo3.mapper.GoodsMapper;
import org.example.demo3.repository.GoodsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    public static final Logger log = LoggerFactory.getLogger(GoodsService.class);

    private final GoodsRepository goodsRepository;
    private final GoodsMapper goodsMapper;

    public GoodsService(GoodsRepository goodsRepository, GoodsMapper goodsMapper) {
        this.goodsRepository = goodsRepository;
        this.goodsMapper = goodsMapper;
    }

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
        if (goodsRepository.existsByNameIgnoreCase(goodsDto.getName())) {
            throw new IllegalStateException("Goods with this name already exists");
        }

        GoodsForDb entity = goodsMapper.toEntity(goodsDto);
        GoodsForDb savedEntity = goodsRepository.save(entity);
        return goodsMapper.toModel(savedEntity);
    }

    public List<GoodsDto> getGoods() {
        log.info("getGoods...");
        List<GoodsForDb> gForDbs = goodsRepository.findAll();
        if(gForDbs.isEmpty()){
            throw new IllegalStateException("No goods found");
        }
        return gForDbs.stream()
                .map(goodsMapper::toModel)
                .collect(Collectors.toList());
    }

    public GoodsDto getGoodsById(int id) {
        log.info("getGoodsById...");
        GoodsForDb goodsForDb = goodsRepository.findById((long) id)
                .orElseThrow(() -> new EntityNotFoundException("No goods found with id " + id));
        return goodsMapper.toModel(goodsForDb);
    }

    public GoodsDto deleteGoodsById(int id) {
        log.info("delete goods by id {}...", id);
        GoodsForDb goodsForDb = goodsRepository.findById((long) id)
                .orElseThrow(() -> new EntityNotFoundException("No goods found with id " + id));
        goodsRepository.deleteById((long) id);
        return goodsMapper.toModel(goodsForDb);
    }

    public String countGoods() {
        log.info("Counting goods...");
        List<GoodsForDb> goodsForDbs = goodsRepository.findAll();
        if (goodsForDbs.isEmpty()) {
            throw new EntityNotFoundException("No goods found");
        }
        Map<Type, Long> countByType = goodsForDbs.stream()
                .collect(Collectors.groupingBy(GoodsForDb::getType, Collectors.counting()));
        StringBuilder result = new StringBuilder("Goods count by type:\n");
        for (Map.Entry<Type, Long> entry : countByType.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }

    public GoodsDto updateGoods(int id, GoodsDto goodsDto) {
        log.info("updateGoods...");
        GoodsForDb existingGoods = goodsRepository.findById((long) id)
                .orElseThrow(() -> new EntityNotFoundException("No goods found with id " + id));
        if (goodsDto.getName() != null) {
            existingGoods.setName(goodsDto.getName());
        }
        if (goodsDto.getPrice() > 0) {
            existingGoods.setPrice((int) goodsDto.getPrice());
        }
        if (goodsDto.getType() != null) {
            existingGoods.setType(goodsDto.getType());
        }
        if (goodsDto.getQuantity() > 0) {
            existingGoods.setQuantity(goodsDto.getQuantity());
        }
        GoodsForDb updatedGoods = goodsRepository.save(existingGoods);
        return goodsMapper.toModel(updatedGoods);
    }

    public List<GoodsDto> sortByPage(int page, int size) {
        return goodsRepository
                .findAll(PageRequest.of(page, size))
                .stream()
                .map(goodsMapper::toModel)
                .toList();
    }


    public List<GoodsDto> sortGoods(String field, String order) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        return goodsRepository.findAll(sort)
                .stream()
                .map(goodsMapper::toModel)
                .toList();
    }
}
