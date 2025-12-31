package org.example.demo3.mapper;

import org.example.demo3.dto.GoodsDto;
import org.example.demo3.entity.GoodsForDb;
import org.springframework.stereotype.Component;

@Component
public class GoodsMapper {

    public GoodsForDb toEntity(GoodsDto dto) {
        if (dto == null) return null;

        GoodsForDb g = new GoodsForDb();
        g.setName(dto.getName());
        g.setPrice((int) dto.getPrice());
        g.setQuantity(dto.getQuantity());
        g.setType(dto.getType());

        return g;
    }

    public GoodsDto toModel(GoodsForDb g) {
        if (g == null) return null;

        return new GoodsDto(
                g.getId() == null ? 0 : g.getId().intValue(),
                g.getName(),
                g.getType(),
                g.getPrice(),
                (int) g.getQuantity()
        );
    }
}

